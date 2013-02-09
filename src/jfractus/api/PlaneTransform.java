/**
 * PlaneTransform.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.api;

import jfractus.math.*;

public abstract class PlaneTransform extends Function
{
	/* compute block of image:
	 * length - number of items of input data
	 * zData - transformed coordinates at complex plane
	 */
	public abstract void compute(int length, Complex[] zData);
}
