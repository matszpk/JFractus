/**
 * Vector3D.java
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

public class Vector3D implements Serializable, Copyable<Vector3D>,
		DOMNodeHandler
{
    private static final long serialVersionUID = -8221286585623334526L;
    
	public double x, y, z;
	
	public Vector3D()
	{
		x = y = z = 0;
	}
	
	public Vector3D(Vector3D v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector3D(Vector2D v)
	{
		x = v.x;
		y = v.y;
		z = 0.0;
	}
	
	public Vector3D(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/* copy method */
	public Vector3D copy()
	{
		return new Vector3D(this);
	}
	
	/* set methods */
	public void set(Vector2D v)
	{
		x = v.x;
		y = v.y;
		z = 0.0;
	}
	public void set(Vector3D v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}
	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	public void set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/* transformations */
	public Vector3D translate(Vector2D v)
	{
		x += v.x;
		y += v.y;
		return this;
	}
	public Vector3D translate(Vector3D v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	public Vector3D translate(double xpos, double ypos)
	{
		x += xpos;
		y += ypos;
		return this;
	}
	public Vector3D translate(double xpos, double ypos, double zpos)
	{
		x += xpos;
		y += ypos;
		z += zpos;
		return this;
	}
	
	public Vector3D scale(double scale)
	{
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}
	public Vector3D scale(Vector2D v)
	{
		x *= v.x;
		y *= v.y;
		return this;
	}
	public Vector3D scale(Vector3D v)
	{
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}
	public Vector3D scale(double xscale, double yscale)
	{
		x *= xscale;
		y *= yscale;
		return this;
	}
	public Vector3D scale(double xscale, double yscale, double zscale)
	{
		x *= xscale;
		y *= yscale;
		z *= zscale;
		return this;
	}
	
	public Vector3D translateAndScale(Vector2D t, Vector2D s)
	{
		x = x*s.x + t.x;
		y = y*s.y + t.y;
		return this;
	}
	public Vector3D translateAndScale(Vector3D t, Vector3D s)
	{
		x = x*s.x + t.x;
		y = y*s.y + t.y;
		z = z*s.z + t.z;
		return this;
	}
	public Vector3D translateAndScale(double tx, double ty, double sx, double sy)
	{
		x = x*sx + tx;
		y = y*sy + ty;
		return this;
	}
	public Vector3D translateAndScale(double tx, double ty, double tz,
			double sx, double sy, double sz)
	{
		x = x*sx + tx;
		y = y*sy + ty;
		z = z*sz + tz;
		return this;
	}
	
	/* rotate vector by order: anglex, angley, anglez */
	public Vector3D rotate(double anglex, double angley, double anglez)
	{
		double tmp = x;
		double c = Math.cos(anglex);
		double s = Math.sin(anglex);
		x = c*x - s*y;
		y = s*tmp + c*y;
		c = Math.cos(angley);
		s = Math.sin(angley);
		tmp = y;
		y = c*y - s*z;
		z = s*tmp + c*z;
		c = Math.cos(anglez);
		s = Math.sin(anglez);
		tmp = z;
		z = c*z - s*x;
		x = s*tmp + c*x;
		return this;
	}
	public Vector3D rotateXAxis(double angle)
	{
		double tmp = x;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		x = c*x - s*y;
		y = s*tmp + c*y;
		return this;
	}
	public Vector3D rotateYAxis(double angle)
	{
		double tmp = y;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		y = c*y - s*z;
		z = s*tmp + c*z;
		return this;
	}
	public Vector3D rotateZAxis(double angle)
	{
		double tmp = z;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		z = c*z - s*x;
		x = s*tmp + c*x;
		return this;
	}
	
	public Vector3D reverse()
	{
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	/* length and distance computing */
	public double length()
	{
		return Math.hypot(Math.hypot(x, y), z);
	}
	public double norm2()
	{
		return x*x + y*y + z*z;
	}
	
	public double distance(Vector3D v)
	{
		double dx = x-v.x;
		double dy = y-v.y;
		double dz = z-v.z;
		return Math.hypot(Math.hypot(dx, dy), dz);
	}
	public double distance(double xpos, double ypos, double zpos)
	{
		double dx = x-xpos;
		double dy = y-ypos;
		double dz = z-zpos;
		return Math.hypot(Math.hypot(dx, dy), dz); 
	}
	
	/* transformations */
	public Vector3D transform(double[] m)
	{
		double tx = x;
		double ty = y;
		x = m[0]*x + m[1]*y + m[2]*z;
		y = m[3]*tx + m[4]*y + m[5]*z;
		z = m[6]*tx + m[7]*ty + m[8]*z;
		return this;
	}
	public Vector3D transform(Matrix3D m)
	{
		double tx = x;
		double ty = y;
		x = m.m[0]*x + m.m[1]*y + m.m[2]*z;
		y = m.m[3]*tx + m.m[4]*y + m.m[5]*z;
		z = m.m[6]*tx + m.m[7]*ty + m.m[8]*z;
		return this;
	}
	
	/* conversions */
	public Vector2D toVector2D()
	{
		return new Vector2D(x, y);
	}
	
	/* Object method overloading */
	public boolean equals(Object ob)
	{
		if (ob instanceof Vector3D)
		{
			Vector3D v3d = (Vector3D)ob;
			return (x == v3d.x) && (y == v3d.y)  && (z == v3d.z);
		}
		else
			return false;
	}
	
	public String toString()
	{
		return "[" + x + ";" + y + ";" + z + "]";
	}
	
	public Node createNode(Document doc)
    {
		Element elem = doc.createElement("vector3d");
		elem.setTextContent(String.format
				(Locale.ENGLISH, "%15.16g %15.16g %15.16g", x, y, z));
	    return (Node)elem;
    }
	
	public void getFromNode(Node node) throws DOMNodeException
    {
		Element elem = (Element)node;
		Scanner scanner = new Scanner(elem.getTextContent());
		scanner.useLocale(Locale.ENGLISH);
		
		if (elem.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Vector3D");
		
		try
		{
			double inx = scanner.nextDouble();
    		double iny = scanner.nextDouble();
    		double inz = scanner.nextDouble();
    		if (scanner.hasNext())
    			throw new DOMNodeParseException("Exception at parsing Vector3D");
    		x = inx;
    		y = iny;
    		z = inz;
		}
		catch(InputMismatchException e)
		{ throw new DOMNodeParseException("Exception at parsing Vector3D"); }
		catch(NoSuchElementException e)
		{ throw new DOMNodeParseException("Exception at parsing Vector3D"); }
    }
	
	public boolean isValidNode(Node node)
    {
	    return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName() == "vector3d");
    }
}
