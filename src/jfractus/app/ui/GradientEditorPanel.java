/**
 * GradientEditorPanel.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import jfractus.app.Gradient;
import jfractus.app.Resources;

public class GradientEditorPanel extends JPanel
{
    private static final long serialVersionUID = -7333623835647416415L;
    
    private GradientEditField editField;
    private StockButton addButton;
    private StockButton removeButton;
    private ColorChooserButton colorChooseButton;
    private JButton resetButton;
    
    public GradientEditorPanel(Window frame)
    {
    	createGUI(frame);
    }
    public GradientEditorPanel(Window frame, Gradient gradient)
    {
    	createGUI(frame);
    }
    
    private void createGUI(Window frame)
    {
    	GridBagLayout layout = new GridBagLayout();
    	setLayout(layout);
    	
    	editField = new GradientEditField();
    	addButton = new StockButton(Stock.ADD);
    	removeButton = new StockButton(Stock.REMOVE);
    	colorChooseButton = new ColorChooserButton(frame);
    	resetButton = new JButton(Resources.getString("Reset"));
    	resetButton.setMnemonic(Resources.getString("ResetMnemonic").charAt(0));
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.gridx = gbc.gridy = 0;
    	gbc.gridwidth = 4;
    	gbc.gridheight = 1;
    	gbc.weightx = 1.0;
    	gbc.weighty = 1.0;
    	gbc.fill = GridBagConstraints.BOTH;
    	add(editField, gbc);
    	
    	gbc.weightx = 1.0/4.0;
    	gbc.weighty = 0.0;
    	gbc.gridx = 0;
    	gbc.gridy = 1;
    	gbc.gridwidth = 1;
    	gbc.fill = GridBagConstraints.BOTH;
    	add(addButton, gbc);
    	
    	gbc.gridx = 1;
    	add(removeButton, gbc);
    	gbc.gridx = 2;
    	add(colorChooseButton, gbc);
    	gbc.gridx = 3;
    	add(resetButton, gbc);
    	
    	editField.addChangeSelectionListener(new GradientChangeSelectionEventListener());
    	colorChooseButton.addColorChangeListener(new ColorChangeEventListener());
    	addButton.addActionListener(new AddPointActionListener());
    	removeButton.addActionListener(new RemovePointActionListener());
    	resetButton.addActionListener(new ResetActionListener());
    	
    	{
    		Gradient gradient = editField.getGradient();
    		removeButton.setEnabled(gradient.isRemovable(editField.getSelectedPointIndex()));
    	}
    }
    
    public Gradient getGradient()
    {
    	return editField.getGradient();
    }
    
    public void setGradient(Gradient gradient)
    {
    	editField.setGradient(gradient);
    	int index = editField.getSelectedPointIndex();
    	Color color = gradient.getPoint(index).color.toAWTColor();
    	colorChooseButton.setColor(color);
    }
    
    /* event listeners */
    
    public class AddPointActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		editField.addNewPoint();
    	}
    }
    
    private class RemovePointActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	editField.removeSelectedPoint();
        }
    }
    
    private class ResetActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		editField.reset();
    	}
    }
    
    private class GradientChangeSelectionEventListener implements
    		GradientChangeSelectionListener
    {
        public void selectionChanged(GradientChangeSelectionEvent e)
        {
        	
        	int index = e.getSelectedPointIndex();
        	Gradient gradient = editField.getGradient(); 
        	if (index >= 0)
        	{
        		Color color = gradient.getPoint(index).color.toAWTColor();
        		colorChooseButton.setColor(color);
        		removeButton.setEnabled(gradient.isRemovable(index));
        	}
        }
    }
    
    private class ColorChangeEventListener implements ColorChangeListener
    {
        public void colorChanged(ColorChangeEvent e)
        {
        	int index = editField.getSelectedPointIndex();
        	if (index >= 0)
        		editField.setColorForSelectedPoint(e.getColor());
        }
    }
}
