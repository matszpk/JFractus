/**
 * ObjectEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.Serializable;
import java.awt.Component;
import javax.swing.event.EventListenerList;

public abstract class ObjectEditor implements Serializable
{
    private static final long serialVersionUID = 2163385270485303098L;

    private int index;
    private String name;
    protected Class<?> type;
    protected Object object;
    protected EventListenerList listenerList;
    
    protected ObjectEditor(int index, String name, Class<?> type)
    {
    	listenerList = new EventListenerList();
    	this.index = index;
    	this.name = name;
    	this.type = type;
    }
    
    public void addObjectChangeListener(ObjectChangeListener l)
    {
    	listenerList.add(ObjectChangeListener.class, l);
    }
    public void fireObjectChange()
    {
    	Object[] listeners = listenerList.getListenerList();
		ObjectChangeEvent event = new ObjectChangeEvent(this, index, name, object);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == ObjectChangeListener.class)
    			((ObjectChangeListener)(listeners[i+1])).objectChanged(event);
    }
    public void removeObjectChangeListener(ObjectChangeListener l)
    {
    	listenerList.remove(ObjectChangeListener.class, l);
    }
    
    public Object getObject()
    {
    	return object;
    }
    public void setObject(Object ob)
    {
    	object = ob;
    	fireObjectChange();
    }
    public Class<?> getType()
    {
    	return type;
    }
    
    public abstract Component getEditor();
}
