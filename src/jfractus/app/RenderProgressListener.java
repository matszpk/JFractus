/**
 * RenderProgressListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventListener;

public interface RenderProgressListener extends EventListener
{
	public void updateProgress(RenderProgressEvent e);
}
