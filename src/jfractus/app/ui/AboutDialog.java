/**
 * AboutDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Font;
import java.awt.Window;
import java.awt.GridLayout;
import javax.swing.*;

import jfractus.app.Resources;

public class AboutDialog extends GenericDialog
{
    private static final long serialVersionUID = 2372463801197994682L;

	public AboutDialog(Window owner)
	{
		super(owner, Resources.getString("AboutProgram"), ModalityType.APPLICATION_MODAL);
		final Object[] buttons = { new StockButton(Stock.OK) };
		setButtons(buttons);
		setSize(400, 200);
		
		JPanel panel = new JPanel(new GridLayout(3, 1));
		JLabel label = new JLabel("JFractus 0.0", SwingConstants.CENTER);
		label.setFont(new Font("Sans", Font.BOLD, 20));
		panel.add(label);
		label = new JLabel(Resources.getString("WrittenBy"), SwingConstants.CENTER);
		label.setFont(new Font("Sans", Font.BOLD, 20));
		panel.add(label);
		label = new JLabel("Mateusz Szpakowski", SwingConstants.CENTER);
		label.setFont(new Font("Sans", Font.BOLD, 20));
		panel.add(label);
		
		setContent(panel);
		
		addResponseListener(new ResponseEventListener());
	}
	
	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			setVisible(false);
		}
	}
}
