/**
 * Emboss.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.functions.filters;

import jfractus.api.OutputFilter;
import jfractus.api.Parameter;
import jfractus.app.Resources;

public class Emboss extends OutputFilter
{
	public static final String angleUserName = Resources.getString("Angle");
	@Parameter
	public double angle;
	public static final String factorUserName = Resources.getString("Factor");
	@Parameter
	public double factor;
	
	private double angleCos;
	private double angleSin;
	private double finalFactor;
	
	public Emboss()
	{
		resetValues();
	}
	
	public void resetValues()
	{
		angle = 0.0;
		factor = 1.0;
		initialize();
	}
	
	public void initialize()
	{
		angleCos = Math.cos(Math.PI * angle / 180.0);
		angleSin = Math.sin(Math.PI * angle / 180.0);
		finalFactor = factor / (6.0 * (Math.abs(angleCos)+Math.abs(angleSin)));
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
				int xm1 = (x+startX > 0) ? x+startX-1 : 0;
				int xp1 = (x+startX < inputWidth-1) ? x+startX+1 : inputWidth-1;
				int ym1 = (y+startY > 0) ? y+startY-1 : 0;
				int yp1 = (y+startY < inputHeight-1) ? y+startY+1 : inputHeight-1;
				
				int pLU = data[ym1*inputWidth + xm1];
				int pU = data[ym1*inputWidth + startX+x];
				int pRU = data[ym1*inputWidth + xp1];
				int pL = data[(startY+y)*inputWidth + xm1];
				int pR = data[(startY+y)*inputWidth + xp1];
				int pLD = data[yp1*inputWidth + xm1];
				int pD = data[yp1*inputWidth + startX+x];
				int pRD = data[yp1*inputWidth + xp1];
				
				double iUR = ((pLU>>16)&0xff) + ((pL>>16)&0xff) + ((pLD>>16)&0xff) -
					((pRU>>16)&0xff) - ((pR>>16)&0xff) - ((pRD>>16)&0xff);
				double iUG = ((pLU>>8)&0xff) + ((pL>>8)&0xff) + ((pLD>>8)&0xff) -
					((pRU>>8)&0xff) - ((pR>>8)&0xff) - ((pRD>>8)&0xff);
				double iUB = (pLU&0xff) + (pL&0xff) + (pLD&0xff) -
					(pRU&0xff) - (pR&0xff) - (pRD&0xff);
				
				double iVR = ((pLU>>16)&0xff) + ((pU>>16)&0xff) + ((pRU>>16)&0xff) -
					((pLD>>16)&0xff) - ((pD>>16)&0xff) - ((pRD>>16)&0xff);
				double iVG = ((pLU>>8)&0xff) + ((pU>>8)&0xff) + ((pRU>>8)&0xff) -
					((pLD>>8)&0xff) - ((pD>>8)&0xff) - ((pRD>>8)&0xff);
				double iVB = (pLU&0xff) + (pU&0xff) + (pRU&0xff) -
					(pLD&0xff) - (pD&0xff) - (pRD&0xff);
				
				double outR = finalFactor*(iUR*angleCos + iVR*angleSin) + 127.5;
				double outG = finalFactor*(iUG*angleCos + iVG*angleSin) + 127.5;
				double outB = finalFactor*(iUB*angleCos + iVB*angleSin) + 127.5;
				
				outData[index + x] = ((int)(outR + 0.5)<<16) |
					((int)(outG + 0.5)<<8) | (int)(outB + 0.5);
			}
		}
	}
}
