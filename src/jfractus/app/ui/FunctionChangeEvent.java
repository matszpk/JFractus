/**
 * FunctionChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.api.Function;

public class FunctionChangeEvent extends EventObject
{
    private static final long serialVersionUID = -3273508281166781801L;
    
    private Class<?> functionType;
	private Function function;
	
	public FunctionChangeEvent(Object source, Class<?> type, Function function)
	{
		super(source);
		this.functionType = type;
		this.function = function;
	}
	
	public Function getFunction()
	{
		return function;
	}
	public Class<?> getFunctionType()
	{
		return functionType;
	}
}
