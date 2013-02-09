/**
 * GradientTab.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import jfractus.app.Resources;
import jfractus.app.Gradient;
import jfractus.app.GradientLocator;

public class GradientTab extends JPanel
{
    private static final long serialVersionUID = -3084027783184217884L;

    private JSpinner colorShiftField;
    private JSpinner colorScaleField;
    
    private GradientLocator gradientLocator;
    private Gradient gradient;
    
    private GradientDirectoryComboBox directoryField;
    private JButton directorySelectTreeButton;
    private JTextField currentLocatorLabel;
    private GradientView currentView;
    private JButton embedButton;

    private JScrollPane scrollPane;
    private GradientDirectoryTable directoryTable;
    
    private StockButton removeButton;
    private StockButton editButton;
    private StockButton newButton;
    private JButton chooseButton;
    
    private GradientEditor gradientEditor;
    private GradientEditorResponseListener gradientEditorResponseListener;
    
    private GradientLocator editedGradientLocator;
    
    private boolean lockColoringSpinners = false;
    
    private Window owner;
    
    public GradientTab(Window owner)
    {
    	gradient = new Gradient();
    	gradientEditorResponseListener = new GradientEditorResponseListener();
    	this.owner = owner;
    	createGUI();
    }
    
    private void createGUI()
    {
    	setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	
    	gbc.gridwidth = 1;
    	gbc.gridheight = 1;
    	gbc.weightx = 1.0;
    	gbc.weighty = 0.0;
    	gbc.fill = GridBagConstraints.BOTH;
    	
    	ColorParameterChangeListener colorParameterChangeListener =
    		new ColorParameterChangeListener();
    	colorShiftField = new JSpinner();
    	colorShiftField.setModel(new SpinnerNumberModel(0.0f, 0.0f, 1.0e9f, 10.0f));
    	colorShiftField.setEditor(new JSpinner.NumberEditor(colorShiftField,
    			"#############.##########"));
    	colorShiftField.addChangeListener(colorParameterChangeListener);
    	colorScaleField = new JSpinner();
    	colorScaleField.setModel(new SpinnerNumberModel(0.0f, 0.0f, 1.0e9f, 10.0f));
    	colorScaleField.setEditor(new JSpinner.NumberEditor(colorScaleField,
    			"#############.##########"));
    	colorScaleField.addChangeListener(colorParameterChangeListener);
    	
    	currentView = new GradientView(gradient);
    	currentLocatorLabel = new JTextField();
    	currentLocatorLabel.setEditable(false);
    	embedButton = new JButton(Resources.getString("Embed"));
    	embedButton.addActionListener(new EmbedButtonListener());
    	
    	directoryField = new GradientDirectoryComboBox();
    	directoryField.addDirectoryLocatorListener(new GradientDirectoryLocatorListener());
    	directorySelectTreeButton = new JButton("...");
    	directorySelectTreeButton.addActionListener(
    			new DirectoryTreeSelectButtonListener());
    	
    	JPanel directoryFieldPanel = new JPanel();
    	{
    		directoryFieldPanel.setLayout(new GridBagLayout());
    		GridBagConstraints tmpGbc = new GridBagConstraints();
    		
    		tmpGbc.fill = GridBagConstraints.BOTH;
    		tmpGbc.gridwidth = tmpGbc.gridheight = 1;
    		tmpGbc.weighty = 1.0;
    		
    		tmpGbc.gridy = 0;
    		tmpGbc.gridx = 0;
    		tmpGbc.weightx = 1.0;
    		directoryFieldPanel.add(directoryField, tmpGbc);
    		
    		tmpGbc.gridx = 1;
    		tmpGbc.weightx = 0.0;
    		directoryFieldPanel.add(directorySelectTreeButton, tmpGbc);
    	}
    	
    	directoryTable = new GradientDirectoryTable(owner);
    	scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scrollPane.setViewportView(directoryTable);
    	directoryTable.setFillsViewportHeight(true);
    	directoryTable.addMouseListener(new DirectoryTableMouseEventListener());
    	directoryTable.getSelectionModel().addListSelectionListener
    			(new DirectoryTableSelectionEventListener());
    	directoryTable.addRenameGradientListener(new RenameGradientEventListener());
    	
    	newButton = new StockButton(Stock.NEW);
    	editButton = new StockButton(Stock.EDIT);
    	removeButton = new StockButton(Stock.REMOVE);
    	chooseButton = new JButton(Resources.getString("ChooseGradient"));
    	
    	newButton.setEnabled(false);
    	chooseButton.setEnabled(false);
    	removeButton.setEnabled(false);
    	
    	newButton.addActionListener(new NewButtonActionListener());
    	editButton.addActionListener(new EditButtonActionListener());
    	removeButton.addActionListener(new RemoveButtonActionListener());
    	chooseButton.addActionListener(new ChooseButtonActionListener());
    	
    	JPanel colorSetPanel = new JPanel();
    	
    	{
    		colorSetPanel.setBorder(new TitledBorder(
    				Resources.getString("ColoringSettings")));
    		colorSetPanel.setLayout(new GridBagLayout());
    		
    		GridBagConstraints gbc2 = new GridBagConstraints();
    		gbc2.gridwidth = 1;
        	gbc2.gridheight = 1;
        	gbc2.fill = GridBagConstraints.BOTH;
    		
    		gbc2.gridx = 0;
        	gbc2.gridy = 0;
        	gbc2.weightx = 0.0;
        	colorSetPanel.add(new JLabel(Resources.getString("ColorShiftLabel")), gbc2);
        	gbc2.gridx = 1;
        	gbc2.weightx = 1.0;
        	colorSetPanel.add(colorShiftField, gbc2);
        	
        	gbc2.gridx = 0;
        	gbc2.gridy = 1;
        	gbc2.weightx = 0.0;
        	colorSetPanel.add(new JLabel(Resources.getString("ColorScaleLabel")), gbc2);
        	gbc2.gridx = 1;
        	gbc2.weightx = 1.0;
        	colorSetPanel.add(colorScaleField, gbc2);
    	}
    	
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	add(colorSetPanel, gbc);
    	
    	JPanel currentGradientPanel = new JPanel();
    	
    	{
    		currentGradientPanel.setBorder(new TitledBorder(
    				Resources.getString("CurrentGradient")));
    		currentGradientPanel.setLayout(new GridBagLayout());
    		
    		GridBagConstraints gbc2 = new GridBagConstraints();
    		gbc2.fill = GridBagConstraints.BOTH;
    		gbc2.gridwidth = gbc2.gridheight = 1;
    		gbc2.weighty = 0.0;
    		
    		gbc2.gridx = 0;
    		gbc2.gridy = 0;
    		gbc2.weightx = 1.0;
    		currentGradientPanel.add(currentLocatorLabel, gbc2);
    		gbc2.gridx = 1;
    		gbc2.weightx = 0.0;
    		currentGradientPanel.add(embedButton, gbc2);
    		gbc2.gridx = 0;
    		gbc2.gridy = 1;
    		gbc2.gridwidth = 2;
    		gbc2.weightx = 1.0;
    		gbc2.weighty = 1.0;
    		currentView.setMinimumSize(new Dimension(200, 20));
    		currentGradientPanel.add(currentView, gbc2);
    	}
    	
    	gbc.gridy = 1;
    	add(currentGradientPanel, gbc);
    	
    	JPanel dirPanel = new JPanel();
    	{
    		dirPanel.setBorder(new TitledBorder(
    				Resources.getString("CurrentDirectory")));
    		dirPanel.setLayout(new GridBagLayout());
    		
    		GridBagConstraints gbc2 = new GridBagConstraints();
    		gbc2.fill = GridBagConstraints.BOTH;
    		gbc2.gridheight = 1;
    		gbc2.gridwidth = 3;
    		
    		gbc2.weighty = 1.0;
    		
    		gbc2.weightx = 1.0;
    		gbc2.weighty = 0.0;
    		gbc2.gridx = 0;
    		gbc2.gridy = 0;
    		dirPanel.add(directoryFieldPanel, gbc2);
    		
    		gbc2.gridy = 1;
    		gbc2.weighty = 1.0;
    		dirPanel.add(scrollPane, gbc2);
    		
    		gbc2.gridwidth = 1;
    		gbc2.weighty = 1.0/3.0;
    		gbc2.gridy = 2;
    		
    		gbc2.weighty = 0.0;
    		gbc2.gridx = 0;
    		dirPanel.add(newButton, gbc2);
    		gbc2.gridx = 1;
    		dirPanel.add(editButton, gbc2);
    		gbc2.gridx = 2;
    		dirPanel.add(removeButton, gbc2);
    		
    		gbc2.gridx = 0;
    		gbc2.gridy = 3;
    		gbc2.gridwidth = 3;
    		gbc2.weightx = 1.0;
    		dirPanel.add(chooseButton, gbc2);
    	}
    	
    	gbc.gridy = 2;
    	gbc.weighty = 1.0;
    	add(dirPanel, gbc);
    	
    	/* set current directory */
    	{
    		GradientLocator dirLocator = new GradientLocator(); 
    		dirLocator.setFromFilePath(System.getProperty("user.dir"));
    		directoryField.setDirectoryLocator(dirLocator);
    		directoryTable.setDirectoryLocator(dirLocator);
    	}
    }
    
    
    public Gradient getGradient()
    {
    	return gradient;
    }
    public GradientLocator getGradientLocator()
    {
    	return gradientLocator;
    }
    
    public void setColorScale(float scale)
    {
    	lockColoringSpinners = true;
    	colorScaleField.setValue(new Double(scale));
    	lockColoringSpinners = false;
    }
    public void setColorShift(float shift)
    {
    	lockColoringSpinners = true;
    	colorShiftField.setValue(new Double(shift));
    	lockColoringSpinners = false;
    }
    
    public void setGradient(Gradient gradient)
    {
    	this.gradient = gradient;
    	currentView.setGradient(this.gradient);
    	//fireGradientChange();
    }
    public void setGradientLocator(GradientLocator locator)
    {
    	gradientLocator = locator;
    	if (locator != null)
    		currentLocatorLabel.setText(gradientLocator.toString());
    	else
    		currentLocatorLabel.setText("");
    	//fireGradientChange();
    }
    
    public void setEnabled(boolean enabled)
    {
    	colorShiftField.setEnabled(enabled);
    	colorScaleField.setEnabled(enabled);
    	embedButton.setEnabled(enabled);
    	currentLocatorLabel.setEnabled(enabled);
    	currentView.setEnabled(enabled);
    	chooseButton.setEnabled(enabled);
    	super.setEnabled(enabled);
    }
    
    /* listeners methods */
    
    public void addGradientChangeListener(GradientChangeListener l)
    {
    	listenerList.add(GradientChangeListener.class, l);
    }
    public void fireGradientChange()
    {
    	Object[] listeners = listenerList.getListenerList();
    	
    	float colorScale = ((Number)colorScaleField.getValue()).floatValue();
    	float colorShift = ((Number)colorShiftField.getValue()).floatValue();
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == GradientChangeListener.class)
    			((GradientChangeListener)(listeners[i+1])).gradientChanged(
    					(new GradientChangeEvent(this, colorScale, colorShift,
    							gradientLocator, gradient)));
    }
    public void removeGradientChangeListener(GradientChangeListener l)
    {
    	listenerList.remove(GradientChangeListener.class, l);
    }
    
    /* editor setting method */
    public GradientEditor getGradientEditor()
    {
    	return gradientEditor;
    }
    public void setGradientEditor(GradientEditor editor)
    {
    	if (gradientEditor != null)
    		gradientEditor.removeResponseListener(gradientEditorResponseListener);
    	gradientEditor = editor;
    	gradientEditor.addResponseListener(gradientEditorResponseListener);
    }
    
    
    /* updating */
    protected void setCurrentGradientFromTable(int index)
    {
    	Gradient selectedGradient = directoryTable.getGradient(index);
		if (selectedGradient != null)
		{
			gradientLocator = directoryTable.getGradientLocator(index);
			gradient = selectedGradient.copy();
			currentView.setGradient(gradient);
			currentLocatorLabel.setText(gradientLocator.toString());
			fireGradientChange();
		}
    }
    
    protected void updateEditedGradient()
    {
    	Gradient edited = gradientEditor.getGradient();
    	GradientLocator toSaveLocator = null;
    	if (editedGradientLocator == null)
    	{
    		System.out.println("is embedded");
    		currentView.setGradient(edited.copy());
    		gradient = edited.copy();
    		fireGradientChange();
    	}
    	else
    	{
    		int tableIndex = -1;
    		tableIndex = directoryTable.findRow(editedGradientLocator);
    		toSaveLocator = editedGradientLocator;
    		
    		if (toSaveLocator != null)
    		{
        		try
        		{ edited.writeToStream(toSaveLocator.getAsOutputStream()); }
        		catch(Exception e)
        		{ }
    		}
    		
    		if (editedGradientLocator.canonicallyEquals(gradientLocator))
    		{
    			Gradient newGradient = new Gradient();
    			try
    			{ newGradient.readFromStream(gradientLocator.getAsInputStream()); }
    			catch(Exception ex)
    			{ newGradient.reset(); }
    			currentView.setGradient(newGradient);
    			fireGradientChange();
    		}
    		
    		if (tableIndex >= 0)
    			directoryTable.updateRow(tableIndex, toSaveLocator);
    	}
    }
    
    public void editGradient()
    {
	    Gradient gradient = null;
		if (directoryTable.getSelectedRowCount() == 1)
		{
			int index = directoryTable.getSelectedRow();
			gradient = directoryTable.getGradient(index);
			editedGradientLocator = directoryTable.getGradientLocator(index);
		}
		else
		{
			gradient = this.gradient;
			editedGradientLocator = gradientLocator;
		}
		
		if (gradient != null)
			gradientEditor.setGradient(gradient.copy());
		else
			gradientEditor.setGradient(new Gradient());
		
		if (editedGradientLocator != null)
			gradientEditor.appendPathToTitle(editedGradientLocator.toString());
		else
			gradientEditor.appendPathToTitle(null);
		gradientEditor.setVisible(true);
    }
    
    /* mouse event listener for GradientDirectoryTable */
    private class DirectoryTableMouseEventListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			/* if double clicked then choose gradient */
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
			{
				int index = e.getY() / directoryTable.getRowHeight();
				if (index < directoryTable.getRowCount())
				{
					if (isEnabled())
						setCurrentGradientFromTable(index);
				}
			}
		}
	}
    
    /* selection changed at directory table */
    private class DirectoryTableSelectionEventListener implements ListSelectionListener
    {
    	public void valueChanged(ListSelectionEvent e)
    	{
    		int count = directoryTable.getSelectedRowCount();
    		if (count == 0)
    			removeButton.setEnabled(false);
    		else
    			removeButton.setEnabled(!directoryTable.getDirectoryLocator().isBuiltin());
    		
    		if (count == 1)
    		{
    			int index = directoryTable.getSelectedRow();
    			editButton.setEnabled(directoryTable.canWrite(index));
    			if (isEnabled())
    				chooseButton.setEnabled(directoryTable.getGradient(index) != null);
    			else
    				chooseButton.setEnabled(false);
    		}
    		else
    		{
    			chooseButton.setEnabled(false);
    			editButton.setEnabled(true);
    		}
    	}
    }
    
    /* Gradient renamed listener */
    private class RenameGradientEventListener implements RenameGradientListener
    {
    	public void gradientRenamed(RenameGradientEvent e)
    	{
    		boolean newEqualsWithLocator = e.getNewLocator().
    				canonicallyEquals(gradientLocator);
    		boolean newEqualsWithEditedLocator = e.getNewLocator().canonicallyEquals
    				(editedGradientLocator);
    		
    		if (e.getOldLocator().canonicallyEquals(gradientLocator))
    		{
    			setGradientLocator(e.getNewLocator());
    			fireGradientChange();
    		}
    		if (e.getOldLocator().canonicallyEquals(editedGradientLocator))
    		{
    			editedGradientLocator = e.getNewLocator();
    			gradientEditor.appendPathToTitle(editedGradientLocator.toString());
    		}
    		/* if requires to update */
    		if (newEqualsWithLocator || newEqualsWithEditedLocator)
    		{
    			Gradient newGradient = new Gradient();
    			try
    			{ newGradient.readFromStream(gradientLocator.getAsInputStream()); }
    			catch(Exception ex)
    			{ newGradient.reset(); }
    			
    			if (newEqualsWithLocator)
    			{
    				setGradient(newGradient);
    				fireGradientChange();
    			}
    			if (newEqualsWithEditedLocator)
    				gradientEditor.setGradient(gradient);
    		}
    	}
    }
    
    /* change spinners listeners */
    
    private class ColorParameterChangeListener implements ChangeListener
    {
    	public void stateChanged(ChangeEvent e)
    	{
    		if (!lockColoringSpinners)
    			fireGradientChange();
    	}
    }
    
    /* buttons listeners */
    private class ChooseButtonActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if (directoryTable.getSelectedRowCount() == 1)
    			setCurrentGradientFromTable(directoryTable.getSelectedRow());
    	}
    }
    
    private class NewButtonActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		directoryTable.newGradient();
    	}
    }
    
    private class EditButtonActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		editGradient();
    	}
    }
    
    private class RemoveButtonActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		int[] selectedRows = directoryTable.getSelectedRows();
    		if (selectedRows != null && selectedRows.length != 0)
    		{
    			MessageDialog questionDialog = new MessageDialog(owner,
    					MessageDialog.MessageType.QUESTION,
    					MessageDialog.ResponseType.YES_NO,
    					Resources.getString("RemovingGradients"),
    					Resources.getString("DoYouWantRemoveGradients"));
    			Object response = questionDialog.run();
    			
    			if (response != Stock.YES)
    				return;
    			
    			int[] sorted = new int[selectedRows.length];
    			System.arraycopy(selectedRows, 0, sorted, 0, selectedRows.length);
    			Arrays.sort(sorted);
    			
    			for(int i = sorted.length-1; i >= 0; i--)
    			{
    				GradientLocator locator =
    					directoryTable.getGradientLocator(sorted[i]);
    				directoryTable.removeGradient(sorted[i]);
    				if (locator.canonicallyEquals(gradientLocator))
    				{
    					setGradientLocator(null);
    					fireGradientChange();
    				}
    				if (locator.canonicallyEquals(editedGradientLocator))
    					gradientEditor.setVisible(false);
    			}
    		}
    	}
    }
    
    private class DirectoryTreeSelectButtonListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		GradientDirectorySelect dialog =
    			new GradientDirectorySelect(owner);
    		dialog.setDirectoryLocator(directoryTable.getDirectoryLocator());
    		Object response = dialog.run();
    		GradientLocator newSelected = dialog.getDirectoryLocator();
    		if (newSelected != null && response == Stock.OK)
    		{
    			directoryField.setDirectoryLocator(newSelected);
    			directoryTable.setDirectoryLocator(newSelected);
        		if (newSelected.isDirectory() && !newSelected.isBuiltin())
        			newButton.setEnabled(true);
        		else newButton.setEnabled(false);
    		}
    	}
    }
    
    private class EmbedButtonListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		gradientLocator = null;
    		currentLocatorLabel.setText(null);
    		editButton.setEnabled(true);
    		fireGradientChange();
    	}
    }
    
    /* GradientEditor listener */
    private class GradientEditorResponseListener implements ResponseListener
    {
    	public void response(ResponseEvent e)
    	{
    		if (e.getResponse() == Stock.CANCEL)
    			return;
    		updateEditedGradient();
    	}
    }
    
    /* GradientDirectoryField listener */
    private class GradientDirectoryLocatorListener implements GradientLocatorListener
    {
    	public void locatorChanged(GradientLocatorEvent e)
    	{
    		//System.out.println("inTab: " + e.getLocator());
    		directoryTable.setDirectoryLocator(e.getLocator());
    		if (e.getLocator().isDirectory() && !e.getLocator().isBuiltin())
    			newButton.setEnabled(true);
    		else newButton.setEnabled(false);
    	}
    }
}
