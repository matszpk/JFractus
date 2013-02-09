/**
 * DOMDocumentHandler.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public interface DOMDocumentHandler
{
	public Document createDocument(DOMImplementation impl);
	public void readFromFile(File file) throws DOMNodeException,
			DOMDocumentParseException, IOException;
	public void readFromStream(InputStream is) throws DOMNodeException,
			DOMDocumentParseException, IOException;
	public void writeToFile(File file) throws DOMDocumentGenerateException,
			IOException;
	public void writeToStream(OutputStream os) throws DOMDocumentGenerateException,
			IOException;
}
