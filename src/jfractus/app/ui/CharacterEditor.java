/**
 * CharacterEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CharacterEditor extends ObjectEditor
{
	private static final long serialVersionUID = -5927473803362870118L;

	private JTextField textField;
	
	public CharacterEditor(int index, String name, Class<?> type)
	{
		super(index, name, type);
		
		textField = new JFormattedTextField(" ");
    	textField.addActionListener(new TextFieldActionListener());
	}
		
	private void changeValue()
	{
		String src = textField.getText();
		if (src.length() == 0)
			src = "\u0000";
		if (type.equals(char.class))
			object = src.charAt(0);
		else if (type.equals(Character.class))
			object = new Character(src.charAt(0));
		
		fireObjectChange();
	}
	
	public void setObject(Object ob)
	{
		if (type.equals(char.class) || type.equals(Character.class))
		{
			if (object == null)
				return;
			char[] cArray = { ((Character)object).charValue() };
			textField.setText(new String(cArray));
		}
		super.setObject(ob);
	}
	
	public Component getEditor()
	{
		return textField;
	}
	
	private class TextFieldActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		changeValue();
    	}
    }
}
