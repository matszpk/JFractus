/**
 * FractusPreferencesEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventObject;

public class FractusPreferencesEvent extends EventObject
{
	private static final long serialVersionUID = 3904222715070187284L;

	public enum Change
	{
		THREAD_NUMBER,
		DEFAULT_IMAGE_SIZE,
		DEFAULT_AA_CONFIG,
	}
	
	private Change change; 
	
	public FractusPreferencesEvent(Object source, Change change)
	{
		super(source);
		this.change = change;
	}
	
	public Change getChange()
	{
		return change;
	}
}
