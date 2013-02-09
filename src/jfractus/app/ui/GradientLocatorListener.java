/**
 * GradientLocatorListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface GradientLocatorListener extends EventListener
{
	public void locatorChanged(GradientLocatorEvent e);
}
