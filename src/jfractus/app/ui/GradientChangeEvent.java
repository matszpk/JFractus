/**
 * GradientChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.app.Gradient;
import jfractus.app.GradientLocator;

public class GradientChangeEvent extends EventObject
{
	private static final long serialVersionUID = 6774244043366774958L;
	
	private float colorScale;
	private float colorShift;
	private GradientLocator gradientLocator;
	private Gradient gradient;
	
	public GradientChangeEvent(Object source, float colorScale, float colorShift,
			GradientLocator gradientLocator, Gradient gradient)
	{
		super(source);
		this.colorScale = colorScale;
		this.colorShift = colorShift;
		this.gradientLocator = gradientLocator;
		this.gradient = gradient;
	}
	
	public float getColorScale()
	{
		return colorScale;
	}
	public float getColorShift()
	{
		return colorShift;
	}
	public GradientLocator getGradientLocator()
	{
		return gradientLocator;
	}
	public Gradient getGradient()
	{
		return gradient;
	}
}
