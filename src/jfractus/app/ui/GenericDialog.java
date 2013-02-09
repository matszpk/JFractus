/**
 * GenericDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Window;
//import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

public class GenericDialog extends JDialog
{
    private static final long serialVersionUID = 5901803776569667067L;

    private JOptionPane optionPane;
    private Object[] buttons;
    private ButtonsActionListener buttonsListener = new ButtonsActionListener();
    private Object response;
    
    protected EventListenerList listenerList = new EventListenerList();
    
    public GenericDialog(Window window)
    {
    	super(window);
    	createGUI(window);
    }
    
    public GenericDialog(Window window, ModalityType modality)
    {
    	super(window, modality);
    	createGUI(window);
    }
    
    public GenericDialog(Window window, String title)
    {
    	super(window, title);
    	createGUI(window);
    }
    
    public GenericDialog(Window window, String title, ModalityType modality)
    {
    	super(window, title, modality);
    	createGUI(window);
    }
    
    public GenericDialog(Window window, String title, Object[] buttons,
    		ModalityType modality)
    {
    	super(window, title, modality);
    	createGUI(window);
    	setButtons(buttons);
    }
    
    public GenericDialog(Window window, String title, Object[] buttons)
    {
    	super(window, title);
    	createGUI(window);
    	setButtons(buttons);
    }
    
    private void createGUI(Window window)
    {
    	optionPane = new JOptionPane();
    	setContentPane(optionPane);
    	setLocation((window.getX() + window.getWidth()) >> 1,
    			(window.getY() + window.getHeight()) >> 1);
    }
    
    public void addResponseListener(ResponseListener l)
    {
    	listenerList.add(ResponseListener.class, l);
    }
    
    public void fireResponse(Object ob)
    {
    	Object[] listeners = listenerList.getListenerList();
    	ResponseEvent event = new ResponseEvent(this, ob);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == ResponseListener.class)
    			((ResponseListener)(listeners[i+1])).response(event);
    }
    
    public void removeResponseListener(ResponseListener l)
    {
    	listenerList.remove(ResponseListener.class, l);
    }
    
    public void setContent(Object ob)
    {
    	optionPane.setMessage(ob);
    }
    
    public Object getContent()
    {
    	return optionPane.getMessage();
    }
    
    public void setButtons(Object[] array)
    {
    	/* removing listener from */
    	{
    		Object[] oldOptions = optionPane.getOptions();
    		if (oldOptions != null)
    			for (Object o: oldOptions)
    				((AbstractButton)o).removeActionListener(buttonsListener);
    	}
    	
    	Object[] dest = new Object[array.length];
    	for (int i = 0; i < array.length; i++)
    		if (!Stock.class.isAssignableFrom(array[i].getClass()))
    			dest[i] = array[i];
    		else
    			dest[i] = new StockButton((Stock)(array[i]));
    	
    	buttons = array;
    	optionPane.setOptions(dest);
    	
    	{
    		Object[] newOptions = optionPane.getOptions();
    		for (Object o: newOptions)
    			((AbstractButton)o).addActionListener(buttonsListener);
    	}
    }
    
    public Object[] getButtons()
    {
    	return buttons;
    }
    
    public Object getResponse()
    {
    	return response;
    }
    
    public Object run()
	{
		setVisible(true);
		dispose();
		return getResponse();
	}
    
    private class ButtonsActionListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
        {
			Object source = e.getSource();
			if (!StockButton.class.isAssignableFrom(source.getClass()))
			{
				response = source;
				fireResponse(source);
			}
			else
			{
				response = ((StockButton)source).getStock();
				fireResponse(((StockButton)source).getStock());
			}
        }
    }
}
