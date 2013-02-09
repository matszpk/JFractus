/**
 * FractalViewer.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;

import jfractus.app.*;
import jfractus.math.*;

public class FractalViewer extends JComponent
{
    private static final long serialVersionUID = -1029155701823518035L;

    private FractalDocument fractal;
    private AntialiasConfig aaConfig;
    FractalRenderer fractalRenderer;
    private int imageWidth;
    private int imageHeight;
    
    private BufferedImage beforeFiltering;
    private BufferedImage finalImage;
    
    private ReentrantLock lock;
    private BufferedImage currentImage;
    
    private RenderWorker currentRenderWorker;
    private ArrayList<Integer> totalFragments; 
    private BitSet filteredFragments;
    private int currentRendererProcess;
    
    private class InteractionStatus
    {
    	private TransformType transformType;
    	private int step;
    	Point[] points = new Point[3];
    	
    	public InteractionStatus()
    	{
    		step = 0;
    	}
    	
    	public void setTransformType(TransformType type)
    	{
    		transformType = type;
    		step = 0;
    		points[0] = points[1] = points[2] = null;
    	}
    	public void reset()
    	{
    		transformType = TransformType.NONE;
    		step = 0;
    		points[0] = points[1] = points[2] = null;
    	}
    	
    	public void setNextPoint(Point p)
    	{
    		if (step < 2 || (step == 2 &&
    			(transformType == TransformType.TRANSFORM ||
    				transformType == TransformType.INVERSE_TRANSFORM)))
    		{
    			points[step] = p;
    			step++;
    		}
    	}
    	public void setPoint(Point p)
    	{
    		int thisStep = (step == 0) ? 0 : step-1;
    		
    		if (thisStep < 2 || (thisStep == 2 &&
    			(transformType == TransformType.TRANSFORM ||
    				transformType == TransformType.INVERSE_TRANSFORM)))
    			points[thisStep] = p;
    	}
    	
    	public int getStep()
    	{
    		return step;
    	}
    	public TransformType getTransformType()
    	{
    		return transformType;
    	}
    	public Point[] getPoints()
    	{
    		return points;
    	}
    	
    	public Rectangle2D getRectangle2D()
    	{
    		double rectX, rectY, rectWidth, rectHeight;
			if (points[0].x < points[1].x)
			{
				rectX = points[0].x;
				rectWidth = points[1].x - points[0].x + 1;
			}
			else
			{
				rectX = points[1].x;
				rectWidth = points[0].x - points[1].x + 1;
			}
			if (points[0].y < points[1].y)
			{
				rectY = points[0].y;
				rectHeight = points[1].y - points[0].y + 1;
			}
			else
			{
				rectY = points[1].y;
				rectHeight = points[0].y - points[1].y + 1;
			}
			return new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
    	}
    	
    	public Rectangle getRectangle()
    	{
    		Rectangle2D rect = getRectangle2D();
    		return new Rectangle((int)(rect.getX()+0.5), (int)(rect.getY() + 0.5),
    				(int)(rect.getWidth() + 0.5), (int)(rect.getHeight() + 0.5));
    	}
    	
    	public Rectangle2D getNormalRectangle2D()
    	{
    		double diffX = points[1].x - points[0].x;
    		double diffY = points[1].y - points[0].y;
    		double newDiffX, newDiffY;
    		if (diffX >= 0.0)
    			diffX++;
    		else
    			diffX--;
    		if (diffY >= 0.0)
    			diffY++;
    		else
    			diffY--;
    		
    		double imageAspect = (double)imageWidth / (double)imageHeight;
    		double rectAspect = Math.abs((double)diffX / (double)diffY);
    		
    		if (rectAspect >= imageAspect)
    		{
    			newDiffX = diffX;
    			newDiffY = Math.abs((double)diffX / imageAspect);
    			if (diffY < 0)
    				newDiffY = -newDiffY;
    		}
    		else
    		{
    			newDiffY = diffY;
    			newDiffX = Math.abs((double)diffY * imageAspect);
    			if (diffX < 0)
    				newDiffX = -newDiffX;
    		}
    		
    		double rectX, rectY, rectWidth, rectHeight;
    		
    		if (newDiffX > 0)
    		{
    			rectX = points[0].x;
    			rectWidth = newDiffX;
    		}
    		else
    		{
    			rectX = points[0].x + newDiffX;
    			rectWidth = -newDiffX;
    		}
    		if (newDiffY > 0)
    		{
    			rectY = points[0].y;
    			rectHeight = newDiffY;
    		}
    		else
    		{
    			rectY = points[0].y + newDiffY;
    			rectHeight = -newDiffY;
    		}
    		
    		return new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
    	}
    	
    	public Rectangle getNormalRectangle()
    	{
    		Rectangle2D rect = getNormalRectangle2D();
    		return new Rectangle((int)(rect.getX()+0.5), (int)(rect.getY() + 0.5),
    				(int)(rect.getWidth() + 0.5), (int)(rect.getHeight() + 0.5));
    	}
    }
    
    private InteractionStatus interactionStatus;
    
    private UndoManager<Operation> undoManager = new UndoManager<Operation>();
    
    private AtomicBoolean renderingFinished = new AtomicBoolean();
    
    public FractalViewer(FractalDocument fractal, UndoManager<Operation> undoManager)
    {
    	imageWidth = FractusPreferencesFactory.prefs.getDefaultImageWidth();
    	imageHeight = FractusPreferencesFactory.prefs.getDefaultImageHeight();
    	aaConfig = FractusPreferencesFactory.prefs.getDefaultAntialiasConfig();
    	
    	this.undoManager = undoManager;
    	
    	lock = new ReentrantLock();
    	totalFragments = new ArrayList<Integer>();
    	this.fractal = fractal;
    	fractalRenderer = new FractalRenderer(imageWidth, imageHeight, aaConfig, fractal);
    	fractalRenderer.addRenderProgressListener(new RenderProgressEventListener());
    	
    	setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    	
    	interactionStatus = new InteractionStatus();
    	
    	MouseEventListener mouseListener = new MouseEventListener();
    	addMouseListener(mouseListener);
    	addMouseMotionListener(mouseListener);
    	
    	getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
		getActionMap().put("Escape", new EscapeKeyAction());
    	
		getInputMap().put(KeyStroke.getKeyStroke('r'), "Rotate");
		getActionMap().put("Rotate", new RotateKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('t'), "Translate");
		getActionMap().put("Translate", new TranslateKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('z'), "RectZoomIn");
		getActionMap().put("RectZoomIn", new RectZoomInKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('x'), "RectZoomOut");
		getActionMap().put("RectZoomOut", new RectZoomOutKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('Z'), "NormalZoomIn");
		getActionMap().put("NormalZoomIn", new NormalZoomInKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('X'), "NormalZoomOut");
		getActionMap().put("NormalZoomOut", new NormalZoomOutKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('f'), "Transform");
		getActionMap().put("Transform", new TransformKeyAction());
		getInputMap().put(KeyStroke.getKeyStroke('g'), "InverseTransform");
		getActionMap().put("InverseTransform", new InverseTransformKeyAction());
		
		setAutoscrolls(true);
    	setComponentSize();
    }
    
    public FractalDocument getFractal()
    {
    	return fractal;
    }
    
    public AntialiasConfig getAntialiasConfig()
    {
    	return aaConfig;
    }
    public int getImageWidth()
    {
    	return imageWidth;
    }
    public int getImageHeight()
    {
    	return imageHeight;
    }
    
    public BufferedImage getRenderedImage()
    {
    	return finalImage;
    }
    
    public void applyRenderOptions(int imageWidth, int imageHeight,
    		AntialiasConfig aaConfig)
    {
    	this.imageWidth = imageWidth;
    	this.imageHeight = imageHeight;
    	this.aaConfig = aaConfig;
    	setComponentSize();
    	doRender();
    }
    
    protected void setComponentSize()
    {
    	Dimension dim = new Dimension(imageWidth, imageHeight);
    	setSize(imageWidth, imageHeight);
    	setMinimumSize(dim);
    	setPreferredSize(dim);
    }
    
    /* fractal Frame Change event listeners methods */
    public void addFractalFrameChangeListener(FractalFrameChangeListener l)
    {
    	listenerList.add(FractalFrameChangeListener.class, l);
    }
    public void fireFractalFrameChange()
    {
    	Object[] listeners = listenerList.getListenerList();
    	FractalFrameChangeEvent event =
    		new FractalFrameChangeEvent(this, fractal.getFractalFrame());
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == FractalFrameChangeListener.class)
    			((FractalFrameChangeListener)(listeners[i+1])).fractalFrameChanged(event);
    }
    public void removeFractalFrameChangeListener(FractalFrameChangeListener l)
    {
    	listenerList.remove(FractalFrameChangeListener.class, l);
    }
    
    public void addFractalInteractionListener(FractalInteractionListener l)
    {
    	listenerList.add(FractalInteractionListener.class, l);
    }
    public void fireInteraction(int step)
    {
    	Object[] listeners = listenerList.getListenerList();
    	FractalInteractionEvent event =
    		new FractalInteractionEvent(this, interactionStatus.getTransformType(), step);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == FractalInteractionListener.class)
    			((FractalInteractionListener)(listeners[i+1])).interactionPerformed(event);
    }
    public void removeFractalInteractionListener(FractalInteractionListener l)
    {
    	listenerList.remove(FractalInteractionListener.class, l);
    }
    
    public void addRenderingStateListener(RenderingStateListener l)
    {
    	listenerList.add(RenderingStateListener.class, l);
    }
    public void fireRenderingState(RenderingStateEvent.State state)
    {
    	Object[] listeners = listenerList.getListenerList();
    	RenderingStateEvent event = new RenderingStateEvent(this, state);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == RenderingStateListener.class)
    			((RenderingStateListener)(listeners[i+1])).renderingFinished(event);
    }
    public void removeRenderingStateListener(RenderingStateListener l)
    {
    	listenerList.remove(RenderingStateListener.class, l);
    }
    
    
    public FractalRenderer getFractalRenderer()
    {
    	return fractalRenderer;
    }
    
    private void paintInteraction(Graphics2D g)
    {
    	TransformType transformType = interactionStatus.getTransformType(); 
    	if (transformType != TransformType.NONE)
    	{
    		g.setXORMode(Color.WHITE);
    		g.setColor(new Color(0, 0, 0, 0));
    		
    		Point[] points = interactionStatus.getPoints();
    		
    		if (interactionStatus.getStep() >= 2)
    		{
    			switch(transformType)
    			{
    			case RECTANGLE_ZOOMIN:
    			case RECTANGLE_ZOOMOUT:
    			{
        			Rectangle rect = interactionStatus.getRectangle();
        			g.drawRect(rect.x, rect.y, rect.width-1, rect.height-1);
        			break;
    			}
    			case NORMAL_ZOOMIN:
    			case NORMAL_ZOOMOUT:
    			{
        			Rectangle rect = interactionStatus.getNormalRectangle();
        			g.drawRect(rect.x, rect.y, rect.width-1, rect.height-1);
        			break;
    			}
    			case TRANSFORM:
    			case INVERSE_TRANSFORM:
    			{
    				g.drawLine(points[0].x, points[0].y, points[1].x, points[1].y);
    				if (interactionStatus.getStep() == 3)
    				{
    					int x2 = points[1].x + points[2].x - points[0].x;
    					int y2 = points[1].y + points[2].y - points[0].y;
    					g.drawLine(points[0].x, points[0].y, points[2].x, points[2].y);
    					g.drawLine(points[1].x, points[1].y, x2, y2);
    					g.drawLine(points[2].x, points[2].y, x2, y2);
    				}
    				break;
    			}
    			case TRANSLATE:
    				g.drawLine(points[0].x, points[0].y, points[1].x, points[1].y);
    				break;
    			case ROTATE:
    			{
    				int distance = (int) (Math.hypot(points[0].x-points[1].x,
    						points[0].y-points[1].y) + 0.5);
    				g.drawLine(points[0].x, points[0].y, points[1].x, points[1].y);
    				g.drawLine(points[0].x, points[0].y, points[0].x + distance, points[0].y);
    				break;
    			}
        		default:
        			break;
    			}
    		}
    	}
    	g.setPaintMode();
    }
    
    public void paintComponent(Graphics gx)
    {
    	Graphics2D g = (Graphics2D)gx;
    	
    	BufferedImage image = null;
    	lock.lock();
    	image = currentImage;
		lock.unlock();
		
    	if (image == null)
    	{	/* default painting */
    		if (finalImage != null)
    			g.drawImage(finalImage, 0, 0, this);
    	}
    	else if (image != null)
    	{	/* progress repaint */
    		lock.lock();
    		if (totalFragments != null)
        	{
        		for (Iterator<Integer> totalIter = totalFragments.iterator();
        				totalIter.hasNext();)
        		{
        			Rectangle rect = fractalRenderer.getFragmentRect(totalIter.next());
        			g.drawImage(image, rect.x, rect.y,
        					rect.x + rect.width, rect.y + rect.height,
        					rect.x, rect.y,
        					rect.x + rect.width, rect.y + rect.height,
        					this);
        		}
        	}
    		if (currentRendererProcess == FractalRenderer.FILTERING_PROCESS)
    		{
    			int fragmentsNum = fractalRenderer.getFragmentsNumber();
    			for (int i = 0; i < fragmentsNum; i++)
    				if (!filteredFragments.get(i))
    				{
    					Rectangle rect = fractalRenderer.getFragmentRect(i);
            			g.drawImage(beforeFiltering, rect.x, rect.y,
            					rect.x + rect.width, rect.y + rect.height,
            					rect.x, rect.y,
            					rect.x + rect.width, rect.y + rect.height,
            					this);
    				}
    		}
    		lock.unlock();
    	}
    	
    	/* draw transform selection */
    	paintInteraction(g);
    }
    
    public class RenderWorker extends SwingWorker<Void, Void>
    {
    	private boolean onlyFilter;
    	
    	public RenderWorker(boolean onlyFilter)
    	{
    		this.onlyFilter = onlyFilter;
    	}

        protected Void doInBackground() throws Exception
        {
        	fireRenderingState(RenderingStateEvent.State.BEGINNING);
        	
        	fractalRenderer.setThreadNumber
        			(FractusPreferencesFactory.prefs.getThreadsNumber());
        	fractalRenderer.setImageSize(imageWidth, imageHeight);
        	fractalRenderer.setAntialiasConfig(aaConfig);

        	if (!onlyFilter)
        		beforeFiltering = fractalRenderer.renderFractalImage();
        	
        	if (fractal.getOutputFilter() != null && !isCancelled())
        	{
        		filteredFragments = new BitSet(fractalRenderer.getFragmentsNumber());
        		finalImage = fractalRenderer.filterImage(beforeFiltering);
        	}
        	else
        		finalImage = beforeFiltering;
	        return null;
        }
        
        protected void done()
        {
        	lock.lock();
        	currentImage = null;
        	lock.unlock();
        	repaint();
        	renderingFinished.set(true);
        	if (!isCancelled())
        		fireRenderingState(RenderingStateEvent.State.FINISHED);        	
        }
    }
    
    public void doRender()
    {
    	if (currentRenderWorker != null)
    		cancelRender();
    	totalFragments.clear();
    	finalImage = null;
    	repaint();
    	renderingFinished.set(false); 
    	currentRenderWorker = new RenderWorker(false);
    	currentRenderWorker.execute();
    }
    
    public void setRenderedImageAsBeforeFilteringImage()
    {
    	finalImage = beforeFiltering;
    	repaint();
    }
    
    public void doFilter()
    {
    	if (fractalRenderer.getRenderingProcess() == FractalRenderer.RENDERING_PROCESS &&
    			!renderingFinished.get())
    		return;
    	
    	if (currentRenderWorker != null)
    		cancelRender();
    	totalFragments.clear();
    	finalImage = null;
    	renderingFinished.set(false); 
    	currentRenderWorker = new RenderWorker(true);
    	currentRenderWorker.execute();
    }
    
    public void cancelRender()
    {
    	currentRenderWorker.cancel(false);
    	fractalRenderer.cancelRender();
    }
    
    public boolean isRenderingFinished()
    {
    	return renderingFinished.get();
    }
    
    
    private void prepareFirstInteractionStep(TransformType type)
    {
    	Point p = getMousePosition();
    	interactionStatus.setTransformType(type);
    	interactionStatus.setNextPoint(p);
		interactionStatus.setNextPoint(new Point(p.x + 1, p.y + 1));
		repaint(interactionStatus.getRectangle());
    	fireInteraction(1);
    }
    /* mouse listener */
    
    private class MouseEventListener extends MouseAdapter
    {
    	 
    	public void mousePressed(MouseEvent e)
    	{
    		if (!isFocusOwner())
    		{
    			grabFocus();
    			return;
    		}
    		
    		int button = e.getButton();
    		
    		int modifiers = e.getModifiersEx();
    		int step = interactionStatus.getStep();
    		if (step == 0)
    		{
    			if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0)
    			{
    				if (button == MouseEvent.BUTTON1)
        				prepareFirstInteractionStep(TransformType.NORMAL_ZOOMIN);
    				else if (button == MouseEvent.BUTTON2)
        				prepareFirstInteractionStep(TransformType.ROTATE);
        			else if (button == MouseEvent.BUTTON3)
        				prepareFirstInteractionStep(TransformType.NORMAL_ZOOMOUT);
        			else
        				return;
    			}
    			else if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0)
    			{
    				if (button == MouseEvent.BUTTON1)
    					prepareFirstInteractionStep(TransformType.TRANSFORM);
    				else if(button == MouseEvent.BUTTON3)
    					prepareFirstInteractionStep(TransformType.INVERSE_TRANSFORM);
    			}
    			else
    			{
        			if (button == MouseEvent.BUTTON1)
        				prepareFirstInteractionStep
        						(TransformType.RECTANGLE_ZOOMIN);
        			else if (button == MouseEvent.BUTTON2)
        				prepareFirstInteractionStep(TransformType.TRANSLATE);
        			else if (button == MouseEvent.BUTTON3)
        				prepareFirstInteractionStep(TransformType.RECTANGLE_ZOOMOUT);
        			else
        				return;
    			}
    		}
    	}
    	
    	public void mouseReleased(MouseEvent e)
    	{
    		int step = interactionStatus.getStep();
    		TransformType transformType = interactionStatus.getTransformType();
    		if (step == 2)
    		{
    			FractalFrame frame = fractal.getFractalFrame();
    			FractalFrame oldFrame = frame.copy();
    			
    			Matrix3D prevTransform = frame.getTransform();
    			double prevCenterPointX = 0.5*(double)(imageWidth);
    			double prevCenterPointY = 0.5*(double)(imageHeight);
    			
    			double scaleFactor = (imageWidth < imageHeight) ? 
    					1.0 / (double)imageWidth : 1.0 / (double)imageHeight;
    			
    			if (transformType == TransformType.RECTANGLE_ZOOMIN ||
    					transformType == TransformType.RECTANGLE_ZOOMOUT ||
    					transformType == TransformType.NORMAL_ZOOMIN ||
    					transformType == TransformType.NORMAL_ZOOMOUT)
    			{
    				Matrix3D transform = new Matrix3D();
    				
    				Rectangle2D rect = null;
    				if (transformType == TransformType.RECTANGLE_ZOOMIN ||
    					transformType == TransformType.RECTANGLE_ZOOMOUT)
    					rect = interactionStatus.getRectangle2D();
    				else
    					rect = interactionStatus.getNormalRectangle2D();
    				
    				double sx = rect.getWidth() / (double)imageWidth;
    				double sy = rect.getHeight() / (double)imageHeight;
    				
    				double newCenterPointX = rect.getX() + 0.5*(rect.getWidth());
    				double newCenterPointY = rect.getY() + 0.5*(rect.getHeight());
    				
    				Vector2D t = new Vector2D(
    						(newCenterPointX-prevCenterPointX) * scaleFactor,
    						(newCenterPointY-prevCenterPointY) * scaleFactor);
    				t.onlyTransform(prevTransform);
    				transform.translateAndScaleMatrix(t.x, -t.y, sx, sy);    				
    				
    				if (transformType == TransformType.RECTANGLE_ZOOMOUT ||
    					transformType == TransformType.NORMAL_ZOOMOUT)
    					transform.inverse();
    				
    				frame.setTransform(transform.onlyTransform(prevTransform));
    			}
    			else if (transformType == TransformType.ROTATE)
    			{
    				Point[] points = interactionStatus.getPoints();
    				double distance = Math.hypot(points[0].x-points[1].x,
    						points[0].y-points[1].y);
    				
    				if (distance == 0.0)
    					return;
    				
    				Matrix3D transform = new Matrix3D();
    				transform.identity();
    				
    				double c = (points[1].x-points[0].x) / distance;
    				double s = (points[0].y-points[1].y) / distance;
    				Matrix3D rotation = new Matrix3D(
    						new Vector3D(c, -s, 0.0),
    						new Vector3D(s, c, 0.0),
    						new Vector3D(0.0, 0.0, 1.0));
    				
    				transform.onlyTransform(rotation);
    				
    				frame.setTransform(transform.onlyTransform(prevTransform));
    			}
    			else if (transformType == TransformType.TRANSLATE)
    			{
    				Point[] points = interactionStatus.getPoints();
    				Vector2D t = new Vector2D(
    						(points[1].x - points[0].x) * scaleFactor,
    						(points[1].y - points[0].y) * scaleFactor);
    				t.onlyTransform(prevTransform);
    				
    				frame.setTransform(prevTransform.translate(t.x, -t.y));
    			}
    			else if (transformType == TransformType.TRANSFORM ||
    				transformType == TransformType.INVERSE_TRANSFORM)
    				interactionStatus.setNextPoint(e.getPoint());
    			
    			if (transformType != TransformType.TRANSFORM &&
    				transformType != TransformType.INVERSE_TRANSFORM)
    			{
        			fireFractalFrameChange();
        			undoManager.doOp(Operation.getFrameOp(oldFrame, frame.copy()));
        			fireInteraction(2);
        			doRender();
        			
        			interactionStatus.reset();
    			}
    		}
    		else if (step == 3 && (transformType == TransformType.TRANSFORM ||
    				transformType == TransformType.INVERSE_TRANSFORM))
    		{
    			FractalFrame frame = fractal.getFractalFrame();
    			FractalFrame oldFrame = frame.copy();
    			Point[] points = interactionStatus.getPoints();
    			
    			Matrix3D prevTransform = frame.getTransform();
    			Matrix3D transform = new Matrix3D();
    			
    			double prevCenterPointX = 0.5*(double)(imageWidth);
    			double prevCenterPointY = 0.5*(double)(imageHeight);
    			
    			double scaleFactor = (imageWidth < imageHeight) ? 
    					1.0 / (double)imageWidth : 1.0 / (double)imageHeight;
    			
    			double newCenterPointX = 0.5*(points[1].x + points[2].x);
    			double newCenterPointY = 0.5*(points[1].y + points[2].y);
    			
    			transform.setColumn2D(0,
    					(points[1].x-points[0].x) / (double)imageWidth,
    					(points[1].y-points[0].y) / (double)imageWidth);
    			transform.setColumn2D(1,
    					(points[2].x-points[0].x) / (double)imageHeight,
    					(points[2].y-points[0].y) / (double)imageHeight);
    			Vector2D t = new Vector2D(
    						(newCenterPointX-prevCenterPointX) * scaleFactor,
    						(newCenterPointY-prevCenterPointY) * scaleFactor);
				t.onlyTransform(prevTransform);
				transform.setColumn(2, t.x, -t.y, 1.0);
    			
				if (transformType == TransformType.INVERSE_TRANSFORM)
					transform.inverse();
				
				frame.setTransform(transform.onlyTransform(prevTransform));
				
    			fireFractalFrameChange();
    			undoManager.doOp(Operation.getFrameOp(oldFrame, frame.copy()));
    			fireInteraction(3);
    			doRender();
    			fireInteraction(3);
    			
    			interactionStatus.reset();
    		}
    	}
    	
    	private void updateTransformAtStep3(MouseEvent e)
    	{
    		Point[] points = interactionStatus.getPoints();
    		
    		int endPointX = points[1].x + points[2].x - points[0].x;
    		int endPointY = points[1].y + points[2].y - points[0].y;
    		
    		int x1 = Math.min(Math.min(points[0].x, points[1].x),
    				Math.min(points[2].x, endPointX));
    		int y1 = Math.min(Math.min(points[0].y, points[1].y),
    				Math.min(points[2].y, endPointY));
    		int x2 = Math.max(Math.max(points[0].x, points[1].x),
    				Math.max(points[2].x, endPointX));
    		int y2 = Math.max(Math.max(points[0].y, points[1].y),
    				Math.max(points[2].y, endPointY));
    		
    		Rectangle rect = new Rectangle(x1, y1, x2-x1+1, y2-y1+1);
    		repaint(rect);
    		interactionStatus.setPoint(e.getPoint());
    		
    		endPointX = points[1].x + points[2].x - points[0].x;
    		endPointY = points[1].y + points[2].y - points[0].y;
    		
    		x1 = Math.min(Math.min(points[0].x, points[1].x),
    				Math.min(points[2].x, endPointX));
    		y1 = Math.min(Math.min(points[0].y, points[1].y),
    				Math.min(points[2].y, endPointY));
    		x2 = Math.max(Math.max(points[0].x, points[1].x),
    				Math.max(points[2].x, endPointX));
    		y2 = Math.max(Math.max(points[0].y, points[1].y),
    				Math.max(points[2].y, endPointY));
    		
    		rect.setBounds(x1, y1, x2-x1+1, y2-y1+1);
    		repaint(rect);
    	}
    	
    	private void processMotion(MouseEvent e)
    	{
    		Rectangle visRect = new Rectangle(e.getX(), e.getY(), 1, 1);
    		((JComponent)e.getSource()).scrollRectToVisible(visRect);
    		
    		int step = interactionStatus.getStep();
    		TransformType transformType = interactionStatus.getTransformType();
    		Point[] points = interactionStatus.getPoints();
    		if (step == 2)
    		{
    			switch(transformType)
    			{
    			case RECTANGLE_ZOOMIN:
    			case RECTANGLE_ZOOMOUT:
    			{
    				repaint(interactionStatus.getRectangle());
    				interactionStatus.setPoint(new Point(e.getX() + 1, e.getY() + 1));
    				//System.out.println("Rect: "  + transformInteraction.getRectangle());
    				repaint(interactionStatus.getRectangle());
    				break;
    			}
    			case NORMAL_ZOOMIN:
    			case NORMAL_ZOOMOUT:
    			{
    				repaint(interactionStatus.getNormalRectangle());
    				interactionStatus.setPoint(new Point(e.getX() + 1, e.getY() + 1));
    				repaint(interactionStatus.getNormalRectangle());
    				break;
    			}
    			case ROTATE:
    			{
    				
    				int distance = (int) (Math.hypot(points[0].x-points[1].x,
    						points[0].y-points[1].y) + 0.5);
    				repaint(interactionStatus.getRectangle());
    				repaint(0, points[0].x, points[1].x, distance+1, 1);
    				interactionStatus.setPoint(e.getPoint());
    				distance = (int) (Math.hypot(points[0].x-points[1].x,
    						points[0].y-points[1].y) + 0.5);
    				repaint(0, points[0].x, points[1].x, distance+1, 1);
    				repaint(interactionStatus.getRectangle());
    				break;
    			}
    			case TRANSFORM:
    			case INVERSE_TRANSFORM:
    			case TRANSLATE:
    			{
    				repaint(interactionStatus.getRectangle());
    				interactionStatus.setPoint(e.getPoint());
    				repaint(interactionStatus.getRectangle());
    				break;
    			}
    			default:
    				break;
    			}
    		}
    		else if (step == 3)
    		{
    			if (transformType == TransformType.TRANSFORM ||
    					transformType == TransformType.INVERSE_TRANSFORM)
    				updateTransformAtStep3(e);
    		}
    	}
    	
    	public void mouseMoved(MouseEvent e)
    	{
    		processMotion(e);
    	}
    	
    	public void mouseDragged(MouseEvent e)
    	{
    		processMotion(e);
    	}
    }
    
    private class EscapeKeyAction extends AbstractAction
    {
		private static final long serialVersionUID = -5009438153779390504L;

		public void actionPerformed(ActionEvent e)
    	{
    		if (interactionStatus.getStep() != 0)
    		{
    			interactionStatus.reset();
    			fireInteraction(0);
    			repaint();
    		}
    	}
    }
    
    private class RotateKeyAction extends AbstractAction
    {
        private static final long serialVersionUID = 4795499212376422475L;
    	
        public void actionPerformed(ActionEvent e)
    	{
        	prepareFirstInteractionStep(TransformType.ROTATE);
    	}
    }
    
    private class TranslateKeyAction extends AbstractAction
    {
    	private static final long serialVersionUID = -8179859988497272556L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.TRANSLATE);
    	}
    }
    
    public class NormalZoomInKeyAction extends AbstractAction
    {
    	private static final long serialVersionUID = -4804713606132973473L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.NORMAL_ZOOMIN);
    	}
    }
    
    private class NormalZoomOutKeyAction extends AbstractAction
    {
        private static final long serialVersionUID = 4678545479244727438L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.NORMAL_ZOOMOUT);
    	}
    }
    
    private class RectZoomInKeyAction extends AbstractAction
    {
    	private static final long serialVersionUID = -7548533740164976906L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.RECTANGLE_ZOOMIN);
    	}
    }
    
    private class RectZoomOutKeyAction extends AbstractAction
    {
        private static final long serialVersionUID = -3132694030019670458L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.RECTANGLE_ZOOMOUT);
    	}
    }
    
    private class TransformKeyAction extends AbstractAction
    {
    	private static final long serialVersionUID = -175348161223080182L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.TRANSFORM);
    	}
    }
    
    private class InverseTransformKeyAction extends AbstractAction
    {
    	private static final long serialVersionUID = -650936744746775719L;

		public void actionPerformed(ActionEvent e)
    	{
    		prepareFirstInteractionStep(TransformType.INVERSE_TRANSFORM);
    	}
    }
    
    /* render progress listener */
    
    private class RenderProgressEventListener implements RenderProgressListener
    {
        public void updateProgress(RenderProgressEvent e)
        {
        	if (e.hasDone())
        		return;
        	
        	ArrayList<Integer> fragments = e.getFragments();
        	lock.lock();
        	totalFragments.addAll(fragments);
        	currentImage = e.getCurrentImage();
        	currentRendererProcess = fractalRenderer.getRenderingProcess();
        	if (currentRendererProcess == FractalRenderer.FILTERING_PROCESS)
        	{
        		for (Iterator<Integer> iter = fragments.iterator(); iter.hasNext();)
        			filteredFragments.set(iter.next(), true);
        	}
        	lock.unlock();
        	
        	if (currentRendererProcess != FractalRenderer.FILTERING_PROCESS)
        		for (Iterator<Integer> iter = fragments.iterator(); iter.hasNext();)
        			repaint(fractalRenderer.getFragmentRect(iter.next()));
        	else
        		repaint();
        }
    }
}
