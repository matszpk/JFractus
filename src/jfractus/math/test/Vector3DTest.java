/**
 * Vector3DTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jfractus.math.Vector2D;
import jfractus.math.Vector3D;
import jfractus.math.Matrix3D;

import jfractus.dom.DOMNodeException;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Vector3DTest
{
	private Vector3D vector;
	
	Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "vector3d", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	private void assertEqualsVector3D(double x, double y, double z)
	{
		assertEquals(x, vector.x, 5.0e-14);
		assertEquals(y, vector.y, 5.0e-14);
		assertEquals(z, vector.z, 5.0e-14);
	}
	
	@Before
	public void setUp() throws Exception
	{
		vector = new Vector3D();
	}

	@Test
	public void testTranslateVector2D()
	{
		vector.set(4.2, -14.1, 54.6);
		vector.translate(new Vector2D(4.6, -5.2));
		assertEqualsVector3D(8.8, -19.3, 54.6);
		vector.set(-5.3, 11.1, 55.3);
		vector.translate(new Vector2D(6.5, 15.5));
		assertEqualsVector3D(1.2, 26.6, 55.3);
	}

	@Test
	public void testTranslateVector3D()
	{
		vector.set(4.2, -14.1, 54.6);
		vector.translate(new Vector3D(4.6, -5.2, 5.5));
		assertEqualsVector3D(8.8, -19.3, 60.1);
		vector.set(-5.3, 11.1, 55.3);
		vector.translate(new Vector3D(6.5, 15.5, -32.1));
		assertEqualsVector3D(1.2, 26.6, 23.2);
	}

	@Test
	public void testTranslateDoubleDouble()
	{
		vector.set(4.2, -14.1, 54.6);
		vector.translate(4.6, -5.2);
		assertEqualsVector3D(8.8, -19.3, 54.6);
		vector.set(-5.3, 11.1, 55.3);
		vector.translate(6.5, 15.5);
		assertEqualsVector3D(1.2, 26.6, 55.3);
	}

	@Test
	public void testTranslateDoubleDoubleDouble()
	{
		vector.set(4.2, -14.1, 54.6);
		vector.translate(4.6, -5.2, 5.5);
		assertEqualsVector3D(8.8, -19.3, 60.1);
		vector.set(-5.3, 11.1, 55.3);
		vector.translate(6.5, 15.5, -32.1);
		assertEqualsVector3D(1.2, 26.6, 23.2);
	}

	@Test
	public void testScaleDouble()
	{
		vector.set(4.4, -7.4, -9.2);
		vector.scale(1.68);
		assertEqualsVector3D(7.3920, -12.4320, -15.4560);
		vector.set(6.4, -4.1, 33.3);
		vector.scale(-6.3);
		assertEqualsVector3D(-40.320, 25.830, -209.790);
	}

	@Test
	public void testScaleVector2D()
	{
		vector.set(4.4, 17.7, 3.7);
		vector.scale(new Vector2D(6.4, 1.1));
		assertEqualsVector3D(28.16, 19.47, 3.7);
		vector.set(23.1, -6.4, 59.1);
		vector.scale(new Vector2D(-3.6, -15.7));
		assertEqualsVector3D(-83.16, 100.48, 59.1);
	}

	@Test
	public void testScaleVector3D()
	{
		vector.set(5.3, -51.5, -54.1);
		vector.scale(new Vector3D(-1.2, -0.6, 11.2));
		assertEqualsVector3D(-6.36, 30.9, -605.92);
		vector.set(-1.3, 0.6, -0.52);
		vector.scale(new Vector3D(124.1, -44.5, 224.4));
		assertEqualsVector3D(-161.33, -26.7, -116.688);
	}

	@Test
	public void testScaleDoubleDouble()
	{
		vector.set(4.4, 17.7, 3.7);
		vector.scale(6.4, 1.1);
		assertEqualsVector3D(28.16, 19.47, 3.7);
		vector.set(23.1, -6.4, 59.1);
		vector.scale(-3.6, -15.7);
		assertEqualsVector3D(-83.16, 100.48, 59.1);	
	}

	@Test
	public void testScaleDoubleDoubleDouble()
	{
		vector.set(5.3, -51.5, -54.1);
		vector.scale(-1.2, -0.6, 11.2);
		assertEqualsVector3D(-6.36, 30.9, -605.92);
		vector.set(-1.3, 0.6, -0.52);
		vector.scale(124.1, -44.5, 224.4);
		assertEqualsVector3D(-161.33, -26.7, -116.688);
	}

	@Test
	public void testTranslateAndScaleVector2DVector2D()
	{
		vector.set(-6.54, 2.5, -12.7);
		vector.translateAndScale(new Vector2D(-1.4, -8.53), new Vector2D(16.4, -2.7));
		assertEqualsVector3D(-108.656, -15.28, -12.7);
		vector.set(-1.06, 0.23, -0.56);
		vector.translateAndScale(new Vector2D(21.1, 17.4), new Vector2D(9.5, 15.6));
		assertEqualsVector3D(11.03, 20.988, -0.56);
	}

	@Test
	public void testTranslateAndScaleVector3DVector3D()
	{
		vector.set(3.2, 6.1, -4.87);
		vector.translateAndScale(new Vector3D(-11.2, 3.5, 8.7),
				new Vector3D(0.4, 3.7, 0.95));
		assertEqualsVector3D(-9.92, 26.07, 4.0735);
	}

	@Test
	public void testTranslateAndScaleDoubleDoubleDoubleDouble()
	{
		vector.set(-6.54, 2.5, -12.7);
		vector.translateAndScale(-1.4, -8.53, 16.4, -2.7);
		assertEqualsVector3D(-108.656, -15.28, -12.7);
		vector.set(-1.06, 0.23, -0.56);
		vector.translateAndScale(21.1, 17.4, 9.5, 15.6);
		assertEqualsVector3D(11.03, 20.988, -0.56);		
	}

	@Test
	public void testTranslateAndScaleDoubleDoubleDoubleDoubleDoubleDouble()
	{
		vector.set(3.2, 6.1, -4.87);
		vector.translateAndScale(-11.2, 3.5, 8.7, 0.4, 3.7, 0.95);
		assertEqualsVector3D(-9.92, 26.07, 4.0735);
	}

	@Test
	public void testRotate()
	{
		Vector3D toCompare = new Vector3D(6.7, 1.4, -12.5);
		vector.set(6.7, 1.4, -12.5);
		toCompare.rotateXAxis(1.3).rotateYAxis(-1.7).rotateZAxis(-2.5);
		vector.rotate(1.3, -1.7, -2.5);
		assertEqualsVector3D(toCompare.x, toCompare.y, toCompare.z);
	}

	@Test
	public void testRotateXAxis()
	{
		vector.set(4.6, 21.5, -6.7);
		vector.rotateXAxis(Math.PI/180.0*78.0);
		assertEqualsVector3D(-20.07377963801513, 8.96958031595733, -6.7);
	}

	@Test
	public void testRotateYAxis()
	{
		vector.set(-6.7, 4.6, 21.5);
		vector.rotateYAxis(Math.PI/180.0*78.0);
		assertEqualsVector3D(-6.7, -20.07377963801513, 8.96958031595733);
	}

	@Test
	public void testRotateZAxis()
	{
		vector.set(21.5, -6.7, 4.6);
		vector.rotateZAxis(Math.PI/180.0*78.0);
		assertEqualsVector3D(8.96958031595733, -6.7, -20.07377963801513);
	}

	@Test
	public void testLength()
	{
		vector.set(6.8, -7.4, 1.5);
		assertEquals(vector.length(), 10.161200716450788, 5.0e-15);
		vector.set(1.77, 7.4, 11.8);
		assertEquals(vector.length(), 14.04040241588538, 5.0e-15);
	}

	@Test
	public void testNorm2()
	{
		vector.set(7.8, 9.2, 11.7);
		assertEquals(vector.norm2(), 282.37, 1.0e-15);
		vector.set(-6.7, -53.1, 42.9);
		assertEquals(vector.norm2(), 4704.91, 1.0e-15);
	}

	@Test
	public void testDistanceVector3D()
	{
		vector.set(-6.4, 32.2, 7.6);
		assertEquals(vector.distance(new Vector3D(7.5, -4.8, 13.7)),
				39.992749342849635,1.0e-15);
		vector.set(0.7, 0.31, -6.6);
		assertEquals(vector.distance(new Vector3D(-1.6, -0.48, 0.5)),
				7.50493837416404, 1.0e-15);
	}

	@Test
	public void testDistanceDoubleDoubleDouble()
	{
		vector.set(-6.4, 32.2, 7.6);
		assertEquals(vector.distance(7.5, -4.8, 13.7),
				39.992749342849635,1.0e-15);
		vector.set(0.7, 0.31, -6.6);
		assertEquals(vector.distance(-1.6, -0.48, 0.5),
				7.50493837416404, 1.0e-15);
	}

	@Test
	public void transformDoubleArrayArray()
	{
		vector.set(-6.4, -2.6, 3.85);
		final double[] m = {
				0.67, 1.2, -5.5,
				-3.5, 1.5, -0.4,
				1.54, -3.7, -4.1
		};
		vector.transform(m);
		assertEqualsVector3D(-28.583, 16.96, -16.021);
	}
	
	@Test
	public void transformMatrix3D()
	{
		vector.set(-6.4, -2.6, 3.85);
		Matrix3D m = new Matrix3D(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		vector.transform(m);
		assertEqualsVector3D(-28.583, 16.96, -16.021);
	}
	
	@Test
	public void testToVector2D()
	{
		vector.set(0.67, 12.6, -5.1);
		Vector2D v2d = vector.toVector2D();
		assertEquals(v2d.x, vector.x, 1.0e-16);
		assertEquals(v2d.y, vector.y, 1.0e-16);
	}

	@Test
	public void testEqualsObject()
	{
		vector.set(4.1, 44.4, -6.54);
		assertTrue(vector.equals(new Vector3D(4.1, 44.4, -6.54)));
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
		elem.setTextContent(" 4.32 \n 2.54 -0.44  ");
		
		try
		{ vector.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(new Vector3D(4.32, 2.54, -0.44), vector);
		
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
		vector.set(64.8, 24.4, -4.42);
		Node node = vector.createNode(document);
		Vector3D newVector = new Vector3D();
		try
		{ newVector.getFromNode(node); }
		catch(DOMNodeException e)
		{  fail("Exception was thrown"); }
		
		assertEquals(newVector, vector);
	}
}
