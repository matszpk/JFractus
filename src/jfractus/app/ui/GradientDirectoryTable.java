/**
 * GradientDirectoryTable.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.Serializable;
import java.util.*;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import jfractus.app.Gradient;
import jfractus.app.GradientLocator;
import jfractus.app.Resources;

public class GradientDirectoryTable extends JTable
{
	private static final long serialVersionUID = -6259803071369416999L;
	
	public class Entry implements Serializable
	{
		private static final long serialVersionUID = -2804326952645122283L;
		public GradientLocator locator;
		public Gradient gradient;
		
		public Entry(GradientLocator locator)
		{
			this.locator = locator;
		}
		public Entry(GradientLocator locator, Gradient gradient)
		{
			this.locator = locator;
			this.gradient = gradient;
		}
	}
	
	private class DirectoryTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 8977046993956676593L;
        
        private ArrayList<Entry> dirEntries;
        
        public DirectoryTableModel()
        {
        	dirEntries = new ArrayList<Entry>();
        }
    
    	public int getColumnCount()
        {
            return 3;
        }
    	
    	public String getColumnName(int col)
    	{
    		if (col == 0)
    			return Resources.getString("Name");
    		else if (col == 1)
    			return Resources.getString("Gradient");
    		else
    			return Resources.getString("Writable");
    	}
    
        public int getRowCount()
        {
            return dirEntries.size();
        }
        
        public Object getValueAt(int rowIndex, int columnIndex)
        {
        	Entry entry = dirEntries.get(rowIndex);
        	if (columnIndex == 0)
        		return entry.locator.getName();
        	else if (columnIndex == 1)
        		return entry.gradient;
        	else
        		return entry.locator.canWrite();
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
        	return (columnIndex == 0);
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex)
        {
        	if (columnIndex == 0)
        	{
        		String newName = (String)value;
        		Entry entry = dirEntries.get(rowIndex);
        		
        		if (newName.equals(entry.locator.getName()))
        			return;
        		/* if illegal character in newName then do not rename */
        		if (newName.indexOf('/') != -1 || newName.indexOf('\\') != -1)
        			return;
        		/* only matched "*.grad" and only fromfile */
        		if (!newName.endsWith(".grad") || entry.locator.isBuiltin())
        			return;
        		String parentPath = entry.locator.getParentLocator().getFilePath();
        		String newPath = parentPath + File.separator + newName;
        		File file = new File(entry.locator.getFilePath());
        		File newFile = new File(newPath);
        		
        		GradientLocator newLocator = new GradientLocator();
        		newLocator.setFromFilePath(newPath);
        		
        		int replacedIndex = -1;
        		if (newFile.exists())
        		{
        			/* new file exists */
        			MessageDialog questionDialog = new MessageDialog(owner,
    					MessageDialog.MessageType.QUESTION,
    					MessageDialog.ResponseType.YES_NO,
    					Resources.getString("RenamingGradient"),
    					Resources.getString("DoYouWantRenameGradient"));
        			Object response = questionDialog.run();
        			if (response != Stock.YES)
        				return;
        			
        			replacedIndex = findGradientLocator(newLocator);
        		}
        		
        		if (file.renameTo(newFile))
        		{
        			GradientLocator oldLocator = entry.locator;
        			entry.locator = newLocator;
        			fireRenameGradient(oldLocator, newLocator);
        			if (replacedIndex != -1)
        				removeEntry(replacedIndex);
        		}
        	}
        }
        
        public void clearDirectory()
        {
        	dirEntries.clear();
        	fireTableDataChanged();
        }
        
        public void addEntry(Entry entry)
        {
        	dirEntries.add(entry);
        	fireTableRowsInserted(dirEntries.size()-1, dirEntries.size()-1);
        }
        
        public void updateRow(int row, GradientLocator loc)
        {
        	Entry entry = new Entry(loc);
        	Gradient loaded = new Gradient();
			try
			{ loaded.readFromStream(loc.getAsInputStream()); }
			catch(Exception e)
			{ loaded = null; }
			entry.gradient = loaded;
        	dirEntries.set(row, entry);
        	fireTableRowsUpdated(row, row);
        }
        
        public void removeEntry(int index)
        {
        	dirEntries.remove(index);
        	fireTableRowsDeleted(index, index);
        }
                
        public Gradient getGradient(int index)
        {
        	return dirEntries.get(index).gradient;
        }
        public GradientLocator getGradientLocator(int index)
        {
        	return dirEntries.get(index).locator;
        }
        
        public int findGradientLocator(GradientLocator toFind)
        {
        	for(int result = 0; result < dirEntries.size(); result++)
        		if (dirEntries.get(result).locator.canonicallyEquals(toFind))
        			return result;
        	return -1;
        }
        
        public void createGradient()
        {
        	if (directoryLocator.isBuiltin())
        		return;
        	/* generate new Name */
        	String newName = Resources.getString("NewGradientName");
        	GradientLocator newLocator = new GradientLocator(
        			directoryLocator.toString());
        	newLocator.appendName(newName + ".grad");

        	for (int i = 2; newLocator.exists(); i++)
        		newLocator.changeName(newName + " " + i + ".grad");
        	/* create */
        	Gradient newGradient = new Gradient();
        	try
        	{ 
        		File newFile = new File(newLocator.getFilePath());
        		newFile.createNewFile();
        		newGradient.writeToFile(newFile);
        	}
        	catch(Exception e)
        	{
        		return;
        	}

        	dirEntries.add(new Entry(newLocator, newGradient));
        	int row = dirEntries.size()-1;
        	fireTableRowsInserted(row, row);
        	editCellAt(row, 0);
        }
        
        public void removeGradient(int index)
        {
        	File toDelete = new File(dirEntries.get(index).locator.getFilePath());
        	if (toDelete.delete())
        		removeEntry(index);
        }
    }
	
	public class DirectoryLoader extends SwingWorker<Void, Entry>
	{
		private GradientLocator dirLocator;
		
		public DirectoryLoader(GradientLocator locator)
		{
			dirLocator = locator;
		}
		
		@Override
        protected Void doInBackground() throws Exception
        {
			GradientLocator[] locators = dirLocator.listFiles();
			Arrays.sort(locators);
			
			//System.out.printf("founded:%d\n", locators.length);
			for (GradientLocator locator: locators)
			{
				if (isCancelled())
					break;
				
				Gradient loaded = new Gradient();
				try
				{ loaded.readFromStream(locator.getAsInputStream()); }
				catch(Exception e)
				{ loaded = null; }
				//System.out.println("loading: " + locators[i]);
				Entry entry = new Entry(locator, loaded);
				publish(entry);
			}
			return null;
        }
		
		protected void process(List<Entry> chunks)
		{
			DirectoryTableModel model = (DirectoryTableModel)getModel();
			for (Iterator<Entry> iter = chunks.iterator(); iter.hasNext();)
				model.addEntry(iter.next());
		}
	}
    
	private GradientViewCellRenderer gradientViewRenderer;
	private CheckBoxCellRenderer checkBoxCellRenderer;
	private DirectoryLoader directoryLoader;
	private Window owner;
	
	private GradientLocator directoryLocator;
	
	public GradientDirectoryTable(Window owner)
	{
		getTableHeader().setReorderingAllowed(false);
		setModel(new DirectoryTableModel());
		
		checkBoxCellRenderer = new CheckBoxCellRenderer();
		gradientViewRenderer = new GradientViewCellRenderer();
		this.owner = owner;
	}
	
	/* renameGradientListener methods */
	public void addRenameGradientListener(RenameGradientListener l)
	{
		listenerList.add(RenameGradientListener.class, l);
	}
	public void fireRenameGradient(GradientLocator oldLoc, GradientLocator newLoc)
	{
		Object[] listeners = listenerList.getListenerList();
		RenameGradientEvent event =
			new RenameGradientEvent(this, oldLoc, newLoc);
		
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == RenameGradientListener.class)
    			((RenameGradientListener)(listeners[i+1])).
    				gradientRenamed(event);
	}
	public void removeRenameGradientListener(RenameGradientListener l)
	{
		listenerList.remove(RenameGradientListener.class, l);
	}
	
	/* get renderer for table */
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if (column == 1)
			return gradientViewRenderer;
		else if (column == 2)
			return checkBoxCellRenderer;
		return super.getCellRenderer(row, column);
	}
	
	protected void loadDirectoryTable()
	{
		if (directoryLocator == null)
			return;
		if (directoryLoader != null)
			directoryLoader.cancel(false);
		
		getSelectionModel().clearSelection();
		((DirectoryTableModel)getModel()).clearDirectory();
		directoryLoader = new DirectoryLoader(directoryLocator);
		directoryLoader.execute();
	}
	
	public void setDirectoryLocator(GradientLocator locator)
	{
		this.directoryLocator = locator;
		loadDirectoryTable();
	}
	public void setDirectoryFromSpec(String spec)
	{
		this.directoryLocator = new GradientLocator(spec);
		loadDirectoryTable();
	}
	
	public GradientLocator getDirectoryLocator()
	{
		return directoryLocator;
	}
	public String getDirectorySpec()
	{
		return directoryLocator.toString();
	}
	
	public void newGradient()
	{
		((DirectoryTableModel)getModel()).createGradient();
	}
	
	public void removeGradient(int index)
	{
		((DirectoryTableModel)getModel()).removeGradient(index);
	}
	
	public Gradient getGradient(int row)
	{
		return ((DirectoryTableModel)getModel()).getGradient(row);
	}
	public GradientLocator getGradientLocator(int row)
	{
		return ((DirectoryTableModel)getModel()).getGradientLocator(row);
	}
	
	public boolean canWrite(int row)
	{
		return ((Boolean)((DirectoryTableModel)getModel()).
				getValueAt(row, 2)).booleanValue();
	}
	
	public int findRow(GradientLocator loc)
	{
		return ((DirectoryTableModel)getModel()).findGradientLocator(loc);
	}
	
	public void updateRow(int row, GradientLocator loc)
	{
		((DirectoryTableModel)getModel()).updateRow(row, loc);
	}
	
	private class GradientViewCellRenderer extends GradientView
		implements TableCellRenderer
    {
        private static final long serialVersionUID = -6378985888020645709L;
    
    	public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column)
        {
    		if (isSelected)
    		{
    			setBackground(table.getSelectionBackground());
    			setForeground(table.getSelectionForeground());
    		}
    		else
    		{
    			setBackground(table.getBackground());
    			setForeground(table.getForeground());
    		}
    		
    		setGradient((Gradient)value);
    		return this;
        }
    }
}
