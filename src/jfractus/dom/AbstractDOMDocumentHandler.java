/**
 * AbstractDOMDocumentHandler.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

import java.io.*;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public abstract class AbstractDOMDocumentHandler implements DOMDocumentHandler,
		DOMNodeHandler
{
	public abstract boolean isValidNode(Node node);
	public abstract void getFromNode(Node node) throws DOMNodeException;
	public abstract Node createNode(Document doc);
	
	public abstract Document createDocument(DOMImplementation impl);

	@Override
	public void readFromFile(File file) throws DOMNodeException,
	        DOMDocumentParseException, IOException
	{
		FileInputStream is = new FileInputStream(file);
		readFromStream(is); 
	}

	@Override
	public void readFromStream(InputStream is) throws DOMNodeException,
	        DOMDocumentParseException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try
		{
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		doc = builder.parse(is);
		}
		catch (ParserConfigurationException e)
		{
			throw new DOMDocumentParseException
				("Parser Configuration exception happens");
		}
		catch (SAXException e)
		{
			throw new DOMDocumentParseException("SAX Parser exception happens");
		}
		Element elem = doc.getDocumentElement();
		if (!isValidNode(elem))
			throw new DOMNodeBadStructureException("Is not good document root element");
		getFromNode(doc.getDocumentElement());
	}

	@Override
	public void writeToFile(File file) throws DOMDocumentGenerateException,
	        IOException
	{
		FileOutputStream os = new FileOutputStream(file);
		writeToStream(os);
	}

	@Override
	public void writeToStream(OutputStream os)
	        throws DOMDocumentGenerateException, IOException
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try
		{ builder = docFactory.newDocumentBuilder(); }
		catch (ParserConfigurationException e)
		{
			throw new DOMDocumentGenerateException
					("Parser Configuration exception happens");
		}
		DOMImplementation domImpl = builder.getDOMImplementation();
		Document doc = createDocument(domImpl);
		
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
		{
			throw new DOMDocumentGenerateException
					("Transformer Configuration exception happens");
		}
		catch(TransformerException e)
		{
			throw new DOMDocumentGenerateException("Transformer exception happens");
		}
	}
}
