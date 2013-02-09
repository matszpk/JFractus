/**
 * OuterFunction.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.api;

import jfractus.math.*;

public abstract class OuterFunction extends Function
{
	/* compute block of image:
	 * itData - input number of iterations
	 * zData - complex values after n[i,j] iteration,
	 * outData - real value of color index
	 */
	public abstract void compute(int length, int[] itData, Complex[] zData,
			float[] outData);
}
