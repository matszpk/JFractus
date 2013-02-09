/**
 * HorizontalScrollPane.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;

public class HorizontalScrollPane extends JScrollPane
{
    private static final long serialVersionUID = 1805549509041377883L;
    
    public HorizontalScrollPane()
    {
    	setup();
    }

    public HorizontalScrollPane(Component view)
    {
    	super(view);
    	setup();
    }
    
    public HorizontalScrollPane(Component view, int vsbPolicy, int hsbPolicy)
    {
    	super(view, vsbPolicy, hsbPolicy);
    	setup();
    }
    public HorizontalScrollPane(int vsbPolicy, int hsbPolicy)
    {
    	super(vsbPolicy, hsbPolicy);
    	setup();
    }
    
    private void setup()
    {
    	viewport.addComponentListener(new ScrollPaneComponentListener());
    }
    
    public void updateViewSize()
    {
    	Dimension dim = getViewport().getSize();
		
    	Component view = viewport.getView();
    	
    	Dimension baseDim = null;
    	if (view instanceof Container)
    		baseDim = ((Container)view).getLayout().minimumLayoutSize((Container)view);
    	else
    		baseDim = view.getMinimumSize();

    	Dimension newDim = new Dimension(dim.width, baseDim.height);
		
		view.setMinimumSize(newDim);
		view.setPreferredSize(newDim);
        view.setMaximumSize(newDim);
    }
    
    private class ScrollPaneComponentListener extends ComponentAdapter
    {

		public void componentResized(ComponentEvent e)
        {
			updateViewSize();
        }
    }
}
