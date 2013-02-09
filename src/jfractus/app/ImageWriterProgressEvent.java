/**
 * ImageWriterProgressEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventObject;

public class ImageWriterProgressEvent extends EventObject
{
	private static final long serialVersionUID = 5139811591862547228L;

	private int frame;
	private int totalFrames;
	private double progress;
	
	public ImageWriterProgressEvent(Object source, double progress)
	{
		super(source);
		frame = totalFrames = 1;
		this.progress = progress;
	}
	
	public ImageWriterProgressEvent(Object source, int frame, int totalFrames, double progress)
	{
		super(source);
		this.frame = frame;
		this.totalFrames = totalFrames;
		this.progress = progress;
	}
	
	public int getFrame()
	{
		return frame;
	}
	public int getTotalFrames()
	{
		return totalFrames;
	}
	
	public double getProgress()
	{
		return progress;
	}
	
	public boolean hasDone()
	{
		return (progress >= 100.0);
	}
}
