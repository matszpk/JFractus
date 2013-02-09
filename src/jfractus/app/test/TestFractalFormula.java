/**
 * TestFractalFormula.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import jfractus.api.FractalFormula;
import jfractus.api.Parameter;
import jfractus.math.*;

public final class TestFractalFormula extends FractalFormula
{
	public enum MyEnum
	{
		LINEAR,
		QUADRATIC,
		CUBIC,
		SPLINES,
		BEZIER,
		NURBS,
	};
	
	public static final String bailoutUserName = "Bailout";
	@Parameter
	public double bailout;
	
	public static final String booleanParamUserName = "Boolean Parameter";
	@Parameter
	public boolean booleanParam;
	public static final String charParamUserName="Character Parameter";
	@Parameter
	public char charParam;
	public static final String byteParamUserName="Byte Parameter";
	@Parameter
	public byte byteParam;
	public static final String shortParamUserName="Short Parameter";
	@Parameter
	public short shortParam;
	public static final String intParamUserName="Integer Parameter";
	@Parameter
	public int intParam;
	public static final String longParamUserName="Long Parameter";
	@Parameter
	public Long longParam;
	public static final String floatParamUserName="Float Parameter";
	@Parameter
	public float floatParam;
	public static final String doubleParamUserName="Double Parameter";
	@Parameter
	public double doubleParam;
	public static final String vector2DParamUserName="Vector2D Parameter";
	@Parameter
	public Vector2D vector2DParam;
	public static final String vector3DParamUserName="Vector3D Parameter";
	@Parameter
	public Vector3D vector3DParam;
	public static final String matrix3DParamUserName="Matrix3D Parameter";
	@Parameter
	public Matrix3D matrix3DParam;
	public static final String complexParamUserName="Complex Parameter";
	@Parameter
	public Complex complexParam;
	public static final String enum1ParamUserName="Enum1 Parameter";
	@Parameter
	public MyEnum enum1Param;
	public static final String stringParamUserName="String Parameter";
	@Parameter
	public String stringParam;
	
	public static final String booleanArrayUserName="Boolean Array";
	@Parameter
	public boolean[] booleanArray;
	public static final String charArrayUserName="Char Array";
	@Parameter
	public char[] charArray;
	public static final String byteArrayUserName="Byte Array";
	@Parameter
	public byte[] byteArray;
	public static final String shortArrayUserName="Short Array";
	@Parameter
	public short[] shortArray;
	public static final String intArrayUserName="Int Array";
	@Parameter
	public int[] intArray;
	public static final String longArrayUserName="Long Array";
	@Parameter
	public long[] longArray;
	public static final String floatArrayUserName="Float Array";
	@Parameter
	public float[] floatArray;
	public static final String doubleArrayUserName="Double Array";
	@Parameter
	public double[] doubleArray;
	
	public static final String BooleanArrayUserName="Boolean Array 2";
	@Parameter
	public Boolean[] BooleanArray;
	public static final String CharacterArrayUserName="Char Array 2";
	@Parameter
	public Character[] CharacterArray;
	public static final String ByteArrayUserName="Byte Array 2";
	@Parameter
	public Byte[] ByteArray;
	public static final String ShortArrayUserName="Short Array 2";
	@Parameter
	public Short[] ShortArray;
	public static final String IntegerArrayUserName="Integer Array 2";
	@Parameter
	public Integer[] IntegerArray;
	public static final String LongArrayUserName="Long Array 2";
	@Parameter
	public Long[] LongArray;
	public static final String FloatArrayUserName="Float Array 2";
	@Parameter
	public Float[] FloatArray;
	public static final String DoubleArrayUserName="Double Array 2";
	@Parameter
	public Double[] DoubleArray;
	
	public static final String enumArrayUserName="Enum Array";
	@Parameter
	public MyEnum[] enumArray;
	public static final String stringArrayUserName="String Array";
	@Parameter
	public String[] stringArray;
	public static final String complexArrayUserName="Complex Array";
	@Parameter
	public Complex[] complexArray;
	public static final String intArrayArrayUserName="Int Array Array";
	@Parameter
	public int[][] intArrayArray;
	
	public static final String emptyArrayUserName="Empty Array";
	@Parameter
	public long[][] emptyArray;
	
	/* constructor */
	public TestFractalFormula()
    {
		resetValues();
    }
	
	@Override
	public void compute(int length, int[] itData, Complex[] data, Complex[] outZData)
	{
	}

	
	public void resetValues()
	{
		booleanParam = false;
		charParam = ' ';
		byteParam = 0;
		shortParam = 0;
		intParam  = 0;
		longParam = 0L;
		floatParam = 0.0f;
		doubleParam = 0.0;
		vector2DParam = new Vector2D();
		vector3DParam = new Vector3D();
		matrix3DParam = new Matrix3D();
		complexParam = new Complex();
		enum1Param = MyEnum.LINEAR; 
		stringParam = "";
		booleanArray = new boolean[0];
		byteArray = new byte[0];
		shortArray = new short[0];
		intArray = new int[0];
		longArray = new long[0];
		floatArray = new float[0];
		doubleArray = new double[0];
		BooleanArray = new Boolean[0];
		ByteArray = new Byte[0];
		ShortArray = new Short[0];
		IntegerArray = new Integer[0];
		LongArray = new Long[0];
		FloatArray = new Float[0];
		DoubleArray = new Double[0];
		enumArray = new MyEnum[0];
		complexArray = new Complex[0];
		stringArray = new String[0];
		//stringArray = new String[] { "xxx", "ddd", "ala", "ma", "kota" };
		intArrayArray = new int[0][0];
		emptyArray = new long[0][0];
		charArray = new char[0];
		CharacterArray = new Character[0];
	}
}
