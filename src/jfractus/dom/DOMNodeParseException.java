/**
 * DOMNodeParseException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

public class DOMNodeParseException extends DOMNodeException
{
	private static final long serialVersionUID = 5330983984952661673L;
	
	public DOMNodeParseException()
	{
    }
	public DOMNodeParseException(String msg)
	{
		super(msg);
	}
}
