/**
 * FractalDocument.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.util.*;

import jfractus.api.Function;
import jfractus.api.FractalFormula;
import jfractus.api.InnerFunction;
import jfractus.api.OuterFunction;
import jfractus.api.OutputFilter;
import jfractus.api.PlaneTransform;

import jfractus.dom.*;

public class FractalDocument extends AbstractDOMDocumentHandler implements Serializable
{
	public static enum Type
	{
		FRAME,
		ANIMATION,
	}
	
    private static final long serialVersionUID = -7814911575530298400L;
    
	private FractalFormula fractalFormula;
	private InnerFunction innerFunction;
	private OuterFunction outerFunction;
	private OutputFilter outputFilter;
	private PlaneTransform planeTransform;
	
	private float colorShift;
	private float colorScale;
	private Gradient gradient;
	private GradientLocator gradientLocator;
	
	private File documentFile;
	
	private FractalFrame fractalFrame;
	private FractalAnimation fractalAnim;
	
	public FractalDocument()
	{
	}
	
	public FractalFormula getFractalFormula()
	{
		return fractalFormula;
	}
	public void setFractalFormula(FractalFormula fractal)
	{
		fractalFormula = fractal;
	}
	
	public InnerFunction getInnerFunction()
	{
		return innerFunction;
	}
	public void setInnerFunction(InnerFunction inner)
	{
		innerFunction = inner;
	}
	
	public OuterFunction getOuterFunction()
	{
		return outerFunction;
	}
	public void setOuterFunction(OuterFunction outer)
	{
		outerFunction = outer;
	}
	
	public OutputFilter getOutputFilter()
	{
		return outputFilter;
	}
	public void setOutputFilter(OutputFilter filter)
	{
		outputFilter = filter;
	}
	
	public PlaneTransform getPlaneTransform()
	{
		return planeTransform;
	}
	public void setPlaneTransform(PlaneTransform plane)
	{
		planeTransform = plane;
	}
	
	public Function getFunction(Class<?> cls)
	{
		if (FractalFormula.class.isAssignableFrom(cls))
			return fractalFormula;
		else if (InnerFunction.class.isAssignableFrom(cls))
			return innerFunction;
		else if (OuterFunction.class.isAssignableFrom(cls))
			return outerFunction;
		else if (OutputFilter.class.isAssignableFrom(cls))
			return outputFilter;
		else if (PlaneTransform.class.isAssignableFrom(cls))
			return planeTransform;
		return null;
	}
	public void setFunction(Function function)
	{
		setFunction(function.getClass(), function);
	}
	
	public void setFunction(Class<?> cls, Function function)
	{
		if (FractalFormula.class.isAssignableFrom(cls))
			fractalFormula = (FractalFormula)function;
		else if (InnerFunction.class.isAssignableFrom(cls))
			innerFunction = (InnerFunction)function;
		else if (OuterFunction.class.isAssignableFrom(cls))
			outerFunction = (OuterFunction)function;
		else if (OutputFilter.class.isAssignableFrom(cls))
			outputFilter = (OutputFilter)function;
		else if (PlaneTransform.class.isAssignableFrom(cls))
			planeTransform = (PlaneTransform)function;
	}
	
	public float getColorShift()
	{
		return colorShift;
	}
	public void setColorShift(float shift)
	{
		colorShift = shift;
	}
	
	public float getColorScale()
	{
		return colorScale;
	}
	public void setColorScale(float scale)
	{
		colorScale = scale;
	}
	
	public GradientLocator getGradientLocator()
	{
		return gradientLocator;
	}
	public GradientLocator getAbsoluteGradientLocator()
	{
		if (documentFile != null)
		{
			if (gradientLocator == null)
				return null;
			return gradientLocator.getAbsoluteFrom(documentFile.getParent());
		}
		else
		{
			if (gradientLocator == null)
				return null;
			return gradientLocator.getAbsoluteLocator();
		}
	}
	public void setGradientLocator(String name)
	{
		gradient = null;
		gradientLocator = new GradientLocator(name);
	}
	public void setGradientLocator(GradientLocator locator)
	{
		gradient = null;
		gradientLocator = locator;
	}
	/* only set gradient Locator without clearing gradient */
	public void setOnlyGradientLocator(String name)
	{
		gradientLocator = new GradientLocator(name);
	}
	public void setOnlyGradientLocator(GradientLocator locator)
	{
		gradientLocator = locator;
	}
	
	public void loadGradient() throws GradientLoadingException
	{
		if (gradientLocator == null)
			return;
		try
		{
    		gradient = new Gradient();
    		GradientLocator dest = getAbsoluteGradientLocator();
    		gradient.readFromStream(dest.getAsInputStream());
		}
		catch (IOException e)
		{ throw new GradientLoadingException("I/O exception in gradient loading"); }
		catch (DOMNodeException e)
		{ throw new GradientLoadingException("DOMNodeException in gradient loading"); }
		catch (DOMDocumentException e)
		{ throw new GradientLoadingException("DOMDocumentException in gradient loading"); }
	}
	
	public Gradient getGradient()
	{
		return gradient;
	}
	public void setGradient(Gradient grad)
	{
		gradientLocator = null;
		gradient = grad; 
	}
	
	public FractalFrame getFractalFrame()
	{
		return fractalFrame;
	}
	public void setFractalFrame(FractalFrame frame)
	{
		fractalAnim = null;
		fractalFrame = frame;
	}
	
	public FractalAnimation getFractalAnimation()
	{
		return fractalAnim;
	}
	public void setFractalAnimation(FractalAnimation anim)
	{
		fractalFrame = null;
		fractalAnim = anim;
	}
	
	public float computeAnimationFramesNumber()
	{
		return fractalAnim.computeFramesNumber();
	}
	
	public FractalFrameEvaluator getFrameEvaluator()
	{
		return fractalAnim.getFrameEvaluator();
	}
	
	public Type getDocumentType()
	{
		return (fractalAnim != null) ? Type.ANIMATION : Type.FRAME;
	}
	
	@Override
    public Node createNode(Document doc)
    {
		Element elem = doc.createElement("fractal");
		generateToNode(elem);
	    return elem;
    }
	
	protected void generateToNode(Node node)
	{
		Document doc = node.getOwnerDocument();
		{
			FunctionDOMHandler ifaceDOMHandler =
				new FunctionDOMHandler(fractalFormula);
			node.appendChild(ifaceDOMHandler.createNode(doc));
		}
		{
			FunctionDOMHandler ifaceDOMHandler =
				new FunctionDOMHandler(innerFunction);
			node.appendChild(ifaceDOMHandler.createNode(doc));
		}
		{
			FunctionDOMHandler ifaceDOMHandler =
				new FunctionDOMHandler(outerFunction);
			node.appendChild(ifaceDOMHandler.createNode(doc));
		}
		if (outputFilter != null)
		{
			FunctionDOMHandler ifaceDOMHandler =
				new FunctionDOMHandler(outputFilter);
			node.appendChild(ifaceDOMHandler.createNode(doc));
		}
		{
			FunctionDOMHandler ifaceDOMHandler =
				new FunctionDOMHandler(planeTransform);
			node.appendChild(ifaceDOMHandler.createNode(doc));
		}
		
		Element colorShiftElem = doc.createElement("colorShift");
		colorShiftElem.setTextContent(String.format(Locale.ENGLISH, "%g", colorShift));
		node.appendChild(colorShiftElem);
		
		Element colorScaleElem = doc.createElement("colorScale");
		colorScaleElem.setTextContent(String.format(Locale.ENGLISH, "%g", colorScale));
		node.appendChild(colorScaleElem);
		
		if(gradientLocator != null)
		{
			Element gradNameElem = doc.createElement("gradientLocator");
			gradNameElem.setTextContent(gradientLocator.toString());
			node.appendChild(gradNameElem);
		}
		else node.appendChild(gradient.createNode(doc));
		
		if (fractalAnim != null)
			node.appendChild(fractalAnim.createNode(doc));
		else
			node.appendChild(fractalFrame.createNode(doc));
	}
	
	@Override
    public void getFromNode(Node node) throws DOMNodeException
    {
		Element elem = (Element)node;
		
		if (elem.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Animation");
		
		NodeList nodeChilds = elem.getChildNodes();
		
		String tmpGradientLocator = null;
		Gradient tmpGradient = new Gradient();
		FractalAnimation tmpAnim = new FractalAnimation();
		FractalFrame tmpFrame = new FractalFrame();
		
		boolean gradientParsed = false;
		boolean gradientLocatorParsed = false;
		boolean frameParsed = false;
		boolean animParsed = false;
		boolean colorShiftParsed = false;
		boolean colorScaleParsed = false;
		
		FunctionDOMHandler fractalDOMHandler =
			new FunctionDOMHandler(FractalFormula.class);
		FunctionDOMHandler innerDOMHandler =
			new FunctionDOMHandler(InnerFunction.class);
		FunctionDOMHandler outerDOMHandler =
			new FunctionDOMHandler(OuterFunction.class);
		FunctionDOMHandler filterDOMHandler =
			new FunctionDOMHandler(OutputFilter.class);
		FunctionDOMHandler planeDOMHandler =
			new FunctionDOMHandler(PlaneTransform.class);
		
		if (nodeChilds != null)
		{
    		for(int i = 0; i < nodeChilds.getLength(); i++)
    		{
    			Node child = nodeChilds.item(i);
    			if (child.getNodeType() == Node.TEXT_NODE ||
    				child.getNodeType() == Node.COMMENT_NODE)
    				continue;
    			
    			if (fractalDOMHandler.isValidNode(child))
    			{
    				if (fractalDOMHandler.getFunction() != null)
    					throw new DOMNodeBadStructureException
    							("Duplicate FractalFormula element");
    				fractalDOMHandler.getFromNode(child);
    			}
    			else if (innerDOMHandler.isValidNode(child))
    			{
    				if (innerDOMHandler.getFunction() != null)
    					throw new DOMNodeBadStructureException
    							("Duplicate InnerFunction element");
    				innerDOMHandler.getFromNode(child);
    			}
    			else if (outerDOMHandler.isValidNode(child))
    			{
    				if (outerDOMHandler.getFunction() != null)
    					throw new DOMNodeBadStructureException
    							("Duplicate OuterFunction element");
    				outerDOMHandler.getFromNode(child);
    			}
    			else if (filterDOMHandler.isValidNode(child))
    			{
    				if (filterDOMHandler.getFunction() != null)
    					throw new DOMNodeBadStructureException
    							("Duplicate OutputFilter element");
    				filterDOMHandler.getFromNode(child);
    			}
    			else if (planeDOMHandler.isValidNode(child))
    			{
    				if (planeDOMHandler.getFunction() != null)
    					throw new DOMNodeBadStructureException
    							("Duplicate PlaneTransform element");
    				planeDOMHandler.getFromNode(child);
    			}
    			else if (tmpGradient.isValidNode(child))
    			{
    				if (gradientLocatorParsed || gradientParsed)
    					throw new DOMNodeBadStructureException
    							("Only one of gradientName or Gradient is permitted");
    				tmpGradient.getFromNode(child);
    				gradientParsed = true;
    			}
    			else if(child.getNodeType() == Node.ELEMENT_NODE &&
    					child.getNodeName().equals("gradientLocator"))
    			{
    				if (gradientLocatorParsed || gradientParsed)
    					throw new DOMNodeBadStructureException
    							("Only one of gradientName or Gradient is permitted");
    				tmpGradientLocator = child.getTextContent();
    				gradientLocatorParsed = true;
    			}
				else if (child.getNodeType() == Node.ELEMENT_NODE &&
						(child.getNodeName().equals("colorShift") ||
						 child.getNodeName().equals("colorScale")))
				{
					float value = 0.0f;
					Scanner scanner = new Scanner(child.getTextContent());
					scanner.useLocale(Locale.ENGLISH);
					
					try
					{ value = scanner.nextFloat(); }
					catch (InputMismatchException e)
					{ throw new DOMNodeParseException
							("Exception at parsing color shift/scale"); }
					catch (NoSuchElementException e)
					{ throw new DOMNodeParseException
							("Exception at parsing color shift/scale"); }
					
					if (scanner.hasNext())
					{ throw new DOMNodeParseException
							("Exception at parsing color shift/scale"); }
					
					if (child.getNodeName().equals("colorShift"))
					{
						if (colorShiftParsed)
							throw new DOMNodeBadStructureException
								("Duplicate colorShift element");
						colorShift = value;
						colorShiftParsed = true;
					}
					else
					{
						if (colorScaleParsed)
							throw new DOMNodeBadStructureException
								("Duplicate colorScale element");
						colorScale = value;
						colorScaleParsed = true;
					}
				}
    			else if (tmpFrame.isValidNode(child))
    			{
    				if (animParsed || frameParsed)
    					throw new DOMNodeBadStructureException
    							("Only one of frame or animation is permitted");
    				tmpFrame.getFromNode(child);
    				frameParsed = true;
    			}
    			else if (tmpAnim.isValidNode(child))
    			{
    				if (animParsed || frameParsed)
    					throw new DOMNodeBadStructureException
    							("Only one of frame or animation is permitted");
    				tmpAnim.getFromNode(child);
    				animParsed = true;
    			}
    			else throw new DOMNodeBadStructureException("Unknown element type");
    		}
    		
    		if (!colorShiftParsed || !colorScaleParsed)
    			throw new DOMNodeBadStructureException
    					("Color scale and color shift is required");
    		
    		if (!gradientLocatorParsed && !gradientParsed)
    			throw new DOMNodeBadStructureException("Gradient definition is required");
    		if (!frameParsed && !animParsed)
    			throw new DOMNodeBadStructureException
    					("Frame on Animation is required");
    		
    		if (fractalDOMHandler.getFunction() == null ||
    			innerDOMHandler.getFunction() == null ||
    			outerDOMHandler.getFunction() == null ||
    			planeDOMHandler.getFunction() == null)
    			throw new DOMNodeBadStructureException
    					("One of required element of function element is not found");
    		
    		if (gradientLocatorParsed)
    		{
    			gradientLocator = new GradientLocator();
    			gradientLocator.setFromSpec(tmpGradientLocator.toString());
    			gradient = null;
    		}
    		else /*if gradientParsed */
    		{
    			gradientLocator = null;
    			gradient = tmpGradient;
    		}
    		
    		fractalFormula = (FractalFormula)fractalDOMHandler.getFunction();
    		innerFunction = (InnerFunction)innerDOMHandler.getFunction();
    		outerFunction = (OuterFunction)outerDOMHandler.getFunction();
    		planeTransform = (PlaneTransform)planeDOMHandler.getFunction();
    		
    		if (filterDOMHandler.getFunction() != null)
    			outputFilter = (OutputFilter)filterDOMHandler.getFunction();
    		
    		if (frameParsed)
    		{
    			fractalFrame = tmpFrame;
    			fractalAnim = null;
    		}
    		else
    		{
    			fractalFrame = null;
    			fractalAnim = tmpAnim;
    		}
		}
		else throw new DOMNodeBadStructureException("Fractal without any element");
	}
	
	@Override
    public boolean isValidNode(Node node)
    {
	    return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName().equals("fractal"));
    }
	
	@Override
	public void readFromFile(File file) throws DOMNodeException,
	        DOMDocumentParseException, IOException
	{
		super.readFromFile(file);
		documentFile = file;
	}
	
	@Override
	public void writeToFile(File file) throws DOMDocumentGenerateException,
	        IOException
	{
		super.writeToFile(file);
		documentFile = file;
	}

	@Override
    public Document createDocument(DOMImplementation impl)
    {
		Document doc = impl.createDocument(null, "fractal", null);
		generateToNode(doc.getDocumentElement());
	    return doc;
    }
}
