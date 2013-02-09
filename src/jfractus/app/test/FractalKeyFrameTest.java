/**
 * FractalKeyFrameTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import jfractus.app.FractalKeyFrame;
import jfractus.dom.DOMNodeException;
import jfractus.math.Matrix3D;

public class FractalKeyFrameTest
{
	FractalKeyFrame fractalKeyFrame;
	
	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "keyFrame", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	@Before
	public void setUp() throws Exception
	{
		fractalKeyFrame = new FractalKeyFrame();
	}

	@Test
	public void testCreateNode()
	{
		final double[] transformData = {
			2.42, 17.3, -2.21,
			-6.5, 6.34, -0.84,
			0.0, 0.0, 1.0
		};
		
		prepareDocument();
		fractalKeyFrame.setInterval(0.44f);
		fractalKeyFrame.setTransform(new Matrix3D(transformData));
		Node elem = fractalKeyFrame.createNode(document);
		
		FractalKeyFrame newKeyFrame =
			new FractalKeyFrame(0.44f, new Matrix3D(transformData));
		try
		{ newKeyFrame.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(fractalKeyFrame, newKeyFrame);
	}

	@Test
	public void testGetFromNode()
	{
		final double[] transformData = {
			2.42, 17.3, -2.21,
			-6.5, 6.34, -0.84,
			0.0, 0.0, 1.0
		};
		
		prepareDocument();
		Element elem = document.getDocumentElement();
		Matrix3D matrix = new Matrix3D(transformData);
		Element intervalElem = document.createElement("interval");
		intervalElem.setTextContent("2.65");
		elem.appendChild(intervalElem);
		elem.appendChild(matrix.createNode(document));
		
		try
		{ fractalKeyFrame.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		FractalKeyFrame newKeyFrame = new FractalKeyFrame(2.65f, matrix);
		assertEquals(fractalKeyFrame, newKeyFrame);
		
		boolean catched = false;
		elem.setAttribute("forTest", "Value");
		try
		{ fractalKeyFrame.getFromNode(elem); }
		catch  (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("DOMNodeException wasnt thrown", catched);
		
		catched = false;
		elem.removeAttribute("forTest");
		intervalElem.setTextContent("2.65 ,");
		try
		{ fractalKeyFrame.getFromNode(elem); }
		catch  (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("DOMNodeException wasnt thrown", catched);
		
		catched = false;
		intervalElem.setTextContent("2.65 5.56");
		try
		{ fractalKeyFrame.getFromNode(elem); }
		catch  (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("DOMNodeException wasnt thrown", catched);
		
		catched = false;
		intervalElem.setTextContent("-6.4");
		try
		{ fractalKeyFrame.getFromNode(elem); }
		catch  (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("DOMNodeException wasnt thrown", catched);
	}

	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(fractalKeyFrame.isValidNode(document.getDocumentElement()));
	}

	@Test
	public void testEqualsObject()
	{
		final double[] transformData = {
			0.65, 23.4, -0.64,
			-4.6, -5.36, 2.24,
			0.0, 0.0, 1.0
		};
		final double[] transformData2 = {
			0.65, 23.4, -2.64,
			-4.6, -4.48, 2.54,
			0.0, 0.0, 1.0
		};
		
		fractalKeyFrame.setInterval(1.3f);
		fractalKeyFrame.setTransform(new Matrix3D(transformData));
		FractalKeyFrame newKeyFrame = new FractalKeyFrame
				(1.3f, new Matrix3D(transformData));
		
		assertTrue(fractalKeyFrame.equals(newKeyFrame));
		assertTrue(fractalKeyFrame.equals(fractalKeyFrame));
		newKeyFrame.setTransform(new Matrix3D(transformData2));
		assertTrue(!fractalKeyFrame.equals(newKeyFrame));
	}
}
