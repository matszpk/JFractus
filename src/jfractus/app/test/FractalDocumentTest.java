/**
 * FractalDocumentTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import jfractus.app.FractalDocument;
import jfractus.app.FractalFrame;
import jfractus.app.FractalKeyFrame;
import jfractus.app.FractalAnimation;
//import jfractus.app.FunctionsLoader;

import jfractus.math.Matrix3D;
import jfractus.math.Vector3D;

import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMDocumentException;
import jfractus.functions.fractals.*;
import jfractus.functions.planes.*;

public class FractalDocumentTest
{
	private FractalDocument fractalDocument;
	
	private Document document;
	
	public FractalDocumentTest()
	{
	}
	
	private void prepareDocument()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument(null, "fractal", null); 
		}
		catch(ParserConfigurationException e)
		{ fail("Exception was throw"); }
	}

	private void generateFractalDocumentWithFrame()
	{
		fractalDocument = new FractalDocument();
		/* sets fractal document properties and content */
		fractalDocument.setFractalFormula(new Mandelbrot());
		fractalDocument.setInnerFunction(new jfractus.functions.inners.Default());
		fractalDocument.setOuterFunction(new jfractus.functions.outers.Default());
		fractalDocument.setPlaneTransform(new NoTransform());
		
		fractalDocument.setColorShift(0.6f);
		fractalDocument.setColorScale(1.6f);
		/* gradient name */
		fractalDocument.setGradientLocator("Gray1.grad");
		/* fractal frame */
		fractalDocument.setFractalFrame(new FractalFrame(new Matrix3D(
				new Vector3D(0.64, 2.44, -4.67),
				new Vector3D(-2.88, 5.31, 4.67),
				new Vector3D(0.0, 0.0, 1.0))));
	}
	
	private void generateFractalDocumentWithAnimation()
	{
		fractalDocument = new FractalDocument();
		/* sets fractal document properties and content */
		fractalDocument.setFractalFormula(new Mandelbrot());
		fractalDocument.setInnerFunction(new jfractus.functions.inners.Default());
		fractalDocument.setOuterFunction(new jfractus.functions.outers.Default());
		fractalDocument.setPlaneTransform(new NoTransform());
		
		fractalDocument.setColorShift(0.6f);
		fractalDocument.setColorScale(1.6f);
		/* gradient name */
		fractalDocument.setGradientLocator("Gray1.grad");
		/* fractal frame */
		FractalAnimation anim = new FractalAnimation(30.0f);
		anim.addKeyFrame(new FractalKeyFrame(0.69f, new Matrix3D(
				new Vector3D(0.64, 2.44, -4.67),
				new Vector3D(-2.883483298325, 5.31, 4.67),
				new Vector3D(0.0, 0.0, 1.0))));
		anim.addKeyFrame(new FractalKeyFrame(0.39f, new Matrix3D(
				new Vector3D(3.08, -6.38, -2.67),
				new Vector3D(4.63, 8.32, -14.67),
				new Vector3D(0.0, 0.0, 1.0))));
		anim.addKeyFrame(new FractalKeyFrame(0.91f, new Matrix3D(
				new Vector3D(2.17, -15.7, -4.67),
				new Vector3D(1.63, 4.31, -14.68),
				new Vector3D(0.0, 0.0, 1.0))));
		fractalDocument.setFractalAnimation(anim);
	}
	
	public boolean equalsFractalDocument(FractalDocument doc1, FractalDocument doc2)
	{
		if (!Utilities.equalsFunctions
				(doc1.getFractalFormula(), doc2.getFractalFormula()))
			return false;
		if (!Utilities.equalsFunctions
				(doc1.getInnerFunction(), doc2.getInnerFunction()))
			return false;
		if (!Utilities.equalsFunctions
				(doc1.getOuterFunction(), doc2.getOuterFunction()))
			return false;
		if (!Utilities.equalsFunctions
				(doc1.getPlaneTransform(), doc2.getPlaneTransform()))
			return false;
		if (((doc1.getOutputFilter() != null) != (doc2.getOutputFilter() != null)))
			return false;
		if (doc1.getOutputFilter() != null &&
				Utilities.equalsFunctions(doc1.getOutputFilter(), doc2.getOutputFilter()))
			return false;
		
		if (doc1.getGradientLocator() != null)
		{
			if (doc2.getGradientLocator() == null ||
					!doc1.getGradientLocator().equals(doc2.getGradientLocator()))
				return false;
		}
		else
		{
			if (doc2.getGradient() == null ||
					!doc1.getGradient().equals(doc2.getGradient()))
				return false;
		}
		
		if (doc1.getDocumentType() == FractalDocument.Type.FRAME)
		{
			if (doc2.getDocumentType() != FractalDocument.Type.FRAME ||
					!doc1.getFractalFrame().equals(doc2.getFractalFrame()))
				return false;
		}
		else
		{
			if (doc2.getDocumentType() != FractalDocument.Type.ANIMATION ||
					!doc1.getFractalAnimation().equals(doc2.getFractalAnimation()))
				return false;
		}
				
		return true;
	}
	
	@Before
	public void setUp() throws Exception
	{
		fractalDocument = new FractalDocument();
	}

	@Test
	public void testIsValidNode()
	{
		prepareDocument();
		assertTrue(fractalDocument.isValidNode(document.getDocumentElement()));
	}

	@Test
	public void testGetFromNodeAndCreateNode()
	{
		prepareDocument();
		generateFractalDocumentWithFrame();
		
		Node elem = fractalDocument.createNode(document);
		
		FractalDocument newFracDocument = new FractalDocument();
		try
		{ newFracDocument.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertTrue(equalsFractalDocument(fractalDocument, newFracDocument));
		
		generateFractalDocumentWithAnimation();
		
		elem = fractalDocument.createNode(document);
		newFracDocument = new FractalDocument();
		try
		{ newFracDocument.getFromNode(elem); }
		catch (DOMNodeException e)
		{ fail("Exception was thrown"); }
		
		assertTrue(equalsFractalDocument(fractalDocument, newFracDocument));
	}

	@Test
	public void testXMLIOStream()
	{
		/* Fractal document with single frame */
		generateFractalDocumentWithFrame();
		
		ByteArrayOutputStream bOStream = new ByteArrayOutputStream();
		try
		{ fractalDocument.writeToStream(bOStream); }
		catch(DOMDocumentException e)
		{ fail("DOMDocument exception was thrown"); }
		catch(IOException e)
		{ fail("IO exception was thrown"); }
		
		/*try
		{ System.out.println(bOStream.toString("UTF-8")); }
		catch (UnsupportedEncodingException e)
		{ }*/
		
		ByteArrayInputStream bIStream = new ByteArrayInputStream(bOStream.toByteArray());
		
		FractalDocument newFracDocument = new FractalDocument();
		try
		{ newFracDocument.readFromStream(bIStream); }
		catch(DOMDocumentException e)
		{ fail("DOMDocument exception was thrown"); }
		catch(DOMNodeException e)
		{ fail("DOMNode exception was thrown"); }
		catch(IOException e)
		{ fail("IO exception was thrown"); }
		
		assertTrue(equalsFractalDocument(fractalDocument, newFracDocument));
		
		/* Fractal document with animation */
		generateFractalDocumentWithAnimation();
		
		bOStream = new ByteArrayOutputStream();
		try
		{ fractalDocument.writeToStream(bOStream); }
		catch(DOMDocumentException e)
		{ fail("DOMDocument exception was thrown"); }
		catch(IOException e)
		{ fail("IO exception was thrown"); }
		
		/*try
		{ System.out.println(bOStream.toString("UTF-8")); }
		catch (UnsupportedEncodingException e)
		{ }*/
		
		bIStream = new ByteArrayInputStream(bOStream.toByteArray());
		
		newFracDocument = new FractalDocument();
		try
		{ newFracDocument.readFromStream(bIStream); }
		catch(DOMDocumentException e)
		{ fail("DOMDocument exception was thrown"); }
		catch(DOMNodeException e)
		{ fail("DOMNode exception was thrown"); }
		catch(IOException e)
		{ fail("IO exception was thrown"); }
		
		assertTrue(equalsFractalDocument(fractalDocument, newFracDocument));
		//fail("Not yet implemented");
	}

	@Test
	public void testSetInterface()
	{
		generateFractalDocumentWithFrame();
		
		fractalDocument.setFunction(new Mandelbrot());
		fractalDocument.setFunction(new jfractus.functions.inners.Default());
		fractalDocument.setFunction(new jfractus.functions.outers.Default());
		fractalDocument.setFunction(new NoTransform());
	}

}
