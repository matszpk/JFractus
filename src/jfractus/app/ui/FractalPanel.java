/**
 * FractalPanel.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Iterator;

import javax.imageio.ImageWriteParam;
import javax.swing.*;

import jfractus.api.*;
import jfractus.app.*;
import jfractus.dom.*;
import jfractus.math.Matrix3D;
import jfractus.math.Vector3D;

public class FractalPanel extends JPanel
{
    private static final long serialVersionUID = -7677498552241093522L;

    private String filePath;
    private FractalDocument fractal;
    
    private JScrollPane scrollPane;
    private FractalViewer fractalViewer;
    private JProgressBar progressBar;
    private JSplitPane splitPane;
    private TransformPanel transformPanel;
    private boolean withTransformPanel;
    private FractalFunctionPanel functionPanel;
    
    private UndoManager<Operation> undoManager = new UndoManager<Operation>();
    private boolean lockUndoPutOperation = false; 
    
    public FractalPanel(FractalFunctionPanel functionPanel)
    {
    	fractal = new FractalDocument();
    	this.functionPanel = functionPanel; 
    	
    	fractal.setFractalFormula(new jfractus.functions.fractals.Mandelbrot());
    	fractal.setInnerFunction(new jfractus.functions.inners.Default());
    	fractal.setOuterFunction(new jfractus.functions.outers.Default());
    	fractal.setPlaneTransform(new jfractus.functions.planes.NoTransform());
    	//fractal.setOutputFilter(new jfractus.functions.filters.Original());
    	
    	setDefaultColoring();
    	
    	createGUI();
    	resetTransform();
    }
    public FractalPanel(FractalFunctionPanel functionPanel, FractalDocument fractal)
    {
    	this.fractal = fractal;
    	this.functionPanel = functionPanel;
    	filePath = null;
    	createGUI();
    	transformPanel.setTransformMatrix(fractal.getFractalFrame().getTransform());
    	doRender();
    }

    public FractalPanel(FractalFunctionPanel functionPanel, File file) throws
    		DOMDocumentParseException, DOMNodeException, IOException, GradientLoadingException
    {
    	fractal = new FractalDocument();
    	this.functionPanel = functionPanel;
		fractal.readFromFile(file);
		filePath = file.getPath();
		fractal.loadGradient();
		createGUI();
		transformPanel.setTransformMatrix(fractal.getFractalFrame().getTransform());
		doRender();
    }
    
    private void createGUI()
    {
    	setLayout(new BorderLayout());
    	scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	fractalViewer = new FractalViewer(fractal, undoManager);
    	
    	transformPanel = new TransformPanel();
    	transformPanel.addTransformChangeListener(new TransformChangeEventListener());
    	fractalViewer.addFractalFrameChangeListener(new FractalFrameChangeEventListener());
    	
    	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, transformPanel);
    	splitPane.setResizeWeight(1.0);
    	splitPane.setMinimumSize(new Dimension(1, 1));
    	
    	scrollPane.setViewportView(fractalViewer);
    	
    	add(splitPane, BorderLayout.CENTER);
    	progressBar = new JProgressBar();
    	progressBar.setStringPainted(true);
    	
    	add(progressBar, BorderLayout.SOUTH);
    	fractalViewer.getFractalRenderer().
    			addRenderProgressListener(new RenderProgressEventListener());
    	
    	withTransformPanel = true;
    }
    
    public void saveToFile(boolean relativePaths)
    		throws DOMDocumentGenerateException, IOException, GradientLoadingException
    {
    	saveToFile(new File(filePath), relativePaths);
    }
    
    public void saveToFile(File file, boolean relativePaths)
    		throws DOMDocumentGenerateException, IOException, GradientLoadingException
    {
    	GradientLocator locator = fractal.getAbsoluteGradientLocator();
    	if (locator != null)
    	{
    		GradientLocator newLocator = null;
    		if (relativePaths)
    			newLocator = locator.getRelativeTo(file.getParent());
    		else
    			newLocator = locator.getAbsoluteLocator();
    		fractal.setOnlyGradientLocator(newLocator);
    	}
    	fractal.writeToFile(file);
    	filePath = file.getPath();
    }
    
    private class ImageSaver extends SwingWorker<Void, Exception>
    {
    	private File file;
    	private BufferedImage image;
    	private ImageWriteParam imageWriteParam;
    	
    	public ImageSaver(File file, BufferedImage image, ImageWriteParam param)
    	{
    		this.file = file;
    		this.image = image;
    		imageWriteParam = param;
    	}

	    protected Void doInBackground() throws Exception
        {
	    	FractalImageWriter imageWriter = new FractalImageWriter(
					image, file.getPath());
	    	imageWriter.addImageWriterProgressListener(new ImageSaverProgressListener());
	    	try
	    	{ imageWriter.write(imageWriteParam); }
	    	catch(Exception e)
	    	{	/* delegate error into swing thread */
	    		publish(e);
	    	}
	    	return null;
        }
	    
	    protected void process(List<Exception> exList)
	    {
	    	for (Iterator<Exception> iter = exList.iterator(); iter.hasNext();)
	    		fireImageSaveError(iter.next());
	    }
    }
    
    private class ImageSaverProgressListener implements ImageWriterProgressListener
    {
    	private final String writingProgressText = Resources.getString("WritingProgress");
        public void updateProgress(ImageWriterProgressEvent e)
        {
        	progressBar.setValue((int)(e.getProgress() + 0.5));
        	progressBar.setString(String.format(writingProgressText +": %03.3f%%",
        			e.getProgress()));
        }
    }
    
    public void addImageSaveErrorListener(ImageSaveErrorListener l)
    {
    	listenerList.add(ImageSaveErrorListener.class, l);
    }
    public void fireImageSaveError(Exception ex)
    {
    	Object[] listeners = listenerList.getListenerList();
    	ImageSaveErrorEvent event = new ImageSaveErrorEvent(this, ex);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == ImageSaveErrorListener.class)
    			((ImageSaveErrorListener)(listeners[i+1])).errorOccured(event);
    }
    public void removeImageSaveErrorListener(ImageSaveErrorListener l)
    {
    	listenerList.remove(ImageSaveErrorListener.class, l);
    }
    
    public void saveImageFile(File file, ImageWriteParam param)
    {
    	ImageSaver imageSaver = new ImageSaver(file, fractalViewer.getRenderedImage(), param);
    	imageSaver.execute();
    }
    
    private void setDefaultColoring()
    {
    	GradientLocator locator = new GradientLocator("builtin:Grayscale.grad");
    	fractal.setGradientLocator(locator);
    	try
    	{ fractal.loadGradient(); }
    	catch (Exception ex)
    	{ ex.printStackTrace(); }
    	
    	fractal.setColorScale(16.0f);
    	fractal.setColorShift(0.0f);
    }

    
    public void resetTransform()
    {
    	fractal.setFractalFrame(new FractalFrame(new Matrix3D(
    			new Vector3D(3.0, 0.0, 0.0),
    			new Vector3D(0.0, 3.0, 0.0),
    			new Vector3D(0.0, 0.0, 1.0))));
    	fractalViewer.fireFractalFrameChange();
    	doRender();
    }
    
    public FractalDocument getFractal()
    {
    	return fractal;
    }

    
    public AntialiasConfig getAntialiasConfig()
    {
    	return fractalViewer.getAntialiasConfig();
    }
    public int getImageWidth()
    {
    	return fractalViewer.getImageWidth();
    }
    public int getImageHeight()
    {
    	return fractalViewer.getImageHeight();
    }
    
    public void applyRenderOptions(int imageWidth, int imageHeight,
    		AntialiasConfig aaConfig)
    {
    	fractalViewer.applyRenderOptions(imageWidth, imageHeight, aaConfig);
    }
    
    public String getFilePath()
    {
    	return filePath;
    }
    
    public void setGradientAndColoring(float colorScale, float colorShift,
    		Gradient gradient, GradientLocator locator)
    {
    	float oldColorScale = fractal.getColorScale();
    	float oldColorShift = fractal.getColorShift();
    	GradientLocator oldLocator;
    	
    	if (fractal.getGradientLocator() != null)
    		oldLocator = fractal.getGradientLocator().copy();
    	else
    		oldLocator = null;
    	Gradient oldGradient = fractal.getGradient();	 
    	
    	fractal.setColorScale(colorScale);
    	fractal.setColorShift(colorShift);
    	if (locator != null)
    	{
    		fractal.setGradientLocator(locator);
    		try
    		{ fractal.loadGradient(); }
    		catch (Exception e)
    		{ }
    	}
    	else fractal.setGradient(gradient);
    	
    	if (!lockUndoPutOperation)
    	{
    		GradientLocator newLocator;
    	
        	if (fractal.getGradientLocator() != null)
        		newLocator = fractal.getGradientLocator().copy();
        	else
        		newLocator = null;
    		
    		undoManager.doOp(Operation.getColoringOp(
    				oldColorScale, oldColorShift, oldLocator, oldGradient,
    				fractal.getColorScale(), fractal.getColorShift(),
    				newLocator, fractal.getGradient()));
    	}
    	fractalViewer.doRender();
    }
    
    public void setFunction(Class<?> type, Function function)
    {
    	Function oldFunction;
    	if (fractal.getFunction(type) != null)
    		oldFunction = fractal.getFunction(type).copy();
    	else
    		oldFunction = null;
    		
    	if (function != null)
    		fractal.setFunction(type, function.copy());
    	else
    		fractal.setFunction(type, null);
    	if (OutputFilter.class.isAssignableFrom(type))
    	{
    		if (function != null)
    			fractalViewer.doFilter();
    		else
    			fractalViewer.setRenderedImageAsBeforeFilteringImage();
    	}
    	else
    		fractalViewer.doRender();
    	
    	if (!lockUndoPutOperation)
    	{
    		Function newFunction;
        	if (fractal.getFunction(type) != null)
        		newFunction = fractal.getFunction(type).copy();
        	else
        		newFunction = null;
    		
        	undoManager.doOp(Operation.getFunctionOp(type, oldFunction, newFunction));
    	}
    }
    
    public boolean isVisibleTransformPanel()
    {
    	return withTransformPanel;
    }
    public void setVisibleTransformPanel(boolean visible)
    {
    	if (!visible)
    	{
    		if (withTransformPanel)
    			splitPane.remove(transformPanel);
    	}
    	else
    	{
    		if (!withTransformPanel)
    			splitPane.add(transformPanel);
    	}
    	withTransformPanel = visible;
    }
    
    public void addFractalFrameChangeListener(FractalFrameChangeListener l)
    {
    	fractalViewer.addFractalFrameChangeListener(l);
    }
    public void removeFractalFrameChangeListener(FractalFrameChangeListener l)
    {
    	fractalViewer.removeFractalFrameChangeListener(l);
    }
    
    public void addFractalInteractionListener(FractalInteractionListener l)
    {
    	fractalViewer.addFractalInteractionListener(l);
    }
    public void removeFractalInteractionListener(FractalInteractionListener l)
    {
    	fractalViewer.removeFractalInteractionListener(l);
    }
    
    public void addRenderingStateListener(RenderingStateListener l)
    {
    	fractalViewer.addRenderingStateListener(l);
    }
    public void removeRenderingStateListener(RenderingStateListener l)
    {
    	fractalViewer.removeRenderingStateListener(l);
    }

    
    public FractalViewer getFractalViewer()
    {
    	return fractalViewer;
    }
    
    public void doRender()
    {
    	fractalViewer.doRender();
    }
    public void doFilter()
    {
    	fractalViewer.doFilter();
    }
    
    public void cancelRender()
    {
    	fractalViewer.cancelRender();
    }
    
    private class RenderProgressEventListener implements RenderProgressListener
    {
    	private final String renderingProgressText =
			Resources.getString("RenderingProgress");
		private final String filteringProgressText =
			Resources.getString("FilteringProgress");
    	
        public void updateProgress(RenderProgressEvent e)
        {
        	if (e.hasDone())
        	{
        		progressBar.setValue(100);
        		return;
        	}
        	progressBar.setValue((int)(e.getProgress() + 0.5));
        	String text = null;
        	switch(e.getProcess())
        	{
        	case RENDERING:
        		text = renderingProgressText;
        		break;
        	case FILTERING:
        		text = filteringProgressText;
        		break;
        	default:
        		text = "";
        		break;
        	}
        	progressBar.setString(String.format(text + ": %03.3f%%", e.getProgress()));
        }
    }
    
    private class FractalFrameChangeEventListener implements FractalFrameChangeListener
    {

		public void fractalFrameChanged(FractalFrameChangeEvent e)
        {
			transformPanel.setTransformMatrix(e.getFractalFrame().getTransform());
	    }
    	
    }
    
    private class TransformChangeEventListener implements TransformChangeListener
    {
    	public void transformChanged(TransformChangeEvent e)
    	{
    		FractalFrame frame = fractal.getFractalFrame();
    		FractalFrame oldFrame = frame.copy();
    		frame.setTransform(e.getMatrix());
    		undoManager.doOp(Operation.getFrameOp(oldFrame, frame.copy()));
    		doRender();
    	}
    }
    
    public BufferedImage getRenderedImage()
    {
    	return fractalViewer.getRenderedImage();
    }
    
    public boolean isRenderingFinished()
    {
    	return fractalViewer.isRenderingFinished();
    }
    
    private void applyOperation(Operation op, boolean useNewValue)
    {
    	Object value = (useNewValue) ? op.getNewValue() : op.getOldValue();
    	
    	lockUndoPutOperation = true;
    	switch(op.getType())
    	{
    	case FUNCTION:
    	{
    		Class<?> type = (Class<?>)op.getIdentifier();
    		Function function = (Function)value; 
    		setFunction(type, function);
    		functionPanel.setFunction(type, function);
    		break;
    	}
    	case COLORING:
    	{
    		ColoringObject coloring = (ColoringObject)value;
    		setGradientAndColoring(coloring.getColorScale(), coloring.getColorShift(),
    				coloring.getGradient(), coloring.getLocator());
    		{
        		functionPanel.setGradientAndColoring(
        				coloring.getColorScale(), coloring.getColorShift(),
        				coloring.getGradient().copy(), coloring.getLocator().copy());
    		}
    		break;
    	}
    	case FRAME:
    	{
    		FractalFrame frame = (FractalFrame)value;
    		fractal.setFractalFrame(frame.copy());
    		transformPanel.setTransformMatrix(frame.getTransform());
    		doRender();
    		break;
    	}
    	}
    	lockUndoPutOperation = false;
    }
    
    public void undo()
    {
    	if (undoManager.hasUndo())
    	{
    		Operation op = undoManager.undoOp();
    		applyOperation(op, false);
    	}
    	//fractalViewer.undo();
    }
    public void redo()
    {
    	if (undoManager.hasRedo())
    	{
    		Operation op = undoManager.redoOp();
    		applyOperation(op, true);
    	}
    	//fractalViewer.redo();
    }
    
    public boolean hasUndo()
    {
    	return undoManager.hasUndo();
    }
    
    public boolean hasRedo()
    {
    	return undoManager.hasRedo();
    }
}
