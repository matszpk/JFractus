/**
 * MessageDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Window;
import javax.swing.*;

import jfractus.app.Resources;

public class MessageDialog extends GenericDialog
{
	private static final long serialVersionUID = -3971017702734848242L;
	
	public static enum MessageType
	{
		INFORMATION,
		QUESTION,
		ERROR,
	}
	public static enum ResponseType
	{
		YES_NO,
		YES_NO_CANCEL,
		OK_CANCEL,
		OK,
	}
	
	private MessageType messageType;
	private ResponseType responseType;
	private JLabel messageLabel;
	
	public MessageDialog(Window owner, MessageType msgType, ResponseType respType)
	{
		super(owner, ModalityType.APPLICATION_MODAL);
		messageType = msgType;
		responseType = respType;
		createButtons();
		createGUI(owner, null);
	}
	public MessageDialog(Window owner, MessageType msgType, ResponseType respType,
			String title, String message)
	{
		super(owner, title, ModalityType.APPLICATION_MODAL);
		messageType = msgType;
		responseType = respType;
		createButtons();
		createGUI(owner, message);
	}
	
	public MessageDialog(Window owner, MessageType msgType, Object[] buttons)
	{
		super(owner, ModalityType.APPLICATION_MODAL);
		messageType = msgType;
		setButtons(buttons);
		createGUI(owner, null);
	}
	public MessageDialog(Window owner, MessageType msgType, Object[] buttons,
			String title, String message)
	{
		super(owner, title, ModalityType.APPLICATION_MODAL);
		messageType = msgType;
		setButtons(buttons);
		createGUI(owner, message);
	}
	
	private void createButtons()
	{
		final Object[] yesNoTable =
			{ new StockButton(Stock.YES), new StockButton(Stock.NO) };
		final Object[] yesNoCancelTable =
			{ new StockButton(Stock.YES), new StockButton(Stock.NO),
				new StockButton(Stock.CANCEL) };
		final Object[] okCancelTable =
			{ new StockButton(Stock.OK), new StockButton(Stock.CANCEL) };
		final Object[] okTable = { new StockButton(Stock.OK) };
		
		switch(responseType)
		{
		case YES_NO:
			setButtons(yesNoTable);
			break;
		case YES_NO_CANCEL:
			setButtons(yesNoCancelTable);
			break;
		case OK_CANCEL:
			setButtons(okCancelTable);
			break;
		case OK:
			setButtons(okTable);
			break;
		}
	}
	
	private void createGUI(Window owner, String message)
	{
		setSize(300, 150);
		String iconName = null;
		switch(messageType)
		{
		case INFORMATION:
			iconName = "icon-msg-info.png";
			break;
		case QUESTION:
			iconName = "icon-msg-question.png";
			break;
		case ERROR:
			iconName = "icon-msg-error.png";
			break;
		default:
			iconName = "icon-msg-info.png";
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.weighty = 1.0;
		gbc.gridy = 0;
		
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		panel.add(new JLabel(new ImageIcon(Resources.getGraphics(iconName))), gbc);
		
		messageLabel = new JLabel(message);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		panel.add(messageLabel, gbc);
		
		setContent(panel);
		
		addResponseListener(new MessageDialogResponseListener());
	}
	
	private class MessageDialogResponseListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			setVisible(false);
		}
	}
}
