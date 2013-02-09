/**
 * GaussianBlur.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.filters;

import jfractus.api.OutputFilter;
import jfractus.api.Parameter;
import jfractus.app.Resources;

public class GaussianBlur extends OutputFilter
{
	public static final String radiusUserName = Resources.getString("Radius");
	@Parameter
	public double radius;
	
	private int kernelSize;
	private int kernelShift;
	private double[] kernel;
	
	public GaussianBlur()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		radius = 0.8;
		initialize();
	}
	
	public void initialize()
	{
		kernelSize = (int)Math.ceil(radius*6.0);
		if ((kernelSize & 1) != 1)
			kernelSize++;
		
		double scaleFactor = 1.0 / (2.0*Math.PI * radius * radius);
		double expFactor = -0.5 / (radius*radius);
		double posFactor = 1.0 / (double)kernelSize;
		
		kernel = new double[kernelSize*kernelSize];
		double kernelFactor = 0.0;
		for (int iy = 0; iy < kernelSize; iy++)
			for (int ix = 0; ix < kernelSize; ix++)
			{
				int pos = iy*kernelSize + ix;
				double x = (ix + 0.5) * posFactor - 0.5;
				double y = (iy + 0.5) * posFactor - 0.5;
				kernel[pos] = scaleFactor*Math.exp(expFactor*(x*x+y*y));
				kernelFactor += kernel[pos];
			}
		kernelFactor = 1.0 / kernelFactor;
		for (int i = 0; i < kernelSize*kernelSize; i++)
			kernel[i] *= kernelFactor;
		
		kernelShift = kernelSize>>1;
	}
	
	@Override
	public void compute(int inputWidth, int inputHeight, int startX,
	        int startY, int width, int height, int[] data, int[] outData)
	{
		for (int y = 0; y < height; y++)
		{
			int index = (startY+y)*inputWidth + startX;
			for (int x = 0; x < width; x++)
			{
				double destR = 0.0;
				double destG = 0.0;
				double destB = 0.0;
				
				for (int fY = -kernelShift; fY <= kernelShift; fY++)
					for (int fX = -kernelShift; fX <= kernelShift; fX++)
					{
						int gX = x + startX + fX;
						int gY = y + startY + fY;
						gX = (gX >= 0) ? ((gX < inputWidth) ? gX : inputWidth-1) : 0;
						gY = (gY >= 0) ? ((gY < inputHeight) ? gY : inputHeight-1) : 0;
						int pixel = data[gY*inputWidth + gX];
						
						double r = (double)((pixel >> 16) & 0xff);
						double g = (double)((pixel >> 8) & 0xff);
						double b = (double)(pixel & 0xff);
						
						double factor = kernel[(fY+kernelShift)*kernelSize + fX+kernelShift];
						destR += r*factor;
						destG += g*factor;
						destB += b*factor;
					}
				
				outData[index + x] = (((int)(destR + 0.5)&0xff)<<16) |
						(((int)(destG + 0.5)&0xff)<<8) |
						((int)(destB + 0.5)&0xff);
			}
		}
	}

}
