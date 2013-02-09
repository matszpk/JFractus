/**
 * TransformChangeEvent.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.EventObject;

import jfractus.math.Matrix3D;

public class TransformChangeEvent extends EventObject
{
	private static final long serialVersionUID = 5730972242184960993L;
	
	private Matrix3D matrix;

	public TransformChangeEvent(Object source, Matrix3D m)
	{
		super(source);
		this.matrix = m;
	}
	
	public Matrix3D getMatrix()
	{
		return matrix;
	}
}
