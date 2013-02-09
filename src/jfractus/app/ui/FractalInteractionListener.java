/**
 * FractalInteractionListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface FractalInteractionListener extends EventListener
{
	public void interactionPerformed(FractalInteractionEvent e);
}
