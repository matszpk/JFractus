/**
 * UnremovablePointInGradientException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

public class UnremovablePointInGradientException extends Exception
{
    private static final long serialVersionUID = 9196548809446581976L;
    
	public UnremovablePointInGradientException()
	{
	}
	public UnremovablePointInGradientException(String msg)
	{
		super(msg);
	}

}
