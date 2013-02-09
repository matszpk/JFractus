/**
 * Matrix3DEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jfractus.math.Matrix3D;

public class Matrix3DEditor extends ObjectEditor
{
    private static final long serialVersionUID = 3282515857175901690L;

    private JPanel panel;
    private JSpinner[] matrixSpinners;
    
    public Matrix3DEditor(int index, String name)
    {
    	super(index, name, Matrix3D.class);
    	panel = new JPanel(new GridLayout(3, 3));
    	
    	matrixSpinners = new JSpinner[9];
    	
    	SpinnerChangeListener listener = new SpinnerChangeListener();
    	
    	for (int i = 0; i < 9; i++)
    	{
    		matrixSpinners[i] = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
        	matrixSpinners[i].setEditor(new JSpinner.NumberEditor
        			(matrixSpinners[i], "################.#################"));
        	
        	matrixSpinners[i].addChangeListener(listener);
        	
        	panel.add(matrixSpinners[i]);
    	}
    }
    
    private void valueChange(int i)
    {
    	((Matrix3D)object).m[i] = ((Number)matrixSpinners[i].getValue()).doubleValue();
    	fireObjectChange();
    }
    
    public Component getEditor()
    {
    	return panel;
    }
    
    public void setObject(Object ob)
    {
    	super.setObject(ob);
    	for (int i = 0; i < 9; i++)
    		matrixSpinners[i].setValue(((Matrix3D)ob).m[i]);
    }
    
    private class SpinnerChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
        	for (int i = 0; i < 9; i++)
        		if (e.getSource() == matrixSpinners[i])
        			valueChange(i);
        }
    }
}
