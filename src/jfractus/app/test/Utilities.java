/**
 * Utilities.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import jfractus.api.Function;
import java.lang.reflect.Array;

public class Utilities
{
	public static boolean equalsArray(Object ob1, Object ob2)
	{
		Class<?> elemCls = ob1.getClass().getComponentType();
		
		if (Array.getLength(ob1) != Array.getLength(ob2))
			return false;
		
		for (int i = 0; i < Array.getLength(ob1); i++)
		{
			if (elemCls.isArray())
			{
				if (!equalsArray(Array.get(ob1, i), Array.get(ob2, i)))
					return false;
			}
			else if (!Array.get(ob1, i).equals(Array.get(ob2, i)))
				return false;
		}
		return true;
	}
	
	public static boolean equalsFunctions(Function func1, Function func2)
	{
		if (!func1.getClass().equals(func2.getClass()))
			return false;
		/*for (int i = 0; i < )
		String[] paramNames = iface1.getParameterNames();*/
		
		for (int i = 0; i < func1.getParametersCount(); i++)
		{
			Object ob1 = func1.getValue(i);
			Object ob2 = func2.getValue(i);
			if (ob1.getClass().isArray())
			{
				if (!equalsArray(ob1, ob2))
					return false;
			}
			else if (!ob1.equals(ob2))
				return false;
		}
		
		return true;
	}
}
