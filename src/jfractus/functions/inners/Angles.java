/**
 * Angles.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.inners;

import jfractus.api.InnerFunction;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Angles extends InnerFunction
{
	public static final String shiftUserName = Resources.getString("Shift");
	@Parameter
	public float shift;
	public static final String scaleUserName = Resources.getString("Scale");
	@Parameter
	public float scale;
	
	private float scaleFactor;
	
	public Angles()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		shift = 0.0f;
		scale = 1.0f;
		initialize();
	}
	
	public void initialize()
	{
		scaleFactor = scale / (float)(2.0*Math.PI);
	}

	@Override
	public void compute(int length, int[] itData, Complex[] data,
	        float[] outData)
	{
		for (int i = 0; i < length; i++)
			if (itData[i] < 0)
				outData[i] = (float)(data[i].arg() + Math.PI) * scaleFactor + shift;
	}

}
