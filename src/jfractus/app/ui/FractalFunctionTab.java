/**
 * FractalFunctionTab.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;

import javax.swing.*;

import jfractus.api.Function;
import jfractus.api.OutputFilter;
import jfractus.api.UnknownParameterException;
import jfractus.app.*;

public class FractalFunctionTab extends JPanel
{
    private static final long serialVersionUID = -8999602972792105722L;

    private Class<?> functionType;
    private Function function;
    
    private JComboBox functionCombo;
    
    private ObjectEditor[] paramEditors;
    private HorizontalScrollPane scrollPane;
    private JPanel paramsPanel;
    private ParameterChangeListener parameterChangeListener;
    
    private boolean lockFunctionChange = false;
    
    public FractalFunctionTab(Class<?> functionType)
    {
    	this.functionType = functionType;
    	createGUI();
    }
    
    private void createGUI()
    {
    	setLayout(new GridBagLayout());
    	
    	JLabel label = new JLabel(Resources.getString("FunctionLabel"));
    	functionCombo = new JComboBox();
    	functionCombo.addActionListener(new FunctionComboChangeListener());
    	
    	parameterChangeListener = new ParameterChangeListener();
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.anchor = GridBagConstraints.NORTH;
    	gbc.gridwidth = gbc.gridheight = 1;
    	gbc.weighty = 0.0;
    	gbc.weightx = 1.0;
    	gbc.gridx = 0;
    	
    	JPanel functionSelectPanel = new JPanel(new GridBagLayout());
    	{
    		GridBagConstraints gbc2 = new GridBagConstraints();
    		gbc2.fill = GridBagConstraints.BOTH;
    		gbc2.gridwidth = gbc2.gridheight = 1;
    		gbc2.weighty = 1.0;
    		
    		gbc2.gridy = 0;
    		gbc2.gridx = 0;
    		gbc2.weightx = 0.0;
    		functionSelectPanel.add(label, gbc2);
    		gbc2.gridx = 1;
    		gbc2.weightx = 1.0;
    		functionSelectPanel.add(functionCombo, gbc2);
    	}
    	
    	gbc.gridy = 0;
    	add(functionSelectPanel, gbc);
    	
    	paramsPanel = new JPanel(new GridBagLayout());
    	scrollPane = new HorizontalScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scrollPane.setViewportView(paramsPanel);
    	
    	gbc.weighty = 1.0;
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.gridy = 1;
    	add(scrollPane, gbc);
    	    	
    	FunctionsLoaderFactory.loader.addClassPathsListener(new ClassPathsChangeListener());
    	
    	updateFunctionCombo();
    }
    
    private void updateParamsPanel()
    {
    	if (paramEditors != null)
    	{
    		for (ObjectEditor e : paramEditors)
    			e.removeObjectChangeListener(parameterChangeListener);
    	}
    	paramsPanel.removeAll();
    	if (function == null)
    	{
    		scrollPane.updateViewSize();
        	/* update view */
        	scrollPane.revalidate();
        	scrollPane.repaint();
    		return;
    	}
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.anchor = GridBagConstraints.NORTH;
    	gbc.gridwidth = gbc.gridheight = 1;
    	gbc.weightx = 1.0;
    	gbc.gridx = 0;
    	gbc.weighty = 0.0;
    	
    	int count = function.getParametersCount();
    	paramEditors = new ObjectEditor[count];
    	
    	for (int i = 0; i < count; i++)
    	{
    		ObjectEditor editor = ObjectEditorFactory.
    			newObjectEditor(i, function.getParameterName(i), function.getType(i));
    		paramEditors[i] = editor;
    		
    		if (editor == null || editor.getEditor() == null)
    			continue;
    		
    		gbc.gridy = i;
    		gbc.weightx = 0.0;
    		gbc.gridx = 0;
    		JLabel label = new JLabel(function.getParameterUserName(i) + ":",
    						SwingConstants.RIGHT); 
    		paramsPanel.add(label, gbc);
    		
    		gbc.weightx = 1.0;
    		gbc.gridx = 1;
    		if (i + 1 == count)
    		{
    			gbc.weighty = 1.0;
    			gbc.fill = GridBagConstraints.HORIZONTAL;
    		}
    		paramsPanel.add(editor.getEditor(), gbc);
    		editor.setObject(function.getValue(i));
    		editor.addObjectChangeListener(parameterChangeListener);
    	}
    	scrollPane.updateViewSize();
    	/* update view */
    	scrollPane.revalidate();
    	scrollPane.repaint();
    	
    	if (!isEnabled() && paramEditors != null)
        	for (ObjectEditor e: paramEditors)
        		e.getEditor().setEnabled(false);
    }
    
    public void addFunctionChangeListener(FunctionChangeListener l)
    {
    	listenerList.add(FunctionChangeListener.class, l);
    }
    public void fireFunctionChange()
    {
    	Object[] listeners = listenerList.getListenerList();
    	FunctionChangeEvent event = new FunctionChangeEvent(this, functionType,
    			function);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == FunctionChangeListener.class)
    			((FunctionChangeListener)(listeners[i+1])).functionChanged(event);
    }
    public void removeFunctionChangeListener(FunctionChangeListener l)
    {
    	listenerList.remove(FunctionChangeListener.class, l);
    }
    
    public void setEnabled(boolean enabled)
    {
    	functionCombo.setEnabled(enabled);
    	if (paramEditors != null)
        	for (ObjectEditor e: paramEditors)
        		e.getEditor().setEnabled(enabled);
    	super.setEnabled(enabled);
    }
    
    public void setFunction(Function function)
    {
    	lockFunctionChange = true;
    	this.function = function;
    	if (function != null)
    		functionCombo.setSelectedItem(function.getClass().getName());
    	else
    		functionCombo.setSelectedItem("");
    	updateParamsPanel();
    	lockFunctionChange = false;
    }
    
    protected void updateFunctionCombo()
    {
    	Class<?>[] functions = FunctionsLoaderFactory.loader.
    				getFunctionsByType(functionType);
		functionCombo.removeAllItems();
		if (functionType.equals(OutputFilter.class))
			functionCombo.addItem("");
		for (Class<?> f: functions)
			functionCombo.addItem(f.getName());
    }
    
    private class FunctionComboChangeListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if (lockFunctionChange)
    			return;
    		//System.out.println("Function was changed");
    		try
    		{
    			String name = (String)functionCombo.getSelectedItem();
    			if(name != null && !name.equals(""))
    			{
    				functionType = FunctionsLoaderFactory.loader.forName(name);
    				function = (Function)(functionType.getConstructor().newInstance());
    			}
    			else
    				function = null;
    			updateParamsPanel();
    			fireFunctionChange();
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    }
    
    private class ClassPathsChangeListener implements ClassPathsListener
    {
    	public void classPathsChanged(ClassPathsEvent e)
    	{
    		updateFunctionCombo();
    		//System.out.println("Class paths changed");
    	}
    }
    
    private class ParameterChangeListener implements ObjectChangeListener
    {
    	public void objectChanged(ObjectChangeEvent e)
    	{
    		try
    		{ function.setValue(e.getName(), e.getObject()); }
    		catch(UnknownParameterException ex)
    		{ }
    		Object o = e.getObject();
    		
    		if (e.getObject() instanceof char[])
    			System.out.println("changed " + e.getName() + " object " + new String((char[])o));
    		else if (o.getClass().isArray())
    		{
    			System.out.print("[");
    			int length = Array.getLength(o);
    			for (int i = 0; i < length; i++)
    			{
    				System.out.print(Array.get(o, i).toString());
    				if (i + 1 == length)
    					System.out.println("]");
    				else
    					System.out.print(',');
    			}
    		}
    		else
    			System.out.println("changed " + e.getName() + " object " + o);
    		
    		if (!lockFunctionChange)
    			fireFunctionChange();
    	}
    }
}
