/**
 * FractalFrameChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.app.FractalFrame;

public class FractalFrameChangeEvent extends EventObject
{
	private static final long serialVersionUID = 1983685755909544862L;
	
	private FractalFrame fractalFrame; 
	
	public FractalFrameChangeEvent(Object source, FractalFrame fractalFrame)
    {
	    super(source);
	    this.fractalFrame = fractalFrame;
	}

	public FractalFrame getFractalFrame()
	{
		return fractalFrame;
	}
}
