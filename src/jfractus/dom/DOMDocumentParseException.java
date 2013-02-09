/**
 * DOMDocumentParseException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

public class DOMDocumentParseException extends DOMDocumentException
{
    private static final long serialVersionUID = -4760989451986579571L;

    public DOMDocumentParseException()
    {
    }
    
    public DOMDocumentParseException(String msg)
    {
    	super(msg);
    }
}
