/**
 * StatusBar.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import jfractus.math.*;

public class StatusBar extends JPanel
{
	private static final long serialVersionUID = 364927009676448884L;

	private Complex position;
	
	private JLabel messageLabel;
	private JLabel positionLabel;
	private JLabel modeLabel;
	private ResizingBox resizer;
	
	public StatusBar(Frame frame)
	{
		position = new Complex();
		
		BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
		setLayout(new GridBagLayout());
		messageLabel = new JLabel("Message");
		messageLabel.setBorder(bb);
		positionLabel = new JLabel("Position");
		positionLabel.setBorder(bb);
		modeLabel = new JLabel("Mode");
		modeLabel.setBorder(bb);
		resizer = new ResizingBox(frame);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 2.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		add(messageLabel, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		add(positionLabel, gbc);
		gbc.gridx = 2;
		add(modeLabel, gbc);
		gbc.gridx = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.0;
		resizer.setBorder(bb);
		add(resizer, gbc);
		
		setPosition(new Complex());
	}
	
	public String getMessage()
	{
		return messageLabel.getText();
	}
	public Complex getPosition()
	{
		return position;
	}
	
	public void setMessage(String message)
	{
		messageLabel.setText(message);
	}
	public void setPosition(Complex p)
	{
		position = p;
		positionLabel.setText(String.format("(%16.16g+%16.16gi)", position.re, position.im));
	}
}
