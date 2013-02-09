/**
 * DOMNodeBadStructureException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

public class DOMNodeBadStructureException extends DOMNodeException
{
	private static final long serialVersionUID = 220206188126823936L;
	
	public DOMNodeBadStructureException()
	{
	}
	public DOMNodeBadStructureException(String msg)
	{
		super(msg);
	}
}
