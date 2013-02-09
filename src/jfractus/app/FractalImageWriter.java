/**
 * FractalImageWriter.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.imageio.event.*;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.event.EventListenerList;

public class FractalImageWriter implements Serializable
{
    private static final long serialVersionUID = -2830135943568437509L;
    
    private EventListenerList listenerList = new EventListenerList();
    
    private FractalRenderer fractalRenderer;
    private BufferedImage image;
    private String filePattern;
    
    public FractalImageWriter(BufferedImage image, String filePattern)
    {
    	this.filePattern = filePattern;
    	this.image = image;
    }
	
    public FractalImageWriter(FractalRenderer renderer, String filePattern)
    {
    	this.filePattern = filePattern;
    	this.fractalRenderer = renderer;
    }
    
    public void addImageWriterProgressListener(ImageWriterProgressListener l)
    {
    	listenerList.add(ImageWriterProgressListener.class, l);
    }
    public void fireImageWriterProgress(double progress)
	{
		Object[] listeners = listenerList.getListenerList();
		ImageWriterProgressEvent event = new ImageWriterProgressEvent(this, progress);
		
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ImageWriterProgressListener.class)
				((ImageWriterProgressListener)listeners[i+1]).updateProgress(event);
	}
    public void removeImageWriterProgressListener(ImageWriterProgressListener l)
    {
    	listenerList.remove(ImageWriterProgressListener.class, l);
    }
    
    public void setFilePattern(String filePattern)
    {
    	this.filePattern = filePattern;
    }
    public String getFilePattern()
    {
    	return filePattern;
    }
    
    public void setFractalRenderer(FractalRenderer renderer)
    {
    	this.fractalRenderer = renderer;
    }
    public FractalRenderer getFractalRenderer()
    {
    	return fractalRenderer;
    }
    
    public void setImage(BufferedImage image)
    {
    	this.image = image;
    }
    public BufferedImage getImage()
    {
    	return image;
    }
    
    private void writeFractalImage(File outFile, String suffix, BufferedImage output,
    		ImageWriteParam param)
		throws IOException
	{
		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(suffix);
		ImageWriter writer = writers.next();
		ProgressListener progressListener = new ProgressListener();
		
		try
		{
    		writer.addIIOWriteProgressListener(progressListener);
    		outFile.delete();
    		ImageOutputStream iios = ImageIO.createImageOutputStream(outFile);
    		writer.setOutput(iios);
    		writer.write(null, new IIOImage(output, null, null), param);
    		
		}
		finally
		{
			writer.removeIIOWriteProgressListener(progressListener);
    		writer.dispose();
		}
	}
    
    public static String getFilenameSuffix(String fileName)
    {
    	String suffix = null;
		{
			int index = fileName.lastIndexOf('.');
			if (index >= 0)
			{
    			suffix = fileName.substring(index+1);
    			suffix.toLowerCase();
			}
		}
		return suffix;
    }
    
    public static ImageWriteParam getDefaultWriteParam(String suffix)
    {
    	Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(suffix);
		ImageWriter writer = writers.next();
		return writer.getDefaultWriteParam();
    }
    
    public void write(ImageWriteParam param) throws IOException
    {
    	//prepareFractal();
		String suffix = getFilenameSuffix(filePattern);
		
		if (image != null)
		{
			File outFile = new File(filePattern);
			writeFractalImage(outFile, suffix, image, param);
			return;
		}
		
		FractalDocument fractal = fractalRenderer.getFractal();
		
		if (fractal.getDocumentType() == FractalDocument.Type.FRAME)
		{
			BufferedImage beforeFiltering = fractalRenderer.renderFractalImage();
			BufferedImage output = null;
			if (fractal.getOutputFilter() != null)
				output = fractalRenderer.filterImage(beforeFiltering);
			else
				output = beforeFiltering;
			File outFile = new File(filePattern);
			writeFractalImage(outFile, suffix, output, param);
		}
		else
		{
			int framesNum = (int)fractal.computeAnimationFramesNumber();
			FractalFrameEvaluator frameEvaluator = fractal.getFrameEvaluator();
			float duration = 1.0f / fractal.getFractalAnimation().getFramesPerSecond();
			
			for (int i = 0; i < framesNum; i++)
			{
				BufferedImage beforeFiltering = fractalRenderer.renderFractalImage(
						frameEvaluator.getFrame(duration));
				BufferedImage output = null;
    			if (fractal.getOutputFilter() != null)
    				output = fractalRenderer.filterImage(beforeFiltering);
    			else
    				output = beforeFiltering;
				String fileName = String.format(Locale.ENGLISH, filePattern, i);
				File outFile = new File(fileName);
				writeFractalImage(outFile, suffix, output, param);
			}
		}
    }
    
    public void write() throws IOException
    {
    	write(null);
    }
    
    private class ProgressListener implements IIOWriteProgressListener
	{

		@Override
        public void imageComplete(ImageWriter source)
        {
			fireImageWriterProgress(100.0);
	    }

		@Override
        public void imageProgress(ImageWriter source, float percentageDone)
        {
			fireImageWriterProgress((double)percentageDone);
	    }

		@Override
        public void imageStarted(ImageWriter source, int imageIndex)
        {
			fireImageWriterProgress(0.0);
	    }

		public void thumbnailComplete(ImageWriter source)
        {
	    }

		public void thumbnailProgress(ImageWriter source, float percentageDone)
        {
	    }

		public void thumbnailStarted(ImageWriter source, int imageIndex,
                int thumbnailIndex)
        {
	    }

		public void writeAborted(ImageWriter source)
        {
	    }
		
	}
}
