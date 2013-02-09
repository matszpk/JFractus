/**
 * ZPolyJulia.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.fractals;

import jfractus.api.FractalFormula;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class ZPolyJulia extends FractalFormula
{
	public static final String bailoutUserName = Resources.getString("Bailout");
	@Parameter
	public double bailout;
	
	public static final String polynomialUserName = Resources.getString("Polynomial");
	@Parameter
	public Complex[] polynomial; 
	
	public static final String pointUserName = Resources.getString("Point");
	@Parameter
	public Complex point;

	public ZPolyJulia()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		maxIterations = 16;
		bailout = 4.0;
		final Complex[] defaultPoly = {
				new Complex(0.0, 0.0), new Complex(0.0, 0.0), new Complex(1.0, 0.0) };
		polynomial = defaultPoly;
		point = new Complex(0.0, 0.0);
	}

	@Override
	public void compute(int length, int[] itData, Complex[] zData,
	        Complex[] outZData)
	{
		Complex z = new Complex();
		Complex x = new Complex();
		for (int i = 0; i < length; i++)
		{
			z.set(zData[i]);
			for (itData[i] = 0;
					itData[i] < maxIterations &&  z.norm2() <= bailout; itData[i]++)
			{
				x.set(z);
				if (polynomial.length >= 1)
				{
    				z.set(polynomial[polynomial.length-1]);
    				for (int c = polynomial.length-2; c >= 0; c--)
    					z.multiply(x).add(polynomial[c]);
				}
				else
					z.set(0.0, 0.0);
				z.add(point);
			}
			outZData[i].set(z);
			if (itData[i] == maxIterations)
				itData[i] = -1;
		}
	}

}
