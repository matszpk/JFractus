/**
 * GradientDirectoryTree.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.IOException;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import jfractus.app.GradientLocator;
import jfractus.app.Resources;

public class GradientDirectoryTree extends JTree
{
    private static final long serialVersionUID = -7228314025881232765L;

    private DefaultMutableTreeNode top;

    public GradientDirectoryTree()
    {
    	//super(top);
    	top = new DefaultMutableTreeNode
    			(Resources.getString("RootDirectories"));
    	setModel(new DirectoryTreeModel(top));
    	GradientLocator[] roots = GradientLocator.listRoots();
    	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    	setCellRenderer(renderer);
    	setCellEditor(new DefaultTreeCellEditor(this, renderer));
    	
    	getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	setEditable(true);
    	
    	for (GradientLocator root: roots)
    		top.add(new DirectoryMutableTreeNode(root.toString()));
    }
    
    public GradientLocator getDirectoryLocator(TreePath treePath)
    {
    	if (treePath == null)
    		return null;
    	
    	Object[] path = treePath.getPath();
    	StringBuilder dirPath = new StringBuilder();
		for (int i = 1; i < path.length; i++)
		{
			String toAppend = (String)(((DefaultMutableTreeNode)path[i]).
					getUserObject());
			dirPath.append(toAppend);
			if (i + 1 != path.length && toAppend.length() != 0 &&
					toAppend.charAt(toAppend.length()-1) != '/')
				dirPath.append('/');
		}
		
    	return new GradientLocator(dirPath.toString());
    }
    
    public GradientLocator getSelectedDirectoryLocator()
    {
    	return getDirectoryLocator(getSelectionPath());
    }
    
    public void setDirectoryLocator(GradientLocator newDir)
    {
    	if (newDir == null)
    		return;
    	GradientLocator canonical;
    	try
    	{ canonical = newDir.getCanonicalLocator(); }
    	catch(IOException e)
    	{ canonical = newDir.getAbsoluteLocator(); }
    	
    	LinkedList<GradientLocator> parents = new LinkedList<GradientLocator>();

    	{
        	GradientLocator parent = canonical;
        	while(true)
        	{
        		parents.addFirst(parent);
        		parent = parent.getParentLocator();
        		if (parent == null)
        			break;
        	}
    	}
    	
    	TreePath path = new TreePath(top);
    	expandPath(path);
    	
    	TreeModel model = getModel();
    	
    	for (Iterator<GradientLocator> iter = parents.iterator(); iter.hasNext();)
    	{
    		GradientLocator parent = iter.next();
    		Object last = path.getLastPathComponent();
    		int count = model.getChildCount(last);
    		int index = 0;
    		
    		String toCompare = (parent == parents.getFirst()) ? parent.toString() :
    			parent.getName();
    		
    		for (index = 0; index < count; index++)
    		{
    			String s = (String)(((DefaultMutableTreeNode)model.getChild(last, index)).
    					getUserObject());
    			if ((parent != parents.getFirst()) && s.length() != 0 &&
    					s.charAt(s.length()-1) == '/')
    				s = s.substring(0, s.length()-1);
    			if (s.equals(toCompare))
    				break;
    		}
    		if (index < count)
    		{
    			path = path.pathByAddingChild(model.getChild(last, index));
    			if (iter.hasNext())
    				expandPath(path);
    		}
    	}
    	setSelectionPath(path);
    	scrollPathToVisible(path);
    }
    
    public void createDirectory()
    {
    	TreePath selPath = getSelectionPath();
    	if (selPath == null)
    		return;
    	
    	GradientLocator selLocator = getSelectedDirectoryLocator();
    	if (selLocator.isBuiltin())
    		return;
    	
    	String newName = Resources.getString("NewDirectoryName");
    	GradientLocator newLocator = selLocator.copy();
    	newLocator.appendName(newName);

    	for (int i = 2; newLocator.exists(); i++)
    		newLocator.changeName(newName + " " + i);
    	
    	expandPath(selPath);
    	if (new File(newLocator.getFilePath()).mkdir())
    	{
    		DirectoryMutableTreeNode parent =
    			(DirectoryMutableTreeNode)selPath.getLastPathComponent();
    		
    		DirectoryMutableTreeNode child =
    			new DirectoryMutableTreeNode(newLocator.getName()); 
    		((DirectoryTreeModel)getModel()).insertNodeInto
    				(child, parent, parent.getChildCount());
    		
    		startEditingAtPath(selPath.pathByAddingChild(child));
    	}
    	
    }

    private class DirectoryTreeModel extends DefaultTreeModel
    {
    	private static final long serialVersionUID = 190623056575878911L;
    	
		public DirectoryTreeModel(TreeNode root)
    	{
    		super(root);
    	}
    	
    	private void updateDirectory(DirectoryMutableTreeNode parent)
    	{
    		if (parent.isUpdated())
    			return;
    		
    		StringBuilder dirPath = new StringBuilder();
    		TreeNode[] path = parent.getPath();
    		for (int i = 1; i < path.length; i++)
    		{
    			String toAppend = (String)(((DefaultMutableTreeNode)path[i]).
    					getUserObject());
    			dirPath.append(toAppend);
    			if (i + 1 != path.length && toAppend.length() != 0 &&
    					toAppend.charAt(toAppend.length()-1) != '/')
    				dirPath.append('/');
    		}
    		//System.out.println("dirPath: " + dirPath);
    		
    		GradientLocator parentLocator = new GradientLocator(dirPath.toString());
    		GradientLocator[] childs = parentLocator.listDirectories();
    		DirectoryMutableTreeNode parentNode =
    			(DirectoryMutableTreeNode)(path[path.length-1]);
    		
    		if (childs != null && childs.length != 0)
    		{
    			String[] sorted = new String[childs.length];
    			for (int i = 0; i < childs.length; i++)
    				sorted[i] = childs[i].getName();
    			Arrays.sort(sorted);
    			for (String child: sorted)
    				parentNode.add(new DirectoryMutableTreeNode(child));
    		}
    		
    		parent.setAsUpdated();
    	}
    	
    	public Object getChild(Object parent, int index)
    	{
    		if (parent != root)
    			updateDirectory((DirectoryMutableTreeNode)parent);
    		return super.getChild(parent, index);
    	}
    	public int getChildCount(Object parent)
    	{
    		if (parent != root)
    			updateDirectory((DirectoryMutableTreeNode)parent);
    		return super.getChildCount(parent);
    	}
    	
    	public void valueForPathChanged(TreePath path, Object newValue)
    	{
    		if (path.getPathCount() <= 2)
    			return;
    		/* rename */
    		String newName = (String)newValue;
    		if (newName.length() == 0 || newName == null)
    			return;
    			
    		GradientLocator locator = getDirectoryLocator(path);
    		if (locator.isBuiltin() || newName.indexOf('/') != -1 ||
    				newName.indexOf('\\') != -1)
    			return;
    		
    		String parentPath = locator.getParentLocator().getFilePath();
    		String newPath = parentPath + File.separator + newName;
    		File file = new File(locator.getFilePath());
    		File newFile = new File(newPath);
    		
    		if (!newFile.exists())
    		{
    			if (file.renameTo(newFile))
    				super.valueForPathChanged(path, newValue);
    		}
    	}
    }
    
    private static class DirectoryMutableTreeNode extends DefaultMutableTreeNode
    {
        private static final long serialVersionUID = 8610055276670187041L;

        private boolean updated; 
        
        public DirectoryMutableTreeNode(Object ob)
        {
        	super(ob);
        	updated = false;
        }
        public DirectoryMutableTreeNode(Object ob, boolean allowsChildren)
        {
        	super(ob, allowsChildren);
        	updated = false;
        }
        
        public boolean isLeaf()
        {
        	return false;
        }
        public boolean isUpdated()
        {
        	return updated;
        }
        public void setAsUpdated()
        {
        	updated = true;
        }
    }
}
