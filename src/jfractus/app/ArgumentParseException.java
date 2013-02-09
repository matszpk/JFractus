/**
 * ArgumentParseException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

public class ArgumentParseException extends Exception
{
    private static final long serialVersionUID = 3988388249696384597L;
    
    public ArgumentParseException()
    {
    }
    public ArgumentParseException(String msg)
    {
    	super(msg);
    }

}
