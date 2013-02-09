/**
 * FractalAnimationTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;
import jfractus.app.FractalAnimation;

import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import jfractus.app.FractalKeyFrame;
import jfractus.dom.DOMNodeException;
import jfractus.math.Matrix3D;
import jfractus.math.Vector3D;

public class FractalAnimationTest
{
	private FractalAnimation fractalAnim;

	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "animation", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	@Before
	public void setUp() throws Exception
	{
		fractalAnim = new FractalAnimation();
		fractalAnim.setFramesPerSecond(25.0f);
		fractalAnim.addKeyFrame(new FractalKeyFrame(0.57f, new Matrix3D(
						new Vector3D(0.55, 1.05, -0.434),
						new Vector3D(-5.72, -0.54, 3.45),
						new Vector3D(0.0, 0.0, 1.0))));
		fractalAnim.addKeyFrame(new FractalKeyFrame(0.34f, new Matrix3D(
						new Vector3D(2.13, 3.55, -3.434),
						new Vector3D(-35.6, -4.32, 33.5),
						new Vector3D(0.0, 0.0, 1.0))));
		fractalAnim.addKeyFrame(new FractalKeyFrame(1.74f, new Matrix3D(
						new Vector3D(4.42, 3.43, -7.644),
						new Vector3D(-4.66, 6.54, 4.33),
						new Vector3D(0.0, 0.0, 1.0))));
		fractalAnim.addKeyFrame(new FractalKeyFrame(0.63f, new Matrix3D(
						new Vector3D(1.37, 0.33, 4.44),
						new Vector3D(-2.47, -1.91, 9.76),
						new Vector3D(0.0, 0.0, 1.0))));
	}

	@Test
	public void testAddKeyFrame()
	{
		fractalAnim.addKeyFrame(1, new FractalKeyFrame(0.99f, new Matrix3D(
						new Vector3D(2.64, -4.21, -8.97),
						new Vector3D(-3.89, 3.51, 6.97),
						new Vector3D(0.0, 0.0, 1.0))));
		fractalAnim.addKeyFrame(3, new FractalKeyFrame(1.21f, new Matrix3D(
						new Vector3D(3.11, -3.45, -0.47),
						new Vector3D(-2.59, 2.59, 1.52),
						new Vector3D(0.0, 0.0, 1.0))));
		
		FractalAnimation newAnim = new FractalAnimation();
		newAnim.setFramesPerSecond(25.0f);
		newAnim.addKeyFrame(new FractalKeyFrame(0.57f, new Matrix3D(
						new Vector3D(0.55, 1.05, -0.434),
						new Vector3D(-5.72, -0.54, 3.45),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.99f, new Matrix3D(
						new Vector3D(2.64, -4.21, -8.97),
						new Vector3D(-3.89, 3.51, 6.97),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.34f, new Matrix3D(
						new Vector3D(2.13, 3.55, -3.434),
						new Vector3D(-35.6, -4.32, 33.5),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(1.21f, new Matrix3D(
						new Vector3D(3.11, -3.45, -0.47),
						new Vector3D(-2.59, 2.59, 1.52),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(1.74f, new Matrix3D(
						new Vector3D(4.42, 3.43, -7.644),
						new Vector3D(-4.66, 6.54, 4.33),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.63f, new Matrix3D(
						new Vector3D(1.37, 0.33, 4.44),
						new Vector3D(-2.47, -1.91, 9.76),
						new Vector3D(0.0, 0.0, 1.0))));
		
		assertEquals(fractalAnim, newAnim);
	}

	@Test
	public void testRemoveKeyFrame()
	{
		fractalAnim.removeKeyFrame(1);
		fractalAnim.removeKeyFrame(2);
		
		FractalAnimation newAnim = new FractalAnimation();
		newAnim.setFramesPerSecond(25.0f);
		newAnim.addKeyFrame(new FractalKeyFrame(0.57f, new Matrix3D(
						new Vector3D(0.55, 1.05, -0.434),
						new Vector3D(-5.72, -0.54, 3.45),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(1.74f, new Matrix3D(
						new Vector3D(4.42, 3.43, -7.644),
						new Vector3D(-4.66, 6.54, 4.33),
						new Vector3D(0.0, 0.0, 1.0))));
		
		assertEquals(fractalAnim, newAnim);
	}

	@Test
	public void testSetKeyFrame()
	{
		fractalAnim.setKeyFrame(1, new FractalKeyFrame(0.89f, new Matrix3D(
						new Vector3D(2.57, 0.32, -5.43),
						new Vector3D(3.78, -3.67, 7.43),
						new Vector3D(0.0, 0.0, 1.0))));
		fractalAnim.setKeyFrame(2, new FractalKeyFrame(0.29f, new Matrix3D(
						new Vector3D(1.33, 3.42, -2.43),
						new Vector3D(1.78, -5.63, -5.43),
						new Vector3D(0.0, 0.0, 1.0))));
		
		FractalAnimation newAnim = new FractalAnimation();
		newAnim.setFramesPerSecond(25.0f);
		newAnim.addKeyFrame(new FractalKeyFrame(0.57f, new Matrix3D(
						new Vector3D(0.55, 1.05, -0.434),
						new Vector3D(-5.72, -0.54, 3.45),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.89f, new Matrix3D(
						new Vector3D(2.57, 0.32, -5.43),
						new Vector3D(3.78, -3.67, 7.43),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.29f, new Matrix3D(
						new Vector3D(1.33, 3.42, -2.43),
						new Vector3D(1.78, -5.63, -5.43),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.63f, new Matrix3D(
						new Vector3D(1.37, 0.33, 4.44),
						new Vector3D(-2.47, -1.91, 9.76),
						new Vector3D(0.0, 0.0, 1.0))));
		
		assertEquals(fractalAnim, newAnim);
	}

	@Test
	public void testCreateNode()
	{
		prepareDocument();
		Node elem = fractalAnim.createNode(document);
		
		FractalAnimation newAnim= new FractalAnimation();
		
		try
		{ newAnim.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(fractalAnim, newAnim);
	}

	@Test
	public void testGetFromNode()
	{
		prepareDocument();
		Element elem = document.getDocumentElement();
		Element fpsElem = document.createElement("fps");
		fpsElem.setTextContent("25.0");
		elem.appendChild(fpsElem);
		
		elem.appendChild(fractalAnim.getKeyFrame(0).createNode(document));
		elem.appendChild(fractalAnim.getKeyFrame(1).createNode(document));
		elem.appendChild(fractalAnim.getKeyFrame(2).createNode(document));
		elem.appendChild(fractalAnim.getKeyFrame(3).createNode(document));
		
		FractalAnimation newAnim = new FractalAnimation();
		
		try
		{ newAnim.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(fractalAnim, newAnim);
		
		boolean catched = false;
		fpsElem.setTextContent("25.0, 55");
		try
		{ newAnim.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("Exception wasnt thrown", catched);
	}

	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(fractalAnim.isValidNode(document.getDocumentElement()));
	}

	@Test
	public void testEqualsObject()
	{
		FractalAnimation newAnim = new FractalAnimation(25.0f);
		newAnim.addKeyFrame(new FractalKeyFrame(0.57f, new Matrix3D(
						new Vector3D(0.55, 1.05, -0.434),
						new Vector3D(-5.72, -0.54, 3.45),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.34f, new Matrix3D(
						new Vector3D(2.13, 3.55, -3.434),
						new Vector3D(-35.6, -4.32, 33.5),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(1.74f, new Matrix3D(
						new Vector3D(4.42, 3.43, -7.644),
						new Vector3D(-4.66, 6.54, 4.33),
						new Vector3D(0.0, 0.0, 1.0))));
		newAnim.addKeyFrame(new FractalKeyFrame(0.63f, new Matrix3D(
						new Vector3D(1.37, 0.33, 4.44),
						new Vector3D(-2.47, -1.91, 9.76),
						new Vector3D(0.0, 0.0, 1.0))));
		
		assertTrue(fractalAnim.equals(newAnim));
		assertTrue(fractalAnim.equals(fractalAnim));
		
		newAnim.setKeyFrame(1, new FractalKeyFrame(0.34f, new Matrix3D(
						new Vector3D(1.13, 3.55, -3.434),
						new Vector3D(-35.6, -4.32, 33.5),
						new Vector3D(0.0, 0.0, 1.0))));
		
		assertFalse(fractalAnim.equals(newAnim));
	}

}
