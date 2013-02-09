/**
 * PointNotFoundAtGradientException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

public class PointNotFoundAtGradientException extends Exception
{
    private static final long serialVersionUID = 8988287830641304971L;

    public PointNotFoundAtGradientException()
	{
	}
	public PointNotFoundAtGradientException(String msg)
	{
		super(msg);
	}
}
