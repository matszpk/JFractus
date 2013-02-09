/**
 * CopyFunctionTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import jfractus.math.*;

public class CopyFunctionTest
{

	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void copyFunctionTest()
	{
		TestFractalFormula formula = new TestFractalFormula();
		
		try
		{
    		formula.setValue("maxIterations", 1000);
    		formula.setValue("bailout", 64.4);
    		
    		formula.setValue("booleanParam", true);
    		formula.setValue("byteParam", (byte)-46);
    		formula.setValue("charParam", '<');
    		formula.setValue("shortParam", (short)1346);
    		formula.setValue("intParam", (int)48998489);
    		formula.setValue("longParam", (long)3040511L);
    		formula.setValue("floatParam", -5.6f);
    		formula.setValue("doubleParam", -43.66);
    		formula.setValue("complexParam", new Complex(-6.42, 24.4));
    		formula.setValue("vector2DParam", new Vector2D(6.75, 62.7));
    		formula.setValue("vector3DParam",
    				new Vector3D(6.75, 62.7, -24.1));
    		formula.setValue("matrix3DParam", new Matrix3D
    				(new Vector3D(-6.64, 2.55, -5.36),
    				 new Vector3D(-3.6, 0.75, 3.66),
    				 new Vector3D(0.89, 34.5, -4.31)));
    		formula.setValue("enum1Param", TestFractalFormula.MyEnum.QUADRATIC);
    		formula.setValue("stringParam", "this is simple text");
    		
    		final boolean[] booleanArrayVal = 
    			{true, false, true, true, false, true};
    		formula.setValue("booleanArray", booleanArrayVal);
    		
    		final char[] charArrayVal =
				{ 't', 'h', 'i', 's', ' ', 'i', 's', ' ', 't', 'e', 's', 't' };
			formula.setValue("charArray", charArrayVal);
    		
    		final byte[] byteArrayVal = 
    			{ (byte)-44, (byte)35, (byte)121, (byte)-101, (byte)76 };
    		formula.setValue("byteArray", byteArrayVal);
    		
    		final short[] shortArrayVal = 
    			{ (short)-6578, (short)2465, (short)54, (short)-31577, (short)4367 };
    		formula.setValue("shortArray", shortArrayVal);
    		
    		final int[] intArrayVal = 
    			{ -5646, 33, 67882243, 544354, 443 };
    		formula.setValue("intArray", intArrayVal);
    		
    		final long[] longArrayVal = 
    			{ -53454L, 354938L, -4334490L, 9434555L };
    		formula.setValue("longArray", longArrayVal);
    		
    		final float[] floatArrayVal = 
    			{ 5.46f, 0.7f, 324.4f, 3.64f, -3.66f, -0.44f };
    		formula.setValue("floatArray", floatArrayVal);
    		
    		final double[] doubleArrayVal = 
    			{ 6.654, 0.644, -3.23, -6.77, -54.2 };
    		formula.setValue("doubleArray", doubleArrayVal);
    		
    		final Boolean[] booleanArrayVal2 = 
    			{true, false, true, true, false, true};
    		formula.setValue("BooleanArray", booleanArrayVal2);
    		
    		final Character[] charArrayVal2 =
				{ 'N', 'o', 'r', 'm', 'a', 'l', ' ', 'm', 'o', 'd', 'e' };
			formula.setValue("CharacterArray", charArrayVal2);
    		
    		final Byte[] byteArrayVal2 = 
    			{ (byte)-44, (byte)35, (byte)121, (byte)-101, (byte)76 };
    		formula.setValue("ByteArray", byteArrayVal2);
    		
    		final Short[] shortArrayVal2 = 
    			{ (short)-6578, (short)2465, (short)54, (short)-31577, (short)4367 };
    		formula.setValue("ShortArray", shortArrayVal2);
    		
    		final Integer[] integerArrayVal2 = 
    			{ -5646, 33, 67882243, 544354, 443 };
    		formula.setValue("IntegerArray", integerArrayVal2);
    		
    		final Long[] longArrayVal2 = 
    			{ -53454L, 354938L, -4334490L, 9434555L };
    		formula.setValue("LongArray", longArrayVal2);
    		
    		final Float[] floatArrayVal2 = 
    			{ -5.46f, 0.7f, 324.4f, 3.64f, -3.66f, -0.44f };
    		formula.setValue("FloatArray", floatArrayVal2);
    		
    		final Double[] doubleArrayVal2 = 
    			{ 6.654, 0.644, -3.23, -6.77, -54.2 };
    		formula.setValue("DoubleArray", doubleArrayVal2);
    		
    		final TestFractalFormula.MyEnum[] enumArrayVal =
    		{ TestFractalFormula.MyEnum.BEZIER,
    		  TestFractalFormula.MyEnum.LINEAR,
    		  TestFractalFormula.MyEnum.CUBIC, };
    		formula.setValue("enumArray", enumArrayVal);
    		
    		final Complex[] complexArrayVal = {
    				new Complex(0.7, -14.5), new Complex(-6.46, -32.6),
    				new Complex(-54.7, -0.64), new Complex(33.6, 7.21)
    		};
    		formula.setValue("complexArray", complexArrayVal);
    		
    		final String[] stringArrayVal = {
    				"one", "two", "three", "four"
    		};
    		formula.setValue("stringArray", stringArrayVal);
    		
    		final int[][] intArrayArrayVal = {
    				{ 133, 55, 22, 224, 133 },
    				{ 434, 43, -54, 22 },
    				{ -43, -754, -557, 3436, 244, 445, 2233 },
    				{ -43, 33, 44 }
    		};
    		formula.setValue("intArrayArray", intArrayArrayVal);
		}
		catch(Exception e)
		{ fail("Exception was thrown"); }
		
		TestFractalFormula newFormula = (TestFractalFormula)formula.copy();
		
		assertTrue(Utilities.equalsFunctions(formula, newFormula));
		
		try
		{
			newFormula.setValue("complexParam", new Complex(1.0, 55.2));
			newFormula.setValue("intParam", (int)445554);
			newFormula.setValue("enum1Param", TestFractalFormula.MyEnum.BEZIER);
			newFormula.setValue("stringParam", "ok");
		}
		catch(Exception e)
		{ fail("Exception was thrown"); }
		
		assertFalse(formula.getValue("complexParam").
				equals(newFormula.getValue("complexParam")));
		assertFalse(formula.getValue("intParam").
				equals(newFormula.getValue("intParam")));
		assertFalse(formula.getValue("enum1Param").
				equals(newFormula.getValue("intParam")));
		assertFalse(formula.getValue("stringParam").
				equals(newFormula.getValue("stringParam")));
	}
}
