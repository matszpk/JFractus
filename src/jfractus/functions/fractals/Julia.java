/**
 * Julia.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.fractals;

import jfractus.api.FractalFormula;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Julia extends FractalFormula
{
	public static final String bailoutUserName = Resources.getString("Bailout");
	@Parameter
	public double bailout;
	
	public static final String pointUserName = Resources.getString("Point");
	@Parameter
	public Complex point;

	public Julia()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		maxIterations = 16;
		bailout = 4.0;
		point = new Complex(0.0, 0.0);
	}
	
	@Override
	public void compute(int length, int[] itData, Complex[] zData,
	        Complex[] outZData)
	{
		Complex z = new Complex();
		for (int i = 0; i < length; i++)
		{
			z.set(zData[i]);
			for (itData[i] = 0;
					itData[i] < maxIterations &&  z.norm2() <= bailout; itData[i]++)
				z.square().add(point);
			outZData[i].set(z);
			if (itData[i] == maxIterations)
				itData[i] = -1;
		}
	}

}
