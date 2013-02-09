/**
 * OutOfGradientRangeException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

public class OutOfGradientRangeException extends Exception
{
	private static final long serialVersionUID = -5526363973937884963L;
	
	public OutOfGradientRangeException()
	{
	}
	public OutOfGradientRangeException(String msg)
	{
		super(msg);
	}
}
