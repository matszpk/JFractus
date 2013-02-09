/**
 * BadValueOfArgumentException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

public class BadValueOfArgumentException extends Exception
{
    private static final long serialVersionUID = 7776688635366070373L;
    
    public BadValueOfArgumentException()
    {
    }
    public BadValueOfArgumentException(String msg)
    {
    	super(msg);
    }
}
