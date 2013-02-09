/**
 * NoTransform.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.planes;

import jfractus.api.PlaneTransform;
import jfractus.math.Complex;

public final class NoTransform extends PlaneTransform
{
	public NoTransform()
	{
		resetValues();
	}
	
	@Override
	public void compute(int length, Complex[] data)
	{
	}
}
