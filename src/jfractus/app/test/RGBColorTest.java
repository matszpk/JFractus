/**
 * RGBColorTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jfractus.app.RGBColor;
import jfractus.dom.DOMNodeException;

import java.awt.Color;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RGBColorTest
{
	private RGBColor color;
	
	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "rgbcolor", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	@Before
	public void setUp() throws Exception
	{
		color = new RGBColor();
	}

	@Test
	public void testSetColor()
	{
		color.set(0.7f, 0.3f, 0.5f);
		assertEquals(new RGBColor(0.7f, 0.3f, 0.5f), color);
	}

	@Test
	public void testToAWTColor()
	{
		color.set(0.7f, 0.3f, 0.5f);
		Color awtColor = color.toAWTColor();
		assertEquals(new Color(0.7f, 0.3f, 0.5f), awtColor);
	}

	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(color.isValidNode(document.getDocumentElement()));
	}

	@Test
	public void testGetFromNode()
	{
		prepareDocument();
		Element elem = document.getDocumentElement();
		elem.setTextContent(" 0.5  0.7    0.33 ");
		try
		{ color.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		assertEquals(new RGBColor(0.5f, 0.7f, 0.33f), color);
		
		elem.setTextContent(" 0.5  0.7    1.33 ");
		boolean catched = false;
		try
		{ color.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
		
		elem.setTextContent(" 0.5  0.7,    0.33 ");
		catched = false;
		try
		{ color.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
		
		elem.setTextContent(" 0.5  0.7   ");
		catched = false;
		try
		{ color.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
		
		elem.setTextContent(" 0.5  0.7  0.6 p ");
		catched = false;
		try
		{ color.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
		
		elem.setAttribute("forTest", "myValue");
		elem.setTextContent(" 0.5  0.7    0.33 ");
		catched = false;
		try
		{ color.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
	}

	@Test
	public void testCreateNode()
	{
		prepareDocument();
		color.set(0.23f, 0.31f, 0.8f);
		Node node = color.createNode(document);
		assertTrue(color.isValidNode(node));
		RGBColor newColor = new RGBColor();
		try
		{ newColor.getFromNode(node); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(newColor, color);
	}

}
