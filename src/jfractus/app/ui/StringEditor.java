/**
 * StringEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class StringEditor extends ObjectEditor
{
    private static final long serialVersionUID = 3754631866300503692L;

    private JTextField textField;
    
    public StringEditor(int index, String name, Class<?> cls)
    {
    	super(index, name, cls);
    	
    	textField = new JTextField();
    	textField.addActionListener(new TextFieldActionListener());
    	textField.addFocusListener(new TextFieldFocusListener());
    }
    
    private void changeValue()
    {
    	if (type.equals(String.class))
    		object = textField.getText();
    	else if (type.isArray())
    	{
    		Class<?> compType = type.getComponentType();
    		if (compType.equals(char.class))
    			object = textField.getText().toCharArray();
    		else if (compType.equals(Character.class))
    		{
    			char[] charArray = textField.getText().toCharArray();
    			Character[] dest = new Character[charArray.length];
    			for (int i = 0; i < charArray.length; i++)
    				dest[i] = new Character(charArray[i]);
    			object = dest;
    		}
    	}
    	fireObjectChange();
    }
    
    public void setObject(Object ob)
    {
    	super.setObject(ob);
    	if (type.equals(String.class))
    		textField.setText((String)ob);
    	else if (type.isArray())
    	{
    		Class<?> compType = type.getComponentType();
    		if (compType.equals(char.class))
    			textField.setText(new String((char[])ob));
    		else
    		{
    			Character[] src = (Character[])ob;
    			StringBuilder sB = new StringBuilder();
    			for (int i = 0; i < src.length; i++)
    				sB.append(src[i].charValue());
    			textField.setText(sB.toString());
    		}
    	}
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
    
    private class TextFieldFocusListener extends FocusAdapter
    {
    	private String oldValue = new String();
    	
    	public void focusLost(FocusEvent e)
    	{
    		String newValue = textField.getText();
    		if (!oldValue.equals(newValue))
    		{
    			oldValue = newValue;
    			changeValue();
    		}
    	}
    }
}
