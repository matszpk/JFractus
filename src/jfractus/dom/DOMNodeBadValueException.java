/**
 * DOMNodeBadValueException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

public class DOMNodeBadValueException extends DOMNodeException
{
	private static final long serialVersionUID = 2300560299369116277L;
	
	public DOMNodeBadValueException()
	{
	}
	public DOMNodeBadValueException(String msg)
	{
		super(msg);
	}
}
