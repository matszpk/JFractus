/**
 * Gradient.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.*;
import java.util.*;

import jfractus.dom.*;
import jfractus.generic.Copyable;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class Gradient extends AbstractDOMDocumentHandler implements Serializable,
		Copyable<Gradient>
{
	private static final long serialVersionUID = 9023568877510746702L;

	public static class Point implements Serializable, Comparable<Point>,
			DOMNodeHandler, Copyable<Point>
	{
        private static final long serialVersionUID = 375240667361690711L;
        
        public float position;
		public RGBColor color;
		
		public Point()
		{
			this.color = new RGBColor();
			this.position = 0.0f;
		}
		public Point(float position)
		{
			this.color = new RGBColor();
			this.position = position;
		}
		public Point(float position, RGBColor color)
		{
			this.position = position;
			this.color = color;
		}
		
		protected Point(Point point)
		{
			this.position = point.position;
			this.color = point.color.copy();
		}
		
		public Point copy()
		{
			return new Point(this);
		}
		
		public void set(float position, RGBColor color)
		{
			this.position = position;
			this.color = color;
			
		}
		
		public void evaluateColor(Point next, float destPosition, RGBColor destColor)
		{
			float factor = 1.0f / (next.position-position);
			destColor.red = (destPosition-position) *
				(next.color.red - color.red) * factor + color.red;
			destColor.green = (destPosition-position) *
				(next.color.green - color.green) * factor + color.green;
			destColor.blue = (destPosition-position) *
				(next.color.blue - color.blue) * factor + color.blue;
		}
		
		public int compareTo(Point p)
		{
			return (position < p.position) ? -1 : (position > p.position) ? 1 : 0;
		}
		
		public boolean equals(Object ob)
		{
			if (ob instanceof Point)
			{
				Point p = (Point)ob;
				return (color.equals(p.color) && position == p.position);
			}
			else return false;
		}
		
		public String toString()
		{
			return "Pos: " + position + ";" + color.toString();
		}
		
		/* DOM Node Handler methods */
		public boolean isValidNode(Node node)
    	{
    		return (node.getNodeType() == Node.ELEMENT_NODE &&
    			node.getNodeName() == "point");
    	}
    	
    	public void getFromNode(Node node) throws DOMNodeException
    	{
    		Element elem = (Element)node;
    		if (elem.getAttributes().getLength() != 0)
    			throw new DOMNodeBadStructureException
    					("Unexpected attributes at Gradient Point");
    		
    		NodeList nodeList = elem.getChildNodes();
    		if (nodeList != null)
    		{
    			int j = 0;
    			for (int i = 0; i < nodeList.getLength(); i++)
    			{
    				Node  child = nodeList.item(i);
    				if (child.getNodeType() == Node.TEXT_NODE ||
    						child.getNodeType() == Node.COMMENT_NODE)
    					continue;
    				
    				j++;
    				if (j > 2)
    					throw new DOMNodeBadStructureException
    							("Bad number of subelement in gradient point");
    				
    				if (color.isValidNode(child))
        				color.getFromNode(child);
    				else if (child.getNodeType() == Node.ELEMENT_NODE &&
    						child.getNodeName() == "position")
    				{
    					if (child.getAttributes().getLength() != 0)
    						throw new DOMNodeBadStructureException
    								("Unexpected attributes at Gradient Point Position");
    					
    					Scanner scanner = new Scanner(child.getTextContent());
                		scanner.useLocale(Locale.ENGLISH);
                		try
                		{
                			float p = scanner.nextFloat();
                			if (scanner.hasNext())
                				throw new DOMNodeParseException
                						("Exception at parsing Gradient Point");
                			if (p < 0.0f || p > 1.0f)
                				throw new DOMNodeBadValueException
                						("Bad value of gradient point position");
                			position = p;
                		}
                		catch (InputMismatchException e)
                		{ throw new DOMNodeParseException
                					("Exception at parsing Gradient Point"); }
                		catch(NoSuchElementException e)
                		{ throw new DOMNodeParseException
                					("Exception at parsing Gradient point"); }
    				}
        			else
        				throw new DOMNodeBadStructureException
        						("Bad tag at gradient point");
    			}
    			
    		}
    		else throw new DOMNodeBadStructureException("No element at gradient point");
    	}
    	 
    	public Node createNode(Document doc)
    	{
    		Element elem = doc.createElement("point");
    		Element posElem = doc.createElement("position");
    		posElem.setTextContent(String.format(Locale.ENGLISH, "%g", position));
    		elem.appendChild(posElem);
    		elem.appendChild(color.createNode(doc));
    		return (Node)elem;
    	}
	};
	
	/* private definitions */
	private Point[] points;
	
	/* constructor and methods */
	public Gradient()
	{
		points = new Point[2];
		points[0] = new Point(0.0f);
		points[1] = new Point(1.0f);
	}
	
	public Gradient(RGBColor color1, RGBColor color2)
	{
		points = new Point[2];
		points[0] = new Point(0.0f, color1);
		points[1] = new Point(1.0f, color2);
	}
	public Gradient(Point[] points)
	{
		this.points = points;
	}
	
	protected Gradient(Gradient gradient)
	{
		points = new Point[gradient.points.length];
		for (int i = 0; i < gradient.points.length; i++)
			points[i] = gradient.points[i].copy();
	}
	
	public Gradient copy()
	{
		return new Gradient(this);
	}
	
	public void setPoints(Point[] points)
	{
		this.points = points;
	}
	
	public void reset()
	{
		points = new Point[2];
		points[0] = new Point(0.0f);
		points[1] = new Point(1.0f);
	}
	
	public int setPoint(Point point)
			throws OutOfGradientRangeException
	{
		int index = binarySearch(point.position);
		if (index <= -points.length-1 || index == -1)
			throw new OutOfGradientRangeException("Position " + point.position +
					" out of range gradient");
		
		if (index < 0)
		{
			index = -index-1;
    		Point[] newPoints = new Point[points.length+1];
    		System.arraycopy(points, 0, newPoints, 0, index);
    		newPoints[index] = point;
    		System.arraycopy(points, index, newPoints, index+1, points.length-index);
    		points = newPoints;
		}
		else points[index] = point;
		return index;
	}
	
	public int setPoint(float position, RGBColor color)
			throws OutOfGradientRangeException
	{
		int index = binarySearch(position);
		if (index <= -points.length-1 || index == -1)
			throw new OutOfGradientRangeException("Position " + position +
					" out of range gradient");
		
		if (index < 0)
		{
			index = -index-1;
    		Point[] newPoints = new Point[points.length+1];
    		System.arraycopy(points, 0, newPoints, 0, index);
    		newPoints[index] = new Point(position, color);
    		System.arraycopy(points, index, newPoints, index+1, points.length-index);
    		points = newPoints;
		}
		else
		{
			points[index].position = position;
			points[index].color = color;
		}
		return index;
	}
	
	public void setPositionByIndex(int index, float position)
	{
		if (index < 0 || index >= points.length)
			throw new IndexOutOfBoundsException("Index of point " + index +
					" out of bounds");
		points[index].position = position;
	}
	public void setColorByIndex(int index, RGBColor color)
	{
		if (index < 0 || index >= points.length)
			throw new IndexOutOfBoundsException("Index of point " + index +
					" out of bounds");
		points[index].color = color;
	}
	
	public boolean removePoint(float position)
			throws PointNotFoundAtGradientException, UnremovablePointInGradientException
	{
		int index = binarySearch(position);
		if (index < 0)
			throw new PointNotFoundAtGradientException("Point with position " +
					position + " not found");
		removePoint(index);
		return true;
	}
	public void removePoint(int index) throws UnremovablePointInGradientException
	{
		if (index < 0 || index >= points.length)
			throw new IndexOutOfBoundsException("Index of point " + index +
					" out of bounds");
		else if (index == 0 || index == points.length-1)
			throw new UnremovablePointInGradientException("Specified point is not removable");
			
		Point[] newPoints = new Point[points.length-1];
		System.arraycopy(points, 0, newPoints, 0, index);
		System.arraycopy(points, index+1, newPoints, index, points.length-index-1);
		points = newPoints;
	}
	
	public Point getPoint(int index)
	{
		return points[index];
	}
	public float getPosition(int index)
	{
		return points[index].position;
	}
	public RGBColor getColor(int index)
	{
		return points[index].color;
	}
	
	public int binarySearch(float position)
	{
		int first = 0;
		int last = points.length-1; 
		int m = points.length >> 1;
		if (points[0].position > position)
			return -1;
		else if (points[last].position < position)
			return -points.length-1;
		while (first != last)
		{
			if (position < points[m].position)
				last = m-1;
			else
				first = m;
			m = first + ((last-first+1) >> 1);
		}
		return (points[m].position == position) ? m : -m-2;
	}
	
	public int floorIndex(float position)
	{
		int index = binarySearch(position);
		return (index >= 0) ? index : -index-2;
	}
	public Point floorPoint(float position)
	{
		int index = binarySearch(position);
		return (index >= 0) ? points[index] : (index < -1) ? points[-index-2] : null;
	}
	
	public int ceilIndex(float position)
	{
		int index = binarySearch(position);
		return (index >= 0) ? index : -index-1;
	}
	public Point ceilPoint(float position)
	{
		int index = binarySearch(position);
		return (index >= 0) ? points[index] :
			(index > -points.length-1) ? points[-index-1] : null;
	}
	
	public boolean contains(float position)
	{
		return (binarySearch(position) >= 0);
	}
	
	public int size()
	{
		return points.length;
	}
	
	public void evaluateColor(float position, RGBColor destColor)
	{
		if (position < 0.0f)
		{
			destColor.red = points[0].color.red;
			destColor.green = points[0].color.green;
			destColor.blue = points[0].color.blue;
		}
		else if (position > 1.0f)
		{
			destColor.red = points[points.length-1].color.red;
			destColor.green = points[points.length-1].color.green;
			destColor.blue = points[points.length-1].color.blue;
		}
		int index = floorIndex(position);
		if (position == points[points.length-1].position)
		{
			destColor.red = points[index].color.red;
			destColor.green = points[index].color.green;
			destColor.blue = points[index].color.blue;
		}
		else if (index >= 0 && index < points.length-1)
			points[index].evaluateColor(points[index+1], position, destColor);
	}
	
	public boolean isRemovable(int index)
	{
		return (index > 0 && index < points.length-1);
	}
	
	public boolean equals(Object ob)
	{
		if (ob instanceof Gradient)
		{
			Gradient gradient = (Gradient)ob;
			if (this == gradient)
				return true;
			if (points.length != gradient.points.length)
				return false;
			for (int i = 0; i < points.length; i++)
				if (!points[i].equals(gradient.points[i]))
					return false;
			return true;
		}
		else return false;
	}
	
	/* DOM Node handler methods */
	public boolean isValidNode(Node node)
	{
		return (node.getNodeType() == Node.ELEMENT_NODE &&
			node.getNodeName() == "gradient");
	}
	
	public void getFromNode(Node node) throws DOMNodeException
	{
		Element elem = (Element)node;
		NodeList nodeList = elem.getChildNodes();
		
		if (elem.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Gradient");
		
		TreeSet<Point> newPoints = new TreeSet<Point>();
		if (nodeList != null)
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				Node child = nodeList.item(i);
				if (child.getNodeType() == Node.TEXT_NODE ||
    					child.getNodeType() == Node.COMMENT_NODE)
					continue;
				
				Point point = new Point();
				if (point.isValidNode(child))
				{
					point.getFromNode(child);
					newPoints.add(point);
				}
				else throw new DOMNodeBadStructureException("Bad tag at gradient node");
			}
			if (newPoints.first().position != 0.0f ||
					newPoints.last().position != 1.0f)
				throw new DOMNodeBadValueException
					("Bad value of position in start or end gradient point");
			points = new Point[newPoints.size()];
			newPoints.toArray(points);
			Arrays.sort(points);
		}
	} 
	
	public Node createNode(Document doc)
	{
		Element elem = doc.createElement("gradient");
		generateToElement(elem);
		return elem;
	}
	
	protected void generateToElement(Element elem)
	{
		Document doc = elem.getOwnerDocument();
		for (int i = 0; i < points.length; i++)
			elem.appendChild(points[i].createNode(doc));
	}
	
    public Document createDocument(DOMImplementation impl)
    {
    	Document doc = impl.createDocument(null, "gradient", null);
    	generateToElement(doc.getDocumentElement());
	    return doc;
    }
}
