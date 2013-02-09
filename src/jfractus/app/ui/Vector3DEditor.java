/**
 * Vector3DEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jfractus.math.Vector3D;

public class Vector3DEditor extends ObjectEditor
{
    private static final long serialVersionUID = 2038340535987542077L;

    private JPanel panel;
    private JSpinner xSpinner, ySpinner, zSpinner;
    
    public Vector3DEditor(int index, String name)
    {
    	super(index, name, Vector3D.class);
    	panel = new JPanel(new GridLayout(1, 3));
    	
    	xSpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	xSpinner.setEditor(
    			new JSpinner.NumberEditor(xSpinner, "################.#################"));
    	
    	ySpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	ySpinner.setEditor(
    			new JSpinner.NumberEditor(ySpinner, "################.#################"));
    	
    	zSpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	zSpinner.setEditor(
    			new JSpinner.NumberEditor(zSpinner, "################.#################"));
    	
    	panel.add(xSpinner);
    	panel.add(ySpinner);
    	panel.add(zSpinner);
    	
    	SpinnerChangeListener listener = new SpinnerChangeListener();
    	xSpinner.addChangeListener(listener);
    	ySpinner.addChangeListener(listener);
    	zSpinner.addChangeListener(listener);
    }
    
    private void xValueChange()
    {
    	((Vector3D)object).x = ((Number)xSpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    private void yValueChange()
    {
    	((Vector3D)object).y = ((Number)ySpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    private void zValueChange()
    {
    	((Vector3D)object).z = ((Number)zSpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    
    public Component getEditor()
    {
    	return panel;
    }
    
    public void setObject(Object ob)
    {
    	super.setObject(ob);
    	xSpinner.setValue(((Vector3D)ob).x);
    	ySpinner.setValue(((Vector3D)ob).y);
    	zSpinner.setValue(((Vector3D)ob).z);
    }
    
    private class SpinnerChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
	        if (e.getSource() == xSpinner)
	        	xValueChange();
	        else if (e.getSource() == ySpinner)
	        	yValueChange();
	        else
	        	zValueChange();
        }
    }
}
