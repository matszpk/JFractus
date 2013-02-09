/**
 * DOMDocumentException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

public class DOMDocumentException extends Exception
{
    private static final long serialVersionUID = -6654870182325943526L;
    
	public DOMDocumentException()
	{
	}
	public DOMDocumentException(String msg)
	{
		super(msg);
	}
}
