/**
 * ClassPathsListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventListener;

public interface ClassPathsListener extends EventListener
{
	public void classPathsChanged(ClassPathsEvent event);
}
