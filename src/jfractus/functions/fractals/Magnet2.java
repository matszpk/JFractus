/**
 * Magnet2.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.fractals;

import jfractus.api.FractalFormula;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Magnet2 extends FractalFormula
{
	public static final String bailoutUserName = Resources.getString("Bailout");
	@Parameter
	public double bailout;
	
	public Magnet2()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		maxIterations = 16;
		bailout = 8.0;
	}
	
	@Override
	public void compute(int length, int[] itData, Complex[] zData,
	        Complex[] outZData)
	{
		Complex z = new Complex();
		Complex x = new Complex();
		Complex c1 = new Complex();
		Complex c2 = new Complex();
		Complex c1c2 = new Complex();
		for (int i = 0; i < length; i++)
		{
			z.set(0.0, 0.0);
			for (itData[i] = 0;
					itData[i] < maxIterations &&  z.norm2() <= bailout; itData[i]++)
			{
				c1.set(zData[i]);
				c2.set(zData[i]);
				c1.subtract(1.0);
				c2.subtract(2.0);
				c1c2.set(c1);
				c1c2.multiply(c2);
				x.set(z);
				c1.multiply(z).multiply(3.0);
				c2.multiply(z).multiply(3.0);
				z.cube().add(c1).add(c1c2);
				x.square().multiply(3.0).add(c2).add(c1c2).add(1.0);
				z.divide(x).square();
			}
			outZData[i].set(z);
			if (itData[i] == maxIterations)
				itData[i] = -1;
		}
	}

}
