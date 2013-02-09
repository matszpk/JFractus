/**
 * FractalFunctionPanel.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Window;
import javax.swing.JTabbedPane;

import jfractus.app.Gradient;
import jfractus.app.GradientLocator;
import jfractus.app.Resources;
import jfractus.app.FractalDocument;
import jfractus.api.*;

public class FractalFunctionPanel extends JTabbedPane
{
    private static final long serialVersionUID = -6221754079588987609L;

    private FractalFunctionTab fractalTab;
    private FractalFunctionTab innerTab;
    private FractalFunctionTab outerTab;
    private FractalFunctionTab planeTab;
    private FractalFunctionTab filterTab;
    private GradientTab gradientTab;
    
    public FractalFunctionPanel(Window owner)
    {
    	createGUI(owner);
    }
    
    protected void createGUI(Window owner)
    {
    	fractalTab = new FractalFunctionTab(FractalFormula.class);
    	innerTab = new FractalFunctionTab(InnerFunction.class);
    	outerTab = new FractalFunctionTab(OuterFunction.class);
    	planeTab = new FractalFunctionTab(PlaneTransform.class);
    	filterTab = new FractalFunctionTab(OutputFilter.class);
    	gradientTab = new GradientTab(owner);
    	
    	setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    	
    	add(Resources.getString("FractalFormula"), fractalTab);
    	add(Resources.getString("InnerFunction"), innerTab);
    	add(Resources.getString("OuterFunction"), outerTab);
    	add(Resources.getString("PlaneTransform"), planeTab);
    	add(Resources.getString("OutputFilter"), filterTab);
    	add(Resources.getString("Gradient"), gradientTab);
    }
    
    public FractalFunctionTab getFractalFormulaTab()
    {
    	return fractalTab;
    }
    public FractalFunctionTab getInnerFunctionTab()
    {
    	return innerTab;
    }
    public FractalFunctionTab getOuterFunctionTab()
    {
    	return outerTab;
    }
    public FractalFunctionTab getPlaneTransformTab()
    {
    	return planeTab;
    }
    public FractalFunctionTab getOutputFilterTab()
    {
    	return filterTab;
    }
    public GradientTab getGradientTab()
    {
    	return gradientTab;
    }
    
    public void setEnabled(boolean enabled)
    {
    	fractalTab.setEnabled(enabled);
    	innerTab.setEnabled(enabled);
    	outerTab.setEnabled(enabled);
    	planeTab.setEnabled(enabled);
    	filterTab.setEnabled(enabled);
    	gradientTab.setEnabled(enabled);
    	//super.setEnabled(enabled);
    }
    
    public void setFunction(Class<?> cls, Function function)
    {
    	Function copy = null;
    	if (function != null)
    		copy = function.copy();
    	if (FractalFormula.class.isAssignableFrom(cls))
			fractalTab.setFunction(copy);
		else if (InnerFunction.class.isAssignableFrom(cls))
			innerTab.setFunction(copy);
		else if (OuterFunction.class.isAssignableFrom(cls))
			outerTab.setFunction(copy);
		else if (OutputFilter.class.isAssignableFrom(cls))
			filterTab.setFunction(copy);
		else if (PlaneTransform.class.isAssignableFrom(cls))
			planeTab.setFunction(copy);
    }
    
    public void setGradientAndColoring(float colorScale, float colorShift,
    		Gradient gradient, GradientLocator locator)
    {
    	gradientTab.setColorScale(colorScale);
    	gradientTab.setColorShift(colorShift);
    	
    	Gradient gradientCopy = gradient.copy();
    	GradientLocator locatorCopy = null;
    	if (locator != null)
    		locatorCopy = locator.copy();
    	gradientTab.setGradient(gradientCopy);
    	gradientTab.setGradientLocator(locatorCopy);
    }

    public void addFunctionChangeListener(FunctionChangeListener l)
    {
    	fractalTab.addFunctionChangeListener(l);
    	innerTab.addFunctionChangeListener(l);
    	outerTab.addFunctionChangeListener(l);
    	planeTab.addFunctionChangeListener(l);
    	filterTab.addFunctionChangeListener(l);
    }
    
    public void removeFunctionChangeListener(FunctionChangeListener l)
    {
    	fractalTab.removeFunctionChangeListener(l);
    	innerTab.removeFunctionChangeListener(l);
    	outerTab.removeFunctionChangeListener(l);
    	planeTab.removeFunctionChangeListener(l);
    	filterTab.removeFunctionChangeListener(l);
    }
    
    public void setFromFractalDocument(FractalDocument fractal)
    {
    	fractalTab.setFunction(fractal.getFractalFormula().copy());
    	innerTab.setFunction(fractal.getInnerFunction().copy());
    	outerTab.setFunction(fractal.getOuterFunction().copy());
    	planeTab.setFunction(fractal.getPlaneTransform().copy());
    	if (fractal.getOutputFilter() != null)
    		filterTab.setFunction(fractal.getOutputFilter().copy());
    	else
    		filterTab.setFunction(null);
    	gradientTab.setColorScale(fractal.getColorScale());
    	gradientTab.setColorShift(fractal.getColorShift());
    	GradientLocator locator = fractal.getGradientLocator();
    	if (locator != null)
    		gradientTab.setGradientLocator(locator.copy());
    	else
    		gradientTab.setGradientLocator(null);
    	gradientTab.setGradient(fractal.getGradient().copy());
    }
}
