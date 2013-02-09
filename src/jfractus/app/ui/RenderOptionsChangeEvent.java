/**
 * RenderOptionsChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.app.AntialiasConfig;;

public class RenderOptionsChangeEvent extends EventObject
{
	private static final long serialVersionUID = -5499842450056662514L;

	private int imageWidth;
	private int imageHeight;
	private AntialiasConfig aaConfig;
	
	public RenderOptionsChangeEvent(Object source, int imageWidth, int imageHeight,
			AntialiasConfig aaConfig)
	{
		super(source);
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.aaConfig = aaConfig;
	}
	
	public int getImageWidth()
	{
		return imageWidth;
	}
	public int getImageHeight()
	{
		return imageHeight;
	}
	
	public AntialiasConfig getAntialiasConfig()
	{
		return aaConfig;
	}
}
