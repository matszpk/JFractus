/**
 * ArrayEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.lang.reflect.Array;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jfractus.app.Resources;

public class ArrayEditor extends ObjectEditor
{
    private static final long serialVersionUID = -1189688416717685036L;

    private JSpinner lengthSpinner;
    private JPanel panel;
    private JPanel lengthPanel;
    private HorizontalScrollPane scrollPane;
    private GridBagLayout elemsLayout;
    private JPanel elemsPanel;
    
    private ObjectEditor[] elemEditors;
    private ElementChangeListener elemChangeListener;
    private boolean lockLengthSpinnerListening = false;
    
    public ArrayEditor(int index, String name, Class<?> type)
    {
    	super(index, name, type);
    	
    	elemEditors = new ObjectEditor[0];
    	panel = new JPanel(new GridBagLayout());
    	
    	scrollPane = new HorizontalScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scrollPane.setMinimumSize(new Dimension(0, 150));
    	scrollPane.setPreferredSize(new Dimension(100, 150));
    	
    	lengthPanel = new JPanel(new GridBagLayout());
    	lengthPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	elemsLayout = new GridBagLayout();
    	elemsPanel = new JPanel(elemsLayout);
    	scrollPane.setViewportView(elemsPanel);
    	//scrollPane.setRowHeaderView(lengthPanel);
    	
    	elemChangeListener = new ElementChangeListener(); 
    	
    	{
    		GridBagConstraints gbc = new GridBagConstraints();
    		gbc.fill = GridBagConstraints.HORIZONTAL;
    		gbc.gridwidth = gbc.gridheight = 1;
    		gbc.weighty = 1.0;
    		gbc.gridy = 0;
    		
    		gbc.weightx = 0.0;
    		gbc.gridx = 0;
    		
    		lengthPanel.add(new JLabel(Resources.getString("LengthLabel")), gbc);
    		
    		gbc.weightx = 1.0;
    		gbc.gridx = 1;
    		lengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    		lengthSpinner.setEditor(new JSpinner.NumberEditor(lengthSpinner, "#########"));
    		lengthPanel.add(lengthSpinner, gbc);
    		lengthSpinner.addChangeListener(new LengthSpinnerChangeListener());
    	}
    	
    	{
    		GridBagConstraints gbc = new GridBagConstraints();
    		gbc.fill = GridBagConstraints.HORIZONTAL;
    		gbc.anchor = GridBagConstraints.NORTH;
    		gbc.gridwidth = gbc.gridheight = 1;
    		gbc.weighty = 0.0;
    		gbc.weightx = 1.0;
    		gbc.gridy = 0;
    		
    		gbc.gridx = 0;
    		panel.add(lengthPanel, gbc);
    		
    		gbc.fill = GridBagConstraints.BOTH;
    		gbc.gridy = 1;
    		gbc.weighty = 1.0;
    		panel.add(scrollPane, gbc);
    	}
    }
    
    private void updateArrayLength(int oldLength, int newLength)
    {
    	ObjectEditor[] newElemEditors = new ObjectEditor[newLength];
    	
    	if (oldLength > newLength)
    	{
    		for (int i = oldLength-1; i >= newLength; i--)
    		{
    			elemEditors[i].removeObjectChangeListener(elemChangeListener);
    			elemsPanel.remove(2*i + 1);
    			elemsPanel.remove(2*i);
    		}
    		
    		System.arraycopy(elemEditors, 0, newElemEditors, 0, newLength);
    		
    		GridBagConstraints gbc = new GridBagConstraints();
    		gbc.anchor = GridBagConstraints.NORTH;
    		gbc.fill = GridBagConstraints.HORIZONTAL;
    		gbc.gridwidth = gbc.gridheight = 1;
    		gbc.weighty = 0.0;
    		
    		for (int i = 0; i < newLength; i++)
    		{
    			gbc.gridy = i;
        		gbc.weightx = 0.0;
        		gbc.gridx = 0;
        		elemsLayout.setConstraints(elemsPanel.getComponent(2*i), gbc);
        		
        		gbc.weightx = 1.0;
        		gbc.gridx = 1;
        		if (i + 1 == newLength)
        		{
        			gbc.weighty = 1.0;
        			gbc.fill = GridBagConstraints.HORIZONTAL;
        		}
        		elemsLayout.setConstraints(elemsPanel.getComponent(2*i+1), gbc);
    		}
    	}
    	else
    	{
    		GridBagConstraints gbc = new GridBagConstraints();
    		gbc.anchor = GridBagConstraints.NORTH;
    		gbc.fill = GridBagConstraints.HORIZONTAL;
    		gbc.gridwidth = gbc.gridheight = 1;
    		gbc.weighty = 0.0;
    		
    		/* change constraints */
    		if (oldLength != 0)
    		{
    			gbc.gridx = 0;
    			gbc.gridy = oldLength-1;
    			gbc.weightx = 0.0;
    			elemsLayout.setConstraints(elemsPanel.getComponent(2*oldLength-2), gbc);
    			
    			gbc.gridx = 1;
    			elemsLayout.setConstraints(elemsPanel.getComponent(2*oldLength-1), gbc);
    		}
    		
    		Class<?> elemType = type.getComponentType();
    		
    		for (int i = oldLength; i < newLength; i++)
    		{
    			ObjectEditor editor = ObjectEditorFactory.newObjectEditor(i, "", elemType);
    			newElemEditors[i] = editor;
    			
    			gbc.gridy = i;
        		gbc.weightx = 0.0;
        		gbc.gridx = 0;
    			elemsPanel.add(new JLabel(String.valueOf(i) + ":", SwingConstants.RIGHT), gbc);

    			gbc.weightx = 1.0;
        		gbc.gridx = 1;
        		if (i + 1 == newLength)
        		{
        			gbc.weighty = 1.0;
        			gbc.fill = GridBagConstraints.HORIZONTAL;
        		}
        		elemsPanel.add(editor.getEditor(), gbc);
        		editor.setObject(Array.get(object, i));
        		editor.addObjectChangeListener(elemChangeListener);
    		}
    		
    		System.arraycopy(elemEditors, 0, newElemEditors, 0, oldLength);
    		
    		/* change alignment */
    		
    	}
    	elemEditors = newElemEditors;
    	
		scrollPane.updateViewSize();
    	scrollPane.revalidate();
    	scrollPane.repaint();
	}
    
    private void elementChanged(int index)
    {
    	Array.set(object, index, elemEditors[index].getObject());
    	fireObjectChange();
    }
    
    public Component getEditor()
    {
    	return panel;
    }
    
    public void setObject(Object ob)
    {
    	int oldLength = (object != null) ? Array.getLength(object) : 0;
    	super.setObject(ob);
    	lockLengthSpinnerListening = true;
    	int newLength = Array.getLength(object);
    	lengthSpinner.setValue(newLength);
    	lockLengthSpinnerListening = false;
    	updateArrayLength(oldLength, newLength);
    }
    
    private class LengthSpinnerChangeListener implements ChangeListener
    {
    	public void stateChanged(ChangeEvent e)
        {
    		if (lockLengthSpinnerListening)
    			return;
    		
    		Class<?> elemType = type.getComponentType();
    		Object src = object;
    		int oldLength = Array.getLength(src);
    		int length = ((Number)lengthSpinner.getValue()).intValue();
    		object = Array.newInstance(elemType, length);
    		
    		int destLength = Math.min(oldLength, length);
    		
    		for (int i = 0; i < destLength; i++)
    			Array.set(object, i, Array.get(src, i));
    		
    		if (!elemType.isPrimitive())
    		{
    			if (elemType.isEnum())
        			for (int i = destLength; i < length; i++)
            				Array.set(object, i, elemType.getEnumConstants()[0]);
    			else if (elemType.equals(Boolean.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Boolean(false));
    			else if (elemType.equals(Byte.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Byte((byte)0));
    			else if (elemType.equals(Short.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Short((short)0));
    			else if (elemType.equals(Integer.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Integer(0));
    			else if (elemType.equals(Long.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Long(0L));
    			else if (elemType.equals(Float.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Float(0));
    			else if (elemType.equals(Double.class))
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, new Double(0));
    			else if (elemType.isArray())
    			{
    				Class<?> subElemType = elemType.getComponentType();
    				for (int i = destLength; i < length; i++)
            			Array.set(object, i, Array.newInstance(subElemType, 0));
    			}
    			else
    			{
        			try
        			{
            			for (int i = destLength; i < length; i++)
            				Array.set(object, i, elemType.getConstructor().newInstance());
        			}
        			catch(Exception ex)
        			{ ex.printStackTrace(); }
    			}
    		}
    		
    		updateArrayLength(oldLength, length);
    		fireObjectChange();
        }
    }
    
    private class ElementChangeListener implements ObjectChangeListener
    {
    	public void objectChanged(ObjectChangeEvent e)
    	{
    		elementChanged(e.getIndex());
    	}
    }
}
