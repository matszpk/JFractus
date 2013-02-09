/**
 * GradientChangeSelectionListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface GradientChangeSelectionListener extends EventListener
{
	public void selectionChanged(GradientChangeSelectionEvent e);
}
