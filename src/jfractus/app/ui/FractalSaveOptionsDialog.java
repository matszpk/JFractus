/**
 * FractalSaveOptionsDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Window;

import javax.swing.JCheckBox;

import jfractus.app.Resources;

public class FractalSaveOptionsDialog extends GenericDialog
{
    private static final long serialVersionUID = -6495987683382925755L;
    
    private JCheckBox saveRelativePathsCheckBox; 

	public FractalSaveOptionsDialog(Window owner)
	{
		super(owner, Resources.getString("FractalSaveOptions"), ModalityType.APPLICATION_MODAL);
		final Object[] buttons = { new StockButton(Stock.OK), new StockButton(Stock.CANCEL) };
		setButtons(buttons);
		addResponseListener(new ResponseEventListener());
		setSize(300, 120);
		saveRelativePathsCheckBox = new JCheckBox(Resources.getString("SaveRelativePaths"));
		setContent(saveRelativePathsCheckBox);
	}
	
	public boolean isSaveRelative()
	{
		return saveRelativePathsCheckBox.isSelected();
	}
	
	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			setVisible(false);
		}
	}
}
