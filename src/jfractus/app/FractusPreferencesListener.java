/**
 * FractusPreferencesListener.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.EventListener;

public interface FractusPreferencesListener extends EventListener
{
	public void preferencesChanged(FractusPreferencesEvent e);
}
