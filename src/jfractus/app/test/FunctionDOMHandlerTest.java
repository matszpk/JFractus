/**
 * FunctionDOMHandlerTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import jfractus.api.UnknownParameterException;
import jfractus.app.FunctionDOMHandler;
import jfractus.dom.DOMNodeException;

import jfractus.math.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class FunctionDOMHandlerTest
{
	private TestFractalFormula testFractalFormula;
	private FunctionDOMHandler ifaceDOMHandler;
	
	private Document document;
	
	private void prepareDocument(String name)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, name, null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
		
	}
	
	private Document readDOMDocumentFromStream(InputStream is)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try
		{
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		doc = builder.parse(is);
		}
		catch (ParserConfigurationException e)
		{ fail("exception"); }
		catch (SAXException e)
		{ fail("exception"); }
		catch(IOException e)
		{ fail("exception"); }
		
		return doc;
	}
	
	private void writeDOMDocumentToStream(Document doc, OutputStream os)
	{
		StreamResult result = new StreamResult(os);
		DOMSource domSource = new DOMSource(doc);
		try
		{
    		TransformerFactory tFactory = TransformerFactory.newInstance();
        	Transformer transformer = tFactory.newTransformer();
        	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        	transformer.setOutputProperty
        			("{http://xml.apache.org/xslt}indent-amount", "2");
        	transformer.transform(domSource, result);
		}
		catch (TransformerConfigurationException e)
		{ fail("exception"); }
		catch(TransformerException e)
		{ fail("exception"); }
	}
	
	private void printDOMDocument(Document doc)
	{
		writeDOMDocumentToStream(doc, System.out);
	}
	
	public FunctionDOMHandlerTest()
	{
	}
	
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testCreateNodeAndGetFromNode()
	{
		prepareDocument("fractalFormula");
		testFractalFormula = new TestFractalFormula();
		/* sets values */
		try
		{
			testFractalFormula.setValue("maxIterations", 1000);
			testFractalFormula.setValue("bailout", 64.4);
			
			testFractalFormula.setValue("booleanParam", true);
			testFractalFormula.setValue("charParam", '<');
			testFractalFormula.setValue("byteParam", (byte)-46);
			testFractalFormula.setValue("shortParam", (short)1346);
			testFractalFormula.setValue("intParam", (int)48998489);
			testFractalFormula.setValue("longParam", (long)3040511L);
			testFractalFormula.setValue("floatParam", -5.6f);
			testFractalFormula.setValue("doubleParam", -43.66);
			testFractalFormula.setValue("complexParam", new Complex(-6.42, 24.4));
			testFractalFormula.setValue("vector2DParam", new Vector2D(6.75, 62.7));
			testFractalFormula.setValue("vector3DParam",
					new Vector3D(6.75, 62.7, -24.1));
			testFractalFormula.setValue("matrix3DParam", new Matrix3D
					(new Vector3D(-6.64, 2.55, -5.36),
					 new Vector3D(-3.6, 0.75, 3.66),
					 new Vector3D(0.89, 34.5, -4.31)));
			testFractalFormula.setValue("enum1Param", TestFractalFormula.MyEnum.QUADRATIC);
			testFractalFormula.setValue("stringParam", "this is simple text");
			
			final boolean[] booleanArrayVal = 
				{true, false, true, true, false, true};
			testFractalFormula.setValue("booleanArray", booleanArrayVal);
			
			final char[] charArrayVal =
				{ 't', 'h', 'i', 's', ' ', 'i', 's', ' ', 't', 'e', 's', 't' };
			testFractalFormula.setValue("charArray", charArrayVal);
			
			final byte[] byteArrayVal = 
				{ (byte)-44, (byte)35, (byte)121, (byte)-101, (byte)76 };
			testFractalFormula.setValue("byteArray", byteArrayVal);
			
			final short[] shortArrayVal = 
				{ (short)-6578, (short)2465, (short)54, (short)-31577, (short)4367 };
			testFractalFormula.setValue("shortArray", shortArrayVal);
			
			final int[] intArrayVal = 
				{ -5646, 33, 67882243, 544354, 443 };
			testFractalFormula.setValue("intArray", intArrayVal);
			
			final long[] longArrayVal = 
				{ -53454L, 354938L, -4334490L, 9434555L };
			testFractalFormula.setValue("longArray", longArrayVal);
			
			final float[] floatArrayVal = 
				{ 5.46f, 0.7f, 324.4f, 3.64f, -3.66f, -0.44f };
			testFractalFormula.setValue("floatArray", floatArrayVal);
			
			final double[] doubleArrayVal = 
				{ 6.654, 0.644, -3.23, -6.77, -54.2 };
			testFractalFormula.setValue("doubleArray", doubleArrayVal);
			
			final Boolean[] booleanArrayVal2 = 
				{true, false, true, true, false, true};
			testFractalFormula.setValue("BooleanArray", booleanArrayVal2);
			
			final Character[] charArrayVal2 =
				{ 'N', 'o', 'r', 'm', 'a', 'l', ' ', 'm', 'o', 'd', 'e' };
			testFractalFormula.setValue("CharacterArray", charArrayVal2);
			
			final Byte[] byteArrayVal2 = 
				{ (byte)-44, (byte)35, (byte)121, (byte)-101, (byte)76 };
			testFractalFormula.setValue("ByteArray", byteArrayVal2);
			
			final Short[] shortArrayVal2 = 
				{ (short)-6578, (short)2465, (short)54, (short)-31577, (short)4367 };
			testFractalFormula.setValue("ShortArray", shortArrayVal2);
			
			final Integer[] integerArrayVal2 = 
				{ -5646, 33, 67882243, 544354, 443 };
			testFractalFormula.setValue("IntegerArray", integerArrayVal2);
			
			final Long[] longArrayVal2 = 
				{ -53454L, 354938L, -4334490L, 9434555L };
			testFractalFormula.setValue("LongArray", longArrayVal2);
			
			final Float[] floatArrayVal2 = 
				{ -5.46f, 0.7f, 324.4f, 3.64f, -3.66f, -0.44f };
			testFractalFormula.setValue("FloatArray", floatArrayVal2);
			
			final Double[] doubleArrayVal2 = 
				{ 6.654, 0.644, -3.23, -6.77, -54.2 };
			testFractalFormula.setValue("DoubleArray", doubleArrayVal2);
			
			final TestFractalFormula.MyEnum[] enumArrayVal =
			{ TestFractalFormula.MyEnum.BEZIER,
			  TestFractalFormula.MyEnum.LINEAR,
			  TestFractalFormula.MyEnum.CUBIC, };
			testFractalFormula.setValue("enumArray", enumArrayVal);
			
			final Complex[] complexArrayVal = {
					new Complex(0.7, -14.5), new Complex(-6.46, -32.6),
					new Complex(-54.7, -0.64), new Complex(33.6, 7.21)
			};
			testFractalFormula.setValue("complexArray", complexArrayVal);
			
			final String[] stringArrayVal = {
					"one", "two", "three", "four"
			};
			testFractalFormula.setValue("stringArray", stringArrayVal);
			
			final int[][] intArrayArrayVal = {
					{ 133, 55, 22, 224, 133 },
					{ 434, 43, -54, 22 },
					{ -43, -754, -557, 3436, 244, 445, 2233 },
					{ -43, 33, 44 }
			};
			testFractalFormula.setValue("intArrayArray", intArrayArrayVal);
		}
		catch (UnknownParameterException e)
		{ fail("Exception was throw"); }
		
		ifaceDOMHandler = new FunctionDOMHandler(testFractalFormula);
		Element rootElem = document.getDocumentElement();
		Node node = ifaceDOMHandler.createNode(document);
		rootElem.appendChild(node);
		
		//printDOMDocument(document);
		//InterfaceDOMHandler newIfaceDOMHandler;
		FunctionDOMHandler newIfaceDOMHandler = new FunctionDOMHandler
				(TestFractalFormula.class);
		
		assertTrue(newIfaceDOMHandler.isValidNode(node));
		
		try
		{ newIfaceDOMHandler.getFromNode(node); }
		catch(DOMNodeException e)
		{ //e.printStackTrace();
			fail("Exception was thrown"); }
		
		assertTrue(Utilities.equalsFunctions
				(newIfaceDOMHandler.getFunction(), testFractalFormula));
		
		/* from byte stream */
		{
			ByteArrayOutputStream bOStream = new ByteArrayOutputStream();
			writeDOMDocumentToStream(document, bOStream);
			newIfaceDOMHandler = new FunctionDOMHandler(TestFractalFormula.class);
			
			/*try
			{ System.out.println(bOStream.toString("UTF-8")); }
			catch(UnsupportedEncodingException e)
			{ fail("exception"); }*/
			
			ByteArrayInputStream bIStream = new ByteArrayInputStream(bOStream.toByteArray());
			
			Document doc = readDOMDocumentFromStream(bIStream);
			Element rootElem2 = doc.getDocumentElement();
			int elemIndex = 0;
			NodeList nodes = rootElem2.getChildNodes();
			
			for(int i = 0; i < nodes.getLength(); i++)
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
				{
					elemIndex = i;
					break;
				}
				
			Node goodElem = rootElem2.getChildNodes().item(elemIndex);
			
			try
    		{ newIfaceDOMHandler.getFromNode(goodElem); }
    		catch(DOMNodeException e)
    		{ fail("Exception was throw"); }
    		
    		assertTrue(Utilities.equalsFunctions
    					(newIfaceDOMHandler.getFunction(), testFractalFormula));
		}
	}
}
