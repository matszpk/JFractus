/**
 * ClassPathsEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventObject;

public class ClassPathsEvent extends EventObject
{
    private static final long serialVersionUID = 8402244678912985509L;

	public ClassPathsEvent(Object source)
	{
		super(source);
	}
}
