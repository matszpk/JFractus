/**
 * FractalFrameTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import jfractus.app.FractalFrame;
import jfractus.dom.DOMNodeException;
import jfractus.math.Matrix3D;

public class FractalFrameTest
{
	private FractalFrame fractalFrame;
	
	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "frame", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	@Before
	public void setUp() throws Exception
	{
		fractalFrame = new FractalFrame();
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
		fractalFrame.setTransform(new Matrix3D(transformData));
		Node elem = fractalFrame.createNode(document);
		
		FractalFrame newFrame = new FractalFrame(new Matrix3D(transformData));
		try
		{ newFrame.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(fractalFrame, newFrame);
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
		elem.appendChild(matrix.createNode(document));
		
		try
		{ fractalFrame.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		FractalFrame newFrame = new FractalFrame(matrix);
		assertEquals(fractalFrame, newFrame);
		
		boolean catched = false;
		elem.setAttribute("forTest", "Value");
		try
		{ fractalFrame.getFromNode(elem); }
		catch  (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("DOMNodeException wasnt thrown", catched);
	}

	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(fractalFrame.isValidNode(document.getDocumentElement()));
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
		
		fractalFrame.setTransform(new Matrix3D(transformData));
		FractalFrame newFrame = new FractalFrame(new Matrix3D(transformData));
		
		assertTrue(fractalFrame.equals(newFrame));
		assertTrue(fractalFrame.equals(fractalFrame));
		newFrame.setTransform(new Matrix3D(transformData2));
		assertTrue(!fractalFrame.equals(newFrame));
	}

}
