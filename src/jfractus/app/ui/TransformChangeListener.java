/**
 * TransformChangeListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface TransformChangeListener extends EventListener
{
	public void transformChanged(TransformChangeEvent e);
}
