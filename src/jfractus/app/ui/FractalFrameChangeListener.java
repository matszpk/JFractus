/**
 * FractalFrameChangeListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface FractalFrameChangeListener extends EventListener
{
	public void fractalFrameChanged(FractalFrameChangeEvent e);
}
