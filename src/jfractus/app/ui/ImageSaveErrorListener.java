/**
 * ImageSaveErrorListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface ImageSaveErrorListener extends EventListener
{
	public void errorOccured(ImageSaveErrorEvent e);
}
