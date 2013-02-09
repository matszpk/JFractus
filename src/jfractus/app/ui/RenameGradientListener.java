/**
 * RenameGradientListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface RenameGradientListener extends EventListener
{
	public void gradientRenamed(RenameGradientEvent e);
}
