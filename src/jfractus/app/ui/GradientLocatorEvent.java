/**
 * GradientLocatorEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.app.GradientLocator;

public class GradientLocatorEvent extends EventObject
{
	private static final long serialVersionUID = -7925985343166247826L;
	private GradientLocator locator;
	
	public GradientLocatorEvent(Object source, GradientLocator locator)
	{
		super(source);
		this.locator = locator;
	}
	
	public GradientLocator getLocator()
	{
		return locator;
	}
}
