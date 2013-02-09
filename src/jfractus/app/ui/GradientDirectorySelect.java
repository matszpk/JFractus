/**
 * GradientDirectorySelect.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import jfractus.app.GradientLocator;
import jfractus.app.Resources;

public class GradientDirectorySelect extends GenericDialog
{
	private static final long serialVersionUID = -6516972597096083868L;

	private GradientDirectoryTree directoryTree;
	private JButton newDirButton;
	
	public GradientDirectorySelect(Window owner)
	{
		super(owner, Resources.getString("ChangeDirectory"),
				ModalityType.APPLICATION_MODAL);
		createGUI();
	}
	
	private void createGUI()
	{
		final Object[] buttons = { new StockButton(Stock.OK),
				new StockButton(Stock.CANCEL) };
		setButtons(buttons);
		addResponseListener(new ResponseEventListener());
		
		setSize(300, 400);
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.weightx = 1.0;
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		directoryTree = new GradientDirectoryTree();
		directoryTree.getSelectionModel().addTreeSelectionListener
				(new TreeSelectionEventListener());
		scrollPane.setViewportView(directoryTree);
		newDirButton = new JButton(Resources.getString("NewDirectory"));
		newDirButton.setMnemonic(Resources.getString("NewDirectoryMnemonic").charAt(0));
		newDirButton.addActionListener(new NewDirectoryButtonListener());
		newDirButton.setEnabled(false);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		panel.add(scrollPane, gbc);
		gbc.gridy = 1;
		gbc.weighty = 0.0;
		panel.add(newDirButton, gbc);
		
		setContent(panel);
	}
	
	public GradientLocator getDirectoryLocator()
	{
		return directoryTree.getSelectedDirectoryLocator();
	}
	
	public void setDirectoryLocator(GradientLocator loc)
	{
		directoryTree.setDirectoryLocator(loc);
	}
	
	private class NewDirectoryButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			directoryTree.createDirectory();
		}
	}
	
	private class TreeSelectionEventListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			newDirButton.setEnabled(directoryTree.getSelectionCount() != 0);
		}
	}
	
	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			if (e.getResponse() == Stock.OK && directoryTree.getSelectedDirectoryLocator() == null)
				return;
			setVisible(false);
		}
	}
}
