/**
 * GradientEditField.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import jfractus.app.OutOfGradientRangeException;
import jfractus.app.RGBColor;
import jfractus.app.Gradient;
import jfractus.app.UnremovablePointInGradientException;

public class GradientEditField extends JComponent
{
    private static final long serialVersionUID = -6869798432023740221L;

    private class Triangle extends Polygon
    {
    	private static final long serialVersionUID = 5180375021949862578L;
		private Point position;
    	
    	public Triangle(Point position)
    	{
    		addPoint(0, -10);
        	addPoint(5, 0);
        	addPoint(-5, 0);
    		translate(position.x, position.y);
    		this.position = position;
    	}
    	
    	public Point getPosition()
    	{
    		return position;
    	}
    }
    
    private Gradient gradient;
    private Triangle[] triangles;
    
    private int selectedPointIndex;
    
    public GradientEditField()
    {
    	gradient = new Gradient();
    	createGUI();
    }
    public GradientEditField(Gradient gradient)
    {
    	this.gradient = gradient;
    	createGUI();
    }
    
    /* listeners */
    public void addChangeSelectionListener(GradientChangeSelectionListener l)
    {
    	listenerList.add(GradientChangeSelectionListener.class, l);
    }
    public void fireChangeSelection(int selected)
    {
    	Object[] listeners = listenerList.getListenerList();
    	GradientChangeSelectionEvent event =
    		new GradientChangeSelectionEvent(this, selected);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == GradientChangeSelectionListener.class)
    			((GradientChangeSelectionListener)(listeners[i+1])).selectionChanged(event);
    }
    public void removeChangeSelectionListener(GradientChangeSelectionListener l)
    {
    	listenerList.remove(GradientChangeSelectionListener.class, l);
    } 
    
    /* setup GUI */
    protected void createGUI()
    {
    	setMinimumSize(new Dimension(300, 30));
    	setPreferredSize(new Dimension(300, 30));
    	setBorder(new BevelBorder(BevelBorder.LOWERED));
    	
    	addComponentListener(new EditFieldComponentListener());
    	EditFieldMouseListener mouseListener = new EditFieldMouseListener();
    	addMouseListener(mouseListener);
    	addMouseMotionListener(mouseListener);
    }
    
    /* update triangles (gradient points) */
    protected void updateTriangles()
    {
    	Dimension dim = getSize();
    	Insets insets = getBorder().getBorderInsets(this);
    	
    	int pointsNum = gradient.size();
    	triangles = new Triangle[pointsNum];
    	
    	int paintableWidth = dim.width-insets.left-insets.right;
    	for (int i = 0; i < pointsNum; i++)
    	{
    		int point = insets.left +
        			(int)(((float)paintableWidth)*gradient.getPosition(i) + 0.5f);
    		
    		triangles[i] = new Triangle(new Point(point, dim.height-insets.bottom-1));
    	}
    }
    
    public void paintComponent(Graphics gx)
    {
    	Dimension dim = getSize();
    	Graphics2D g = (Graphics2D)gx;
    	
    	if (triangles == null)
    		updateTriangles();
    	
    	Insets insets = getBorder().getBorderInsets(this);
    	Rectangle paintRect = new Rectangle(insets.left, insets.right,
    			dim.width-insets.left-insets.right,
    			dim.height-insets.top-insets.bottom); 
    	
    	int pointsNum = gradient.size();
    	float[] fractions = new float[pointsNum];
    	Color[] colors = new Color[pointsNum];
    	for (int i = 0; i < pointsNum; i++)
    	{
    		Gradient.Point point = gradient.getPoint(i);
    		fractions[i] = point.position;
    		colors[i] = point.color.toAWTColor();
    	}
    	
    	LinearGradientPaint gradPaint = new LinearGradientPaint(
    			new Point2D.Float((float)paintRect.x, 0.0f),
    			new Point2D.Float((float)paintRect.getMaxX(), 0.0f),
    			fractions, colors);
    	
    	g.setPaint(gradPaint);
    	g.fillRect(paintRect.x, paintRect.y, paintRect.width, paintRect.height);
    	
    	/* draw points */
    	for (int i = 0; i < pointsNum; i++)
    	{
    		Color drawColor, fillColor;
    		if (i != selectedPointIndex)
    		{
    			drawColor = Color.BLACK;
    			fillColor = Color.WHITE;
    		}
    		else
    		{
    			fillColor = Color.BLACK;
    			drawColor = Color.WHITE;
    		}
    		
    		g.setColor(fillColor);
    		g.fill(triangles[i]);
    		g.setColor(drawColor);
    		g.draw(triangles[i]);
    	}
    }
    
    public int getSelectedPointIndex()
    {
    	return selectedPointIndex;
    }
    public void setSelectedPointIndex(int index)
    {
    	selectedPointIndex = index;
    	repaint();
    	fireChangeSelection(index);
    }
    
    public void setGradient(Gradient gradient)
    {
    	selectedPointIndex = 0;
    	this.gradient = gradient;
    	updateTriangles();
    	repaint();
    }
    public Gradient getGradient()
    {
    	return gradient;
    }
    
    public void setPoint(Gradient.Point point)
    {
    	try
    	{ gradient.setPoint(point); }
    	catch (OutOfGradientRangeException e)
    	{ }
    }
    
    public void setColorForSelectedPoint(Color color)
    {
    	if (selectedPointIndex >= 0 && selectedPointIndex < gradient.size())
    	{
    		gradient.setColorByIndex(selectedPointIndex, new RGBColor(color));
    		repaint();
    	}
    }
    
    public void addNewPoint()
    {
    	int index = selectedPointIndex;
    	if (index == -1)
    		index = 0;
    	else if (index == gradient.size()-1)
    		index--; /* go to previous point */
    	
    	float newPosition =
    		0.5f*(gradient.getPosition(index+1) + gradient.getPosition(index));
    	RGBColor newColor = new RGBColor();
    	try
    	{
    		gradient.evaluateColor(newPosition, newColor);
    		int newIndex = gradient.setPoint(newPosition, newColor);
    		
    		updateTriangles();
    		repaint();
    		setSelectedPointIndex(newIndex);
    	}
    	catch(OutOfGradientRangeException e)
    	{ }
    }
    
    public void removeSelectedPoint()
    {
    	if (selectedPointIndex == 0 || selectedPointIndex == gradient.size()-1 ||
    			selectedPointIndex == -1)
    		return;
    	try
    	{ gradient.removePoint(selectedPointIndex); }
    	catch(UnremovablePointInGradientException e)
    	{ }
    	
    	updateTriangles();
    	repaint();
    	fireChangeSelection(selectedPointIndex);
    }
    
    
    public void reset()
    {
    	selectedPointIndex = 0;
    	gradient.reset();
    	updateTriangles();
    	repaint();
    	fireChangeSelection(selectedPointIndex);
    }
    /* event listeners */
    
    private class EditFieldComponentListener extends ComponentAdapter
    {
    	public void componentResized(ComponentEvent e)
    	{
    		updateTriangles();
    		repaint();
    	}
    }
    
    private class EditFieldMouseListener extends MouseAdapter
    {
    	private int draggedIndex = -1;
    	private int posXAtTriangle = 0;
    	
    	private float getPositionFromX(int posX)
    	{
    		Dimension dim = getSize();
			Insets insets = GradientEditField.this.getBorder().
					getBorderInsets(GradientEditField.this);
			return (float)(posX-insets.left) / (float)(dim.width-insets.left);
    	}
    	
    	public void mousePressed(MouseEvent e)
    	{
    		if (e.getButton() == MouseEvent.BUTTON1)
    		{
    			int choosen = 0;
    			for (choosen = 0; choosen < triangles.length; choosen++)
    				if (triangles[choosen].contains(e.getPoint()))
    					break;
    			if (choosen < triangles.length)
    			{
    				draggedIndex = choosen;
    				
    				posXAtTriangle = e.getX() - triangles[choosen].getPosition().x;
    				setSelectedPointIndex(choosen);
    			}
    			else
    			{
    				float newPosition = getPositionFromX(e.getX());
    				RGBColor newColor = new RGBColor();
    				try
    				{
    					gradient.evaluateColor(newPosition, newColor);
    					int newSelected = gradient.setPoint(newPosition, newColor);
    					updateTriangles();
    					setSelectedPointIndex(newSelected);
    				}
    				catch (OutOfGradientRangeException ex)
    				{ }
    			}
    		}
    	}
    	
    	public void mouseReleased(MouseEvent e)
    	{
    		/* drop point */
    		if (e.getButton() == MouseEvent.BUTTON1)
    			draggedIndex = -1;
    	}
    	
    	public void mouseDragged(MouseEvent e)
    	{
    		if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK)
    		{
    			/* do not drag first and last */
    			if (draggedIndex == 0 || draggedIndex == triangles.length-1 ||
    					draggedIndex == -1)
    				return;
    			
    			float newPosition = getPositionFromX(e.getX()-posXAtTriangle);
    			if (newPosition <= gradient.getPosition(draggedIndex-1))
    				return;
    			if (newPosition >= gradient.getPosition(draggedIndex+1))
    				return;
    			
    			gradient.setPositionByIndex(draggedIndex, newPosition);
    			updateTriangles();
    			repaint();
    		}
    	}
    }
}
