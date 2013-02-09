/**
 * RenameGradientEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.app.GradientLocator;

public class RenameGradientEvent extends EventObject
{
	private static final long serialVersionUID = -6389293326903664192L;

	private GradientLocator oldLocator;
	private GradientLocator newLocator;
	
	public RenameGradientEvent(Object source, GradientLocator oldLocator,
			GradientLocator newLocator)
	{
		super(source);
		this.oldLocator = oldLocator;
		this.newLocator = newLocator;
	}
	
	public GradientLocator getOldLocator()
	{
		return oldLocator;
	}
	public GradientLocator getNewLocator()
	{
		return newLocator;
	}
}
