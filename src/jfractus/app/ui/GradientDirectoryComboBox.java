/**
 * GradientDirectoryComboBox.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.event.*;
import javax.swing.JComboBox;

import jfractus.app.GradientLocator;

public class GradientDirectoryComboBox extends JComboBox
{
	private static final long serialVersionUID = 9119923113300794128L;
	private GradientLocator locator;
	private boolean lockItemSelection = false;
	
	public GradientDirectoryComboBox()
	{
		setEditable(true);
		locator = new GradientLocator();
		getEditor().addActionListener(new ComboEditorActionListener());
		addItemListener(new ComboEditorItemListener());
	}
	
	/* listener adding/fire/remove methods */
	public void addDirectoryLocatorListener(GradientLocatorListener l)
	{
		listenerList.add(GradientLocatorListener.class, l);
	}
	public void fireDirectoryLocator(GradientLocator loc)
	{
		Object[] listeners = listenerList.getListenerList();
		GradientLocatorEvent event = new GradientLocatorEvent(this, loc);
		
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == GradientLocatorListener.class)
    			((GradientLocatorListener)(listeners[i+1])).
    				locatorChanged(event);
	}
	public void removeDirectoryLocatorListener(GradientLocatorListener l)
	{
		listenerList.remove(GradientLocatorListener.class, l);
	}
	
	public GradientLocator getDirectoryLocator()
	{
		return locator;
	}
	
	public void setDirectoryLocator(GradientLocator newLoc)
	{
		getEditor().setItem(newLoc.toString());
		locator = newLoc;
		updateComboBox();
	}
	
	protected void updateComboBox()
	{
		lockItemSelection = true;
		removeAllItems();
		GradientLocator parent = locator;
		GradientLocator lastParent = locator;
		while(parent != null)
		{
			addItem(parent.toString());
			parent = parent.getParentLocator();
			if (parent != null)
				lastParent = parent;
		}
		GradientLocator[] roots = GradientLocator.listRoots();
		for (GradientLocator root: roots)
		{
			if (!lastParent.canonicallyEquals(root))
				addItem(root.toString());
		}
		lockItemSelection = false;
	}
	
	/* listeners */
	private class ComboEditorActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			locator.setFromSpec(getEditor().getItem().toString());
			updateComboBox();
			fireDirectoryLocator(locator);
		}
	}
	private class ComboEditorItemListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange() == ItemEvent.SELECTED && !lockItemSelection)
			{
				locator.setFromSpec(getEditor().getItem().toString());
    			//updateComboBox();
    			fireDirectoryLocator(locator);
			}
		}
	}
}
