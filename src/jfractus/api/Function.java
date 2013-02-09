/**
 * Function.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */


package jfractus.api;

import java.util.*;
import java.lang.reflect.*;

import jfractus.generic.Copyable;

public class Function implements Copyable<Function>
{
	private static class FunctionEntry
	{
		public int startIndex;
		public Field[] paramFields;
		public HashMap<String, Field> paramFieldsMap;
		
		public FunctionEntry()
		{
		}
		
		public FunctionEntry(int startIndex, Field[] paramFields,
				HashMap<String, Field> paramMap)
		{
			this.startIndex = startIndex;
			this.paramFields = paramFields;
			this.paramFieldsMap = paramMap;
		}
	}
	
	private static HashMap<Class<?>, FunctionEntry> functionsBase =
		new HashMap<Class<?>, FunctionEntry>();
	
	public Function()
	{
		if (functionsBase.get(this.getClass()) != null)
			return;
		/* 
		 * if required, create FunctionEntry
		 */
		LinkedList<Class<?>> classStack = new LinkedList<Class<?>>();
		
		for (Class<?> c = this.getClass(); !c.equals(Function.class);
			c = c.getSuperclass())
			classStack.addFirst(c);
		
		int currentIndex = 0;
		for (Iterator<Class<?>> iter = classStack.iterator(); iter.hasNext();)
		{
			Class<?> c = iter.next();
			FunctionEntry funcEntry = functionsBase.get(c);
			if (funcEntry == null)
			{
				ArrayList<Field> paramFields = new ArrayList<Field>();
				HashMap<String, Field> paramFieldsMap = new HashMap<String, Field>();
				
				Field[] fields = c.getDeclaredFields();
    			for (Field f : fields)
    			{
    				Parameter param = f.getAnnotation(Parameter.class);
    				int modifiers = f.getModifiers();
    				if (!Modifier.isPublic(modifiers) ||
    						Modifier.isStatic(modifiers) ||
    						Modifier.isAbstract(modifiers) || param == null)
    					continue;
    			
    				paramFields.add(f);
    				paramFieldsMap.put(f.getName(), f);
    			}
    			
    			funcEntry = new FunctionEntry(currentIndex,
    					paramFields.toArray(new Field[0]), paramFieldsMap);
    			functionsBase.put(c, funcEntry);
			}
			currentIndex += funcEntry.paramFields.length;
		}
	}
	
	/* copy all parameters to new instance */
	private static Object copyObject(Object ob)
	{
		Class<?> cls = ob.getClass();
		
		if (boolean.class.equals(cls) ||
				char.class.equals(cls) ||
				byte.class.equals(cls) ||
				short.class.equals(cls) ||
				int.class.equals(cls) ||
				long.class.equals(cls) ||
				float.class.equals(cls) ||
				double.class.equals(cls) ||
				cls.isEnum())
			return ob;
		else if (Boolean.class.equals(cls))
			return new Boolean(((Boolean)ob).booleanValue());
		else if (Character.class.equals(cls))
			return new Character(((Character)ob).charValue());
		else if (Byte.class.equals(cls))
			return new Byte(((Byte)ob).byteValue());
		else if (Short.class.equals(cls))
			return new Short(((Short)ob).shortValue());
		else if (Integer.class.equals(cls))
			return new Integer(((Integer)ob).intValue());
		else if (Long.class.equals(cls))
			return new Long(((Long)ob).longValue());
		else if (Float.class.equals(cls))
			return new Float(((Float)ob).floatValue());
		else if (Double.class.equals(cls))
			return new Double(((Double)ob).doubleValue());
		else if (String.class.equals(cls))
			return new String(((String)ob));
		else if (cls.isArray())
		{
			Class<?> comp = cls.getComponentType();
			int length = Array.getLength(ob);
			Object newArray = Array.newInstance(comp, length);
			for (int i = 0; i < length; i++)
				Array.set(newArray, i, copyObject(Array.get(ob, i)));
			return newArray;
		}
		else 
		{
			try
			{
				Method method = cls.getMethod("copy");
				return method.invoke(ob);
			}
			catch(NoSuchMethodException e)
			{ return null; }
            catch (IllegalArgumentException e)
            { return null; }
            catch (IllegalAccessException e)
            { return null; }
            catch (InvocationTargetException e)
            { return null; }
		}
	}
	
	public Function copy()
	{
		Function newFunction;
		try
		{ newFunction = this.getClass().newInstance(); }
		catch (Exception e)
		{ return null; }
		
		int count = getParametersCount();
		for (int i = 0; i < count; i++)
			newFunction.setValue(i, copyObject(getValue(i)));
		
		return newFunction;
	}
	
	public Object copyParameter(int index)
	{
		try
		{ return copyObject(findParamField(index).get(this)); }
		catch (Exception e)
		{ return null; }
	}
	
	public Object copyParameter(String name)
	{
		Field f = findParamField(name);
		if (f != null)
		{
			try
			{ return copyObject(f.get(this)); }
			catch(Exception e)
			{ return null; }
		}
		return null;
	}
	
	public int getParametersCount()
	{
		FunctionEntry funcEntry = functionsBase.get(this.getClass());
		return funcEntry.startIndex + funcEntry.paramFields.length;
	}
	
	private Field findParamField(int index)
	{
		Class<?> c = this.getClass();
		FunctionEntry funcEntry = functionsBase.get(c);
		while(funcEntry.startIndex > index)
		{
			c = c.getSuperclass();
			funcEntry = functionsBase.get(c);
		}
		return funcEntry.paramFields[index-funcEntry.startIndex];
	}
	
	private Field findParamField(String name)
	{
		Class<?> c = this.getClass();
		FunctionEntry funcEntry = functionsBase.get(c);
		while(!c.equals(Function.class))
		{
			Field field = funcEntry.paramFieldsMap.get(name);
			if (field != null)
				return field;
			c = c.getSuperclass();
			funcEntry = functionsBase.get(c);
		}
		return null;
	}
	
	public String getParameterName(int index)
	{
		return findParamField(index).getName();
	}
	public String getParameterUserName(int index)
	{
		String name = findParamField(index).getName();
		try
		{ return (String)this.getClass().getField(name + "UserName").get(null); }
		catch(Exception e)
		{ return name; }
	}
	public Object getValue(int index)
	{
		try
		{ return findParamField(index).get(this); }
		catch (Exception e)
		{ return null; }
	}
	public Class<?> getType(int index)
	{
		return findParamField(index).getType();
	}
	public void setValue(int index, Object value)
	{
		try
		{ findParamField(index).set(this, value); }
		catch (Exception e)
		{ }
		initialize();
	}
	
	public void resetValues()
	{
	}
	
	/* initialization for computing */
	public void initialize()
	{
	}
	
	public Object getValue(String name)
	{
		Field f = findParamField(name);
		if (f != null)
		{
			try
			{ return f.get(this); }
			catch(Exception e)
			{ return null; }
		}
		return null;
	}
	public Class<?> getType(String name)
	{
		Field f = findParamField(name);
		if (f != null)
			return f.getType();
		return null;
	}
	public void setValue(String name, Object ob) throws UnknownParameterException
	{
		Field f = findParamField(name);
		if (f != null)
		{
			try
			{ f.set(this, ob); }
			catch (Exception e)
			{ }
		}
		else
		{ throw new UnknownParameterException("Unknown paremeter to set", name); }
	}
}
