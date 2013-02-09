/**
 * ImageSaveErrorEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

public class ImageSaveErrorEvent extends EventObject
{
    private static final long serialVersionUID = 8839331020349211962L;
    
    private Exception exception;

	public ImageSaveErrorEvent(Object source, Exception ex)
	{
		super(source);
		this.exception = ex;
	}

	public Exception getException()
	{
		return exception;
	}
}
