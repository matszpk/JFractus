/**
 * FractalAnimation.java
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

import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMNodeBadStructureException;
import jfractus.dom.DOMNodeHandler;

public class FractalAnimation implements Serializable, DOMNodeHandler
{
    private static final long serialVersionUID = 4347592105976847565L;
    
    private float framesPerSecond;
    private ArrayList<FractalKeyFrame> keyFrames;

    public FractalAnimation()
    {
    	framesPerSecond = 25.0f;
    	keyFrames = new ArrayList<FractalKeyFrame>();
    }
    
    public FractalAnimation(float framesPerSecond)
    {
    	this.framesPerSecond = framesPerSecond;
    	keyFrames = new ArrayList<FractalKeyFrame>();
    }
    
    public float getFramesPerSecond()
    {
    	return framesPerSecond;
    }
    public void setFramesPerSecond(float framesPerSecond)
    {
    	this.framesPerSecond = framesPerSecond;
    }
    
    public void addKeyFrame(FractalKeyFrame keyFrame)
    {
    	keyFrames.add(keyFrame);
    }
    public void addKeyFrame(int position, FractalKeyFrame keyFrame)
    {
    	keyFrames.add(position, keyFrame);
    }
    public void removeKeyFrame(int position)
    {
    	keyFrames.remove(position);
    }
    
    public FractalKeyFrame getKeyFrame(int position)
    {
    	return keyFrames.get(position);
    }
    public void setKeyFrame(int position, FractalKeyFrame keyFrame)
    {
    	keyFrames.set(position, keyFrame);
    }
    
    public float computeTime()
    {
    	float x = 0.0f;
    	for (Iterator<FractalKeyFrame> iter = keyFrames.iterator(); iter.hasNext();)
    		x += iter.next().getInterval();
    	return x;
    }
    
    public float computeFramesNumber()
    {
    	return computeTime() * framesPerSecond;
    }
    
    public FractalFrameEvaluator getFrameEvaluator()
    {
    	return new FractalFrameEvaluator(keyFrames.iterator());
    }
    
	@Override
	public Node createNode(Document doc)
	{
		Element elem = doc.createElement("animation");
		Element fpsElem = doc.createElement("fps");
		fpsElem.setTextContent(String.format(Locale.ENGLISH, "%g", framesPerSecond));
		elem.appendChild(fpsElem);
		for (Iterator<FractalKeyFrame> iter = keyFrames.iterator(); iter.hasNext();)
			elem.appendChild(iter.next().createNode(doc));
		return elem;
	}

	@Override
	public void getFromNode(Node node) throws DOMNodeException
	{
		Element elem = (Element)node;
		
		if (node.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Animation");
		
		NodeList nodeChilds = elem.getChildNodes();
		
		boolean fpsParsed = false;
		if (nodeChilds != null)
		{
			ArrayList<FractalKeyFrame> tmpKeyFrames = new ArrayList<FractalKeyFrame>();
			float tmpFps = 0.0f;
			
    		for(int i = 0; i < nodeChilds.getLength(); i++)
    		{
    			Node child = nodeChilds.item(i);
    			if (child.getNodeType() == Node.TEXT_NODE ||
   					child.getNodeType() == Node.COMMENT_NODE)
   					continue;
    			
    			if (!fpsParsed)
    			{
    				if (child.getNodeType() != Node.ELEMENT_NODE ||
    					!child.getNodeName().equals("fps"))
    					throw new DOMNodeBadStructureException("Expected fps element");
    				
    				Scanner scanner = new Scanner(child.getTextContent());
    				scanner.useLocale(Locale.ENGLISH);
    				
    				try
    				{ tmpFps = scanner.nextFloat(); }
    				catch (InputMismatchException e)
    				{ throw new DOMNodeBadStructureException
    						("Parse exception at fps element"); }
    				catch (NoSuchElementException e)
    				{ throw new DOMNodeBadStructureException
    						("Parse exception at fps element"); }
    				
    				if (scanner.hasNext())
    					throw new DOMNodeBadStructureException
    						("Parse exception at fps element");
    				
    				fpsParsed = true;
    			}
    			else
    			{
    				FractalKeyFrame keyFrame = new FractalKeyFrame(); 
    				
    				if (keyFrame.isValidNode(child))
    				{
    					keyFrame.getFromNode(child);
    					tmpKeyFrames.add(keyFrame);
    				}
    				else throw new DOMNodeBadStructureException("Is not key frame element");
    			}
    		}
    		framesPerSecond = tmpFps;
    		keyFrames = tmpKeyFrames;
		}
		else throw new DOMNodeBadStructureException("Animation without any element");
	}

	@Override
	public boolean isValidNode(Node node)
	{
		return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName().equals("animation"));
	}

	public boolean equals(Object ob)
	{
		if (this == ob)
			return true;
		else if (ob instanceof FractalAnimation)
		{
			FractalAnimation anim = (FractalAnimation)ob;
			return (framesPerSecond == anim.framesPerSecond) &&
					keyFrames.equals(anim.keyFrames);
		}
		else return false;
	}
}
