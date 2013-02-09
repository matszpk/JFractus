/**
 * Vector2D.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math;

import java.io.Serializable;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import jfractus.dom.*;
import jfractus.generic.Copyable;

public class Vector2D implements Serializable, Copyable<Vector2D>,
		DOMNodeHandler
{
	private static final long serialVersionUID = -1297723177514905872L;
    
	public double x, y;
	
	public Vector2D()
	{
		x = y = 0;
	}
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	protected Vector2D(Vector2D v)
	{
		x = v.x;
		y = v.y;
	}
	public Vector2D(Vector3D v)
	{
		x = v.x;
		y = v.y;
	}
	
	/* copy method */
	public Vector2D copy()
	{
		return new Vector2D(this);
	}
	
	/* set method */
	public void set(Vector2D v)
	{
		x = v.x;
		y = v.y;
	}
	public void set(Vector3D v)
	{
		x = v.x;
		y = v.y;
	}
	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/* transformations */
	public Vector2D translate(Vector2D v)
	{
		x += v.x;
		y += v.y;
		return this;
	}
	public Vector2D translate(double xpos, double ypos)
	{
		x += xpos;
		y += ypos;
		return this;
	}
	
	public Vector2D scale(double scale)
	{
		x *= scale;
		y *= scale;
		return this;
	}
	public Vector2D scale(Vector2D v)
	{
		x *= v.x;
		y *= v.y;
		return this;
	}
	public Vector2D scale(double xscale, double yscale)
	{
		x *= xscale;
		y *= yscale;
		return this;
	}
	
	public Vector2D translateAndScale(Vector2D t, Vector2D s)
	{
		x = x*s.x + t.x;
		y = y*s.y + t.y;
		return this;
	}
	public Vector2D translateAndScale(double tx, double ty, double sx, double sy)
	{
		x = x*sx + tx;
		y = y*sy + ty;
		return this;
	}
	
	public Vector2D rotate(double angle)
	{
		double tmpx = x;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		x = c*x - s*y;
		y = s*tmpx + c*y;
		return this;
	}
	
	public Vector2D reverse()
	{
		x = -x;
		y = -y;
		return this;
	}
	public Vector2D transpose()
	{
		double tmpx = x;
		x = y;
		y = tmpx;
		return this;
	}
	
	/* length and distance computing */
	public double length()
	{
		return Math.hypot(x, y);
	}
	public double norm2()
	{
		return x*x + y*y;
	}
	
	public double distance(Vector2D v)
	{
		double dx = x-v.x;
		double dy = y-v.y;
		return Math.hypot(dx, dy);
	}
	public double distance(double xpos, double ypos)
	{
		double dx = x-xpos;
		double dy = y-ypos;
		return Math.hypot(dx, dy);
	}
	
	
	/* transformations */
	public Vector2D transform(double[] m)
	{
		double tx = x;
		x = m[0]*x + m[1]*y + m[2];
		y = m[3]*tx + m[4]*y + m[5];
		return this;
	}
	public Vector2D transform(Matrix3D m)
	{
		double tx = x;
		x = m.m[0]*x + m.m[1]*y + m.m[2];
		y = m.m[3]*tx + m.m[4]*y + m.m[5];
		return this;
	}
	
	public Vector2D onlyTransform(double[] m)
	{
		double tx = x;
		x = m[0]*x + m[1]*y;
		y = m[3]*tx + m[4]*y;
		return this;
	}
	public Vector2D onlyTransform(Matrix3D m)
	{
		double tx = x;
		x = m.m[0]*x + m.m[1]*y;
		y = m.m[3]*tx + m.m[4]*y;
		return this;
	}
	
	/* conversions */
	public Vector3D toVector3D()
	{
		return new Vector3D(x, y, 0.0);
	}
	
	/* Object method overloading */
	public boolean equals(Object ob)
	{
		if (ob instanceof Vector2D)
			return (x == ((Vector2D)ob).x) && (y == ((Vector2D)ob).y);
		else
			return false;
	}
	
	public String toString()
	{
		return "[" + x + ";" + y + "]";  
	}
	
	public Node createNode(Document doc)
    {
		Element elem = doc.createElement("vector2d");
		elem.setTextContent(String.format(Locale.ENGLISH, "%15.16g %15.16g", x, y));
	    return (Node)elem;
    }
	
	public void getFromNode(Node node) throws DOMNodeException
    {
		Element elem = (Element)node;
		Scanner scanner = new Scanner(elem.getTextContent());
		scanner.useLocale(Locale.ENGLISH);
		
		if (elem.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Vector2D");
		
		try
		{
			double inx = scanner.nextDouble();
    		double iny = scanner.nextDouble();
    		if (scanner.hasNext())
    			throw new DOMNodeParseException("Exception at parsing Vector2D");
    		x = inx;
    		y = iny;
		}
		catch(InputMismatchException e)
		{ throw new DOMNodeParseException("Exception at parsing Vector2D"); }
		catch(NoSuchElementException e)
		{ throw new DOMNodeParseException("Exception at parsing Vector2D"); }
    }
	
	public boolean isValidNode(Node node)
    {
	    return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName() == "vector2d");
    }
}
