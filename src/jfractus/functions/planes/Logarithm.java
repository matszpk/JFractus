/**
 * Logarithm.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.planes;

import jfractus.api.PlaneTransform;
import jfractus.math.Complex;

public class Logarithm extends PlaneTransform
{

	@Override
	public void compute(int length, Complex[] data)
	{
		for (int i = 0; i < length; i++)
			data[i].log();
	}

}
