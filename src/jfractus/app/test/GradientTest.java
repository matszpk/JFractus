/**
 * GradientTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import jfractus.app.OutOfGradientRangeException;
import jfractus.app.PointNotFoundAtGradientException;
import jfractus.app.RGBColor;
import jfractus.app.Gradient;
import jfractus.app.UnremovablePointInGradientException;
import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMDocumentException;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class GradientTest
{
	private Gradient gradient;
	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "gradient", null); 
		}
		catch(ParserConfigurationException e)
		{
			fail("Exception was throw");
		}
	}

	private void assertEqualsRGBColor(RGBColor expected, RGBColor color)
	{
		assertEquals((double)expected.red, (double)color.red, 1.0e-6);
		assertEquals((double)expected.green, (double)color.green, 1.0e-6);
		assertEquals((double)expected.blue, (double)color.blue, 1.0e-6);
	}
	
	@Before
	public void setUp() throws Exception
	{
		gradient = new Gradient();
	}

	@Test
	public void testGradientRGBColorRGBColor()
	{
		Gradient newGradient = new Gradient(new RGBColor(0.3f, 0.8f, 0.1f),
				new RGBColor(1.0f, 1.0f, 0.7f));
		assertEquals(newGradient.getPoint(0),
				new Gradient.Point(0.0f, new RGBColor(0.3f, 0.8f, 0.1f)));
		assertEquals(newGradient.getPoint(1),
				new Gradient.Point(1.0f, new RGBColor(1.0f, 1.0f, 0.7f)));
	}

	@Test
	public void testSetPointPoint()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.4f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.75f, 0.11f, 0.37f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.88f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.58f, 0.67f, 0.7f))
		};
		gradient.setPoints(points);
		try
		{
    		gradient.setPoint(new Gradient.Point(0.3f,
    				new RGBColor(0.7f, 0.2f, 1.0f)));
    		gradient.setPoint(new Gradient.Point(0.7f,
    				new RGBColor(0.93f, 0.12f, 0.33f)));
    		gradient.setPoint(new Gradient.Point(0.55f,
    				new RGBColor(0.53f, 0.21f, 0.0f)));
    		gradient.setPoint(new Gradient.Point(0.23f,
    				new RGBColor(0.11f, 0.6f, 0.2f)));
    		gradient.setPoint(new Gradient.Point(0.87f,
    				new RGBColor(0.88f, 0.44f, 0.94f)));
		}
		catch(OutOfGradientRangeException e)
		{ fail("Exception was throw"); }
		
		boolean catched = false;
		try
		{ gradient.setPoint(new Gradient.Point(-0.3f, new RGBColor(0.7f, 0.2f, 1.0f))); }
		catch (OutOfGradientRangeException e)
		{ catched = true; }
		assertTrue("OutOfGradintRangeException not thrown", catched);
		
		catched = false;
		try
		{ gradient.setPoint(new Gradient.Point(1.3f, new RGBColor(0.7f, 0.2f, 1.0f))); }
		catch (OutOfGradientRangeException e)
		{ catched = true; }
		assertTrue("OutOfGradintRangeException not thrown", catched);
		
		final Gradient.Point[] expectedPoints = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.4f, 0.7f)),
			new Gradient.Point(0.23f, new RGBColor(0.11f, 0.6f, 0.2f)),
			new Gradient.Point(0.3f, new RGBColor(0.7f, 0.2f, 1.0f)),
			new Gradient.Point(0.55f, new RGBColor(0.53f, 0.21f, 0.0f)),
			new Gradient.Point(0.7f, new RGBColor(0.93f, 0.12f, 0.33f)),
			new Gradient.Point(0.87f, new RGBColor(0.88f, 0.44f, 0.94f)),
			new Gradient.Point(1.0f, new RGBColor(0.58f, 0.67f, 0.7f))
			};
		
		assertEquals(new Gradient(expectedPoints), gradient);
	}

	@Test
	public void testSetPointFloatRGBColor()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.4f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.75f, 0.11f, 0.37f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.88f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.58f, 0.67f, 0.7f))
		};
		gradient.setPoints(points);
		try
		{
    		gradient.setPoint(0.3f, new RGBColor(0.7f, 0.2f, 1.0f));
    		gradient.setPoint(0.7f, new RGBColor(0.93f, 0.12f, 0.33f));
    		gradient.setPoint(0.55f, new RGBColor(0.53f, 0.21f, 0.0f));
    		gradient.setPoint(0.23f, new RGBColor(0.11f, 0.6f, 0.2f));
    		gradient.setPoint(0.87f, new RGBColor(0.88f, 0.44f, 0.94f));
		}
		catch(OutOfGradientRangeException e)
		{ fail("Exception was throw"); }
		
		boolean catched = false;
		try
		{ gradient.setPoint(-0.3f, new RGBColor(0.7f, 0.2f, 1.0f)); }
		catch (OutOfGradientRangeException e)
		{ catched = true; }
		assertTrue("OutOfGradintRangeException not thrown", catched);
		
		catched = false;
		try
		{ gradient.setPoint(1.3f, new RGBColor(0.7f, 0.2f, 1.0f)); }
		catch (OutOfGradientRangeException e)
		{ catched = true; }
		assertTrue("OutOfGradintRangeException not thrown", catched);
		
		final Gradient.Point[] expectedPoints = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.4f, 0.7f)),
			new Gradient.Point(0.23f, new RGBColor(0.11f, 0.6f, 0.2f)),
			new Gradient.Point(0.3f, new RGBColor(0.7f, 0.2f, 1.0f)),
			new Gradient.Point(0.55f, new RGBColor(0.53f, 0.21f, 0.0f)),
			new Gradient.Point(0.7f, new RGBColor(0.93f, 0.12f, 0.33f)),
			new Gradient.Point(0.87f, new RGBColor(0.88f, 0.44f, 0.94f)),
			new Gradient.Point(1.0f, new RGBColor(0.58f, 0.67f, 0.7f))
			};
		
		assertEquals(new Gradient(expectedPoints), gradient);
	}

	@Test
	public void testRemovePointFloat()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		
		try
		{
			gradient.removePoint(0.3f);
			gradient.removePoint(0.7f);
		}
		catch(PointNotFoundAtGradientException e)
		{ fail("Exception was thrown"); }
		catch(UnremovablePointInGradientException e)
		{ fail("Exception was thrown"); }
		
		boolean catched = false;
		try
		{ gradient.removePoint(0.0f); }
		catch (UnremovablePointInGradientException e)
		{ catched = true; }
		catch(PointNotFoundAtGradientException e)
		{ fail("Exception was thrown"); }
		
		assertTrue("UnremovablePointInGradientException is not thrown", catched);
		
		catched = false;
		try
		{ gradient.removePoint(1.0f); }
		catch (UnremovablePointInGradientException e)
		{ catched = true; }
		catch(PointNotFoundAtGradientException e)
		{ fail("Exception was thrown"); }
		
		assertTrue("UnremovablePointInGradientException is not thrown", catched);
		
		catched = false;
		try
		{ gradient.removePoint(0.44f); }
		catch (UnremovablePointInGradientException e)
		{ fail("Exception was thrown"); }
		catch(PointNotFoundAtGradientException e)
		{ catched = true; }
		
		assertTrue("PointNotFounfAtGradientException is not thrown", catched);
		
		final Gradient.Point[] expectedPoints = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		assertEquals(new Gradient(expectedPoints), gradient);
	}
	
	@Test
	public void testRemovePointInt()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		
		try
		{
			gradient.removePoint(1);
			gradient.removePoint(2);
		}
		catch(IndexOutOfBoundsException e)
		{ fail("Exception was thrown"); }
		catch(UnremovablePointInGradientException e)
		{ fail("Exception was thrown"); }
		
		boolean catched = false;
		try
		{ gradient.removePoint(0); }
		catch (UnremovablePointInGradientException e)
		{ catched = true; }
		catch(IndexOutOfBoundsException e)
		{ fail("Exception was thrown"); }
		
		assertTrue("UnremovablePointInGradientException not throw", catched);
		
		catched = false;
		try
		{ gradient.removePoint(2); }
		catch (UnremovablePointInGradientException e)
		{ catched = true; }
		catch(IndexOutOfBoundsException e)
		{ fail("Exception was thrown"); }
		
		assertTrue("UnremovablePointInGradientException not throw", catched);
		
		final Gradient.Point[] expectedPoints = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		assertEquals(new Gradient(expectedPoints), gradient);
	}

	@Test
	public void testBinarySearch()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		assertEquals(-1, gradient.binarySearch(-0.4f));
		assertEquals(0, gradient.binarySearch(0.0f));
		assertEquals(-2, gradient.binarySearch(0.24f));
		assertEquals(1, gradient.binarySearch(0.3f));
		assertEquals(-3, gradient.binarySearch(0.41f));
		assertEquals(2, gradient.binarySearch(0.5f));
		assertEquals(-4, gradient.binarySearch(0.63f));
		assertEquals(3, gradient.binarySearch(0.7f));
		assertEquals(-5, gradient.binarySearch(0.91f));
		assertEquals(4, gradient.binarySearch(1.0f));
		assertEquals(-6, gradient.binarySearch(1.6f));
	}

	@Test
	public void testFloorIndex()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		assertEquals(-1, gradient.floorIndex(-0.4f));
		assertEquals(0, gradient.floorIndex(0.0f));
		assertEquals(0, gradient.floorIndex(0.24f));
		assertEquals(1, gradient.floorIndex(0.3f));
		assertEquals(1, gradient.floorIndex(0.41f));
		assertEquals(2, gradient.floorIndex(0.5f));
		assertEquals(2, gradient.floorIndex(0.63f));
		assertEquals(3, gradient.floorIndex(0.7f));
		assertEquals(3, gradient.floorIndex(0.91f));
		assertEquals(4, gradient.floorIndex(1.0f));
		assertEquals(4, gradient.floorIndex(1.6f));
	}

	@Test
	public void testFloorPoint()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		assertEquals(null, gradient.floorPoint(-0.4f));
		assertEquals(new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
				gradient.floorPoint(0.0f));
		assertEquals(new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
				gradient.floorPoint(0.24f));
		assertEquals(new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
				gradient.floorPoint(0.3f));
		assertEquals(new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
				gradient.floorPoint(0.41f));
		assertEquals(new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
				gradient.floorPoint(0.5f));
		assertEquals(new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
				gradient.floorPoint(0.63f));
		assertEquals(new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
				gradient.floorPoint(0.7f));
		assertEquals(new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
				gradient.floorPoint(0.91f));
		assertEquals(new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f)),
				gradient.floorPoint(1.0f));
		assertEquals(new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f)),
				gradient.floorPoint(1.6f));
	}

	@Test
	public void testCeilIndex()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		assertEquals(0, gradient.ceilIndex(-0.4f));
		assertEquals(0, gradient.ceilIndex(0.0f));
		assertEquals(1, gradient.ceilIndex(0.24f));
		assertEquals(1, gradient.ceilIndex(0.3f));
		assertEquals(2, gradient.ceilIndex(0.41f));
		assertEquals(2, gradient.ceilIndex(0.5f));
		assertEquals(3, gradient.ceilIndex(0.63f));
		assertEquals(3, gradient.ceilIndex(0.7f));
		assertEquals(4, gradient.ceilIndex(0.91f));
		assertEquals(4, gradient.ceilIndex(1.0f));
		assertEquals(5, gradient.ceilIndex(1.6f));
	}

	@Test
	public void testCeilPoint()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		assertEquals(new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
				gradient.ceilPoint(-0.4f));
		assertEquals(new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
				gradient.ceilPoint(0.0f));
		assertEquals(new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
				gradient.ceilPoint(0.24f));
		assertEquals(new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
				gradient.ceilPoint(0.3f));
		assertEquals(new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
				gradient.ceilPoint(0.41f));
		assertEquals(new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
				gradient.ceilPoint(0.5f));
		assertEquals(new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
				gradient.ceilPoint(0.63f));
		assertEquals(new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
				gradient.ceilPoint(0.7f));
		assertEquals(new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f)),
				gradient.ceilPoint(0.91f));
		assertEquals(new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f)),
				gradient.ceilPoint(1.0f));
		assertEquals(null, gradient.ceilPoint(1.6f));
	}

	@Test
	public void testContains()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.6f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.25f, 0.15f, 0.57f)),
			new Gradient.Point(0.5f, new RGBColor(0.16f, 0.5f, 0.6f)),
			new Gradient.Point(0.7f, new RGBColor(0.35f, 0.38f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.52f, 0.63f, 0.9f))
		};
		gradient.setPoints(points);
		assertFalse(gradient.contains(-0.4f));
		assertTrue(gradient.contains(0.0f));
		assertFalse(gradient.contains(0.24f));
		assertTrue(gradient.contains(0.3f));
		assertTrue(gradient.contains(0.7f));
		assertTrue(gradient.contains(1.0f));
		assertFalse(gradient.contains(1.6f));
	}

	@Test
	public void testEvaluateColor()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.66f, 0.57f)),
			new Gradient.Point(0.3f, new RGBColor(0.75f, 0.71f, 0.7f)),
			new Gradient.Point(0.5f, new RGBColor(0.38f, 0.15f, 0.25f)),
			new Gradient.Point(0.7f, new RGBColor(0.25f, 0.38f, 0.89f)),
			new Gradient.Point(1.0f, new RGBColor(0.12f, 0.33f, 0.43f))
		};
		gradient.setPoints(points);
		
		RGBColor destColor = new RGBColor();
		{
			gradient.evaluateColor(0.16f, destColor);
			assertEqualsRGBColor(
					new RGBColor(0.4466666f, 0.6866666f, 0.63933333f), destColor);
			gradient.evaluateColor(0.64f, destColor);
			assertEqualsRGBColor(
					new RGBColor(0.289f, 0.311f, 0.698f), destColor);
			gradient.evaluateColor(0.0f, destColor);
			assertEqualsRGBColor(
					new RGBColor(0.1f, 0.66f, 0.57f), destColor);
			gradient.evaluateColor(1.0f, destColor);
			assertEqualsRGBColor(
					new RGBColor(0.12f, 0.33f, 0.43f), destColor);
			gradient.evaluateColor(0.5f, destColor);
			assertEqualsRGBColor(
					new RGBColor(0.38f, 0.15f, 0.25f), destColor);
		}
		
		gradient.evaluateColor(-0.6f, destColor);
		assertEqualsRGBColor(
					 new RGBColor(0.1f, 0.66f, 0.57f), destColor);
		
		gradient.evaluateColor(1.6f, destColor);
		assertEqualsRGBColor(
					 new RGBColor(0.12f, 0.33f, 0.43f), destColor);
	}

	@Test
	public void testEqualsObject()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.5f, 0.16f, 0.32f)),
			new Gradient.Point(0.7f, new RGBColor(0.75f, 0.31f, 0.8f)),
			new Gradient.Point(1.0f, new RGBColor(0.88f, 0.42f, 0.35f))
		};
		gradient.setPoints(points);
		assertTrue(gradient.equals(new Gradient(points)));
		assertTrue(gradient.equals(gradient));
		
		final Gradient.Point[] points2 = {
			new Gradient.Point(0.0f, new RGBColor(0.5f, 0.16f, 0.32f)),
			new Gradient.Point(0.67f, new RGBColor(0.75f, 0.31f, 0.8f)),
			new Gradient.Point(1.0f, new RGBColor(0.88f, 0.42f, 0.35f))
		};
		
		assertFalse(gradient.equals(new Gradient(points2)));
	}
	
	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(gradient.isValidNode(document.getDocumentElement()));
	}

	@Test
	public void testGetFromNode()
	{
		prepareDocument();
		Element docElem = document.getDocumentElement();
		Element pointElem1 = document.createElement("point");
		Element pointElem2 = document.createElement("point");
		Element pointElem3 = document.createElement("point");
		Element posElem1 = document.createElement("position");
		Element posElem2 = document.createElement("position");
		Element posElem3 = document.createElement("position");
		posElem1.setTextContent(" 0.0");
		posElem2.setTextContent(" 0.6");
		posElem3.setTextContent(" 1.0 ");
		RGBColor color1 = new RGBColor(0.73f, 0.45f, 0.12f);
		RGBColor color2 = new RGBColor(0.64f, 0.13f, 0.87f);
		RGBColor color3 = new RGBColor(0.28f, 0.73f, 0.57f);
		pointElem1.appendChild(posElem1);
		pointElem1.appendChild(color1.createNode(document));
		pointElem2.appendChild(posElem2);
		pointElem2.appendChild(color2.createNode(document));
		pointElem3.appendChild(posElem3);
		pointElem3.appendChild(color3.createNode(document));
		docElem.appendChild(pointElem1);
		docElem.appendChild(pointElem3);
		docElem.appendChild(pointElem2);
		
		try
		{ gradient.getFromNode(docElem); }
		catch(DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		final Gradient.Point[] expectedPoints = {
			new Gradient.Point(0.0f, new RGBColor(0.73f, 0.45f, 0.12f)),
			new Gradient.Point(0.6f, new RGBColor(0.64f, 0.13f, 0.87f)),
			new Gradient.Point(1.0f, new RGBColor(0.28f, 0.73f, 0.57f))
		};
		assertEquals(new Gradient(expectedPoints), gradient);
		
		posElem2.setTextContent(" 0.6x");
		boolean catched = false;
		try
		{ gradient.getFromNode(docElem); }
		catch(DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
		
		posElem2.setTextContent(" 0.6");
		posElem2.setAttribute("Fortest", "MyValue");
		catched = false;
		try
		{ gradient.getFromNode(docElem); }
		catch(DOMNodeException e)
		{ catched = true; }
		assertTrue("No DOMNodeException", catched);
		
		posElem2.removeAttribute("Fortest");
		
		docElem.setAttribute("Fortest2", "Myvalue2");
		catched = false;
		try
		{ gradient.getFromNode(docElem); }
		catch(DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
	}

	@Test
	public void testCreateNode()
	{
		prepareDocument();
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.22f, 0.64f, 0.24f)),
			new Gradient.Point(0.4f, new RGBColor(0.73f, 0.45f, 0.54f)),
			new Gradient.Point(0.7f, new RGBColor(0.34f, 0.84f, 0.25f)),
			new Gradient.Point(1.0f, new RGBColor(0.57f, 0.33f, 0.64f)),
		};
		gradient.setPoints(points);
		Node node = gradient.createNode(document);
		Gradient newGradient = new Gradient();
		try
		{ newGradient.getFromNode(node); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(newGradient, gradient);
	}
	
	/* testing XML input/output stream */
	@Test
	public void testIOStream()
	{
		final Gradient.Point[] points = {
			new Gradient.Point(0.0f, new RGBColor(0.1f, 0.4f, 0.7f)),
			new Gradient.Point(0.3f, new RGBColor(0.75f, 0.11f, 0.37f)),
			new Gradient.Point(0.7f, new RGBColor(0.00035f, 0.88f, 0.16f)),
			new Gradient.Point(1.0f, new RGBColor(0.58f, 0.67f, 0.7f))
		};
		
		gradient.setPoints(points);
		ByteArrayOutputStream bOStream = new ByteArrayOutputStream();
		try
		{ gradient.writeToStream(bOStream); }
		catch(DOMDocumentException e)
		{ fail("DOMDocument exception was thrown"); }
		catch(IOException e)
		{ fail("IO exception was thrown"); }
		
		ByteArrayInputStream bIStream = new ByteArrayInputStream(bOStream.toByteArray());
		
		Gradient newGradient = new Gradient();
		try
		{ newGradient.readFromStream(bIStream); }
		catch(DOMDocumentException e)
		{ fail("DOMDocument exception was thrown"); }
		catch(DOMNodeException e)
		{ fail("DOMNode exception was thrown"); }
		catch(IOException e)
		{ fail("IO exception was thrown"); }
		
		assertEquals(gradient, newGradient);
	}
}
