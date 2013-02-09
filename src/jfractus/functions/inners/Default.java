/**
 * Default.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.inners;

import jfractus.api.InnerFunction;
import jfractus.math.Complex;

public final class Default extends InnerFunction
{
	public Default()
	{
		resetValues();
	}
		
	@Override
	public void compute(int length, int[] itData, Complex[] data, float[] outData)
	{
		/* do nothing */
		for (int i = 0; i < length; i++)
			if (itData[i] < 0)
				outData[i] = 0;
	}
}
