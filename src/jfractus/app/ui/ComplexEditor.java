/**
 * ComplexEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jfractus.math.Complex;

public class ComplexEditor extends ObjectEditor
{
    private static final long serialVersionUID = -2932305435539491079L;
    
    private JPanel panel;
    private JSpinner reSpinner, imSpinner; 

    public ComplexEditor(int index, String name)
    {
    	super(index, name, Complex.class);
    	panel = new JPanel(new GridLayout(1, 2));
    	
    	reSpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	reSpinner.setEditor(
    			new JSpinner.NumberEditor(reSpinner, "################.#################"));
    	
    	imSpinner = new JSpinner(new SpinnerNumberModel(0.0,
    			-Double.MAX_VALUE, Double.MAX_VALUE, 1.0));
    	imSpinner.setEditor(
    			new JSpinner.NumberEditor(imSpinner, "################.#################"));
    	
    	panel.add(reSpinner);
    	panel.add(imSpinner);
    	
    	SpinnerChangeListener listener = new SpinnerChangeListener();
    	reSpinner.addChangeListener(listener);
    	imSpinner.addChangeListener(listener);
    }
    
    private void reValueChange()
    {
    	((Complex)object).re = ((Number)reSpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    private void imValueChange()
    {
    	((Complex)object).im = ((Number)imSpinner.getValue()).doubleValue();
    	fireObjectChange();
    }
    
    public Component getEditor()
    {
    	return panel;
    }
    
    public void setObject(Object ob)
    {
    	super.setObject(ob);
    	reSpinner.setValue(((Complex)ob).re);
    	imSpinner.setValue(((Complex)ob).im);
    }
    
    private class SpinnerChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
	        if (e.getSource() == reSpinner)
	        	reValueChange();
	        else
	        	imValueChange();
        }
    }
}
