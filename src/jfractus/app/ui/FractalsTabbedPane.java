/**
 * FractalsTabbedPane.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import javax.swing.JTabbedPane;

public class FractalsTabbedPane extends JTabbedPane
{
    private static final long serialVersionUID = -8761759459547089174L;

    public FractalsTabbedPane()
    {
    	setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    public void addFractal(String name, FractalPanel fractalPanel)
    {
    	add(name, fractalPanel);
    }
    
    public FractalPanel getSelectedFractalPanel()
    {
    	return (FractalPanel)getSelectedComponent();
    }
    public FractalPanel getFractalPanel(int index)
    {
    	return (FractalPanel)getComponentAt(index);
    }
}
