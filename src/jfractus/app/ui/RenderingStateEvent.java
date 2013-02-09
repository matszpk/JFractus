/**
 * RenderingStateEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

public class RenderingStateEvent extends EventObject
{
	private static final long serialVersionUID = -3550272877954049903L;
	
	public enum State
	{
		BEGINNING,
		FINISHED
	}
	
	private State state;

	public RenderingStateEvent(Object source, State state)
	{
		super(source);
		this.state = state;
	}
	
	public State getState()
	{
		return state;
	}
}
