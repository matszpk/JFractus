/**
 * FractalKeyFrame.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.Serializable;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import jfractus.math.Matrix3D;

import jfractus.dom.DOMNodeBadValueException;
import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMNodeBadStructureException;
import jfractus.dom.DOMNodeParseException;

public class FractalKeyFrame extends FractalFrame implements Serializable
{
    private static final long serialVersionUID = -2417615759182894690L;

    private float interval;
    
    public FractalKeyFrame()
    {
    	interval = 0.0f;
    }
    public FractalKeyFrame(float interval, Matrix3D transform)
    {
    	super(transform);
    	this.interval = interval;
    }
    
    public float getInterval()
    {
    	return interval;
    }
    public void setInterval(float interval)
    {
    	this.interval = interval;
    }

    
    public FractalFrame evaluateFrame(FractalFrame frame2, float time)
    {
    	Matrix3D evaluated = frame2.transform;
    	/* interpolation */
    	evaluated.subtract(transform);
    	evaluated.scale(time / interval);
    	evaluated.add(transform);
    	
    	return new FractalFrame(evaluated);
    }
    
	@Override
	public Node createNode(Document doc)
	{
		Element elem = doc.createElement("keyFrame");
		Element intervalElem = doc.createElement("interval");
		intervalElem.setTextContent(String.format(Locale.ENGLISH, "%g", interval));
		elem.appendChild(intervalElem);
		elem.appendChild(transform.createNode(doc));
	    return elem;
	}

	@Override
	public void getFromNode(Node node) throws DOMNodeException
	{
		Element elem = (Element)node;
		
		if (node.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at KeyFrame");
		
		NodeList childs = elem.getChildNodes();
		
		if (childs != null)
		{
			Matrix3D tmpTransform = new Matrix3D();
			float tmpInterval = 0.0f;
			
			boolean parsedTransform = false;
			boolean parsedInterval = false;
			for (int i = 0; i < childs.getLength(); i++)
			{
				Node child = childs.item(i);
				if (child.getNodeType() == Node.TEXT_NODE ||
					child.getNodeType() == Node.COMMENT_NODE)
					continue;
				
				if (transform.isValidNode(child))
				{
					if (parsedTransform)
						throw new DOMNodeBadStructureException
								("Duplicate transform value");
					tmpTransform.getFromNode(child);
					parsedTransform = true;
				}
				else if (child.getNodeType() == Node.ELEMENT_NODE &&
						child.getNodeName().equals("interval"))
				{
					if (parsedInterval)
						throw new DOMNodeBadStructureException
								("Duplicate interval value");
					
					Scanner scanner = new Scanner(child.getTextContent());
					scanner.useLocale(Locale.ENGLISH);
					
					try
					{ tmpInterval = scanner.nextFloat(); }
					catch (InputMismatchException e)
					{ throw new DOMNodeParseException("Parse exception at interval value"); }
					catch (NoSuchElementException e)
					{ throw new DOMNodeParseException("Parse exception at interval value"); }
					
					if (scanner.hasNext())
						throw new DOMNodeParseException
								("Parse exception at interval value");
					if (tmpInterval <= 0.0f)
						throw new DOMNodeBadValueException("Bad value of interval");
					parsedInterval = true;
				}
			}
			if (!parsedInterval || !parsedTransform)
				throw new DOMNodeBadStructureException
						("Required interval and/or transform elements is not found");
			
			interval = tmpInterval;
			transform = tmpTransform;
		}
		else throw new DOMNodeBadStructureException("KeyFrame without any element");
	}

	@Override
	public boolean isValidNode(Node node)
	{
		return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName().equals("keyFrame"));
	}

	public boolean equals(Object ob)
	{
		if (this == ob)
			return true;
		else if (ob instanceof FractalKeyFrame)
		{
			FractalKeyFrame keyFrame = (FractalKeyFrame)ob;
			return interval == keyFrame.interval && transform.equals(keyFrame.transform);
		}
		else return false;
	}
}
