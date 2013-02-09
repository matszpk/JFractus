/**
 * RenderOptionsChangeListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface RenderOptionsChangeListener extends EventListener
{
	public void renderOptionsChanged(RenderOptionsChangeEvent e);
}
