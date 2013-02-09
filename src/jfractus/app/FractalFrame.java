/**
 * FractalFrame.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import jfractus.dom.DOMNodeBadStructureException;
import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMNodeHandler;
import jfractus.generic.Copyable;
import jfractus.math.Matrix3D;

public class FractalFrame implements Serializable, DOMNodeHandler, Copyable<FractalFrame>
{
	private static final long serialVersionUID = -5318396289622373539L;
    
	protected Matrix3D transform;
	
	/* constuctors */
	public FractalFrame()
	{
		transform = new Matrix3D();
		transform.identity();
	}
	public FractalFrame(Matrix3D transform)
	{
		this.transform = transform;
	}
	
	public FractalFrame copy()
	{
		return new FractalFrame(transform.copy());
	}
	
	/* methods for access to fields */
	public Matrix3D getTransform()
	{
		return transform;
	}
	public void setTransform(Matrix3D transform)
	{
		this.transform = transform;
	}
	
	/* DOMNodeHandler inherited methods */
	
	public Node createNode(Document doc)
    {
		Element elem = doc.createElement("frame");
		elem.appendChild(transform.createNode(doc));
	    return elem;
    }

	public void getFromNode(Node node) throws DOMNodeException
    {
		if (node.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Frame");
		
		Element elem = (Element)node;
		NodeList childs = elem.getChildNodes();
		
		if (childs != null)
		{
			boolean parsed = false;
			for (int i = 0; i < childs.getLength(); i++)
			{
				Node child = childs.item(i);
				if (child.getNodeType() == Node.TEXT_NODE ||
					child.getNodeType() == Node.COMMENT_NODE)
					continue;
				
				if (transform.isValidNode(child))
				{
					if (parsed)
						throw new DOMNodeBadStructureException
								("Duplicate transform value");
					transform.getFromNode(child);
					parsed = true;
				}
			}
		}
		else throw new DOMNodeBadStructureException("Frame without any element");
	}

	public boolean isValidNode(Node node)
    {
	    return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName().equals("frame"));
    }
	
	public boolean equals(Object ob)
	{
		if (this == ob)
			return true;
		else if (ob instanceof FractalFrame)
			return transform.equals(((FractalFrame)ob).transform);
		else return false;
	}
}
