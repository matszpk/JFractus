/**
 * EnumEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class EnumEditor extends ObjectEditor
{
    private static final long serialVersionUID = 5641196235848972907L;

    private JComboBox comboBox;
    private Enum<?>[] enumValues;
    
    public EnumEditor(int index, String name, Class<?> type)
    {
    	super(index, name, type);
    	comboBox = new JComboBox();
    	
    	enumValues = (Enum<?>[])type.getEnumConstants();
    	for (int i = 0; i < enumValues.length; i++)
    		comboBox.addItem(enumValues[i].name());
    	
    	comboBox.addActionListener(new ComboBoxActionListener());
    }
    
    public void changeValue()
    {
    	object = enumValues[comboBox.getSelectedIndex()];
    	fireObjectChange();
    }
    
    public Component getEditor()
    {
    	return comboBox;
    }
    
    public void setObject(Object ob)
    {
    	comboBox.setSelectedIndex(((Enum<?>)ob).ordinal());
    	super.setObject(ob);
    }
    
    private class ComboBoxActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		changeValue();
    	}
    }
}
