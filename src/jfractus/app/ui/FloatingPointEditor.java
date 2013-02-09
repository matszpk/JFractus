/**
 * FloatingPointEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FloatingPointEditor extends ObjectEditor
{
    private static final long serialVersionUID = -2439171093826712747L;

    private JSpinner spinner;
    
	public FloatingPointEditor(int index, String name, Class<?> type)
	{
		super(index, name, type);
		
		Comparable<?> min = null, max = null;
    	Number zero = null, step = null;
    	String format = "";
    	if (type.equals(Float.class) || type.equals(float.class))
    	{
    		zero = new Float(0.0f);
    		step = new Float(1.0f);
    		min = new Float(-Float.MAX_VALUE);
    		max = new Float(Float.MAX_VALUE);
    		format = "######.########";
    	}
    	else if (type.equals(Double.class) || type.equals(double.class))
    	{
    		zero = new Double(0.0);
    		step = new Double(1.0);
    		min = new Double(-Double.MAX_VALUE);
    		max = new Double(Double.MAX_VALUE);
    		format = "################.#################";
    	}
    	
    	spinner = new JSpinner(new SpinnerNumberModel(zero, min, max, step));
    	spinner.setEditor(new JSpinner.NumberEditor(spinner, format));
    	spinner.addChangeListener(new SpinnerChangeListener());
	}
	
	public Component getEditor()
	{
		return spinner;
	}
	
	private void changeValue()
	{
		Number ob = (Number)(spinner.getValue());
    	if (type.equals(float.class))
    		object = ob.floatValue();
    	else if (type.equals(Float.class))
    		object = new Float(ob.floatValue());
    	else if (type.equals(double.class))
    		object = ob.doubleValue();
    	else if (type.equals(Double.class))
    		object = new Double(ob.doubleValue());
    	
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
