/**
 * ResponseEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

public class ResponseEvent extends EventObject
{
	private static final long serialVersionUID = -3927199441678319603L;
	
	private Object response;
	
	public ResponseEvent(Object source, Object response)
	{
		super(source);
		this.response = response;
	}
	
	public Object getResponse()
	{
		return response;
	}
}
