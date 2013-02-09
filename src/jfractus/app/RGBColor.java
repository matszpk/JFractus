/**
 * RGBColor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.Serializable;
import java.util.*;
import java.awt.Color;

import jfractus.dom.DOMNodeBadStructureException;
import jfractus.dom.DOMNodeBadValueException;
import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMNodeHandler;
import jfractus.dom.DOMNodeParseException;
import jfractus.generic.Copyable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class RGBColor implements Serializable, DOMNodeHandler, Copyable<RGBColor>
{
	private static final long serialVersionUID = 693283607420413736L;
    
	public float red, green, blue;
	
	public RGBColor()
	{
		red = green = blue = 0.0f;
	}
	public RGBColor(Color color)
	{
		red = (float)color.getRed() / 255.0f;
		green = (float)color.getGreen() / 255.0f;
		blue = (float)color.getBlue() / 255.0f;
	}
	public RGBColor(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	protected RGBColor(RGBColor color)
	{
		this.red = color.red;
		this.green = color.green;
		this.blue = color.blue;
	}
	
	public RGBColor copy()
	{
		return new RGBColor(this);
	}
	
	public void set(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	public void set(Color color)
	{
		red = (float)color.getRed() / 255.0f;
		green = (float)color.getGreen() / 255.0f;
		blue = (float)color.getBlue() / 255.0f;
	}
	
	public Color toAWTColor()
	{
		return new Color(red, green, blue);
	}
	
	public boolean equals(Object ob)
	{
		if (ob instanceof RGBColor)
		{
			RGBColor color = (RGBColor)ob;
			return (color.red == red && color.green == green && color.blue == blue);
		}
		else return false;
	}
	
	public String toString()
	{
		return "Red: " + red + ", Green: " + green + ", Blue: " + blue;
	}
	
	/* DOM Node handler methods */
	public boolean isValidNode(Node node)
	{
		return (node.getNodeType() == Node.ELEMENT_NODE &&
			node.getNodeName() == "rgbcolor");
	}
	
	public void getFromNode(Node node) throws DOMNodeException
	{
		Element elem = (Element)node;
		Scanner scanner = new Scanner(elem.getTextContent());
		scanner.useLocale(Locale.ENGLISH);
		
		if (elem.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at RGB Color");
		
		try
		{
			float r = scanner.nextFloat();
    		float g = scanner.nextFloat();
    		float b = scanner.nextFloat();
    		if (scanner.hasNext())
    			throw new DOMNodeParseException("Exception at parsing RGB Color");
    		if (r < 0.0f || r > 1.0f || g < 0.0f || g > 1.0f || b < 0.0f || b > 1.0f)
    			throw new DOMNodeBadValueException("Bad value of rgb components");
    		red = r;
    		green = g;
    		blue = b;
		}
		catch(InputMismatchException e)
		{ throw new DOMNodeParseException("Exception at parsing RGB Color"); }
		catch(NoSuchElementException e)
		{ throw new DOMNodeParseException("Exception at parsing RGB Color"); }
	}
	 
	public Node createNode(Document doc)
	{
		Element elem = doc.createElement("rgbcolor");
		elem.setTextContent(String.format(Locale.ENGLISH, "%g %g %g", red, green, blue));
		return (Node)elem;
	}
}
