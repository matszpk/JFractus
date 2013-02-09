/**
 * Newton.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.fractals;

import jfractus.api.FractalFormula;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Newton extends FractalFormula
{
	public final static String epsilonUserName = Resources.getString("Epsilon");
	@Parameter
	public double epsilon;
	
	private double epsilon2;
	
	public Newton()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		maxIterations = 64;
		epsilon = 1.0e-13;
		initialize();
	}
	
	public void initialize()
	{
		epsilon2 = epsilon*epsilon;
	}
	
	@Override
	public void compute(int length, int[] itData, Complex[] zData,
	        Complex[] outZData)
	{
		Complex last = new Complex();
		Complex z = new Complex();
		Complex pz = new Complex();
		Complex dpz = new Complex();
		Complex res = new Complex();
		
		for (int i = 0; i < length; i++)
		{
			last.set(0.0, 0.0);
			z.set(zData[i]);
			for (itData[i] = 0; itData[i] < maxIterations; itData[i]++)
			{
				last.set(z);
				pz.set(z);
				pz.cube().subtract(1.0);
				dpz.set(z);
				dpz.square().multiply(3.0);
				pz.divide(dpz);
				z.subtract(pz);
				res.set(z);
				res.subtract(last);
				if (res.norm2() < epsilon2)
					break;
			}
			outZData[i].set(z);
			if (itData[i] == maxIterations)
				itData[i] = -1;
		}
	}

}
