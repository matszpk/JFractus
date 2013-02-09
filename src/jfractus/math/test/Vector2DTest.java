/**
 * Vector2DTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jfractus.dom.DOMNodeException;
import jfractus.math.Vector2D;
import jfractus.math.Vector3D;
import jfractus.math.Matrix3D;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Vector2DTest
{
	private Vector2D vector;
	
	private void assertEqualsVector2D(double x, double y)
	{
		assertEquals(x, vector.x, 3.0e-13);
		assertEquals(y, vector.y, 3.0e-13);
	}
	
	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "vector2d", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	@Before
	public void setUp() throws Exception
	{
		vector = new Vector2D();
	}

	@Test
	public void testTranslateVector2D()
	{
		vector.set(4.0, 6.0);
		vector.translate(new Vector2D(11.0, 7.5));
		assertEqualsVector2D(15.0, 13.5);
		vector.set(-4.6, 7.0);
		vector.translate(new Vector2D(61.0, 17.5));
		assertEqualsVector2D(56.4, 24.5);
	}

	@Test
	public void testTranslateDoubleDouble()
	{
		vector.set(4.0, 6.0);
		vector.translate(11.0, 7.5);
		assertEqualsVector2D(15.0, 13.5);
		vector.set(-4.6, 7.0);
		vector.translate(61.0, 17.5);
		assertEqualsVector2D(56.4, 24.5);
	}

	@Test
	public void testScaleDouble()
	{
		vector.set(7.5, 11.6);
		vector.scale(1.5);
		assertEqualsVector2D(11.25, 17.4);
		vector.set(7.5, -11.6);
		vector.scale(-5);
		assertEqualsVector2D(-37.5, 58.0);
	}

	@Test
	public void testScaleVector2D()
	{
		vector.set(11, -0.75);
		vector.scale(new Vector2D(-2.5, 7));
		assertEqualsVector2D(-27.5, -5.25);
		vector.set(7, 23);
		vector.scale(new Vector2D(-13, 0.25));
		assertEqualsVector2D(-91.0, 5.75);
	}

	@Test
	public void testScaleDoubleDouble()
	{
		vector.set(11, -0.75);
		vector.scale(-2.5, 7);
		assertEqualsVector2D(-27.5, -5.25);
		vector.set(7, 23);
		vector.scale(-13, 0.25);
		assertEqualsVector2D(-91.0, 5.75);
	}

	@Test
	public void testTranslateAndScaleVector2DVector2D()
	{
		vector.set(1.6, 65.1);
		vector.translateAndScale(new Vector2D(53.0, -6.1), new Vector2D(-4.2, 11.2));
		assertEqualsVector2D(46.28, 723.02);
		vector.set(-12.5, -5.1);
		vector.translateAndScale(new Vector2D(3.3, -5.5), new Vector2D(22.2, -1.2));
		assertEqualsVector2D(-274.2, 0.62);
	}

	@Test
	public void testTranslateAndScaleDoubleDoubleDoubleDouble()
	{
		vector.set(1.6, 65.1);
		vector.translateAndScale(53.0, -6.1, -4.2, 11.2);
		assertEqualsVector2D(46.28, 723.02);
		vector.set(-12.5, -5.1);
		vector.translateAndScale(3.3, -5.5, 22.2, -1.2);
		assertEqualsVector2D(-274.2, 0.62);
	}

	@Test
	public void testRotate()
	{
		vector.set(4.6, 21.5);
		vector.rotate(Math.PI/180.0*78.0);
		assertEqualsVector2D(-20.07377963801513, 8.96958031595733);
	}

	@Test
	public void testLength()
	{
		vector.set(3, 5);
		assertEquals(vector.length(), 5.83095189484530, 1.0e-14);
		vector.set(11.2, 7.6);
		assertEquals(vector.length(), 13.5351394525509, 1.0e-14);
	}

	@Test
	public void testNorm2()
	{
		vector.set(3, 5);
		assertEquals(vector.norm2(), 34.0, 1.0e-14);
		vector.set(11.2, 7.6);
		assertEquals(vector.norm2(), 183.2, 1.0e-14);
	}

	@Test
	public void testDistanceVector2D()
	{
		vector.set(2.7, 6.11);
		assertEquals(vector.distance(new Vector2D(-1.32, 4.1)),
				4.49449663477458, 1.0e-14);
	}

	@Test
	public void testDistanceDoubleDouble()
	{
		vector.set(2.7, 6.11);
		assertEquals(vector.distance(-1.32, 4.1), 4.49449663477458, 1.0e-14);
	}
	
	@Test
	public void transformDoubleArrayArray()
	{
		vector.set(2.7, 6.11);
		final double[]m = {
				0.67, 1.2, -5.5,
				-3.5, 1.5, -0.4,
				1.54, -3.7, -4.1
		};
		vector.transform(m);
		assertEqualsVector2D(3.641, -0.685);
	}
	
	@Test
	public void transformMatrix3D()
	{
		vector.set(2.7, 6.11);
		Matrix3D m = new Matrix3D(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		vector.transform(m);
		assertEqualsVector2D(3.641, -0.685);
	}

	@Test
	public void testToVector3D()
	{
		vector.set(5.4, 1.3);
		Vector3D v3d = vector.toVector3D();
		assertEquals(v3d.x, 5.4, 1.0e-16);
		assertEquals(v3d.y, 1.3, 1.0e-16);
		assertEquals(v3d.z, 0.0, 1.0e-16);
	}

	@Test
	public void testEqualsObject()
	{
		vector.set(4.1, 44.4);
		assertTrue(vector.equals(new Vector2D(4.1, 44.4)));
		assertTrue(vector.equals(vector));
	}

	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(vector.isValidNode(document.getDocumentElement()));
	}
	
	@Test
	public void testGetFromNode()
	{
		prepareDocument();
		Element elem = document.getDocumentElement();
		elem.setTextContent(" -43.67 2.55  ");
		
		try
		{ vector.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(new Vector2D(-43.67, 2.55), vector);
		
		boolean catched = false;
		elem.setTextContent(" 43.67,, 4.87  ");
		try
		{ vector.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
		
		catched = false;
		elem.setTextContent(" 43.67 4.87  ");
		elem.setAttribute("Fortest", "Value");
		try
		{ vector.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
	}
	
	@Test
	public void testCreateNode()
	{
		prepareDocument();
		vector.set(0.775, -1.4);
		Node node = vector.createNode(document);
		Vector2D newVector = new Vector2D();
		try
		{ newVector.getFromNode(node); }
		catch(DOMNodeException e)
		{  fail("Exception was thrown"); }
		
		assertEquals(newVector, vector);
	}
}
