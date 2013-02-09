/**
 * ColorChangeListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface ColorChangeListener extends EventListener
{
	public void colorChanged(ColorChangeEvent e);
}
