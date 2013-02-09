/**
 * DOMNodeHandler.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface DOMNodeHandler
{
	public boolean isValidNode(Node node);
	public void getFromNode(Node node) throws DOMNodeException;
	public Node createNode(Document doc);
}
