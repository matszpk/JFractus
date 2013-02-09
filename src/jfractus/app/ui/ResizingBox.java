/**
 * ResizingBox.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ResizingBox extends JComponent
{
	private static final long serialVersionUID = 6801279075464909980L;
	private Frame owner;
	private Point basePos;
	private int baseWidth;
	private int baseHeight;
	
	public ResizingBox(Frame owner)
	{
		this.owner = owner;
		setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
		setMinimumSize(new Dimension(16, 16));
		setPreferredSize(new Dimension(20, 16));
		MouseEventListener mouseEventListener = new MouseEventListener();
		addMouseListener(mouseEventListener);
		addMouseMotionListener(mouseEventListener);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(0x80c0ff));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private class MouseEventListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
    			baseWidth = owner.getWidth();
    			baseHeight = owner.getHeight();
    			basePos = MouseInfo.getPointerInfo().getLocation();
			}
		}
		public void mouseDragged(MouseEvent e)
		{
			if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
			{
				Point cur = MouseInfo.getPointerInfo().getLocation();
				owner.setSize(baseWidth + cur.x - basePos.x,
					baseHeight + cur.y - basePos.y);
			}
		}
	}
}
