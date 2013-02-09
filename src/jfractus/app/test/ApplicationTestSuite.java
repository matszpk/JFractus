/**
 * ApplicationTestSuite.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({FractalFrameTest.class, FractalKeyFrameTest.class,
	FractalAnimationTest.class, FractalDocumentTest.class, RGBColorTest.class,
	GradientTest.class, GradientLocatorTest.class, FunctionDOMHandlerTest.class,
	CopyFunctionTest.class})
public class ApplicationTestSuite
{
}
