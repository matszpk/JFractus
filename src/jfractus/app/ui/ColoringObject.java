/**
 * ColoringObject.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.Serializable;

import jfractus.app.Gradient;
import jfractus.app.GradientLocator;

public class ColoringObject implements Serializable
{
	private static final long serialVersionUID = -4398186846178706747L;
	
	private float colorScale;
	private float colorShift;
	private GradientLocator locator;
	private Gradient gradient;

	public ColoringObject(float colorScale, float colorShift, Gradient gradient)
	{
		this.colorScale = colorScale;
		this.colorShift = colorShift;
		locator = null;
		this.gradient = gradient;
	}
	public ColoringObject(float colorScale, float colorShift,
			GradientLocator locator, Gradient gradient)
	{
		this.colorScale = colorScale;
		this.colorShift = colorShift;
		this.locator = locator;
		this.gradient = gradient;
	}
	
	public GradientLocator getLocator()
	{
		return locator;
	}
	public Gradient getGradient()
	{
		return gradient;
	}
	public float getColorScale()
	{
		return colorScale;
	}
	public float getColorShift()
	{
		return colorShift;
	}
}
