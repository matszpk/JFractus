/**
 * Matrix3D.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math;

import java.io.Serializable;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jfractus.dom.*;
import jfractus.generic.Copyable;

public class Matrix3D implements Serializable, Copyable<Matrix3D>,
		DOMNodeHandler
{
	private static final long serialVersionUID = -9065352423243032626L;
	
	static final public int X = 0;
	static final public int Y = 1;
	static final public int Z = 2;
	
	/* holding matrix in following format:
	 * [ m[0], m[1], m[2] ]
	 * [ m[3], m[4], m[5] ]
	 * [ m[6], m[6], m[7] ]
	 */
	public double[] m = new double[9];
	
	public Matrix3D()
	{
		for (int i = 0; i < 9; i++)
			m[i] = 0.0;; 
	}
	
	public Matrix3D(double[] v)
	{
		for (int i = 0; i < 9; i++)
			m[i] = v[i];
	}
	public Matrix3D(Vector3D x, Vector3D y, Vector3D z)
	{
		m[0] = x.x;
		m[1] = x.y;
		m[2] = x.z;
		m[3] = y.x;
		m[4] = y.y;
		m[5] = y.z;
		m[6] = z.x;
		m[7] = z.y;
		m[8] = z.z;
	}
	
	protected Matrix3D(Matrix3D mx)
	{
		for (int i = 0; i < 9; i++)
			m[i] = mx.m[i];
	}
	
	/* copy method */
	public Matrix3D copy()
	{
		return new Matrix3D(this);
	}
	
	/* setting/getting */
	public Vector3D getColumn(int column)
	{
		return new Vector3D(m[column], m[3+column], m[6+column]);
	}
	public Vector2D getColumn2D(int column)
	{
		return new Vector2D(m[column], m[3+column]);
	}
	
	public void setColumn(int column, Vector3D v)
	{
		m[column] = v.x;
		m[3+column] = v.y;
		m[6+column] = v.z;
	}
	public void setColumn(int column, double x, double y, double z)
	{
		m[column] = x;
		m[3+column] = y;
		m[6+column] = z;
	}
	public void setColumn2D(int column, Vector2D v)
	{
		m[column] = v.x;
		m[3+column] = v.y;
		m[6+column] = 0.0;
	}
	public void setColumn2D(int column, double x, double y)
	{
		m[column] = x;
		m[3+column] = y;
		m[6+column] = 0.0;
	}
	
	public Vector3D getRow(int row)
	{
		return new Vector3D(m[3*row], m[3*row + 1], m[3*row + 2]);
	}
	public Vector2D getRow2D(int row)
	{
		return new Vector2D(m[3*row], m[3*row + 1]);
	}
	
	public void setRow(int row, Vector3D v)
	{
		m[3*row] = v.x;
		m[3*row + 1] = v.y;
		m[3*row + 2] = v.z;
	}
	public void setRow(int row, double x, double y, double z)
	{
		m[3*row] = x;
		m[3*row + 1] = y;
		m[3*row + 2] = z;
	}
	public void setRow2D(int row, Vector2D v)
	{
		m[3*row] = v.x;
		m[3*row + 1] = v.y;
		m[3*row + 2] = 0.0;
	}
	public void setRow2D(int row, double x, double y)
	{
		m[3*row] = x;
		m[3*row + 1] = y;
		m[3*row + 2] = 0.0;
	}
	
	public void set(double[] v)
	{
		for (int i = 0; i < 9; i++)
			m[i] = v[i];
	}
	
	public void set(Vector3D x, Vector3D y, Vector3D z)
	{
		m[0] = x.x;
		m[1] = x.y;
		m[2] = x.z;
		m[3] = y.x;
		m[4] = y.y;
		m[5] = y.z;
		m[6] = z.x;
		m[7] = z.y;
		m[8] = z.z;
	}
	
	public void set(Matrix3D mx)
	{
		for (int i = 0; i < 9; i++)
			m[i] = mx.m[i];
	}
	
	/* generating various matrices (identity, rotating, translation) */
	/* methods which generates identity matrix */
	public Matrix3D identity()
	{
		m[0] = m[4] = m[8] = 1.0;
		m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	public Matrix3D zero()
	{
		for (int i = 0; i < 9; i++)
			m[i] = 0.0;
		return this;
	}
	
	/* methods which generates scale matrix */
	public Matrix3D scaleMatrix(double s)
	{
		m[0] = m[4] = m[8] = s;
		m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	
	public Matrix3D scaleMatrix(Vector2D v)
	{
		m[0] = v.x;
		m[4] = v.y;
		m[8] = 1.0;
		m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	public Matrix3D scaleMatrix(Vector3D v)
	{
		m[0] = v.x;
		m[4] = v.y;
		m[8] = v.z;
		m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	
	public Matrix3D scaleMatrix(double x, double y)
	{
		m[0] = x;
		m[4] = y;
		m[8] = 1.0;
		m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	public Matrix3D scaleMatrix(double x, double y, double z)
	{
		m[0] = x;
		m[4] = y;
		m[8] = z;
		m[1] = m[2] = m[3] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	
	/* methods which generates translate matrix */
	public Matrix3D translateMatrix(Vector2D v)
	{
		m[2] = v.x;
		m[5] = v.y;
		m[0] = m[4] = m[8] = 1.0;
		m[1] = m[3] = m[6] = m[7] = 0.0;
		return this;
	}
	
	public Matrix3D translateMatrix(double x, double y)
	{
		m[2] = x;
		m[5] = y;
		m[0] = m[4] = m[8] = 1.0;
		m[1] = m[3] = m[6] = m[7] = 0.0;
		return this;
	}
	
	/* methods which generates translate and scale matrix */
	public Matrix3D translateAndScaleMatrix(Vector2D t, Vector2D s)
	{
		m[2] = t.x;
		m[5] = t.y;
		m[0] = s.x;
		m[4] = s.y;
		m[8] = 1.0;
		m[1] = m[3] = m[6] = m[7] = 0.0;
		return this;
	}
	
	public Matrix3D translateAndScaleMatrix(double tx, double ty,
			double sx, double sy)
	{
		m[2] = tx;
		m[5] = ty;
		m[0] = sx;
		m[4] = sy;
		m[8] = 1.0;
		m[1] = m[3] = m[6] = m[7] = 0.0;
		return this;
	}
	
	/* methods which generates rotation matrix */
	public Matrix3D rotateMatrix(double anglex, double angley, double anglez)
	{
		double cx = Math.cos(anglex);
		double sx = Math.sin(anglex);
		double cy = Math.cos(angley);
		double sy = Math.sin(angley);
		double cz = Math.cos(anglez);
		double sz = Math.sin(anglez);
		m[0] = cz*cx + sz*sy*sx;
		m[1] = sz*sy*cx - cz*sx;
		m[2] = sz*cy;
		m[3] = cy*sx;
		m[4] = cy*cx;
		m[5] = -sy;
		m[6] = cz*sy*sx - sz*cx;
		m[7] = sz*sx + cz*sy*cx;
		m[8] = cz*cy;
		return this;
	}
	
	public Matrix3D rotateXAxisMatrix(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		m[0] = m[4] = c;
		m[1] = -s;
		m[3] = s;
		m[8] = 1.0;
		m[2] = m[5] = m[6] = m[7] = 0.0;
		return this;
	}
	
	public Matrix3D rotateYAxisMatrix(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		m[4] = m[8] = c;
		m[5] = -s;
		m[7] = s;
		m[0] = 1.0;
		m[1] = m[2] = m[3] = m[6] = 0.0;
		return this;
	}
	
	public Matrix3D rotateZAxisMatrix(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		m[8] = m[0] = c;
		m[6] = -s;
		m[2] = s;
		m[4] = 1.0;
		m[1] = m[3] = m[5] = m[7] = 0.0;
		return this;
	}
	
	/* methods which generates unit-square->parallelogram transformation matrix  */
	public Matrix3D paraMatrix(Vector2D pos, Vector2D line1, Vector2D line2)
	{
		m[0] = line1.x;
		m[3] = line1.y;
		m[1] = line2.x;
		m[4] = line2.y;
		m[2] = pos.x;
		m[5] = pos.y;
		m[6] = m[7] = 0.0;
		m[8] = 1.0;
		return this;
	}
	public Matrix3D paraMatrix(double posx, double posy, double l1x, double l1y,
			double l2x, double l2y)
	{
		m[0] = l1x;
		m[3] = l1y;
		m[1] = l2x;
		m[4] = l2y;
		m[2] = posx;
		m[5] = posy;
		m[6] = m[7] = 0.0;
		m[8] = 1.0;
		return this;
	}
	
	/* simple operations */
	public Matrix3D add(double[] mx)
	{
		for(int i = 0; i < 9; i++)
			m[i] += mx[i];
		return this;
	}
	public Matrix3D add(Matrix3D mx)
	{
		for(int i = 0; i < 9; i++)
			m[i] += mx.m[i];
		return this;
	}
	
	public Matrix3D subtract(double[] mx)
	{
		for(int i = 0; i < 9; i++)
			m[i] -= mx[i];
		return this;
	}
	public Matrix3D subtract(Matrix3D mx)
	{
		for(int i = 0; i < 9; i++)
			m[i] -= mx.m[i];
		return this;
	}
	
	/* perform operation: this=mx*this */
	public Matrix3D transform(double[] mx)
	{
		for (int j = 0; j < 3; j++)
		{
			double t0 = m[j];
			double t1 = m[3+j];
			double t2 = m[6+j];
			m[j] = mx[0]*t0 + mx[1]*t1 + mx[2]*t2;
			m[3+j] = mx[3]*t0 + mx[4]*t1 + mx[5]*t2;
			m[6+j] = mx[6]*t0 + mx[7]*t1 + mx[8]*t2;
		}
		return this;
	}
	public Matrix3D transform(Matrix3D mx)
	{
		for (int j = 0; j < 3; j++)
		{
			double t0 = m[j];
			double t1 = m[3+j];
			double t2 = m[6+j];
			m[j] = mx.m[0]*t0 + mx.m[1]*t1 + mx.m[2]*t2;
			m[3+j] = mx.m[3]*t0 + mx.m[4]*t1 + mx.m[5]*t2;
			m[6+j] = mx.m[6]*t0 + mx.m[7]*t1 + mx.m[8]*t2;
		}
		return this;
	}
	
	/* onyl transform, and after translate */
	public Matrix3D onlyTransform(double[] mx)
	{
		double t0 = m[0];
		double t1 = m[3];
		double t2 = m[6];
		m[0] = mx[0]*t0 + mx[1]*t1 + mx[2]*t2;
		m[3] = mx[3]*t0 + mx[4]*t1 + mx[5]*t2;
		t0 = m[1];
		t1 = m[4];
		t2 = m[7];
		m[1] = mx[0]*t0 + mx[1]*t1 + mx[2]*t2;
		m[4] = mx[3]*t0 + mx[4]*t1 + mx[5]*t2;
		m[2] += mx[2];
		m[5] += mx[5];
		return this;
	}
	
	public Matrix3D onlyTransform(Matrix3D mx)
	{
		double t0 = m[0];
		double t1 = m[3];
		double t2 = m[6];
		m[0] = mx.m[0]*t0 + mx.m[1]*t1 + mx.m[2]*t2;
		m[3] = mx.m[3]*t0 + mx.m[4]*t1 + mx.m[5]*t2;
		t0 = m[1];
		t1 = m[4];
		t2 = m[7];
		m[1] = mx.m[0]*t0 + mx.m[1]*t1 + mx.m[2]*t2;
		m[4] = mx.m[3]*t0 + mx.m[4]*t1 + mx.m[5]*t2;
		m[2] += mx.m[2];
		m[5] += mx.m[5];
		return this;
	}
	
	public Matrix3D negate()
	{
		for (int i = 0; i < 9; i++)
			m[i] = -m[i];
		return this;
	}
	public Matrix3D inverse()
	{
		double invDet = 1.0 / (m[0]*(m[4]*m[8]-m[5]*m[7]) +
			m[1]*(m[5]*m[6]-m[3]*m[8]) + m[2]*(m[3]*m[7]-m[4]*m[6]));
		
		double m00 = m[4]*m[8]-m[7]*m[5];
		double m01 = m[5]*m[6]-m[8]*m[3];
		double m02 = m[3]*m[7]-m[6]*m[4];
		double m10 = m[7]*m[2]-m[1]*m[8];
		double m11 = m[8]*m[0]-m[2]*m[6];
		double m12 = m[6]*m[1]-m[0]*m[7];
		double m20 = m[1]*m[5]-m[2]*m[4];
		double m21 = m[2]*m[3]-m[5]*m[0];
		double m22 = m[0]*m[4]-m[3]*m[1];
		m[0] = m00 * invDet;
		m[1] = m10 * invDet;
		m[2] = m20 * invDet;
		m[3] = m01 * invDet;
		m[4] = m11 * invDet;
		m[5] = m21 * invDet;
		m[6] = m02 * invDet;
		m[7] = m12 * invDet;
		m[8] = m22 * invDet;	
		return this;
	}
	
	public double determinant()
	{
		return m[0]*(m[4]*m[8]-m[5]*m[7]) +
			m[1]*(m[5]*m[6]-m[3]*m[8]) + m[2]*(m[3]*m[7]-m[4]*m[6]);
	}
	
	public Matrix3D transpose()
	{
		double tmp;
		tmp = m[1];
		m[1] = m[3];
		m[3] = tmp;
		tmp = m[2];
		m[2] = m[6];
		m[6] = tmp;
		tmp = m[5];
		m[5] = m[7];
		m[7] = tmp;
		return this;
	}
	public Matrix3D square()
	{
		double m00 = m[0];
		double m01 = m[1];
		double m02 = m[2];
		double m10 = m[3];
		double m11 = m[4];
		double m12 = m[5];
		double m20 = m[6];
		double m21 = m[7];
		double m22 = m[8];
		m[0] = m00*m00 + m01*m10 + m02*m20;
		m[1] = m00*m01 + m01*m11 + m02*m21;
		m[2] = m00*m02 + m01*m12 + m02*m22;
		m[3] = m10*m00 + m11*m10 + m12*m20;
		m[4] = m10*m01 + m11*m11 + m12*m21;
		m[5] = m10*m02 + m11*m12 + m12*m22;
		m[6] = m20*m00 + m21*m10 + m22*m20;
		m[7] = m20*m01 + m21*m11 + m22*m21;
		m[8] = m20*m02 + m21*m12 + m22*m22;
		return this;
	}
	
	/* operations2 */
	public Matrix3D translate(Vector2D t)
	{
		m[2] += t.x;
		m[5] += t.y;
		return this;
	}
	public Matrix3D translate(double x, double y)
	{
		m[2] += x;
		m[5] += y;
		return this;
	}
	
	public Matrix3D scale(double s)
	{
		for (int i = 0; i < 9; i++)
			m[i] *= s;
		return this;
	}
	public Matrix3D scale(Vector2D v)
	{
		m[0] *= v.x;
		m[1] *= v.x;
		m[2] *= v.x;
		m[3] *= v.y;
		m[4] *= v.y;
		m[5] *= v.y;
		return this;
	}
	public Matrix3D scale(double x, double y)
	{
		m[0] *= x;
		m[1] *= x;
		m[2] *= x;
		m[3] *= y;
		m[4] *= y;
		m[5] *= y;
		return this;
	}
	public Matrix3D scale(Vector3D v)
	{
		for(int i = 0; i < 3; i++)
		{
			m[i] *= v.x;
			m[3+i] *= v.y;
			m[6+i] *= v.z;
		}
		return this;
	}
	public Matrix3D scale(double x, double y, double z)
	{
		for(int i = 0; i < 3; i++)
		{
			m[i] *= x;
			m[3+i] *= y;
			m[6+i] *= z;
		}
		return this;
	}
	
	/* do not translate only scale */
	public Matrix3D onlyScale(double s)
	{
		m[0] *= s;
		m[1] *= s;
		m[3] *= s;
		m[4] *= s;
		return this;
	}
	public Matrix3D onlyScale(double x, double y)
	{
		m[0] *= x;
		m[1] *= x;
		m[3] *= y;
		m[4] *= y;
		return this;
	}
	public Matrix3D onlyScale(Vector2D v)
	{
		m[0] *= v.x;
		m[1] *= v.x;
		m[3] *= v.y;
		m[4] *= v.y;
		return this;
	}
	
	public Matrix3D translateAndScale(Vector2D t, Vector2D s)
	{
		m[0] = m[0]*s.x + t.x*m[6];
		m[1] = m[1]*s.x + t.x*m[7];
		m[2] = m[2]*s.x + t.x*m[8];
		m[3] = m[3]*s.y + t.y*m[6];
		m[4] = m[4]*s.y + t.y*m[7];
		m[5] = m[5]*s.y + t.y*m[8];
		return this;
	}
	public Matrix3D translateAndScale(double tx, double ty, double sx, double sy)
	{
		m[0] = m[0]*sx + tx*m[6];
		m[1] = m[1]*sx + tx*m[7];
		m[2] = m[2]*sx + tx*m[8];
		m[3] = m[3]*sy + ty*m[6];
		m[4] = m[4]*sy + ty*m[7];
		m[5] = m[5]*sy + ty*m[8];
		return this;
	}
	
	public Matrix3D onlyTranslateAndScale(Vector2D t, Vector2D s)
	{
		m[0] = m[0]*s.x;
		m[1] = m[1]*s.x;
		m[2] += t.x;
		m[3] = m[3]*s.y;
		m[4] = m[4]*s.y;
		m[5] += t.y;
		return this;
	}
	public Matrix3D onlyTranslateAndScale(double tx, double ty, double sx, double sy)
	{
		m[0] = m[0]*sx;
		m[1] = m[1]*sx;
		m[2] += tx;
		m[3] = m[3]*sy;
		m[4] = m[4]*sy;
		m[5] += ty;
		return this;
	}
	
	public Matrix3D rotate(double anglex, double angley, double anglez)
	{
		double cx = Math.cos(anglex);
		double sx = Math.sin(anglex);
		double cy = Math.cos(angley);
		double sy = Math.sin(angley);
		double cz = Math.cos(anglez);
		double sz = Math.sin(anglez);
		double m00 = cz*cx + sz*sy*sx;
		double m01 = sz*sy*cx - cz*sx;
		double m02 = sz*cy;
		double m10 = cy*sx;
		double m11 = cy*cx;
		double m12 = -sy;
		double m20 = cz*sy*sx - sz*cx;
		double m21 = sz*sx + cz*sy*cx;
		double m22 = cz*cy;
		for (int j = 0; j < 3; j++)
		{
			double t0 = m[j];
			double t1 = m[3+j];
			double t2 = m[6+j];
			m[j] = m00*t0 + m01*t1 + m02*t2;
			m[3+j] = m10*t0 + m11*t1 + m12*t2;
			m[6+j] = m20*t0 + m21*t1 + m22*t2;
		}
		return this;
	}
	public Matrix3D rotateXAxis(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		for (int i = 0; i < 3; i++)
		{
    		double tmp = m[i];
    		m[i] = c*m[i] - s*m[3+i];
    		m[3+i] = s*tmp + c*m[3+i];
		}
		return this;
	}
	public Matrix3D rotateYAxis(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		for (int i = 0; i < 3; i++)
		{
    		double tmp = m[3+i];
    		m[3+i] = c*m[3+i] - s*m[6+i];
    		m[6+i] = s*tmp + c*m[6+i];
		}
		return this;
	}
	public Matrix3D rotateZAxis(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		for (int i = 0; i < 3; i++)
		{
    		double tmp = m[6+i];
    		m[6+i] = c*m[6+i] - s*m[i];
    		m[i] = s*tmp + c*m[i];
		}
		return this;
	}
	
	/* do not translate only rotate */
	public Matrix3D onlyRotate(double angle)
	{
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double tmp = m[0];
		m[0] = c*m[0] - s*m[3];
		m[3] = s*tmp + c*m[3];
		tmp = m[1];
		m[1] = c*m[1] - s*m[4];
		m[4] = s*tmp + c*m[4];
		return this;
	}
	
	/* Object method overloading */
	public boolean equals(Object ob)
	{
		if (ob instanceof Matrix3D)
		{
			Matrix3D m3d = (Matrix3D)ob;
			if (this == m3d)
				return true;
			for(int i = 0; i < 9; i++)
				if (m[i] != m3d.m[i])
						return false;
			return true;
		}
		else
			return false;
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder("[");
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				s.append(m[3*i+j]);
				if (j == 2)
				{
					if (i == 2)
						s.append("]");
					else
						s.append(";");
				}
				else 
					s.append(',');
			}
		return s.toString();
	}

    public Node createNode(Document doc)
    {
    	Element elem = doc.createElement("matrix3d");
    	Element xElem = doc.createElement("x");
    	Element yElem = doc.createElement("y");
    	Element zElem = doc.createElement("z");
    	xElem.setTextContent(String.format
				(Locale.ENGLISH, "%15.16g %15.16g %15.16g", m[0], m[1], m[2]));
    	yElem.setTextContent(String.format
				(Locale.ENGLISH, "%15.16g %15.16g %15.16g", m[3], m[4], m[5]));
    	zElem.setTextContent(String.format
				(Locale.ENGLISH, "%15.16g %15.16g %15.16g", m[6], m[7], m[8]));
    	elem.appendChild(xElem);
    	elem.appendChild(yElem);
    	elem.appendChild(zElem);
	    return elem;
    }

    public void getFromNode(Node node) throws DOMNodeException
    {
    	Element elem = (Element)node;
    	
    	if (elem.getAttributes().getLength() != 0)
    		throw new DOMNodeBadStructureException("Unexpected attributes at Matrix3D");
    	
    	NodeList nodeList = elem.getChildNodes();
    	
    	double newM[] = new double[9];
    	boolean parsedX = false;
    	boolean parsedY = false;
    	boolean parsedZ = false;
    	
    	for (int i = 0; i < nodeList.getLength(); i++)
    	{
    		Node child = nodeList.item(i);
    		if (child.getNodeType() == Node.TEXT_NODE ||
    				child.getNodeType() == Node.COMMENT_NODE)
    			continue;
    		
    		if (child.getNodeType() != Node.ELEMENT_NODE)
    			throw new DOMNodeBadStructureException
    					("Bad type of subelement in Matrix3D");
    		
    		if (child.getAttributes().getLength() != 0)
    			throw new DOMNodeBadStructureException
    					("Unexpected attributes at subelement in Matrix3D");
    		
    		String childName = child.getNodeName();
    		int index = 0;
    		boolean isParsed = false;
    		if (childName == "x")
    		{
    			index = 0;
    			isParsed = parsedX;
    			parsedX = true;
    		}
    		else if (childName == "y")
    		{
    			index = 3;
    			isParsed = parsedY;
    			parsedY = true;
    		}
    		else if (childName == "z")
    		{
    			index = 6;
    			isParsed = parsedZ;
    			parsedZ = true;
    		}
    		else throw new DOMNodeBadStructureException
    				("Bad name of subelement in Matrix3D");
    		
    		if (isParsed)
    			throw new DOMNodeBadStructureException
    				("Duplication of subelement in Matrix3D");
    		
    		Scanner scanner = new Scanner(child.getTextContent());
    		scanner.useLocale(Locale.ENGLISH);
    		try
    		{
    			newM[index] = scanner.nextDouble();
    			newM[index+1] = scanner.nextDouble();
    			newM[index+2] = scanner.nextDouble();
    			if (scanner.hasNext())
    				throw new DOMNodeParseException
    					("Exception at parsing subelement in Matrix3D");
    		}
    		catch (InputMismatchException e)
    		{ throw new DOMNodeParseException
    				("Exception at parsing subelement in Matrix3D"); }
    		catch (NoSuchElementException e)
    		{ throw new DOMNodeParseException
    				("Exception at parsing subelement in Matrix3D"); }
    	}
    	
    	if (!parsedX || !parsedY || !parsedZ)
    		throw new DOMNodeBadStructureException("Not all parts of matrix is parsed");
    	/* save to object */
    	m = newM;
    }

    public boolean isValidNode(Node node)
    {
	    return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName() == "matrix3d");
    }
}
