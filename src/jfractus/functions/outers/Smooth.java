/**
 * Smooth.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.outers;

import jfractus.api.OuterFunction;
import jfractus.api.Parameter;
import jfractus.app.Resources;
import jfractus.math.Complex;

public class Smooth extends OuterFunction
{
	public static final String bailoutUserName = Resources.getString("Bailout");
	@Parameter
	public double bailout;
	
	private static final double pValue = 1.0 / Math.log(2.0);
	private double blFactor; 

	public Smooth()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		bailout = 2.0;
		initialize();
	}
	
	public void initialize()
	{
		blFactor = Math.log(2.0 * Math.log(bailout));
	}
	
	public void compute(int length, int[] itData, Complex[] data, float[] outData)
	{
		for (int i = 0; i < length; i++)
			if (itData[i] >= 0)
			{
				double l = (blFactor - Math.log(Math.log(data[i].abs()))) * pValue;
				outData[i] = itData[i] + (float)l;
			}
	}

	
}
