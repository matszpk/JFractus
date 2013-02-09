/**
 * UnknownParameterException.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.api;

public class UnknownParameterException extends Exception
{
	private static final long serialVersionUID = 7836225097927722236L;

	public UnknownParameterException()
	{
	}
	public UnknownParameterException(String msg, String paramName)
	{
		super(msg + ":" + paramName);
	}
}
