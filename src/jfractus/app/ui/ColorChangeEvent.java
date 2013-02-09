/**
 * ColorChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;
import java.awt.Color;

public class ColorChangeEvent extends EventObject
{
	private static final long serialVersionUID = -3220253617933947440L;
	
	private Color color;
	
	public ColorChangeEvent(Object source, Color color)
	{
		super(source);
		this.color = color;
	}
	
	public Color getColor()
	{
		return color;
	}
}
