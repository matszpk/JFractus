/**
 * GradientChangeSelectionEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

public class GradientChangeSelectionEvent extends EventObject
{
    private static final long serialVersionUID = -1322422804261584441L;
    
    private int selectedPointIndex;

	public GradientChangeSelectionEvent(Object source, int selected)
	{
		super(source);
		selectedPointIndex = selected;
	}
	
	public int getSelectedPointIndex()
	{
		return selectedPointIndex;
	}
}
