/**
 * PixelDifference.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.filters;

import jfractus.api.OutputFilter;
import jfractus.api.Parameter;
import jfractus.app.Resources;

public class PixelDifference extends OutputFilter
{
	public static final String thresholdUserName = Resources.getString("Threshold");
	@Parameter
	public short threshold;
	
	public PixelDifference()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		threshold = 32;
	}

	@Override
	public void compute(int inputWidth, int inputHeight, int startX, int startY,
	        int width, int height, int[] data, int[] outData)
	{
		for (int y = 0; y < height; y++)
		{
			int index = (startY+y)*inputWidth + startX;
			for (int x = 0; x < width; x++)
			{
				int first = data[index + x];
				int second = (startX + x + 1 < inputWidth) ? data[index + x + 1] : 0;
				
				int r, g, b;
				r = Math.abs(((first>>16) & 0xff) - ((second>>16) & 0xff));
				g = Math.abs(((first>>8) & 0xff) - ((second>>8) & 0xff));
				b = Math.abs((first & 0xff) - (second & 0xff));
				r = (r >= threshold) ? r : 0;
				g = (g >= threshold) ? g : 0;
				b = (b >= threshold) ? b : 0;
				outData[index + x] = (r<<16) | (g<<8) | b; 
			}
		}
	}

}
