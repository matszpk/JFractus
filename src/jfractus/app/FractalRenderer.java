/**
 * FractalRenderer.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.awt.Rectangle;
import java.awt.image.*;

import javax.swing.event.EventListenerList;

import jfractus.api.*;
import jfractus.math.*;

public class FractalRenderer implements Serializable
{
    private static final long serialVersionUID = 4000312316803304018L;
    
    private static final int FRAGMENT_WIDTH = 16;
    private static final int FRAGMENT_HEIGHT = 16;
    
	private FractalDocument fractal;
	private AntialiasConfig antialiasConfig;
	private int outWidth, outHeight;
	
	private int threadsNum;
	
	private int fragmentsPerColumn;
	private int fragmentsPerRow;
	private int fragmentsNumInImage;
	private int aaFragWidth, aaFragHeight;
	private int aaOutWidth, aaOutHeight;
	private float colorScale, colorShift;
	
	public static int RENDERING_PROCESS = 1;
	public static int FILTERING_PROCESS = 2;
	public static int PROCESS_DONE = 3;
	
	private AtomicInteger renderProcess = new AtomicInteger();
	
	private BufferedImage currentImage;
	
	private ReentrantLock renderCancelLock;
	private Condition renderCancelCond;
	private boolean renderCancel;
	
	private AbstractRenderThread.SharedData sharedData = 
		new AbstractRenderThread.SharedData();
	
	private EventListenerList listenerList = new EventListenerList(); 
	
	public FractalRenderer()
	{
		sharedData.reset();
		initializeLocks();
	}
	
	public FractalRenderer(FractalDocument fractal)
	{
		this.fractal = fractal;
		sharedData.reset();
		initializeLocks();
	}
	
	public FractalRenderer(int width, int height, AntialiasConfig aaConfig,
			FractalDocument fractal)
	{
		outWidth = width;
		outHeight = height;
		this.antialiasConfig = aaConfig;
		this.fractal = fractal;
		sharedData.reset();
		updateVariables();
		initializeLocks();
	}
	
	private void initializeLocks()
	{
		renderCancelLock = new ReentrantLock();
		renderCancelCond = renderCancelLock.newCondition();
	}
	
	public void addRenderProgressListener(RenderProgressListener l)
	{
		listenerList.add(RenderProgressListener.class, l);
	}
	public void fireRenderProgress(RenderProgressEvent.Process process, double progress)
	{
		Object[] listeners = listenerList.getListenerList();
		ArrayList<Integer> fragments = new ArrayList<Integer>();
		
		sharedData.getFragmentQueue().drainTo(fragments);
		RenderProgressEvent event =
			new RenderProgressEvent(this, process, progress, fragments, currentImage);
		
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == RenderProgressListener.class)
				((RenderProgressListener)listeners[i+1]).updateProgress(event);
	}
	public void fireRenderProgressDone()
	{
		Object[] listeners = listenerList.getListenerList();
		RenderProgressEvent event = new RenderProgressEvent(this, true);
		
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == RenderProgressListener.class)
				((RenderProgressListener)listeners[i+1]).updateProgress(event);
	}
	
	public void removeRenderProgressListener(RenderProgressListener l)
	{
		listenerList.remove(RenderProgressListener.class, l);
	}
	
	public FractalDocument getFractal()
	{
		return fractal;
	}
	public void setFractal(FractalDocument fractal)
	{
		this.fractal = fractal;
	}
	
	public int getThreadsNumber()
	{
		return threadsNum;
	}
	public void setThreadNumber(int number)
	{
		threadsNum = number;
	}
	
	public AntialiasConfig getAntiAliasConfig()
	{
		return antialiasConfig;
	}
	public void setAntialiasConfig(AntialiasConfig aaConf)
	{
		antialiasConfig = aaConf;
		updateVariables();
	}
	
	public int getImageWidth()
	{
		return outWidth;
	}
	public int getImageHeight()
	{
		return outHeight;
	}
	public void setImageSize(int width, int height)
	{
		this.outWidth = width;
		this.outHeight = height;
		updateVariables();
	}
	
	public Rectangle getFragmentRect(int index)
	{
		int y = index / fragmentsPerColumn;
		int x = index - fragmentsPerColumn*y;
		
		x *= FRAGMENT_WIDTH;
		y *= FRAGMENT_HEIGHT;
		
		int width = (x + FRAGMENT_WIDTH <= outWidth) ? FRAGMENT_WIDTH :
				outWidth-x;
		int height = (y + FRAGMENT_HEIGHT <= outHeight) ? FRAGMENT_HEIGHT :
			outHeight-y;
		return new Rectangle(x, y, width, height);
	}
	
	public int getFragmentsNumber()
	{
		return fragmentsNumInImage;
	}
	
	/* get current shared Image between threads */
	public BufferedImage geCurrentImage()
	{
		return currentImage;
	}
	
	
	private void updateVariables()
	{
		if (antialiasConfig.getMethod() == AntialiasConfig.Method.NORMAL)
		{
    		aaFragWidth = FRAGMENT_WIDTH*antialiasConfig.getSamplingWidth();
    		aaFragHeight = FRAGMENT_WIDTH*antialiasConfig.getSamplingHeight();
    		aaOutWidth = outWidth*antialiasConfig.getSamplingWidth();
    		aaOutHeight = outHeight*antialiasConfig.getSamplingHeight();
		}
		else
		{
			aaFragWidth = FRAGMENT_WIDTH;
    		aaFragHeight = FRAGMENT_WIDTH;
    		aaOutWidth = outWidth;
    		aaOutHeight = outHeight;
		}
		
		fragmentsPerColumn = (outWidth/FRAGMENT_WIDTH);
		fragmentsPerRow = (outHeight/FRAGMENT_HEIGHT);
		
		if (fragmentsPerColumn * FRAGMENT_WIDTH != outWidth)
			fragmentsPerColumn++;
		if (fragmentsPerRow * FRAGMENT_HEIGHT != outHeight)
			fragmentsPerRow++;
		
		fragmentsNumInImage = fragmentsPerColumn * fragmentsPerRow;
	}
	
	public void prepareFractal() throws GradientLoadingException
	{
		if (fractal.getGradient() == null)
		{
			if (fractal.getGradientLocator() != null)
				fractal.loadGradient();
		}
	}
	
	public BufferedImage renderFractalImage()
	{
		return renderFractalImage(fractal.getFractalFrame());
	}
	
	protected class MainRenderThread extends AbstractRenderThread
	{		
		private Matrix3D baseMatrix;
		
		private Complex[] inZData; /* input transforms coordinates map */
		private Complex[] outZData; /* input transforms coordinates map */
		private int[] itData; /* iterations number data */
		private float[] mapData; /* coloring value data */
		private RGBColor[] renderedData;
		private int[] outData; /* output image data */
		//private BufferedImage outputImage;
		
		private PlaneTransform planeTransform;
		private FractalFormula fractalFormula;
		private InnerFunction innerFunction;
		private OuterFunction outerFunction;
		private Gradient gradient;
		
		public MainRenderThread(FractalFrame frame, int[] outData)
		{
			//this.outputImage = output;
			this.outData = outData;
			
			gradient = fractal.getGradient();
			planeTransform = fractal.getPlaneTransform();
			fractalFormula = fractal.getFractalFormula();
			innerFunction = fractal.getInnerFunction();
			outerFunction = fractal.getOuterFunction();
			
			inZData = new Complex[aaFragWidth*aaFragHeight];
			outZData = new Complex[aaFragWidth*aaFragHeight];
			itData = new int[aaFragWidth*aaFragHeight];
			mapData = new float[aaFragWidth*aaFragHeight];
			renderedData = new RGBColor[aaFragWidth*aaFragHeight];
			//outData = new int[FRAGMENT_WIDTH*FRAGMENT_HEIGHT];
			
			for (int i = 0; i < aaFragWidth*aaFragHeight; i++)
			{
				inZData[i] = new Complex();
				outZData[i] = new Complex();
				renderedData[i] = new RGBColor();
			}
			
			baseMatrix = frame.getTransform().copy();
			/* only scale kernel, not translations */
			baseMatrix.onlyScale(1.0, -1.0);
			
			double scaleFactor = (outWidth < outHeight) ? 
					1.0 / (double)outWidth : 1.0 / (double)outHeight;
					
			Vector2D translation = new Vector2D(-0.5*(float)aaOutWidth,
					-0.5*(float)aaOutHeight);
			
			//Vector3D baseVector =
			if (antialiasConfig.getMethod() == AntialiasConfig.Method.NORMAL)
			{
				double sX = scaleFactor / (float)antialiasConfig.getSamplingWidth();
				double sY = scaleFactor / (float)antialiasConfig.getSamplingHeight();
				
				baseMatrix.m[0] *= sX;
				baseMatrix.m[3] *= sX;
				baseMatrix.m[1] *= sY;
				baseMatrix.m[4] *= sY;
			}
			else
				baseMatrix.onlyScale(scaleFactor, scaleFactor);
			
			translation.transform(baseMatrix);
			baseMatrix.setColumn(2, translation.x, translation.y, 1.0);
		}
		
		public void sampleRender(int startX, int startY, int width, int height)
		{
			int sampWidth = antialiasConfig.getSamplingWidth();
			int sampHeight = antialiasConfig.getSamplingHeight();
			int aaWidth = width*sampWidth;
			//int aaHeight = height*sampHeight;
			
			if (antialiasConfig.getMethod() == AntialiasConfig.Method.NORMAL &&
					(antialiasConfig.getSamplingWidth() != 1 ||
					 antialiasConfig.getSamplingHeight() != 1))
			{
				float scaleRGB = 1.0f / (float)(sampWidth * sampHeight);
    			for (int y = 0; y < height; y++)
    				for (int x = 0; x < width; x++)
    				{
    					float r = 0.0f, g = 0.0f, b = 0.0f;
    					int pos = (y*aaWidth*sampHeight + x*sampWidth);
    					int outPos = ((y+startY)*outWidth + x + startX);
    					for (int aaY = 0; aaY < sampHeight; aaY++)
    						for (int aaX = 0; aaX < sampWidth; aaX++)
    						{
    							RGBColor pixel = renderedData[pos + aaY*aaWidth + aaX];
    							r += pixel.red;
    							g += pixel.green;
    							b += pixel.blue;
    						}
    					r *= scaleRGB;
    					g *= scaleRGB;
    					b *= scaleRGB;
    					outData[outPos] = 0xff000000 |
    							(((int)(r * 255.0f + 0.5f))<<16) |
    							(((int)(g * 255.0f + 0.5f))<<8) |
    							((int)(b * 255.0f + 0.5f));
    				}
			}
			else
			{
				//System.out.println(width + "x" + height);
				for (int y = 0; y < height; y++)
    				for (int x = 0; x < width; x++)
    				{
    					int pos = ((startY+y)*outWidth + x + startX);
    					RGBColor pixel = renderedData[y*width+x];
    					outData[pos] = 0xff000000 |
    							(((int)(pixel.red * 255.0f + 0.5f))<<16) |
    							(((int)(pixel.green * 255.0f + 0.5f))<<8) |
    							((int)(pixel.blue * 255.0f + 0.5f));
    				}
			}
		}
		
		public void renderFragment(int index)
		{
			Rectangle fragRect = getFragmentRect(index);
			
			int aaX;
			int aaY;
			
			if (antialiasConfig.getMethod() == AntialiasConfig.Method.NORMAL)
			{
				aaX = fragRect.x * antialiasConfig.getSamplingWidth();
				aaY = fragRect.y * antialiasConfig.getSamplingHeight();
			}
			else
			{
				aaX = fragRect.x;
				aaY = fragRect.y;
			}
			
			int aaWidth;
			int aaHeight;
			
			if (antialiasConfig.getMethod() == AntialiasConfig.Method.NORMAL)
			{
				aaWidth = fragRect.width*antialiasConfig.getSamplingWidth();
				aaHeight = fragRect.height*antialiasConfig.getSamplingHeight();
			}
			else
			{
				aaWidth = fragRect.width;
				aaHeight = fragRect.height;
			}
			
			Matrix3D pixelTransform = baseMatrix.copy();
			Vector2D vec = new Vector2D(aaX, aaY);
			vec.transform(pixelTransform);
			pixelTransform.setColumn(2, vec.x, vec.y, 1.0);
			//pixelTransform.translate(x, y);
			
			int length = aaWidth*aaHeight;
			
			/* matrix transforms data computations */
			{
				Complex zStart = new Complex(pixelTransform.m[2], pixelTransform.m[5]);
        		Complex xBase = new Complex(pixelTransform.m[0], pixelTransform.m[3]);
        		Complex yBase = new Complex(pixelTransform.m[1], pixelTransform.m[4]);
        		Complex xInc = new Complex(0.0, 0.0);
        		Complex yInc = new Complex(0.0, 0.0);
        		Complex xz = new Complex(0.0, 0.0);
        		
        		for (int y = 0; y < aaHeight; y++)
        		{
        			yInc.set(yBase);
        			yInc.multiply((double)y);
        			for (int x = 0; x < aaWidth; x++)
        			{
        				xInc.set(xBase);
        				xInc.multiply((double)x);
        				xInc.add(yInc);
        				xz.set(zStart);
        				xz.add(xInc);
        				inZData[aaWidth*y + x].set(xz);
        			}
        		}
			}
			
			planeTransform.compute(length, inZData);
			fractalFormula.compute(length, itData, inZData, outZData);
			innerFunction.compute(length, itData, inZData, mapData);
			outerFunction.compute(length, itData, outZData, mapData);
			
			float cScale = 1.0f / colorScale;
			float cShift = colorShift / colorScale;
			for(int tmpY = 0; tmpY < aaHeight; tmpY++)
				for(int tmpX = 0; tmpX < aaWidth; tmpX++)
				{
					int pos = tmpY*aaWidth + tmpX;
					float gradPos = (itData[pos] >= 0) ?
							(mapData[pos]*cScale + cShift) : mapData[pos];
					gradPos %= 1.0f;
					gradient.evaluateColor(gradPos, renderedData[pos]);
				}
			
			sampleRender(fragRect.x, fragRect.y, fragRect.width, fragRect.height);
		}
		
		public int[] getImageData()
		{
			return outData;
		}
	}
	
	protected class OutputFilterThread extends AbstractRenderThread
	{
		private int[] inData;
		private int[] outData;
		
		private OutputFilter outFilter;
		
		public OutputFilterThread(int[] inData, int[] outData)
		{
			this.inData = inData;
			this.outData = outData;
			
			outFilter = fractal.getOutputFilter();
		}
		
		public void renderFragment(int index)
		{
			Rectangle fragRect = getFragmentRect(index);
			outFilter.compute(outWidth, outHeight, fragRect.x, fragRect.y,
					fragRect.width, fragRect.height, inData, outData);
		}
	}
	
	private void manageRenderWork(FractalFrame frame, BufferedImage output)
	{
		AtomicInteger globalFragmentIndex = sharedData.getGlobalFragmentIndex();
		ReentrantLock finishLock = sharedData.getFinishLock();
		Condition finishCond = sharedData.getFinishCond();
		sharedData.setFragmentsNumber(fragmentsNumInImage);
		
		long time = System.nanoTime();
		
		int[] outData = null;
		
		sharedData.getFragmentQueue().clear();
		
		WritableRaster raster = output.getRaster();
		DataBufferInt dBuffer = ((DataBufferInt)raster.getDataBuffer());
		outData = dBuffer.getData();
		currentImage = output;
		
		MainRenderThread[] renderThreads = new MainRenderThread[threadsNum];
		
		for(int i = 0; i < threadsNum; i++)
		{
			renderThreads[i] = new MainRenderThread(frame, outData);
			renderThreads[i].setSharedData(sharedData);
			renderThreads[i].start();
		}
		
		finishLock.lock();
		try
		{
			while(globalFragmentIndex.get() < fragmentsNumInImage)
			{
				finishCond.await(100, TimeUnit.MILLISECONDS);
				int x = globalFragmentIndex.get();
				x = (fragmentsNumInImage < x) ? fragmentsNumInImage : x;
				fireRenderProgress(RenderProgressEvent.Process.RENDERING,
						100.0 * (double)x / (double)fragmentsNumInImage);
				
				/* if canceling */
				if (sharedData.getCancelIndicator().get())
					break;
    		}
		}
		catch(InterruptedException e)
		{ }
		finally
		{ finishLock.unlock(); }
		
		try
		{
			for (MainRenderThread t: renderThreads)
				t.join();
		}
		catch(InterruptedException e)
		{ }
		
		long endTime = System.nanoTime();
		
		System.out.printf("Total Time: %f\n", (double)(endTime-time)*1.0e-9);
	}
	
	private void manageFilterWork(BufferedImage input, BufferedImage output)
	{
		AtomicInteger globalFragmentIndex = sharedData.getGlobalFragmentIndex();
		ReentrantLock finishLock = sharedData.getFinishLock();
		Condition finishCond = sharedData.getFinishCond();
		sharedData.setFragmentsNumber(fragmentsNumInImage);
		
		sharedData.getFragmentQueue().clear();
		sharedData.reset();
		
		int[] inData = null;
		{
			WritableRaster inputRaster = input.getRaster();
			DataBufferInt inputDBuffer = ((DataBufferInt)inputRaster.getDataBuffer());
			inData = inputDBuffer.getData();
		}
		
		WritableRaster raster = output.getRaster();
		DataBufferInt dBuffer = ((DataBufferInt)raster.getDataBuffer());
		int[] outData = dBuffer.getData();
		currentImage = output;
		
		OutputFilterThread[] filterThreads = new OutputFilterThread[threadsNum];
		
		for(int i = 0; i < threadsNum; i++)
		{
			filterThreads[i] = new OutputFilterThread(inData, outData);
			filterThreads[i].setSharedData(sharedData);
			filterThreads[i].start();
		}
		
		finishLock.lock();
		try
		{
			while(globalFragmentIndex.get() < fragmentsNumInImage)
			{
				finishCond.await(100, TimeUnit.MILLISECONDS);
				int x = globalFragmentIndex.get();
				x = (fragmentsNumInImage < x) ? fragmentsNumInImage : x;
				fireRenderProgress(RenderProgressEvent.Process.FILTERING,
						100.0 * (double)x / (double)fragmentsNumInImage);
				
				/* if canceling */
				if (sharedData.getCancelIndicator().get())
					break;
    		}
		}
		catch(InterruptedException e)
		{ }
		finally
		{ finishLock.unlock(); }
		
		try
		{
			for (OutputFilterThread t: filterThreads)
				t.join();
		}
		catch(InterruptedException e)
		{ }
	}
	
	public BufferedImage renderFractalImage(FractalFrame frame)
	{
		sharedData.reset();
		renderProcess.set(RENDERING_PROCESS);
		renderCancel = false;
		colorScale = fractal.getColorScale();
		colorShift = fractal.getColorShift();
		
		BufferedImage output = new BufferedImage(outWidth, outHeight,
				BufferedImage.TYPE_INT_RGB);
		manageRenderWork(frame, output);
		
		fireRenderProgressDone();
		
		renderCancelLock.lock();
		renderCancel = true;
		renderCancelCond.signal();
		renderCancelLock.unlock();
		return output;
	}
	
	public BufferedImage filterImage(BufferedImage image)
	{
		sharedData.reset();
		renderCancel = false;
		renderProcess.set(FILTERING_PROCESS);
		
		BufferedImage output = new BufferedImage(outWidth, outHeight,
				BufferedImage.TYPE_INT_RGB);
		manageFilterWork(image, output);
		
		fireRenderProgressDone();
		
		renderCancelLock.lock();
		renderCancel = true;
		renderCancelCond.signal();
		renderCancelLock.unlock();
		return output;
	}
	
	public void cancelRender()
	{
		sharedData.getCancelIndicator().set(true);
		try
		{
    		renderCancelLock.lock();
    		while(!renderCancel)
    			renderCancelCond.await();
    		renderCancelLock.unlock();
		}
		catch(InterruptedException e)
		{ }
	}

	public int getRenderingProcess()
	{
		return renderProcess.get();
	}
}
