/**
 * OutputFilter.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.api;

public abstract class OutputFilter extends Function
{
	/* inputWidth, inputHeight - size of whole image
	 * width, height - size of image block
	 * x, y - start position of block
	 * data - input image data (in ARGB integer format)
	 * outData - output image data (in ARGB integer format)
	 */
	public abstract void compute(int inputWidth, int inputHeight,
			int startX, int startY, int width, int height, int[] data, int[] outData);
}
