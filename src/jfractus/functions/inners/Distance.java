/**
 * Distance.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.inners;

import jfractus.api.InnerFunction;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Distance extends InnerFunction
{
	public static final String shiftUserName = Resources.getString("Shift");
	@Parameter
	public float shift;
	public static final String scaleUserName = Resources.getString("Scale");
	@Parameter
	public float scale;
	
	public Distance()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		shift = 0.0f;
		scale = 1.0f;
	}


	@Override
	public void compute(int length, int[] itData, Complex[] data,
	        float[] outData)
	{
		for (int i = 0; i < length; i++)
			if (itData[i] < 0)
				outData[i] = (float)(data[i].abs()) * scale + shift;
	}
}
