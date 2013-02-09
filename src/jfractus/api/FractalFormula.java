/**
 * FractalFormula.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.api;

import jfractus.app.Resources;
import jfractus.math.*;

public abstract class FractalFormula extends Function
{
	public static final String maxIterationsUserName = Resources.getString("MaxIterations");
	@Parameter
	public int maxIterations;
	
	/* compute block of image: 
	 * itData - output iterations data used for generate image
	 * zData - input: coordinates at complex plane
	 * outZData - output: complex values after n[i,j] iterations
	 */
	public abstract void compute(int length, int[] itData, Complex[] zData, Complex[] outZData);
}
