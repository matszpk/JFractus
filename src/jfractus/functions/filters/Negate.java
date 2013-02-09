/**
 * Negate.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.filters;

import jfractus.api.OutputFilter;

public class Negate extends OutputFilter
{

	public void compute(int inputWidth, int inputHeight, int startX, int startY,
			int width, int height, int[] data, int[] outData)
	{
		for (int y = 0; y < height; y++)
		{
			int index = (startY+y)*inputWidth + startX;
			for (int x = 0; x < width; x++)
				outData[index + x] = data[index + x] ^ 0xffffff;
		}
	}

}
