/**
 * FunctionDOMHandler.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.lang.reflect.*;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jfractus.api.*;
import jfractus.dom.DOMNodeBadStructureException;
import jfractus.dom.DOMNodeBadValueException;
import jfractus.dom.DOMNodeException;
import jfractus.dom.DOMNodeHandler;
import jfractus.dom.DOMNodeParseException;

public class FunctionDOMHandler implements DOMNodeHandler
{
	private Function function;
	private Class<?> functionTypeClass;
	private String functionType;
	private String functionName;
	
	public FunctionDOMHandler(Class<?> functionCls)
	{
		setFunctionTypeFromClass(functionCls);
	}
	public FunctionDOMHandler(Function function)
	{
		this.function = function;
		setFunctionTypeFromClass(function.getClass());
		functionName = function.getClass().getName(); 
	}
	
	protected void setFunctionTypeFromClass(Class<?> functionCls)
	{
		functionType = "Unknown";
		if (FractalFormula.class.isAssignableFrom(functionCls))
		{
			functionType = "fractalFormula";
			functionTypeClass = FractalFormula.class;
		}
		else if (InnerFunction.class.isAssignableFrom(functionCls))
		{
			functionType = "innerFunction";
			functionTypeClass = InnerFunction.class;
		}
		else if (OuterFunction.class.isAssignableFrom(functionCls))
		{
			functionType = "outerFunction";
			functionTypeClass = OuterFunction.class;
		}
		else if (OutputFilter.class.isAssignableFrom(functionCls))
		{
			functionType = "outputFilter";
			functionTypeClass = OutputFilter.class;
		}
		else if (PlaneTransform.class.isAssignableFrom(functionCls))
		{
			functionType = "planeTransform";
			functionTypeClass = PlaneTransform.class;
		}
	}
	
	public Function getFunction()
	{
		return function;
	}
	
	private Node createNodeFromArray(Document doc, Object ob)
	{
		Element elem = doc.createElement("array");
		
		Class<?> cls = ob.getClass();
		Class<?> elemCls = cls.getComponentType();
		
		if (DOMNodeHandler.class.isAssignableFrom(elemCls))
		{
			DOMNodeHandler[] obArray = (DOMNodeHandler[])ob;
			for(int i = 0; i < obArray.length; i++)
			{
				Node obElem = obArray[i].createNode(doc);
				elem.appendChild(obElem);
			}
		}
		if (boolean.class.equals(elemCls))
		{
			boolean[] booleanArray = (boolean[])ob;
			StringBuilder sB = new StringBuilder();
			for (int i = 0; i < booleanArray.length; i++)
			{
				sB.append(booleanArray[i] ? "true" : "false");
				if (i+1 < booleanArray.length)
					sB.append(" ");
			}
			
			elem.setTextContent(sB.toString());
		}
		else if (Boolean.class.equals(elemCls))
		{
			Boolean[] booleanArray = (Boolean[])ob;
			StringBuilder sB = new StringBuilder();
			for (int i = 0; i < booleanArray.length; i++)
			{
				sB.append(booleanArray[i].booleanValue() ? "true" : "false");
    			if (i+1 < booleanArray.length)
    				sB.append(" ");
			}
			
			elem.setTextContent(sB.toString());
		}
		else if (char.class.equals(elemCls))
			elem.setTextContent(new String((char[])ob));
		else if (Character.class.equals(elemCls))
		{
			StringBuilder sB = new StringBuilder();
			Character[] charArray = (Character[])ob;
			for (int i = 0; i < charArray.length; i++)
				sB.append(charArray[i].charValue());
			elem.setTextContent(sB.toString());
		}
		else if(byte.class.equals(elemCls) ||
				Byte.class.equals(elemCls) ||
				short.class.equals(elemCls) ||
				Short.class.equals(elemCls) ||
				int.class.equals(elemCls) ||
				Integer.class.equals(elemCls) ||
				long.class.equals(elemCls) ||
				Long.class.equals(elemCls))
		{
			Formatter formatter = new Formatter(Locale.ENGLISH);
			for (int i = 0; i < Array.getLength(ob); i++)
			{
				if (i + 1 < Array.getLength(ob))
					formatter.format("%d ", Array.get(ob, i));
				else
					formatter.format("%d", Array.get(ob, i));
			}
			
			elem.setTextContent(formatter.toString());
		}
		else if (float.class.equals(elemCls) ||
				 Float.class.equals(elemCls))
		{
			Formatter formatter = new Formatter(Locale.ENGLISH);
			for (int i = 0; i < Array.getLength(ob); i++)
			{
				if (i + 1 < Array.getLength(ob))
					formatter.format("%g ", Array.get(ob, i));
				else
					formatter.format("%g", Array.get(ob, i));
			}
			
			elem.setTextContent(formatter.toString());
		}
		else if (double.class.equals(elemCls) ||
				 Double.class.equals(elemCls))
		{
			Formatter formatter = new Formatter(Locale.ENGLISH);
			for (int i = 0; i < Array.getLength(ob); i++)
			{
				if (i + 1 < Array.getLength(ob))
					formatter.format("%15.16g ", Array.get(ob, i));
				else
					formatter.format("%15.16g", Array.get(ob, i));
			}
			
			elem.setTextContent(formatter.toString());
		}
		else if (String.class.equals(elemCls))
		{
			Object[] obArray = (Object[])ob;
			for(int i = 0; i < obArray.length; i++)
			{
				Element stringElem = doc.createElement("string");
				stringElem.setTextContent(((CharSequence)(obArray[i])).toString());
				elem.appendChild(stringElem);
			}
		}
		else if (elemCls.isEnum())
		{
			Enum<?>[] enumArray = (Enum<?>[])ob;
			StringBuilder sB = new StringBuilder();
			for(int i = 0; i < enumArray.length; i++)
			{
				sB.append((enumArray[i]).name());
				if (i + 1 < enumArray.length)
					sB.append(" ");
			}
			
			elem.setTextContent(sB.toString());
		}
		else if (elemCls.isArray())
		{
			Object[] obArray = (Object[])ob;
			for(int i = 0; i < obArray.length; i++)
			{
				Node obElem = createNodeFromArray(doc, obArray[i]);
				elem.appendChild(obElem);
			}
		}
		return elem;
	}
	
	public Node createNode(Document doc)
	{
		Element elem = doc.createElement(functionType);
		elem.setAttribute("name", functionName);
		
		for (int i = 0; i < function.getParametersCount(); i++)
		{
			Element paramElem = doc.createElement("parameter");
			Node valueNode = null;
			String paramName = function.getParameterName(i);
			paramElem.setAttribute("name", paramName);
			
			Object ob = function.getValue(i);
			Class<?> cls = function.getType(i);
			/* if with DOMNodeHandler interface */
			if (DOMNodeHandler.class.isAssignableFrom(cls))
			{
				DOMNodeHandler d = (DOMNodeHandler)ob;
				valueNode = d.createNode(doc);
			}
			else if (String.class.equals(cls))
				valueNode = doc.createTextNode(ob.toString());
			else if (char.class.equals(cls) ||
					Character.class.equals(cls))
			{
				StringBuilder sB = new StringBuilder();
				sB.append((Character)ob);
				valueNode = doc.createTextNode(sB.toString());
			}
			/* if float/double */
			else if (Float.class.equals(cls) ||
					float.class.equals(cls))
				valueNode = doc.createTextNode
					(String.format(Locale.ENGLISH,"%g", ob));
			else if (Double.class.equals(cls) ||
					double.class.equals(cls))
				valueNode = doc.createTextNode
					(String.format(Locale.ENGLISH,"%15.16g", ob));
			/* if integral numbers */
			else if (Byte.class.equals(cls) ||
					byte.class.equals(cls) ||
					Short.class.equals(cls) ||
					short.class.equals(cls) ||
					Integer.class.equals(cls) ||
					int.class.equals(cls) ||
					Long.class.equals(cls) ||
					long.class.equals(cls))
				valueNode = doc.createTextNode
					(String.format(Locale.ENGLISH,"%d", ob));
			else if (Boolean.class.equals(cls))
				valueNode = doc.createTextNode
						(((Boolean)ob).booleanValue() ? "true" : "false");
			else if (boolean.class.equals(cls))
				valueNode = doc.createTextNode
						(((Boolean)ob).booleanValue() ? "true" : "false");
			/* if enumerate type */
			else if (cls.isEnum())
				valueNode = doc.createTextNode(((Enum<?>)ob).name());
			else if (cls.isArray())
			{
				Node arrayElem = createNodeFromArray(doc, ob);
				paramElem.appendChild(arrayElem);
			}
			
			if (valueNode != null)
				paramElem.appendChild(valueNode);
			
			elem.appendChild(paramElem);
		}
    	
    	return elem;
	}
	
	private Object getArrayFromNode(Class<?> cls, Node node) throws DOMNodeException
	{
		Class<?> elemCls = cls.getComponentType();
		
		Object outOb = null;
		ArrayList<Object> arrayList = new ArrayList<Object>();
		
		if (DOMNodeHandler.class.isAssignableFrom(elemCls))
		{
			NodeList valuesNodes = node.getChildNodes();
			if (valuesNodes != null)
			{
				try
				{
        			for (int j = 0; j < valuesNodes.getLength(); j++)
        			{
        				Node arrayChild = valuesNodes.item(j);
        				Object newOb = (elemCls.getConstructor().newInstance());
        				
        				if (((DOMNodeHandler)newOb).isValidNode(arrayChild))
        					((DOMNodeHandler)newOb).getFromNode(arrayChild);
        				else if (arrayChild.getNodeType() == Node.TEXT_NODE ||
            					arrayChild.getNodeType() == Node.COMMENT_NODE)
            				continue;
        				else throw new DOMNodeBadStructureException("Unexpected element");
        				
        				arrayList.add(newOb);
        			}
				}
				catch (InstantiationException e)
                { }
                catch (IllegalAccessException e)
                { }
                catch (NoSuchMethodException e)
                { }
                catch (InvocationTargetException e)
                { }
			}
		}
		else if (boolean.class.equals(elemCls) ||
			Boolean.class.equals(elemCls))
		{
			StringTokenizer tokenizer = new StringTokenizer(node.getTextContent());
			
			while(tokenizer.hasMoreTokens())
			{
				boolean value = false;
				String token = tokenizer.nextToken();
				if (token.equals("true"))
					value = true;
				else if (token.equals("false"))
					value = false;
				else
					throw new DOMNodeBadValueException("Bad value of element in array");
				
				arrayList.add(value);
			}
		}
		else if (char.class.equals(elemCls))
		{
			String s = node.getTextContent();
			for (int i = 0; i < s.length(); i++)
				arrayList.add(s.charAt(i));
		}
		else if (Character.class.equals(elemCls))
		{
			String s = node.getTextContent();
			for (int i = 0; i < s.length(); i++)
				arrayList.add(new Character(s.charAt(i)));
		}
		else if (byte.class.equals(elemCls) ||
				 Byte.class.equals(elemCls) ||
				 short.class.equals(elemCls) ||
				 Short.class.equals(elemCls) ||
				 int.class.equals(elemCls) ||
				 Integer.class.equals(elemCls) ||
				 long.class.equals(elemCls) ||
				 Long.class.equals(elemCls) ||
				 float.class.equals(elemCls) ||
				 Float.class.equals(elemCls) ||
				 double.class.equals(elemCls) ||
				 Double.class.equals(elemCls))
		{
			Scanner scanner = new Scanner(node.getTextContent());
			scanner.useLocale(Locale.ENGLISH);
			try
			{
				Object ob = null;
				if (byte.class.equals(elemCls) ||
					Byte.class.equals(elemCls))
					while(scanner.hasNext())
        			{
        				ob = (byte)scanner.nextByte();
						arrayList.add(ob);
        			}
				else if (short.class.equals(elemCls) ||
						 Short.class.equals(elemCls))
        			while(scanner.hasNext())
        			{
        				ob = (short)scanner.nextShort();
        				arrayList.add(ob);
        			}
				else if (int.class.equals(elemCls) ||
						 Integer.class.equals(elemCls))
        			while(scanner.hasNext())
        			{
        				ob = (int)scanner.nextInt();
						arrayList.add(ob);
        			}
				else if (long.class.equals(elemCls) ||
						 Long.class.equals(elemCls))
        			while(scanner.hasNext())
        			{
        				ob = (long)scanner.nextLong();
						arrayList.add(ob);
        			}
				else if (float.class.equals(elemCls) ||
						 Float.class.equals(elemCls))
        			while(scanner.hasNext())
        			{
        				ob = (float)scanner.nextFloat();
						arrayList.add(ob);
        			}
				else if (double.class.equals(elemCls) ||
						 Double.class.equals(elemCls))
        			while(scanner.hasNext())
        			{
        				ob = (double)scanner.nextDouble();
        				arrayList.add(ob);
        			}
			}
			catch(InputMismatchException e)
			{ throw new DOMNodeParseException("Parse exception at parameter value"); }
			catch(NoSuchElementException e)
			{ throw new DOMNodeParseException("Parse exception at parameter value"); }
		}
		else if (String.class.equals(elemCls))
		{
			NodeList valuesNodes = node.getChildNodes();
			if (valuesNodes != null)
			{
    			for (int j = 0; j < valuesNodes.getLength(); j++)
    			{
    				Node arrayChild = valuesNodes.item(j);
    				if (arrayChild.getNodeType() == Node.TEXT_NODE ||
    					arrayChild.getNodeType() == Node.COMMENT_NODE)
    					continue;
    				
    				if (arrayChild.getNodeType() == Node.ELEMENT_NODE &&
    					arrayChild.getNodeName().equals("string"))
    					arrayList.add(arrayChild.getTextContent());
    				else throw new DOMNodeBadStructureException
    							("Is not string element in array");
    			}
			}
		}
		else if (elemCls.isEnum())
		{
			Enum<?>[] enumVals = (Enum<?>[])elemCls.getEnumConstants();
			StringTokenizer tokenizer = new StringTokenizer(node.getTextContent());
			
			while(tokenizer.hasMoreTokens())
			{
				String enumText = tokenizer.nextToken();
				
				int out = -1;
				for (int j = 0; j < enumVals.length; j++)
					if (enumText.equals(enumVals[j].name()))
					{
						out = j;
						break;
					}
				
				if (out == -1)
					throw new DOMNodeBadValueException("Bad value of enum parameter");    				
				arrayList.add(enumVals[out]);
			}
		}
		else if(elemCls.isArray())
		{
			NodeList arrayNodes = node.getChildNodes();
			if (arrayNodes != null)
			{
    			for(int j = 0; j < arrayNodes.getLength(); j++)
    			{
    				Node arrayChild = arrayNodes.item(j);
    				if (arrayChild.getNodeType() == Node.TEXT_NODE ||
        					arrayChild.getNodeType() == Node.COMMENT_NODE)
        				continue;
    				else if (arrayChild.getNodeType() == Node.ELEMENT_NODE &&
    						arrayChild.getNodeName().equals("array"))
    					arrayList.add(getArrayFromNode(elemCls, arrayChild));
    				else throw new DOMNodeBadValueException("Bad value of element in array");
    			}
			}
		}
		
		outOb = Array.newInstance(elemCls, arrayList.size());
    	int index = 0;
    	for (Iterator<Object> iter = arrayList.iterator(); iter.hasNext(); index++)
    		Array.set(outOb, index, iter.next());
		
		return outOb;
	}

	public void getFromNode(Node node) throws DOMNodeException
	{
		Element elem = (Element)node;
		functionName = elem.getAttribute("name");
		Class<?> ifaceCls = null;
		try
		{ ifaceCls = FunctionsLoaderFactory.loader.forName(functionName); }
		catch(ClassNotFoundException e)
		{ throw new DOMNodeBadValueException("Unknown interface name"); }
		
		if (!functionTypeClass.isAssignableFrom(ifaceCls))
			throw new DOMNodeBadValueException("Interface type mismatch");
		
		try
        { function = (Function)(ifaceCls.getConstructor().newInstance()); }
        catch (InstantiationException e)
        { }
        catch (IllegalAccessException e)
        { }
        catch (NoSuchMethodException e)
        { }
        catch (InvocationTargetException e)
        { }
		
		NodeList nodeList = elem.getChildNodes();
		if (nodeList == null)
			throw new DOMNodeBadStructureException("No parameters in interface");
		
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node child = nodeList.item(i);
			if (child.getNodeType() == Node.TEXT_NODE ||
					child.getNodeType() == Node.COMMENT_NODE)
				continue;
			
			if (child.getNodeType() != Node.ELEMENT_NODE ||
					!child.getNodeName().equals("parameter"))
				throw new DOMNodeBadStructureException("Is not parameter element");
			
			Element paramElem = (Element)child;
			String paramName = paramElem.getAttribute("name");
			
			Object ob = null;
			Class<?> cls = null;
			cls = function.getType(paramName);
			if (cls == null)
				throw new DOMNodeBadStructureException
						("Unknown parameter in interface");
			
			boolean internalError = false;
			
			try
			{
    			if (DOMNodeHandler.class.isAssignableFrom(cls))
    			{
    				NodeList valuesNodes = paramElem.getChildNodes();
    				if (valuesNodes == null)
    					throw new DOMNodeBadStructureException
    							("No required subelement in parameter element");
    				Node valueNode = null;
    				Object newOb = cls.getConstructor().newInstance();
    				for (int j = 0; j < valuesNodes.getLength(); j++)
    				{
    					Node paramChild = valuesNodes.item(j);
    					
    					if (((DOMNodeHandler)newOb).isValidNode(paramChild))
    					{
    						if (valueNode != null)
    							throw new DOMNodeBadStructureException
    								("Duplicate parameter value");
    						valueNode = paramChild;
    					}
    					else if (paramChild.getNodeType() == Node.TEXT_NODE ||
            					paramChild.getNodeType() == Node.COMMENT_NODE)
            				continue;
    					else throw new DOMNodeBadStructureException("Unexpected element");
    				}
    				ob = newOb;
    				((DOMNodeHandler)ob).getFromNode(valueNode);
    			}
    			else if (String.class.equals(cls))
    				ob = paramElem.getTextContent();
    			/* boolean */
    			else if (Boolean.class.equals(cls) ||
    					boolean.class.equals(cls))
    			{
    				StringTokenizer tokenizer = new StringTokenizer
    						(paramElem.getTextContent());
    				
    				String boolText = null;
    				try
    				{ boolText = tokenizer.nextToken(); }
    				catch(NoSuchElementException e)
    				{ throw new DOMNodeParseException
    							("Parse exception at parameter value"); }
    				
    				if (tokenizer.hasMoreTokens()) 
    					throw new DOMNodeParseException
    							("Parse exception at parameter value");
    				
    				if (Boolean.class.equals(cls))
    				{
        				if (boolText.equals("true"))
        					ob = new Boolean(true);
        				else if(boolText.equals("false"))
        					ob = new Boolean(false);
        				else
        					throw new DOMNodeBadValueException("Bad value of parameter");
    				}
    				else
    				{
    					if (boolText.equals("true"))
        					ob = true;
        				else if(boolText.equals("false"))
        					ob = false;
        				else
        					throw new DOMNodeBadValueException("Bad value of parameter");
    				}
    			}
    			if (char.class.equals(cls))
    			{
    				String s = paramElem.getTextContent();
    				if (s.length() != 1)
    					throw new DOMNodeBadValueException("Must be one character");
    				ob = s.charAt(0);
    			}
    			if (Character.class.equals(cls))
    			{
    				String s = paramElem.getTextContent();
    				if (s.length() != 1)
    					throw new DOMNodeBadValueException("Must be one character");
    				ob = new Character(s.charAt(0));
    			}
    			/* if numericals */
    			else  if (Byte.class.equals(cls) ||
    						byte.class.equals(cls) ||
        					Short.class.equals(cls) ||
        					short.class.equals(cls) ||
        					Integer.class.equals(cls) ||
        					int.class.equals(cls) ||
        					Long.class.equals(cls) ||
        					long.class.equals(cls) ||
        					Float.class.equals(cls) ||
        					float.class.equals(cls) ||
        					Double.class.equals(cls) ||
        					double.class.equals(cls))
    			{
    				Scanner scanner = new Scanner(paramElem.getTextContent());
    				scanner.useLocale(Locale.ENGLISH);
    				
    				try
    				{
        				if (Byte.class.equals(cls))
        					ob = scanner.nextByte();
        				else if (Short.class.equals(cls))
        					ob = scanner.nextShort();
        				else if (Integer.class.equals(cls))
        					ob = scanner.nextInt();
        				else if (Long.class.equals(cls))
        					ob = scanner.nextLong();
        				else if (Float.class.equals(cls))
        					ob = scanner.nextFloat();
        				else if (Double.class.equals(cls))
        					ob = scanner.nextDouble();
        				else if (byte.class.equals(cls))
        					ob = (byte)scanner.nextByte();
        				else if (short.class.equals(cls))
        					ob = (short)scanner.nextShort();
        				else if (int.class.equals(cls))
        					ob = (int)scanner.nextInt();
        				else if (long.class.equals(cls))
        					ob = (long)scanner.nextLong();
        				else if (float.class.equals(cls))
        					ob = (float)scanner.nextFloat();
        				else if (double.class.equals(cls))
        					ob = (double)scanner.nextDouble();
    				
        				if (scanner.hasNext())
        				{ throw new DOMNodeParseException
    						("Parse exception at parameter value"); }
    				}
    				catch(InputMismatchException e)
    				{ throw new DOMNodeParseException
    						("Parse exception at parameter value"); }
    				catch(NoSuchElementException e)
    				{ throw new DOMNodeParseException
    						("Parse exception at parameter value"); }
    			}
    			else if(cls.isEnum())
    			{
    				Enum<?>[] enumVals = (Enum<?>[])cls.getEnumConstants();
    				StringTokenizer tokenizer = new StringTokenizer
    						(paramElem.getTextContent());
    				
    				String enumText = null;
    				try
    				{ enumText = tokenizer.nextToken(); }
    				catch(NoSuchElementException e)
    				{ throw new DOMNodeParseException
    							("Parse exception at parameter value"); }
    				
    				if (tokenizer.hasMoreTokens()) 
    					throw new DOMNodeParseException
    							("Parse exception at parameter value");
    				
    				int out = -1;
    				for (int j = 0; j < enumVals.length; j++)
    					if (enumText.equals(enumVals[j].name()))
    					{
    						out = j;
    						break;
    					}
    				
    				if (out == -1)
    					throw new DOMNodeBadValueException("Bad value of enum parameter");    				
    				ob = enumVals[out];
    			}
    			else if (cls.isArray())
    			{
    				NodeList arrayNodes = paramElem.getChildNodes();
    				if (arrayNodes == null)
    					throw new DOMNodeBadStructureException
    							("No required subelement in parameter element");
    				
    				Node arrayNode = null;
    				for(int j = 0; j < arrayNodes.getLength(); j++)
    				{
    					Node paramChild = arrayNodes.item(j);
    					if (paramChild.getNodeType() == Node.TEXT_NODE ||
            					paramChild.getNodeType() == Node.COMMENT_NODE)
            				continue;
    					else if (paramChild.getNodeType() == Node.ELEMENT_NODE &&
    							paramChild.getNodeName().equals("array"))
    					{
    						if (arrayNode != null)
    							throw new DOMNodeBadStructureException
    								("Duplicate parameter value");
    						arrayNode = paramChild;
    					}
    					else throw new DOMNodeBadStructureException("Unexpected element");
    				}
    				
    				ob = getArrayFromNode(cls, arrayNode);
    			}
			}
            catch (IllegalArgumentException e)
            { internalError = true; }
            catch (SecurityException e)
            { internalError = true; }
            catch (InstantiationException e)
            { internalError = true; }
            catch (IllegalAccessException e)
            { internalError = true; }
            catch (InvocationTargetException e)
            { internalError = true; }
            catch (NoSuchMethodException e)
            { internalError = true; }
            
            if (internalError)
            	throw new DOMNodeBadStructureException("Internal error");
			
			try
			{ function.setValue(paramName, ob); }
			catch(UnknownParameterException e)
			{ }
		}
	}

	public boolean isValidNode(Node node)
	{
		return (node.getNodeType() == Node.ELEMENT_NODE &&
				node.getNodeName() == functionType);
	}

}
