/**
 * ObjectEditorFactory.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import jfractus.math.*;

public class ObjectEditorFactory
{
	public static ObjectEditor newObjectEditor(int index, String name, Class<?> cls)
	{
		if (boolean.class.equals(cls) || Boolean.class.equals(cls))
			return new BooleanEditor(index, name, cls);
		else if (char.class.equals(cls) || Character.class.equals(cls))
			return new CharacterEditor(index, name, cls);
		else if (byte.class.equals(cls) || Byte.class.equals(cls) ||
				short.class.equals(cls) || Short.class.equals(cls) ||
				int.class.equals(cls) || Integer.class.equals(cls) ||
				long.class.equals(cls) || Long.class.equals(cls))
			return new IntegerEditor(index, name, cls);
		else if (float.class.equals(cls) || Float.class.equals(cls) ||
				double.class.equals(cls) || Double.class.equals(cls))
			return new FloatingPointEditor(index, name, cls);
		else if (String.class.equals(cls))
			return new StringEditor(index, name, cls);
		else if (Complex.class.equals(cls))
			return new ComplexEditor(index, name);
		else if (Vector2D.class.equals(cls))
			return new Vector2DEditor(index, name);
		else if (Vector3D.class.equals(cls))
			return new Vector3DEditor(index, name);
		else if (Matrix3D.class.equals(cls))
			return new Matrix3DEditor(index, name);
		else if (cls.isEnum())
			return new EnumEditor(index, name, cls);
		else if(cls.isArray())
		{
			Class<?> compType = cls.getComponentType();
			if (char.class.equals(compType) || Character.class.equals(compType))
				return new StringEditor(index, name, cls);
			else
				return new ArrayEditor(index, name, cls);
		}
			
		return null;
	}
	
	public boolean arrayRequired(Class<?> cls)
	{
		if(cls.isArray())
		{
			Class<?> compType = cls.getComponentType();
			return !(char.class.equals(compType) || Character.class.equals(compType));
		}
		return false;
	}
}
