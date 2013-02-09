/**
 * Magnet1.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.fractals;

import jfractus.api.FractalFormula;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Magnet1 extends FractalFormula
{
	public static final String bailoutUserName = Resources.getString("Bailout");
	@Parameter
	public double bailout;
	
	public Magnet1()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		maxIterations = 16;
		bailout = 4.0;
	}
	
	@Override
	public void compute(int length, int[] itData, Complex[] zData,
	        Complex[] outZData)
	{
		Complex z = new Complex();
		Complex x = new Complex();
		for (int i = 0; i < length; i++)
		{
			z.set(0.0, 0.0);
			for (itData[i] = 0;
					itData[i] < maxIterations &&  z.norm2() <= bailout; itData[i]++)
			{
				x.set(z);
				z.square().add(zData[i]).subtract(1.0);
				x.multiply(2.0).add(zData[i]).subtract(2.0);
				z.divide(x).square();
			}
			outZData[i].set(z);
			if (itData[i] == maxIterations)
				itData[i] = -1;
		}
	}

}
