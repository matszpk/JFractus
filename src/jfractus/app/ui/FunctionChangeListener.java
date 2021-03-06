/**
 * FunctionChangeListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventListener;

public interface FunctionChangeListener extends EventListener
{
	public void functionChanged(FunctionChangeEvent e);
}
