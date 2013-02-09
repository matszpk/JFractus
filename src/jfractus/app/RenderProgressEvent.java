/**
 * RenderProgressEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventObject;

public class RenderProgressEvent extends EventObject
{
    private static final long serialVersionUID = 6189404208443623248L;
    
    public enum Process
    {
    	RENDERING,
    	FILTERING
    }
    
    private boolean done;
    private int frame;
    private int totalFrames;
    private Process process;
    private double progressInPercent;
    private BufferedImage currentImage;
    private ArrayList<Integer> fragments;
    
	public RenderProgressEvent(Object source, Process process, double progress,
			ArrayList<Integer> fragments, BufferedImage image)
	{
		super(source);
		frame = totalFrames = 1;
		done = false;
		this.process = process; 
		progressInPercent = progress;
		this.fragments = fragments;
		currentImage = image;
	}
	
	public RenderProgressEvent(Object source, int frame, int totalFrames,
			Process process, double progress, ArrayList<Integer> fragments,
			BufferedImage image)
	{
		super(source);
		this.frame = frame;
		this.totalFrames = totalFrames;
		done = false;
		this.process = process; 
		progressInPercent = progress;
		this.fragments = fragments;
		currentImage = image;
	}
	
	public RenderProgressEvent(Object source, boolean done)
	{
		super(source);
		this.done = done;
	}
	
	public int getFrame()
	{
		return frame;
	}
	public int getTotalFrames()
	{
		return totalFrames;
	}
	
	public Process getProcess()
	{
		return process;
	}
	
	public ArrayList<Integer> getFragments()
	{
		return fragments;
	}
	public BufferedImage getCurrentImage()
	{
		return currentImage;
	}
	
	public double getProgress()
	{
		return progressInPercent;
	}
	public boolean hasDone()
	{
		return done;
	}
}
