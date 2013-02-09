/**
 * TransformPanel.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jfractus.math.Matrix3D;

public class TransformPanel extends JPanel
{
    private static final long serialVersionUID = 7231980019123631472L;
    
    private JSpinner[] transformSpinners;
    private Matrix3D matrix;
    
    private boolean lockTransformSpinners = false;
    
    private static final String labelNames[] =
    { "XX:", "XY:", "TX:", "YX:", "YY:", "TY:" };

    public TransformPanel()
    {
    	setLayout(new GridBagLayout());
    	
    	transformSpinners = new JSpinner[6];
    	
    	TransformSpinnerChangeListener listener = new TransformSpinnerChangeListener();
    	for (int i = 0; i < 6; i++)
    	{
    		transformSpinners[i] = new JSpinner(new SpinnerNumberModel(0.0,
    				-Double.MAX_VALUE, Double.MAX_VALUE, 0.1));
    		transformSpinners[i].setEditor(new JSpinner.NumberEditor(transformSpinners[i],
    				"0.##################E0"));
    		transformSpinners[i].addChangeListener(listener);
    	}
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.gridwidth = gbc.gridheight = 1;
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.weighty = 1.0;
    	
    	for (int i = 0; i < 2; i++)
    		for (int j = 0; j < 3; j++)
    		{
    			int index = 3*i + j;
    			gbc.gridx = 2*j;
    			gbc.gridy = i;
    			gbc.weightx = 0.0;
    			add(new JLabel(labelNames[index], SwingConstants.RIGHT), gbc);
    			
    			gbc.gridx = 2*j+1;
    			gbc.weightx = 1.0;
    			add(transformSpinners[index], gbc);
    		}
    	
    	matrix = new Matrix3D();
    }
    
    public void setTransformMatrix(Matrix3D m)
    {
    	matrix = m.copy();
    	lockTransformSpinners = true;
    	for (int i = 0; i < 6; i++)
    		transformSpinners[i].setValue(matrix.m[i]);
    	lockTransformSpinners = false;
    }
    
    public void addTransformChangeListener(TransformChangeListener l)
    {
    	listenerList.add(TransformChangeListener.class, l);
    }
    public void fireTransformChange()
    {
    	Object[] listeners = listenerList.getListenerList();
		TransformChangeEvent event = new TransformChangeEvent(this, matrix);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == TransformChangeListener.class)
    			((TransformChangeListener)(listeners[i+1])).transformChanged(event);
    }
    public void removeTransformChangeListener(TransformChangeListener l)
    {
    	listenerList.remove(TransformChangeListener.class, l);
    }
    
    private class TransformSpinnerChangeListener implements ChangeListener
    {
		public void stateChanged(ChangeEvent e)
        {
			if (lockTransformSpinners)
				return;
			
			int field;
			for (field = 0; field < 6; field++)
				if (e.getSource() == transformSpinners[field])
					break;
			
			matrix.m[field] = ((Number)transformSpinners[field].getValue()).doubleValue();
			fireTransformChange();
        }
    }
}
