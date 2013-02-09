/**
 * GradientLoadingException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

public class GradientLoadingException extends Exception
{
    private static final long serialVersionUID = -7311497974255827931L;

	public GradientLoadingException()
    {
    }
    
    public GradientLoadingException(String msg)
    {
    	super(msg);
    }
}
