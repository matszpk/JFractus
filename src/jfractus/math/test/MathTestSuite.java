/**
 * MathTestSuite.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math.test;

import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({ComplexTest.class, Matrix3DTest.class, Vector2DTest.class,
	Vector3DTest.class})
public class MathTestSuite
{

}
