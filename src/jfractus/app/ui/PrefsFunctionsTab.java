/**
 * PrefsFunctionsTab.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.*;

import jfractus.app.FunctionsLoaderFactory;
import jfractus.app.Resources;

public class PrefsFunctionsTab extends JPanel implements PreferencesTab
{
    private static final long serialVersionUID = 6758049955997583258L;

    private Window owner;
    
    private JList libraryPathList;
    private DefaultListModel libraryPathListModel;
    
    private JButton addDirectoryButton;
    private JButton addJarButton;
    private StockButton removeButton;
    
    private File currentJarFileChooserDirectory;
    private File currentDirChooserDirectory;
    
    public PrefsFunctionsTab(Window owner)
    {
    	this.owner = owner;
    	setLayout(new GridBagLayout());
    	
    	JLabel label = new JLabel(Resources.getString("LibraryPathListLabel"));
    	
    	addDirectoryButton = new JButton(Resources.getString("AddDirectory"));
    	addDirectoryButton.addActionListener(new AddDirectorButtonListener());
    	addJarButton = new JButton(Resources.getString("AddJar"));
    	addJarButton.addActionListener(new AddJARButtonListener());
    	removeButton = new StockButton(Stock.REMOVE);
    	removeButton.addActionListener(new RemoveButtonListener());
    	
    	JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	libraryPathListModel = new DefaultListModel();
    	libraryPathList = new JList(libraryPathListModel);
    	libraryPathList.getSelectionModel().
    		addListSelectionListener(new LibraryPathListSelectionListener());
    	removeButton.setEnabled(false);
    	scrollPane.setViewportView(libraryPathList);
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.gridwidth = 1;
    	
    	gbc.weightx = 1.0;
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	gbc.gridy = 0;
    	gbc.weighty = 0.0;
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	add(label, gbc);
    	gbc.weighty = 1.0;
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.gridy = 1;
    	gbc.gridheight = 3;
    	add(scrollPane, gbc);
    	
    	gbc.gridheight = 1;
    	gbc.anchor = GridBagConstraints.NORTH;
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.weightx = 0.0;
    	gbc.weighty = 0.0;
    	gbc.gridx = 1;
    	
    	gbc.gridy = 1;
    	add(addDirectoryButton, gbc);
    	
    	gbc.gridy = 2;
    	add(addJarButton, gbc);
    	
    	gbc.gridy = 3;
    	add(removeButton, gbc);
    	
    	{
    		File dirFile = new File(System.getProperty("user.dir"));
    		currentDirChooserDirectory = dirFile;
    		currentJarFileChooserDirectory = dirFile;
    	}
    }
    
    private class AddDirectorButtonListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		DirectorySelect dialog = new DirectorySelect(owner);
    		dialog.setDirectory(currentDirChooserDirectory);
    		Object response = dialog.run();
    		
    		if (response == Stock.OK)
    		{
    			File file = dialog.getDirectory();
    			if (file != null)
    			{
    				libraryPathListModel.addElement(file.getPath());
    				currentDirChooserDirectory = file;
    			}
    		}
    	}
    }
    
    private class AddJARButtonListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		JFileChooser fileChooser = new JFileChooser(currentJarFileChooserDirectory);
    		fileChooser.addChoosableFileFilter
    				(new FileNameExtensionFilter(
    						Resources.getString("JarFiles"), "jar"));
    		
    		int returnValue = fileChooser.showDialog
    				(PrefsFunctionsTab.this, Resources.getString("Select"));
    		
    		if (returnValue == JFileChooser.APPROVE_OPTION)
    		{
    			libraryPathListModel.addElement
    					(fileChooser.getSelectedFile().getAbsolutePath());
    			
    			currentJarFileChooserDirectory = fileChooser.getCurrentDirectory();
    		}
    	}
    }
    
    private class RemoveButtonListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		int[] indices = libraryPathList.getSelectedIndices();
    		for (int i = indices.length-1; i >= 0; i--)
    			libraryPathListModel.remove(indices[i]);
    	}
    }
    
    public class LibraryPathListSelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
        	removeButton.setEnabled(libraryPathList.getSelectedIndices().length != 0);
        }
    }
    
    public void applyPreferences()
    {
    	String[] functionLibsStringArray = new String[libraryPathListModel.size()];
    	int i  = 0;
    	for (Enumeration<?> enums = libraryPathListModel.elements();
    				enums.hasMoreElements(); i++)
    		functionLibsStringArray[i] = (String)enums.nextElement();
    	
    	if (!Arrays.equals(functionLibsStringArray,
    			FunctionsLoaderFactory.loader.getClassPaths()))
    		FunctionsLoaderFactory.loader.setClassPaths(functionLibsStringArray);
    }
    public void fetchPreferences()
    {
    	String[] stringArray = FunctionsLoaderFactory.loader.getClassPaths();
    	libraryPathListModel.clear();
    	removeButton.setEnabled(false);
    	for (String s : stringArray)
    		libraryPathListModel.addElement(s);
    }
}
