/**
 * IntegerEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class IntegerEditor extends ObjectEditor
{
    private static final long serialVersionUID = -438496707957818941L;

    private JSpinner spinner;
    
    public IntegerEditor(int index, String name, Class<?> type)
    {
    	super(index, name, type);
    	
    	Comparable<?> min = null, max = null;
    	Number zero = null, step = null;
    	if (type.equals(Byte.class) || type.equals(byte.class))
    	{
    		zero = new Byte((byte)0);
    		step = new Byte((byte)1);
    		min = new Byte(Byte.MIN_VALUE);
    		max = new Byte(Byte.MAX_VALUE);
    	}
    	else if (type.equals(Short.class) || type.equals(short.class))
    	{
    		zero = new Short((short)0);
    		step = new Short((short)1);
    		min = new Short(Short.MIN_VALUE);
    		max = new Short(Short.MAX_VALUE);
    		
    	}
    	else if (type.equals(Integer.class) || type.equals(int.class))
    	{
    		zero = new Integer(0);
    		step = new Integer(1);
    		min = new Integer(Integer.MIN_VALUE);
    		max = new Integer(Integer.MAX_VALUE);
    	}
    	else if (type.equals(Long.class) || type.equals(long.class))
    	{
    		zero = new Long(0L);
    		step = new Long(1L);
    		min = new Long(Long.MIN_VALUE);
    		max = new Long(Long.MAX_VALUE);
    	}
    	
    	//System.out.println("min: " + min + "max: " + max);
    	
    	spinner = new JSpinner(new SpinnerNumberModel(zero, min, max, step));
    	spinner.setEditor(new JSpinner.NumberEditor(spinner, "############################"));
    	spinner.addChangeListener(new SpinnerChangeListener());
    }
    
    public Component getEditor()
    {
    	return spinner;
    }
    
    private void changeValue()
    {
    	Number ob = (Number)(spinner.getValue());
    	if (type.equals(byte.class))
    		object = ob.byteValue();
    	else if (type.equals(Byte.class))
    		object = new Byte(ob.byteValue());
    	else if (type.equals(short.class))
    		object = ob.shortValue();
    	else if (type.equals(Short.class))
    		object = new Short(ob.shortValue());
    	else if (type.equals(int.class))
    		object = ob.intValue();
    	else if (type.equals(Integer.class))
    		object = new Integer(ob.intValue());
    	else if (type.equals(long.class))
    		object = ob.longValue();
    	else if (type.equals(Long.class))
    		object = new Long(ob.longValue());
    	
    	fireObjectChange();
    }
    
    public void setObject(Object ob)
    {
    	super.setObject(ob);
    	spinner.setValue(ob);
    }
    
    private class SpinnerChangeListener implements ChangeListener
    {
    	public void stateChanged(ChangeEvent e)
    	{
    		changeValue(); 
    	}
    }
}
