/**
 * Operation.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.Serializable;

import jfractus.api.Function;
import jfractus.app.FractalFrame;
import jfractus.app.Gradient;
import jfractus.app.GradientLocator;

public class Operation implements Serializable
{
	private static final long serialVersionUID = 5876359998439311999L;
	
	public enum Type
	{
		FUNCTION,
		COLORING,
		FRAME,
	}
	
	private Type type;
	private Object identifier;
	private Object oldValue;
	private Object newValue;
	
	public Operation(Type type, Object identifier, Object oldValue, Object newValue)
	{
		this.type = type;
		this.identifier = identifier;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public static Operation getFunctionOp(Class<?> type, Function oldFunc, Function newFunc)
	{
		return new Operation(Type.FUNCTION, type, oldFunc, newFunc);
	}
	
	public static Operation getColoringOp(float oldColorScale, float oldColorShift,
			GradientLocator oldLocator, Gradient oldGradient,
			float newColorScale, float newColorShift,
			GradientLocator newLocator, Gradient newGradient)
	{
		return new Operation(Type.COLORING, null,
				new ColoringObject(oldColorScale, oldColorShift, oldLocator, oldGradient),
				new ColoringObject(newColorScale, newColorShift, newLocator, newGradient));
	}
	
	public static Operation getFrameOp(FractalFrame oldFrame, FractalFrame newFrame)
	{
		return new Operation(Type.FRAME, null, oldFrame, newFrame);
	}
	
	public Type getType()
	{
		return type;
	}
	public Object getIdentifier()
	{
		return identifier;
	}
	public Object getOldValue()
	{
		return oldValue;
	}
	public Object getNewValue()
	{
		return newValue;
	}
	
	public String toString()
	{
		return type + ":" + identifier + ":" + oldValue + ":" + newValue;
	}
}
