/**
 * ObjectChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

public class ObjectChangeEvent extends EventObject
{
    private static final long serialVersionUID = 8645351399469223034L;
    
    private int index;
	private String name;
	private Object object;
	
	public ObjectChangeEvent(Object source, int index, String name, Object value)
	{
		super(source);
		this.index = index;
		this.name = name;
		object = value;
	}
	
	public int getIndex()
	{
		return index;
	}
	public String getName()
	{
		return name;
	}
	public Object getObject()
	{
		return object;
	}
}
