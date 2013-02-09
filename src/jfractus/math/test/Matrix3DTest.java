/**
 * Matrix3DTest.java
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

public class Matrix3DTest
{
	private Matrix3D matrix;
	
	Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "matrix3d", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	private void assertEqualsMatrix3D(Vector3D x, Vector3D y, Vector3D z)
	{
		Matrix3D m = new Matrix3D(x, y, z);
		for(int i = 0; i < 9; i++)
			assertEquals(m.m[i], matrix.m[i], 2.0e-14);
	}
	
	private void assertStrictEqualsMatrix3D(double[] m)
	{
		for(int i = 0; i < 9; i++)
			assertEquals(m[i], matrix.m[i], 0.0);
	}
	
	@Before
	public void setUp() throws Exception
	{
		matrix = new Matrix3D();
	}

	@Test
	public void testGetColumn()
	{
		final double[] x = {
				0.1, 0.2, 4.5,
				-5.1, 1.2, 6.5,
				2.1, -6.7, 14.6 };
		matrix.set(x);
		assertEquals(matrix.getColumn(0), new Vector3D(0.1, -5.1, 2.1));
		assertEquals(matrix.getColumn(1), new Vector3D(0.2, 1.2, -6.7));
		assertEquals(matrix.getColumn(2), new Vector3D(4.5, 6.5, 14.6));
	}

	@Test
	public void testGetColumn2D()
	{
		final double[] x = {
				0.1, 0.2, 4.5,
				-5.1, 1.2, 6.5,
				2.1, -6.7, 14.6 };
		matrix.set(x);
		assertEquals(matrix.getColumn2D(0), new Vector2D(0.1, -5.1));
		assertEquals(matrix.getColumn2D(1), new Vector2D(0.2, 1.2));
		assertEquals(matrix.getColumn2D(2), new Vector2D(4.5, 6.5));
	}
	
	@Test
	public void testSetColumnIntVector3D()
	{
		matrix.zero();
		matrix.setColumn(0, new Vector3D(4.1, 22.7, -55.1));
		assertEquals(matrix.getColumn(0), new Vector3D(4.1, 22.7, -55.1));
		matrix.setColumn(1, new Vector3D(76.1, -2.3, -7.61));
		assertEquals(matrix.getColumn(1), new Vector3D(76.1, -2.3, -7.61));
		matrix.setColumn(2, new Vector3D(66.2, -1.77, 0.6));
		assertEquals(matrix.getColumn(2), new Vector3D(66.2, -1.77, 0.6));
	}
	
	@Test
	public void testSetColumnIntDoubleDoubleDouble()
	{
		matrix.zero();
		matrix.setColumn(0, 4.1, 22.7, -55.1);
		assertEquals(matrix.getColumn(0), new Vector3D(4.1, 22.7, -55.1));
		matrix.setColumn(1, 76.1, -2.3, -7.61);
		assertEquals(matrix.getColumn(1), new Vector3D(76.1, -2.3, -7.61));
		matrix.setColumn(2, 66.2, -1.77, 0.6);
		assertEquals(matrix.getColumn(2), new Vector3D(66.2, -1.77, 0.6));
	}

	@Test
	public void testSetColumn2DIntVector2D()
	{
		matrix.zero();
		matrix.setColumn2D(0, new Vector2D(4.1, 22.7));
		assertEquals(matrix.getColumn(0), new Vector3D(4.1, 22.7, 0.0));
		matrix.setColumn2D(1, new Vector2D(76.1, -2.3));
		assertEquals(matrix.getColumn(1), new Vector3D(76.1, -2.3, 0.0));
		matrix.setColumn2D(2, new Vector2D(66.2, -1.77));
		assertEquals(matrix.getColumn(2), new Vector3D(66.2, -1.77, 0.0));
	}
	
	@Test
	public void testSetColumn2DIntDoubleDouble()
	{
		matrix.zero();
		matrix.setColumn2D(0, 4.1, 22.7);
		assertEquals(matrix.getColumn(0), new Vector3D(4.1, 22.7, 0.0));
		matrix.setColumn2D(1, 76.1, -2.3);
		assertEquals(matrix.getColumn(1), new Vector3D(76.1, -2.3, 0.0));
		matrix.setColumn2D(2, 66.2, -1.77);
		assertEquals(matrix.getColumn(2), new Vector3D(66.2, -1.77, 0.0));
	}

	@Test
	public void testGetRow()
	{
		final double[] x = {
				0.1, 0.2, 4.5,
				-5.1, 1.2, 6.5,
				2.1, -6.7, 14.6 };
		matrix.set(x);
		assertEquals(matrix.getRow(0), new Vector3D(0.1, 0.2, 4.5));
		assertEquals(matrix.getRow(1), new Vector3D(-5.1, 1.2, 6.5));
		assertEquals(matrix.getRow(2), new Vector3D(2.1, -6.7, 14.6));
	}

	@Test
	public void testGetRow2D()
	{
		final double[] x = {
				0.1, 0.2, 4.5,
				-5.1, 1.2, 6.5,
				2.1, -6.7, 14.6 };
		matrix.set(x);
		assertEquals(matrix.getRow2D(0), new Vector2D(0.1, 0.2));
		assertEquals(matrix.getRow2D(1), new Vector2D(-5.1, 1.2));
		assertEquals(matrix.getRow2D(2), new Vector2D(2.1, -6.7));
	}

	@Test
	public void testSetRowIntVector3D()
	{
		matrix.zero();
		matrix.setRow(0, new Vector3D(4.1, 22.7, -55.1));
		assertEquals(matrix.getRow(0), new Vector3D(4.1, 22.7, -55.1));
		matrix.setRow(1, new Vector3D(76.1, -2.3, -7.61));
		assertEquals(matrix.getRow(1), new Vector3D(76.1, -2.3, -7.61));
		matrix.setRow(2, new Vector3D(66.2, -1.77, 0.6));
		assertEquals(matrix.getRow(2), new Vector3D(66.2, -1.77, 0.6));
	}
	
	@Test
	public void testSetRowIntDoubleDoubleDouble()
	{
		matrix.zero();
		matrix.setRow(0, 4.1, 22.7, -55.1);
		assertEquals(matrix.getRow(0), new Vector3D(4.1, 22.7, -55.1));
		matrix.setRow(1, 76.1, -2.3, -7.61);
		assertEquals(matrix.getRow(1), new Vector3D(76.1, -2.3, -7.61));
		matrix.setRow(2, 66.2, -1.77, 0.6);
		assertEquals(matrix.getRow(2), new Vector3D(66.2, -1.77, 0.6));
	}

	@Test
	public void testSetRow2DVector2D()
	{
		matrix.zero();
		matrix.setRow2D(0, new Vector2D(4.1, 22.7));
		assertEquals(matrix.getRow(0), new Vector3D(4.1, 22.7, 0.0));
		matrix.setRow2D(1, new Vector2D(76.1, -2.3));
		assertEquals(matrix.getRow(1), new Vector3D(76.1, -2.3, 0.0));
		matrix.setRow2D(2, new Vector2D(66.2, -1.77));
		assertEquals(matrix.getRow(2), new Vector3D(66.2, -1.77, 0.0));
	}
	
	@Test
	public void testSetRow2DIntDoubleDouble()
	{
		matrix.zero();
		matrix.setRow2D(0, 4.1, 22.7);
		assertEquals(matrix.getRow(0), new Vector3D(4.1, 22.7, 0.0));
		matrix.setRow2D(1, 76.1, -2.3);
		assertEquals(matrix.getRow(1), new Vector3D(76.1, -2.3, 0.0));
		matrix.setRow2D(2, 66.2, -1.77);
		assertEquals(matrix.getRow(2), new Vector3D(66.2, -1.77, 0.0));
	}

	@Test
	public void testSetDoubleArray()
	{
		final double[] x = {
				0.1, 0.2, 4.5,
				-5.1, 1.2, 6.5,
				2.1, -6.7, 14.6 };
		matrix.set(x);
		assertEquals(matrix.getRow(0), new Vector3D(0.1, 0.2, 4.5));
		assertEquals(matrix.getRow(1), new Vector3D(-5.1, 1.2, 6.5));
		assertEquals(matrix.getRow(2), new Vector3D(2.1, -6.7, 14.6));
	}

	@Test
	public void testSetVector3DVector3DVector3D()
	{
		matrix.set(new Vector3D(0.1, 0.2, 4.5),
				new Vector3D(-5.1, 1.2, 6.5),
				new Vector3D(2.1, -6.7, 14.6));
		assertEquals(matrix.getRow(0), new Vector3D(0.1, 0.2, 4.5));
		assertEquals(matrix.getRow(1), new Vector3D(-5.1, 1.2, 6.5));
		assertEquals(matrix.getRow(2), new Vector3D(2.1, -6.7, 14.6));
	}

	@Test
	public void testIdentity()
	{
		matrix.zero();
		final double[] identity = {
			1.0, 0.0, 0.0,
			0.0, 1.0, 0.0,
			0.0, 0.0, 1.0,
		};
		matrix.identity();
		assertStrictEqualsMatrix3D(identity);
	}
	
	@Test
	public void testZero()
	{
		final double[] zero = {
			0.0, 0.0, 0.0,
			0.0, 0.0, 0.0,
			0.0, 0.0, 0.0,
		};
		matrix.zero();
		assertStrictEqualsMatrix3D(zero);
	}

	@Test
	public void testScaleMatrixDouble()
	{
		matrix.zero();
		final double[] scale1 = {
			-8.0, 0.0, 0.0,
			0.0, -8.0, 0.0,
			0.0, 0.0, -8.0,
		};
		matrix.scaleMatrix(-8.0);
		assertStrictEqualsMatrix3D(scale1);
	}

	@Test
	public void testScaleMatrixVector2D()
	{
		matrix.zero();
		final double[] scale1 = {
			3.56, 0.0, 0.0,
			0.0, 11.2, 0.0,
			0.0, 0.0, 1.0,
		};
		matrix.scaleMatrix(new Vector2D(3.56, 11.2));
		assertStrictEqualsMatrix3D(scale1);
	}

	@Test
	public void testScaleMatrixVector3D()
	{
		matrix.zero();
		final double[] scale1 = {
			0.676, 0.0, 0.0,
			0.0, 51.54, 0.0,
			0.0, 0.0, -56.1,
		};
		matrix.scaleMatrix(new Vector3D(0.676, 51.54, -56.1));
		assertStrictEqualsMatrix3D(scale1);
	}

	@Test
	public void testScaleMatrixDoubleDouble()
	{
		matrix.zero();
		final double[] scale1 = {
			3.56, 0.0, 0.0,
			0.0, 11.2, 0.0,
			0.0, 0.0, 1.0,
		};
		matrix.scaleMatrix(3.56, 11.2);
		assertStrictEqualsMatrix3D(scale1);
	}

	@Test
	public void testScaleMatrixDoubleDoubleDouble()
	{
		matrix.zero();
		final double[] scale1 = {
			0.676, 0.0, 0.0,
			0.0, 51.54, 0.0,
			0.0, 0.0, -56.1,
		};
		matrix.scaleMatrix(0.676, 51.54, -56.1);
		assertStrictEqualsMatrix3D(scale1);
	}

	@Test
	public void testTranslateMatrixVector2D()
	{
		matrix.zero();
		final double[] translate1 = {
			1.0, 0.0, 5.3,
			0.0, 1.0, -6.4,
			0.0, 0.0, 1.0,
		};
		matrix.translateMatrix(new Vector2D(5.3, -6.4));
		assertStrictEqualsMatrix3D(translate1);
	}

	@Test
	public void testTranslateMatrixDoubleDouble()
	{
		matrix.zero();
		final double[] translate1 = {
			1.0, 0.0, 5.3,
			0.0, 1.0, -6.4,
			0.0, 0.0, 1.0,
		};
		matrix.translateMatrix(5.3, -6.4);
		assertStrictEqualsMatrix3D(translate1);
	}

	@Test
	public void testTranslateAndScaleMatrixVector2DVector2D()
	{
		matrix.zero();
		final double[] st1 = {
			4.1, 0.0, 33.1,
			0.0, -6.45, -0.7,
			0.0, 0.0, 1.0,
		};
		matrix.translateAndScaleMatrix(new Vector2D(33.1, -0.7),
				new Vector2D(4.1, -6.45));
		assertStrictEqualsMatrix3D(st1);
	}

	@Test
	public void testTranslateAndScaleMatrixDoubleDoubleDoubleDouble()
	{
		matrix.zero();
		final double[] st1 = {
			4.1, 0.0, 33.1,
			0.0, -6.45, -0.7,
			0.0, 0.0, 1.0,
		};
		matrix.translateAndScaleMatrix(33.1, -0.7, 4.1, -6.45);
		assertStrictEqualsMatrix3D(st1);
	}

	@Test
	public void testRotateMatrix()
	{
		matrix.zero();
		matrix.rotateMatrix(0.62, -0.43, 1.4);
		assertEqualsMatrix3D(
				new Vector3D(-0.100359680827442, -0.433102404354524, 0.895740052585874),
				new Vector3D(0.528141060285259, 0.739787641504499,  0.416870802429211),
				new Vector3D(-0.843205167740910, 0.514914121790752, 0.154494311466416));
		matrix.rotateMatrix(-2.77, -1.53, 0.0);
		assertEqualsMatrix3D(
				new Vector3D(-0.931750235288572, 0.363099847204168, 0.0),
				new Vector3D(-0.014809031350042, -0.038001443820600, 0.999167945271476),
				new Vector3D( 0.362797728259376, 0.930974968099497, 0.040785011241591));
	}

	@Test
	public void testRotateXAxisMatrix()
	{
		matrix.zero();
		matrix.rotateXAxisMatrix(1.7);
		assertEqualsMatrix3D(
				new Vector3D(-0.128844494295525, -0.991664810452469, 0.0),
				new Vector3D(0.991664810452469, -0.128844494295525, 0.0),
				new Vector3D(0.0, 0.0, 1.0));
	}

	@Test
	public void testRotateYAxisMatrix()
	{
		matrix.zero();
		matrix.rotateYAxisMatrix(1.7);
		assertEqualsMatrix3D(
				new Vector3D(1.0, 0.0, 0.0),
				new Vector3D(0.0, -0.128844494295525, -0.991664810452469),
				new Vector3D(0.0, 0.991664810452469, -0.128844494295525));
	}

	@Test
	public void testRotateZAxisMatrix()
	{
		matrix.zero();
		matrix.rotateZAxisMatrix(1.7);
		assertEqualsMatrix3D(
				new Vector3D(-0.128844494295525, 0.0, 0.991664810452469),
				new Vector3D(0.0, 1.0, 0.0),
				new Vector3D(-0.991664810452469, 0.0, -0.128844494295525));
	}

	@Test
	public void testParaMatrixVector2DVector2DVector2D()
	{
		matrix.zero();
		final double[] para = {
			17.3, 7.3, -22.6,
			-14.1, -8.5, 7.21,
			0.0, 0.0, 1.0,
		};
		matrix.paraMatrix(new Vector2D(-22.6, 7.21),
				new Vector2D(17.3, -14.1), new Vector2D(7.3, -8.5));
		assertStrictEqualsMatrix3D(para);
	}

	@Test
	public void testParaMatrixDoubleDoubleDoubleDoubleDoubleDouble()
	{
		matrix.zero();
		final double[] para = {
			17.3, 7.3, -22.6,
			-14.1, -8.5, 7.21,
			0.0, 0.0, 1.0,
		};
		matrix.paraMatrix(-22.6, 7.21, 17.3, -14.1, 7.3, -8.5);
		assertStrictEqualsMatrix3D(para);
	}

	@Test
	public void testAddDoubleArray()
	{
		matrix.set(new Vector3D(0.7, -4.34, -6.4),
				new Vector3D(1.45, 1.6, -62.1),
				new Vector3D(13.5, -5.4, -0.74));
		final double[] toAdd = {
			-4.3, 1.2, -0.53,
			16.7, -4.5, -8.72,
			5.4, -4.6, 1.67,
		};
		matrix.add(toAdd);
		assertEqualsMatrix3D(new Vector3D(-3.6, -3.14, -6.93),
				new Vector3D(18.15, -2.9, -70.82),
				new Vector3D(18.9, -10., 0.93));
	}

	@Test
	public void testAddMatrix3D()
	{
		matrix.set(new Vector3D(0.7, -4.34, -6.4),
				new Vector3D(1.45, 1.6, -62.1),
				new Vector3D(13.5, -5.4, -0.74));
		final double[] toSub= {
			-4.3, 1.2, -0.53,
			16.7, -4.5, -8.72,
			5.4, -4.6, 1.67,
		};
		matrix.add(new Matrix3D(toSub));
		assertEqualsMatrix3D(new Vector3D(-3.6, -3.14, -6.93),
				new Vector3D(18.15, -2.9, -70.82),
				new Vector3D(18.9, -10., 0.93));
	}

	@Test
	public void testSubtractDoubleArray()
	{
		matrix.set(new Vector3D(0.7, -4.34, -6.4),
				new Vector3D(1.45, 1.6, -62.1),
				new Vector3D(13.5, -5.4, -0.74));
		final double[] toAdd = {
			-4.3, 1.2, -0.53,
			16.7, -4.5, -8.72,
			5.4, -4.6, 1.67,
		};
		matrix.subtract(toAdd);
		assertEqualsMatrix3D(new Vector3D(5., -5.54, -5.87),
				new Vector3D(-15.25,  6.1, -53.38),
				new Vector3D(8.1, -0.8, -2.41));
	}

	@Test
	public void testSubtractMatrix3D()
	{
		matrix.set(new Vector3D(0.7, -4.34, -6.4),
				new Vector3D(1.45, 1.6, -62.1),
				new Vector3D(13.5, -5.4, -0.74));
		final double[] toAdd = {
			-4.3, 1.2, -0.53,
			16.7, -4.5, -8.72,
			5.4, -4.6, 1.67,
		};
		matrix.subtract(new Matrix3D(toAdd));
		assertEqualsMatrix3D(new Vector3D(5., -5.54, -5.87),
				new Vector3D(-15.25,  6.1, -53.38),
				new Vector3D(8.1, -0.8, -2.41));
	}

	@Test
	public void testTransformDoubleArray()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		final double[] m2 = {
				-0.51, 0.41, -1.4,
				3.2, 0.5, 0.6,
				2.3, -3.66, 2.7
		};
		matrix.transform(m2);
		assertEqualsMatrix3D(new Vector3D(-3.9327, 5.183, 8.381),
				new Vector3D(1.318,  2.37, -20.26),
				new Vector3D(18.509, -12.72, -22.256));
	}

	@Test
	public void testTransformMatrix3D()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		final double[] m2 = {
				-0.51, 0.41, -1.4,
				3.2, 0.5, 0.6,
				2.3, -3.66, 2.7
		};
		matrix.transform(new Matrix3D(m2));
		assertEqualsMatrix3D(new Vector3D(-3.9327, 5.183, 8.381),
				new Vector3D(1.318,  2.37, -20.26),
				new Vector3D(18.509, -12.72, -22.256));
	}

	@Test
	public void testNegate()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		matrix.negate();
		final double[] neg = {
				-0.67, -1.2, 5.5,
				3.5, -1.5, 0.4,
				-1.54, 3.7, 4.1
		};
		assertStrictEqualsMatrix3D(neg);
	}

	@Test
	public void testInverse()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		matrix.inverse();
		assertEqualsMatrix3D(
				new Vector3D(0.0935148722964336, -0.3097143935689222, -0.0952307415128819),
				new Vector3D(0.1834264192383257, -0.0701422823266696, -0.2392166811902740),
				new Vector3D(-0.1304060604500725, -0.0530326149969421, -0.0637935662258108));
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		matrix.inverse();
		assertEqualsMatrix3D(
				new Vector3D(0.2681946551664153, 0.3038178031030712, 0.0715488279152366),
				new Vector3D(-0.5490956560936762, 0.1393916383168932, -0.3156921857486231),
				new Vector3D(-0.9727917808094851, -0.0698546484804572, -0.1185169274240758));
	}

	@Test
	public void testDeterminant()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		assertEquals(matrix.determinant(), -81.5913, 5.0e-14);
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		assertEquals(matrix.determinant(), 13.22174, 5.0e-14);
	}

	@Test
	public void testTranspose()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		matrix.transpose();
		final double[] trans = {
				0.67, -3.5, 1.54,
				1.2, 1.5, -3.7,
				-5.5, -0.4, -4.1
		};
		assertStrictEqualsMatrix3D(trans);
	}

	@Test
	public void testSquare()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		matrix.square();
		assertEqualsMatrix3D(
				new Vector3D(-12.2211, 22.954, 18.385),
				new Vector3D(-8.211, -0.47, 20.29),
				new Vector3D(7.6678, 11.468, 9.82));
	}

	@Test
	public void testTranslateVector2D()
	{
		matrix.set(new Vector3D(4.2, 11.2, 5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(0.0, 0.0, 1.0));
		matrix.translate(new Vector2D(6.6, 1.2));
		assertEqualsMatrix3D(new Vector3D(4.2, 11.2, 11.8),
				new Vector3D(-1.4, 2.3, 2.6),
				new Vector3D(0.0, 0.0, 1.0));
	}

	@Test
	public void testTranslateDoubleDouble()
	{
		matrix.set(new Vector3D(4.2, 11.2, 5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(0.0, 0.0, 1.0));
		matrix.translate(6.6, 1.2);
		assertEqualsMatrix3D(new Vector3D(4.2, 11.2, 11.8),
				new Vector3D(-1.4, 2.3, 2.6),
				new Vector3D(0.0, 0.0, 1.0));
	}

	@Test
	public void testScaleDouble()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(2.4, 22.0, -2.0));
		matrix.scale(5.6);
		assertEqualsMatrix3D(new Vector3D(23.52, 62.72, -29.12),
				new Vector3D(-7.84, 12.88, 7.84),
				new Vector3D(13.44, 123.2, -11.2));
	}

	@Test
	public void testScaleVector2D()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(2.4, 22.0, -2.0));
		matrix.scale(new Vector2D(-5.7, 11.7));
		assertEqualsMatrix3D(new Vector3D(-23.94, -63.84, 29.64),
				new Vector3D(-16.38, 26.91, 16.38),
				new Vector3D(2.4, 22.0, -2.0));
	}

	@Test
	public void testScaleDoubleDouble()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(2.4, 22.0, -2.0));
		matrix.scale(-5.7, 11.7);
		assertEqualsMatrix3D(new Vector3D(-23.94, -63.84, 29.64),
				new Vector3D(-16.38, 26.91, 16.38),
				new Vector3D(2.4, 22.0, -2.0));
	}

	@Test
	public void testScaleVector3D()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(2.4, 22.0, -2.0));
		matrix.scale(new Vector3D(-5.7, 11.7, 4.5));
		assertEqualsMatrix3D(new Vector3D(-23.94, -63.84, 29.64),
				new Vector3D(-16.38, 26.91, 16.38),
				new Vector3D(10.8, 99.0, -9.0));
	}

	@Test
	public void testScaleDoubleDoubleDouble()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(2.4, 22.0, -2.0));
		matrix.scale(-5.7, 11.7, 4.5);
		assertEqualsMatrix3D(new Vector3D(-23.94, -63.84, 29.64),
				new Vector3D(-16.38, 26.91, 16.38),
				new Vector3D(10.8, 99.0, -9.0));
	}

	@Test
	public void testTranslateAndScaleVector2DVector2D()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(0.0, 0.0, 1.0));
		matrix.translateAndScale(new Vector2D(-17.9, 15.2), new Vector2D(-5.7, 11.7));
		assertEqualsMatrix3D(new Vector3D(-23.94, -63.84, 11.74),
				new Vector3D(-16.38, 26.91, 31.58),
				new Vector3D(0.0, 0.0, 1.0));
	}

	@Test
	public void testTranslateAndScaleDoubleDoubleDoubleDouble()
	{
		matrix.set(new Vector3D(4.2, 11.2, -5.2),
				new Vector3D(-1.4, 2.3, 1.4),
				new Vector3D(0.0, 0.0, 1.0));
		matrix.translateAndScale(-17.9, 15.2, -5.7, 11.7);
		assertEqualsMatrix3D(new Vector3D(-23.94, -63.84, 11.74),
				new Vector3D(-16.38, 26.91, 31.58),
				new Vector3D(0.0, 0.0, 1.0));
	}

	@Test
	public void testRotate()
	{
		matrix.set(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		matrix.rotate(0.62, -0.43, 1.4);
		assertEqualsMatrix3D(
				new Vector3D(2.828057110068695, -4.084323418092450, -2.947315009309342),
				new Vector3D(-1.593421199133637, 0.201028765610979, -4.909861178130487),
				new Vector3D(-2.129225648995761, -0.811103971028703, 3.798236096846400));
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		matrix.rotate(-2.77, -1.53, 0.0);
		assertEqualsMatrix3D(
				new Vector3D(1.637112131050511, -0.200467672866230, 1.522310237726502),
				new Vector3D(2.184034259886998, -3.682027104457419, 2.695685229830684),
				new Vector3D(2.887898582361768, 0.464961411491869, 0.160787691648868));
	}

	@Test
	public void testRotateXAxis()
	{
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		matrix.rotateXAxis(-2.5);
		assertEqualsMatrix3D(
				new Vector3D(2.3236941050615973, -0.0292328103222645, 1.4806843482280809),
				new Vector3D(-2.2584387762571700, -0.6459453868560890, 0.3571748324173789),
				new Vector3D(2.3, -3.66, 2.7));
	}

	@Test
	public void testRotateYAxis()
	{
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		matrix.rotateYAxis(-2.5);
		assertEqualsMatrix3D(
				new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(-1.187173638311088, -2.590979855193948, 1.135188619752522),
				new Vector3D(-3.757741176890608, 2.632949560849799, -2.522171048439095));
	}

	@Test
	public void testRotateZAxis()
	{
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		matrix.rotateZAxis(-2.5);
		assertEqualsMatrix3D(
				new Vector3D(-0.967902687510164, 1.861939165046238, -0.494273727314976),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(-2.147851109250965, 3.177559211984399, -3.000948763722260));
	}

	@Test
	public void testEqualsObject()
	{
		matrix.set(new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7));
		assertTrue(matrix.equals(new Matrix3D(
				new Vector3D(-0.51, 0.41, -1.4),
				new Vector3D(3.2, 0.5, 0.6),
				new Vector3D(2.3, -3.66, 2.7))));
		assertTrue(matrix.equals(matrix));
	}
	
	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(matrix.isValidNode(document.getDocumentElement()));
	}
	
	@Test
	public void testGetFromNode()
	{
		prepareDocument();
		Element elem = document.getDocumentElement();
		
		Element xElem = document.createElement("x");
		Element yElem = document.createElement("y");
		Element zElem = document.createElement("z");
		xElem.setTextContent(" 0.6 1.4 33.65");
		yElem.setTextContent(" -0.7 -3.23 4.6");
		zElem.setTextContent(" 1.5 -6.43 1.4   ");
		elem.appendChild(zElem);
		elem.appendChild(xElem);
		elem.appendChild(yElem);
		
		try
		{ matrix.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		Matrix3D newMatrix = new Matrix3D(
				new Vector3D(0.6, 1.4, 33.65),
				new Vector3D(-0.7, -3.23, 4.6),
				new Vector3D(1.5, -6.43, 1.4));
		assertEquals(newMatrix, matrix);
		
		boolean catched = false;
		
		zElem.setAttribute("Name", "ThisName");
		try
		{ matrix.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
		
		catched = false;
		zElem.removeAttribute("Name");
		yElem.setTextContent(" -0.7 -3.23, f 4.6");
		try
		{ matrix.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
		
		catched = false;
		zElem.removeAttribute("Name");
		yElem.setTextContent(" -0.7 -3.23 4.6 6");
		try
		{ matrix.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
		
		catched = false;
		elem.setAttribute("Test", "testval");
		try
		{ matrix.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
	}
	
	@Test
	public void testCreateNode()
	{
		prepareDocument();
		matrix.set(new Vector3D(4.3, -14.1, -4.6),
				new Vector3D(-6.66, 3.53, 2.45),
				new Vector3D(-15.5, -0.47, -7.34));
		Node node = matrix.createNode(document);
		Matrix3D newMatrix = new Matrix3D();
		try
		{ newMatrix.getFromNode(node); }
		catch(DOMNodeException e)
		{  fail("Exception was thrown"); }
		
		assertEquals(newMatrix, matrix);
	}
}
