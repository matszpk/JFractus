/**
 * GradientView.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.*;
import javax.swing.border.*;

import jfractus.app.Gradient;

public class GradientView extends JComponent
{
	private static final long serialVersionUID = -3577277471892668904L;
	
	private Gradient gradient;

	public GradientView()
	{
		setGUI();
	}
	public GradientView(Gradient gradient)
	{
		this.gradient = gradient;
		setGUI();
	}
	
	protected void setGUI()
	{
		setBorder(LineBorder.createBlackLineBorder());
	}
	
	public void paintComponent(Graphics gx)
    {
    	Dimension dim = getSize();
    	Graphics2D g = (Graphics2D)gx;
    	
    	Insets insets = getBorder().getBorderInsets(this);
    	Rectangle paintRect = new Rectangle(insets.left, insets.right,
        			dim.width-insets.left-insets.right,
        			dim.height-insets.top-insets.bottom);
    	
    	if (gradient != null)
    	{
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
    	}
    	else
    	{
    		g.setBackground(getBackground());
    		g.setColor(getForeground());
    		g.clearRect(paintRect.x, paintRect.y, paintRect.x + paintRect.width,
    				paintRect.y + paintRect.height);
    		g.drawLine(paintRect.x, paintRect.y, paintRect.x + paintRect.width,
    				paintRect.y + paintRect.height);
    		g.drawLine(paintRect.x + paintRect.width, paintRect.y, paintRect.x,
    				paintRect.y + paintRect.height);
    	}
    }
	
	public void setGradient(Gradient gradient)
    {
    	this.gradient = gradient;
    	repaint();
    }
	
    public Gradient getGradient()
    {
    	return gradient;
    }
}
