/**
 * DirectorySelect.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.File;
import java.awt.Window;
import javax.swing.JScrollPane;

import jfractus.app.Resources;

public class DirectorySelect extends GenericDialog
{
	private static final long serialVersionUID = 8320813877494059617L;

	private DirectoryTree dirTree;
	
	public DirectorySelect(Window owner)
	{
		super(owner, Resources.getString("SelectDirectory"), ModalityType.APPLICATION_MODAL);
		createGUI();
	}
	
	private void createGUI()
	{
		final Object[] buttons = { new StockButton(Stock.OK),
				new StockButton(Stock.CANCEL) };
		setButtons(buttons);
		
		setSize(300, 400);
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dirTree = new DirectoryTree();
		scrollPane.setViewportView(dirTree);
		
		setContent(scrollPane);
		
		addResponseListener(new ResponseEventListener());
	}
	
	public File getDirectory()
	{
		return dirTree.getSelectedDirectory();
	}
	
	public void setDirectory(File dir)
	{
		dirTree.setDirectory(dir);
	}
	
	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			setVisible(false);
		}
	}
}
