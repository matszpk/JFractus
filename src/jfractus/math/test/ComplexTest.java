/**
 * ComplexTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jfractus.dom.DOMNodeException;
import jfractus.math.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ComplexTest
{
	private Complex complex;
	
	private Document document;
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "complex", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}
	
	private void assertEqualsComplex(double re, double im)
	{
		assertEquals(re, complex.re, 5.0e-13);
		assertEquals(im, complex.im, 5.0e-13);
	}

	@Before
	public void setUp() throws Exception
	{
		complex = new Complex();
	}

	@Test
	public void testAddDouble()
	{
		complex.set(1.7, 65.5);
		complex.add(15.3);
		assertEqualsComplex(17.0, 65.5);
		complex.set(-15.7, -7.5);
		complex.add(43.1);
		assertEqualsComplex(27.4, -7.5);
	}

	@Test
	public void testAddDoubleDouble()
	{
		complex.set(1.81, -63.1);
		complex.add(-2.2, 1.32);
		assertEqualsComplex(-0.39, -61.78);
		complex.set(25.2, -3.12);
		complex.add(-6.1, 3.5);
		assertEqualsComplex(19.1, 0.38);
	}

	@Test
	public void testAddComplex()
	{
		complex.set(1.81, -63.1);
		complex.add(new Complex(-2.2, 1.32));
		assertEqualsComplex(-0.39, -61.78);
		complex.set(25.2, -3.12);
		complex.add(new Complex(-6.1, 3.5));
		assertEqualsComplex(19.1, 0.38);
	}

	@Test
	public void testSubtractDouble()
	{
		complex.set(1.7, 65.5);
		complex.subtract(15.3);
		assertEqualsComplex(-13.6, 65.5);
		complex.set(-15.7, -7.5);
		complex.subtract(43.1);
		assertEqualsComplex(-58.8, -7.5);
	}

	@Test
	public void testSubtractDoubleDouble()
	{
		complex.set(2.9, -43.5);
		complex.subtract(-7.2, 5.72);
		assertEqualsComplex(10.1, -49.22);
		complex.set(12.2, -32.12);
		complex.subtract(-6.1, 12.5);
		assertEqualsComplex(18.3, -44.62);
	}

	@Test
	public void testSubtractComplex()
	{
		complex.set(2.9, -43.5);
		complex.subtract(new Complex(-7.2, 5.72));
		assertEqualsComplex(10.1, -49.22);
		complex.set(12.2, -32.12);
		complex.subtract(new Complex(-6.1, 12.5));
		assertEqualsComplex(18.3, -44.62);
	}

	@Test
	public void testMultiplyDouble()
	{
		complex.set(-67.1, -44.1);
		complex.multiply(-6.3);
		assertEqualsComplex(422.73, 277.83);
		complex.set(-7.3, 11.2);
		complex.multiply(34.2);
		assertEqualsComplex(-249.66, 383.04);
	}

	@Test
	public void testMultiplyDoubleDouble()
	{
		complex.set(-4.2, 11.8);
		complex.multiply(-3.89, 5.21);
		assertEqualsComplex(-45.140, -67.784);
		complex.set(-0.7, 11.3);
		complex.multiply(54.8, 7.4);
		assertEqualsComplex(-121.98, 614.06);
	}

	@Test
	public void testMultiplyComplex()
	{
		complex.set(-4.2, 11.8);
		complex.multiply(new Complex(-3.89, 5.21));
		assertEqualsComplex(-45.140, -67.784);
		complex.set(-0.7, 11.3);
		complex.multiply(new Complex(54.8, 7.4));
		assertEqualsComplex(-121.98, 614.06);
	}

	@Test
	public void testDivideDouble()
	{
		complex.set(7.6, 2.46);
		complex.divide(7.5);
		assertEqualsComplex(1.013333333333333, 0.328);
		complex.set(-6.4, -4.1);
		complex.divide(0.7);
		assertEqualsComplex(-9.14285714285714, -5.85714285714286);
	}

	@Test
	public void testDivideDoubleDouble()
	{
		complex.set(-67.1, 4.92);
		complex.divide(-1.3, 6.2);
		assertEqualsComplex(2.93381510092200, 10.20742586593571);
		complex.set(3.5, 4.31);
		complex.divide(-0.5, -0.13);
		assertEqualsComplex(-8.65605095541401, -6.36942675159236);
		complex.set(1.67, -5.2);
		complex.divide(0.0, -1.45);
		assertEqualsComplex(3.58620689655172, 1.15172413793103);
	}

	@Test
	public void testDivideComplex()
	{
		complex.set(-67.1, 4.92);
		complex.divide(new Complex(-1.3, 6.2));
		assertEqualsComplex(2.93381510092200, 10.20742586593571);
		complex.set(3.5, 4.31);
		complex.divide(new Complex(-0.5, -0.13));
		assertEqualsComplex(-8.65605095541401, -6.36942675159236);
		complex.set(1.67, -5.2);
		complex.divide(new Complex(0.0, -1.45));
		assertEqualsComplex(3.58620689655172, 1.15172413793103);
	}

	@Test
	public void testNeg()
	{
		complex.set(11.7, -64.1);
		complex.neg();
		assertEqualsComplex(-11.7, 64.1);
	}

	@Test
	public void testConj()
	{
		complex.set(11.7, -64.1);
		complex.conj();
		assertEqualsComplex(11.7, 64.1);
	}

	@Test
	public void testAbs()
	{
		complex.set(11.4, 66.34);
		assertEquals(complex.abs(), 67.31237330535895, 1.0e-14);
		complex.set(-15.7, 4.1);
		assertEquals(complex.abs(), 16.2265215003093, 1.0e-14);
	}

	@Test
	public void testArg()
	{
		complex.set(-5.63, 3.23);
		assertEquals(complex.arg(), 2.62072668627117, 1.0e-14);
		complex.set(-5.63, -3.23);
		assertEquals(complex.arg(), -2.62072668627117, 1.0e-14);
		complex.set(0.0, 3.23);
		assertEquals(complex.arg(), 1.57079632679490, 1.0e-14);
		complex.set(0.0, -3.23);
		assertEquals(complex.arg(), -1.57079632679490, 1.0e-14);
		complex.set(0.0, 0.0);
		assertEquals(complex.arg(), 0.0, 1.0e-14);
		complex.set(4.4, 0.0);
		assertEquals(complex.arg(), 0.0, 1.0e-14);
		complex.set(-4.4, 0.0);
		assertEquals(complex.arg(), Math.PI, 1.0e-14);
		complex.set(0.0, -4.4);
		assertEquals(complex.arg(), -Math.PI/2.0, 1.0e-14);
	}

	@Test
	public void testNorm2()
	{
		complex.set(6.6, -13.1);
		assertEquals(complex.norm2(), 215.170000000000, 1.0e-15);
		complex.set(-4.3, -7.43);
		assertEquals(complex.norm2(), 73.6949000000000, 1.0e-13);
	}

	@Test
	public void testRecip()
	{
		complex.set(-5.4, 14.4);
		complex.recip();
		assertEqualsComplex(-0.0228310502283105, -0.0608828006088280);
		complex.set(5.71, 5.5);
		complex.recip();
		assertEqualsComplex(0.0908453068296261, -0.0875042360005155);
		complex.set(0.0, 5.0);
		complex.recip();
		assertEqualsComplex(0.0, -0.2);
	}

	@Test
	public void testExp()
	{
		complex.set(4.1, -7.51);
		complex.exp();
		assertEqualsComplex(20.3490465240468, -56.8055156907719);
		complex.set(-8.4, 6.41);
		complex.exp();
		assertEqualsComplex(2.23061591736940e-04, 2.84401085641044e-05);
	}

	@Test
	public void testPowDouble()
	{
		complex.set(6.1, -7.67);
		complex.pow(2.4);
		assertEqualsComplex(-132.462691340145, -199.290041038744);
		complex.set(6.1, 7.67);
		complex.pow(2.4);
		assertEqualsComplex(-132.462691340145, 199.290041038744);
		complex.set(6.1, 7.67);
		complex.pow(-2.4);
		assertEqualsComplex(-0.00231323816839720, -0.00348026546077393);
		complex.set(-6.1, 23.1);
		complex.pow(0.3);
		assertEqualsComplex(2.21069900530823, 1.35141469340142);
		complex.set(-6.1, 23.1);
		complex.pow(0.0);
		assertEqualsComplex(1.0, 0.0);
	}

	@Test
	public void testPowDoubleDouble()
	{
		complex.set(-6.1, 23.1);
		complex.pow(0.7, -1.41);
		assertEqualsComplex(-121.38063615527405, 6.41655511709379);
		complex.set(-0.1, -0.53);
		complex.pow(-2.7, -1.41);
		assertEqualsComplex(0.348953165161046, -0.275327540458986);
		complex.set(6.1, 7.67);
		complex.pow(2.4, 0.0);
		assertEqualsComplex(-132.462691340145, 199.290041038744);
	}

	@Test
	public void testPowComplex()
	{
		complex.set(-6.1, 23.1);
		complex.pow(new Complex(0.7, -1.41));
		assertEqualsComplex(-121.38063615527405, 6.41655511709379);
		complex.set(-0.1, -0.53);
		complex.pow(new Complex(-2.7, -1.41));
		assertEqualsComplex(0.348953165161046, -0.275327540458986);
		complex.set(6.1, 7.67);
		complex.pow(new Complex(2.4, 0.0));
		assertEqualsComplex(-132.462691340145, 199.290041038744);
	}
	
	@Test
	public void testSquare()
	{
		complex.set(-43.1, 54.32);
		complex.square();
		assertEqualsComplex(-1093.0524, -4682.384);
		complex.set(-0.72, -1.4);
		complex.square();
		assertEqualsComplex(-1.4416, 2.016);
	}
	
	@Test
	public void testCube()
	{
		complex.set(3.6, -5.14);
		complex.cube();
		assertEqualsComplex(-238.67568, -64.046456);
		complex.set(-3.6, -5.14);
		complex.cube();
		assertEqualsComplex(238.67568, -64.046456);
	}

	@Test
	public void testSqrt()
	{
		complex.set(3.2, 8.7);
		complex.sqrt();
		assertEqualsComplex(2.49698253699599, 1.74210269216912);
		complex.set(3.2, -8.7);
		complex.sqrt();
		assertEqualsComplex(2.49698253699599, -1.74210269216912);
		complex.set(-74.1, -46.1);
		complex.sqrt();
		assertEqualsComplex(2.56610712016496, -8.98247770674446);
		complex.set(0.0, -4.3);
		complex.sqrt();
		assertEqualsComplex(1.46628782986152, -1.46628782986152);
		complex.set(-4.3, 0.0);
		complex.sqrt();
		assertEqualsComplex(0.0, 2.073644135332772);
		complex.set(4.3, 0.0);
		complex.sqrt();
		assertEqualsComplex(2.073644135332772, 0.0);
		complex.set(0.0, 4.3);
		complex.sqrt();
		assertEqualsComplex(1.46628782986152, 1.46628782986152);
		complex.set(0.0, 0.0);
		complex.sqrt();
		assertEqualsComplex(0.0, 0.0);
	}

	@Test
	public void testCbrt()
	{
		complex.set(-3.9, 7.54);
		complex.cbrt();
		assertEqualsComplex(1.58271445022707, 1.28699988156124);
		complex.set(5.7, -6.1);
		complex.cbrt();
		assertEqualsComplex(1.953462545995961, -0.547150253497000);
		complex.set(0.0, -67.1);
		complex.cbrt();
		assertEqualsComplex(3.51915291593268, -2.03178388333319);
		complex.set(-67.1, 0.0);
		complex.cbrt();
		assertEqualsComplex(2.03178388333319, 3.51915291593268);
	}

	@Test
	public void testLog()
	{
		complex.set(546.0,-64.7);
		complex.log();
		assertEqualsComplex(6.309591047202788, -0.1179481518967567);
		complex.set(546.0,64.7);
		complex.log();
		assertEqualsComplex(6.309591047202788, 0.1179481518967567);
		complex.set(-54.5, 0.0);
		complex.log();
		assertEqualsComplex(3.99820070166920, 3.14159265358979);
		complex.set(0.0, -54.5);
		complex.log();
		assertEqualsComplex(3.99820070166920, -1.57079632679490);
		complex.set(-6.5, 13.1);
		complex.log();
		assertEqualsComplex(2.68266079766426, 2.03138584135324);
		complex.set(0.0759649545900167, 0.0);
		complex.log();
		assertEqualsComplex(-2.57748316886166, 0.0);
	}

	@Test
	public void testCos()
	{
		complex.set(62.6, -1.8);
		complex.cos();
		assertEqualsComplex(3.024324231937605, -0.676056941128280);
		complex.set(0.6, 7.1);
		complex.cos();
		assertEqualsComplex(500.140135832547, -342.163810362774);
	}

	@Test
	public void testSin()
	{
		complex.set(62.6, -1.8);
		complex.sin();
		assertEqualsComplex(-0.714039551878142, -2.863448367595196);
		complex.set(0.6, 7.1);
		complex.sin();
		assertEqualsComplex(342.164276252059, 500.139454844068);
	}

	@Test
	public void testTan()
	{
		complex.set(62.6, -1.8);
		complex.tan();
		assertEqualsComplex(-0.0232863932888528, -0.9520114493700783);
		complex.set(1.8, 1.1);
		complex.tan();
		assertEqualsComplex(-0.120540008943895, 1.214089665824317);
	}

	@Test
	public void testCot()
	{
		complex.set(62.6, -1.8);
		complex.cot();
		assertEqualsComplex(-0.0256778184420418, 1.0497794505331188);
		complex.set(1.8, 1.1);
		complex.cot();
		assertEqualsComplex(-0.0809784866901403, -0.8156225033163232);
	}

	@Test
	public void testAcos()
	{
		complex.set(6.7, 73.1);
		complex.acos();
		assertEqualsComplex(1.47940486256872, -4.98920396565625);
		complex.set(-7.5, -4.24);
		complex.acos();
		assertEqualsComplex(2.62415263415640, 2.84498672058957);
		complex.set(6.37);
		complex.acos();
		assertEqualsComplex(0.0, 2.538527777293806);
		complex.set(0.0, 6.544);
		complex.acos();
		assertEqualsComplex(1.57079632679490, -2.57748316886166);
	}

	@Test
	public void testAsin()
	{
		complex.set(6.7, 73.1);
		complex.asin();
		assertEqualsComplex(0.0913914642261860, 4.9892039656574356);
		complex.set(-7.5, -4.24);
		complex.asin();
		assertEqualsComplex(-1.05335630736151, -2.84498672058956);
		complex.set(6.37);
		complex.asin();
		assertEqualsComplex(1.57079632679490, -2.53852777729380);
		complex.set(0.0, 6.544);
		complex.asin();
		assertEqualsComplex(0.0, 2.57748316886166);
	}

	@Test
	public void testAtan()
	{
		complex.set(6.7, 73.1);
		complex.atan();
		assertEqualsComplex(1.5695527098698197, 0.013566738748191476);
		complex.set(-7.5, -4.24);
		complex.atan();
		assertEqualsComplex(-1.4697734215557243, -0.0566028580683581);
		complex.set(6.37);
		complex.atan();
		assertEqualsComplex(1.41508133178627, 0.0);
		complex.set(0.0, 6.544);
		complex.atan();
		assertEqualsComplex(1.570796326794897, 0.154018141721777);
	}

	@Test
	public void testAcot()
	{
		complex.set(6.7, 73.1);
		complex.acot();
		assertEqualsComplex(0.0012436169251191292, -0.01356673874819179);
		complex.set(-7.5, -4.24);
		complex.acot();
		assertEqualsComplex(-0.1010229052391723, 0.0566028580683581);
		complex.set(6.37);
		complex.acot();
		assertEqualsComplex(0.15571499500862507, 0.0);
		complex.set(0.0, 6.544);
		complex.acot();
		assertEqualsComplex(0.0, -0.154018141721777);
	}

	@Test
	public void testCosh()
	{
		complex.set(4.56, 7.5);
		complex.cosh();
		assertEqualsComplex(16.5681182148999, 44.8237442248025);
		complex.set(-6.6, -7.3);
		complex.cosh();
		assertEqualsComplex(193.358883927125, 312.575355836231);
		complex.set(-0.6, -0.3);
		complex.cosh();
		assertEqualsComplex(1.132518179576088, 0.188143998168130);
	}

	@Test
	public void testSinh()
	{
		complex.set(4.56, 7.5);
		complex.sinh();
		assertEqualsComplex(16.5644916957729, 44.8335576358484);
		complex.set(-6.6, -7.3);
		complex.sinh();
		assertEqualsComplex(-193.358168268085, -312.576512743027);
		complex.set(-0.6, -0.3);
		complex.sinh();
		assertEqualsComplex(-0.608218397958741, -0.350328926284785);
	}

	@Test
	public void testTanh()
	{
		complex.set(4.56, 7.5);
		complex.tanh();
		assertEqualsComplex(1.00016630648534e+00, 1.4237776755393586e-04);
		complex.set(-4.2, -1.1);
		complex.tanh();
		assertEqualsComplex(-1.00026463824065e+00, -3.6370508906644587e-04);
		complex.set(0.56, -0.7);
		complex.tanh();
		assertEqualsComplex(0.733992036710211, -0.528239986541791);
	}

	@Test
	public void testCoth()
	{
		complex.set(4.56, 7.5);
		complex.coth();
		assertEqualsComplex(9.99833700906587e-01, -1.423304197883971e-04);
		complex.set(-4.2, -1.1);
		complex.coth();
		assertEqualsComplex(-9.99735299597812e-01, 3.6351261684371846e-04);
		complex.set(0.56, -0.7);
		complex.coth();
		assertEqualsComplex(0.897540202889952, 0.645942463926883);
	}

	@Test
	public void testAcosh()
	{
		complex.set(-6.7, 1.2);
		complex.acosh();
		assertEqualsComplex(-2.60594798387338, -2.96246552676575);
		complex.set(-7.54, -6.7);
		complex.acosh();
		assertEqualsComplex(-3.00408524686047, 2.41267274383177);
		complex.set(10.7, 0.0);
		complex.acosh();
		assertEqualsComplex(3.06120013815900, 0.0);
		complex.set(0.0, -4.17);
		complex.acosh();
		assertEqualsComplex(-2.13513970396137, 1.57079632679490);
	}

	@Test
	public void testAsinh()
	{
		complex.set(-6.7, 1.2);
		complex.asinh();
		assertEqualsComplex(-2.616069766901094, 0.175380524424792);
		complex.set(-7.54, -6.7);
		complex.asinh();
		assertEqualsComplex(-3.004662992153346, -0.724039674422024);
		complex.set(10.7, 0.0);
		complex.asinh();
		assertEqualsComplex(3.06556740121351, 0.0);
		complex.set(0.0, -4.17);
		complex.asinh();
		assertEqualsComplex(-2.10636588642862, -1.57079632679490);
	}

	@Test
	public void testAtanh()
	{
		complex.set(15.0, -6.4);
		complex.atanh();
		assertEqualsComplex(0.0564265247695868, -1.5466605016280708);
		complex.set(-0.7, 1.7);
		complex.atanh();
		assertEqualsComplex(-0.165620095541743, 1.090761145592053);
		complex.set(-7.51);
		complex.atanh();
		assertEqualsComplex(-0.133951243182402, 1.570796326794897);
	}

	@Test
	public void testAcoth()
	{
		complex.set(15.0, -6.4);
		complex.acoth();
		assertEqualsComplex( 0.0564265247695868, 0.024135825166826646);
		complex.set(-0.7, 1.7);
		complex.acoth();
		assertEqualsComplex(-0.165620095541743, -0.480035181202844);
		complex.set(-7.51);
		complex.acoth();
		assertEqualsComplex(-0.133951243182402, 0.0);
	}
	
	@Test
	public void transformDoubleArrayArray()
	{
		complex.set(2.7, 6.11);
		final double[] m = {
				0.67, 1.2, -5.5,
				-3.5, 1.5, -0.4,
				1.54, -3.7, -4.1
		};
		complex.transform(m);
		assertEqualsComplex(3.641, -0.685);
	}
	
	@Test
	public void transformMatrix3D()
	{
		complex.set(2.7, 6.11);
		Matrix3D m = new Matrix3D(new Vector3D(0.67, 1.2, -5.5),
				new Vector3D(-3.5, 1.5, -0.4),
				new Vector3D(1.54, -3.7, -4.1));
		complex.transform(m);
		assertEqualsComplex(3.641, -0.685);
	}

	@Test
	public void testEqualsObject()
	{
		complex.set(4.1, 44.4);
		assertTrue(complex.equals(new Complex(4.1, 44.4)));
		assertTrue(complex.equals(complex));
	}
	
	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(complex.isValidNode(document.getDocumentElement()));
	}
	
	@Test
	public void testGetFromNode()
	{
		prepareDocument();
		Element elem = document.getDocumentElement();
		elem.setTextContent(" 43.67 \n 4.87  ");
		
		try
		{ complex.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertEquals(new Complex(43.67, 4.87), complex);
		
		boolean catched = false;
		elem.setTextContent(" 43.67,, 4.87  ");
		try
		{ complex.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
		
		catched = false;
		elem.setTextContent(" 43.67 4.87  ");
		elem.setAttribute("Fortest", "Value");
		try
		{ complex.getFromNode(elem); }
		catch (DOMNodeException e)
		{ catched = true; }
		
		assertTrue("No DOMNodeException", catched);
	}
	
	@Test
	public void testCreateNode()
	{
		prepareDocument();
		complex.set(64.8, 24.4);
		Node node = complex.createNode(document);
		Complex newComplex = new Complex();
		try
		{ newComplex.getFromNode(node); }
		catch(DOMNodeException e)
		{  fail("Exception was thrown"); }
		
		assertEquals(newComplex, complex);
	}
}
