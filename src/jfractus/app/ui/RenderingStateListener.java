/**
 * RenderingStateListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface RenderingStateListener extends EventListener
{
	public void renderingFinished(RenderingStateEvent e);
}
