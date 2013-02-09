/**
 * FractalInteractionEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

public class FractalInteractionEvent extends EventObject
{
	private static final long serialVersionUID = 8713743630310117280L;
	
	private TransformType transformType;
	private int interactionStep;

	public FractalInteractionEvent(Object source, TransformType transformType,
			int interactionStep)
	{
		super(source);
		this.transformType = transformType;
		this.interactionStep = interactionStep; 
	}
	
	public TransformType getTransformType()
	{
		return transformType;
	}
	public int getInteractionStep()
	{
		return interactionStep;
	}
}
