/**
 * ImageWriterProgressListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventListener;

public interface ImageWriterProgressListener extends EventListener
{
	public void updateProgress(ImageWriterProgressEvent e);
}
