/**
 * BooleanEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

public class BooleanEditor extends ObjectEditor
{
    private static final long serialVersionUID = -1093440025356997784L;

    private JCheckBox checkBox;
    
    public BooleanEditor(int index, String name, Class<?> type)
    {
    	super(index, name, type);
    	
    	checkBox = new JCheckBox();
    	checkBox.addItemListener(new CheckBoxItemListener());
    }
    
    public Component getEditor()
    {
    	return checkBox;
    }
    
    private void valueChanged()
    {
    	if (type.equals(boolean.class))
    		object = checkBox.isSelected();
    	else if (type.equals(Boolean.class))
    		object = new Boolean(checkBox.isSelected());
    	fireObjectChange();
    }
    
    public void setObject(Object ob)
    {
    	if (type.equals(boolean.class) || type.equals(Boolean.class))
    		checkBox.setSelected(((Boolean)ob).booleanValue());
    	super.setObject(ob);
    }
    
    private class CheckBoxItemListener implements ItemListener
    {
        public void itemStateChanged(ItemEvent e)
        {
        	valueChanged();
        }
    }
}
