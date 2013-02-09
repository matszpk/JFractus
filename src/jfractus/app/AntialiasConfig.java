/**
 * AntialiasConfig.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.Serializable;

public class AntialiasConfig implements Serializable
{
	private static final long serialVersionUID = -3983305185344226332L;
	
	public static enum Method
	{
		NONE,
		NORMAL,
	};
	
	private Method method;
	private int samplingWidth;
	private int samplingHeight;
	
	public AntialiasConfig(Method method, int sWidth, int sHeight)
	{
		this.method = method;
		samplingWidth = sWidth;
		samplingHeight = sHeight;
	}
	
	public Method getMethod()
	{
		return method;
	}
	public void setMethod(Method method)
	{
		this.method = method;
	}
	
	public int getSamplingWidth()
	{
		return samplingWidth;
	}
	public int getSamplingHeight()
	{
		return samplingHeight;
	}
	public void setSamplingSize(int sWidth, int sHeight)
	{
		samplingWidth = sWidth;
		samplingHeight = sHeight;
	}
	
	public boolean equals(Object ob)
	{
		if (this == ob)
			return true;
		else if (ob instanceof AntialiasConfig)
		{
			AntialiasConfig aC = (AntialiasConfig)ob;
			if (method == Method.NONE && method == aC.method)
				return true;
			return (method == aC.method && samplingWidth == aC.samplingWidth &&
					samplingHeight == aC.samplingHeight);
		}
		else return false;
	}
	
	public String toString()
	{
		return method.toString() + ":" + samplingWidth + "x" + samplingHeight;
	}
}
