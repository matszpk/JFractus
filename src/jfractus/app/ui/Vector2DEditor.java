/**
 * Vector2DEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jfractus.math.*;

public class Vector2DEditor extends ObjectEditor
{
    private static final long serialVersionUID = 7493888980636995445L;

    private JPanel panel;
    private JSpinner xSpinner, ySpinner; 
    
    public Vector2DEditor(int index, String name)
    {
    	super(index, name, Vector2D.class);
    	panel = new JPanel(new GridLayout(1, 2));
    	
    	xSpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	xSpinner.setEditor(
    			new JSpinner.NumberEditor(xSpinner, "################.#################"));
    	
    	ySpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	ySpinner.setEditor(
    			new JSpinner.NumberEditor(ySpinner, "################.#################"));
    	
    	panel.add(xSpinner);
    	panel.add(ySpinner);
    	
    	SpinnerChangeListener listener = new SpinnerChangeListener();
    	xSpinner.addChangeListener(listener);
    	ySpinner.addChangeListener(listener);
    }
    
    private void xValueChange()
    {
    	((Vector2D)object).x = ((Number)xSpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    private void yValueChange()
    {
    	((Vector2D)object).y = ((Number)ySpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    
    public Component getEditor()
    {
    	return panel;
    }
    
    public void setObject(Object ob)
    {
    	super.setObject(ob);
    	xSpinner.setValue(((Vector2D)ob).x);
    	ySpinner.setValue(((Vector2D)ob).y);
    }
    
    private class SpinnerChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
	        if (e.getSource() == xSpinner)
	        	xValueChange();
	        else
	        	yValueChange();
        }
    }
}
