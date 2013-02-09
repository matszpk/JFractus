/**
 * ColorChooserButton.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;

public class ColorChooserButton extends JButton
{
    private static final long serialVersionUID = 1240944681148762435L;
    
    private Color color;
    private Window frame;
    private BufferedImage bufImage;
    private ImageIcon icon;

    public ColorChooserButton(Window window)
    {
    	addActionListener(new ColorChooserButtonActionListener());
    	color = new Color(0);
    	createIcon();
    }
    public ColorChooserButton(Window window, Color color)
    {
    	frame = window;
    	this.color = color;
    	addActionListener(new ColorChooserButtonActionListener());
    	createIcon();
    }
    
    protected void createIcon()
    {
    	bufImage = new BufferedImage(32, 16, BufferedImage.TYPE_3BYTE_BGR);
    	icon = new ImageIcon(bufImage);
    	setIcon(icon);
    }
    
    protected void updateIcon()
    {
    	Graphics2D g = (Graphics2D)(bufImage.getGraphics());
    	g.setColor(color);
    	g.fillRect(0, 0, 32, 16);
    }
    
    public void addColorChangeListener(ColorChangeListener l)
    {
    	listenerList.add(ColorChangeListener.class, l);
    }
    public void fireColorChange(Color color)
    {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == ColorChangeListener.class)
    			((ColorChangeListener)(listeners[i+1])).colorChanged
    					(new ColorChangeEvent(this, color));   
    }
    public void removeColorChangeListener(ColorChangeListener l)
    {
    	listenerList.remove(ColorChangeListener.class, l);
    }
    
    public Color getColor()
    {
    	return color;
    }
    public void setColor(Color color)
    {
    	this.color = color;
    	updateIcon();
    	repaint();
    	fireColorChange(color);
    }
    
    private class ColorChooserButtonActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	Color choosen = JColorChooser.showDialog(frame, "Choose color", color);
        	if (choosen != null)
        		setColor(choosen);
        }
    }
}
