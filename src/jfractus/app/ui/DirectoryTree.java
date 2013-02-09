/**
 * DirectoryTree.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.*;
import java.util.*;

import javax.swing.tree.*;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jfractus.app.Resources;

public class DirectoryTree extends JTree
{
    private static final long serialVersionUID = 8363199610456908785L;
    
	private static DefaultMutableTreeNode top = new DefaultMutableTreeNode
			(Resources.getString("RootDirectories"));
	
	public DirectoryTree()
    {
        //super(top);
	    top = new DefaultMutableTreeNode
			(Resources.getString("RootDirectories"));
        setModel(new DirectoryTreeModel(top));
        File[] roots = File.listRoots();
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        setCellRenderer(renderer);
        setCellEditor(new DefaultTreeCellEditor(this, renderer));
       	
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setEditable(true);
        	
        for (File root: roots)
            top.add(new DirectoryMutableTreeNode(root.toString()));
    }
	
	public File getDirectory(TreePath treePath)
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
					toAppend.charAt(toAppend.length()-1) != File.separatorChar)
				dirPath.append(File.separatorChar);
		}
		
    	return new File(dirPath.toString());
    }
    
    public File getSelectedDirectory()
    {
    	return getDirectory(getSelectionPath());
    }
    
    public void setDirectory(File newDir)
    {
    	if (newDir == null)
    		return;
    	File canonical;
    	try
    	{ canonical = newDir.getCanonicalFile(); }
    	catch(IOException e)
    	{ canonical = newDir.getAbsoluteFile(); }
    	
    	LinkedList<File> parents = new LinkedList<File>();

    	{
        	File parent = canonical;
        	while(true)
        	{
        		parents.addFirst(parent);
        		parent = parent.getParentFile();
        		if (parent == null)
        			break;
        	}
    	}
    	
    	TreePath path = new TreePath(top);
    	expandPath(path);
    	
    	TreeModel model = getModel();
    	
    	for (Iterator<File> iter = parents.iterator(); iter.hasNext();)
    	{
    		File parent = iter.next();
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
    					s.charAt(s.length()-1) == File.separatorChar)
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
	
	
	private class DirectoryTreeModel extends DefaultTreeModel
    {    	
		private static final long serialVersionUID = 5207117066249939401L;
        
		public DirectoryTreeModel(TreeNode root)
    	{
    		super(root);
    	}
		
		private class DirFileFilter implements FileFilter
		{

			public boolean accept(File pathname)
            {
	            return pathname.isDirectory();
            }
			
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
    					toAppend.charAt(toAppend.length()-1) != File.separatorChar)
    				dirPath.append(File.separator);
    		}
    		//System.out.println("dirPath: " + dirPath);
    		
    		File parentDir = new File(dirPath.toString());
    		File[] childs = parentDir.listFiles(new DirFileFilter());
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
    }
    
    private static class DirectoryMutableTreeNode extends DefaultMutableTreeNode
    {
        private static final long serialVersionUID = 3696948973941535823L;
        
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