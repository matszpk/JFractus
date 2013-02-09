/**
 * Resources.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.jar.*;

public class Resources
{
	private static final ResourceBundle bundle = 
		ResourceBundle.getBundle("jfractus.resources.texts");
	
	public static String getString(String key)
	{
		return bundle.getString(key);
	}
	
	public static URL getGraphics(String name)
	{
		return Resources.class.getResource("/jfractus/graphics/" + name);
	}
	public static InputStream getGraphicsAsStream(String name)
	{
		return Resources.class.getResourceAsStream("jfractus/graphics/" + name);
	}
	
	public static URL getGradient(String name)
	{
		return Resources.class.getResource("/jfractus/gradients/" + name);
	}
	public static InputStream getGradientAsStream(String name)
	{
		return Resources.class.getResourceAsStream("/jfractus/gradients/" + name);
	}
	
	public static ArrayList<String> listResourceDirectory(String path)
	{
		URL url = Resources.class.getResource(path);
		if (url.getProtocol().equals("file"))
		{
			File dir =  new File(url.getPath().replace('/', File.separatorChar));
			File[] childs = dir.listFiles();
			ArrayList<String> names = new ArrayList<String>();
			for (File child: childs)
				names.add(child.getName());
			return names;
		}
		else
		{
			URLConnection connection = null;
			try
			{
				connection = url.openConnection();
				if (!JarURLConnection.class.isAssignableFrom(connection.getClass()))
					return null;
				
				JarURLConnection jarCon = (JarURLConnection)connection;
				JarFile jarFile = jarCon.getJarFile();
				JarEntry parent = jarCon.getJarEntry();
				
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				
				String dirPath = parent.getName();
				if (dirPath.charAt(dirPath.length()-1) != '/')
					dirPath = dirPath + '/';
				
				ArrayList<String> names = new ArrayList<String>();
				String urlString = url.toString();
				if (urlString.charAt(dirPath.length()-1) != '/')
					urlString = urlString + '/';
				
				while(jarEntries.hasMoreElements())
				{
					JarEntry entry = jarEntries.nextElement();
					
					String entryName = entry.getName();
					if (entryName.startsWith(dirPath))
					{
						int lastSlashIndex = entryName.indexOf('/', dirPath.length());
						/* find only child files/ child dirs (exclude parent) */
						if ((lastSlashIndex == -1 ||
								lastSlashIndex == (entryName.length()-1)) &&
							!entryName.equals(dirPath))
							names.add(entryName.substring(dirPath.length()));
					}
				}
				return names;
			}
			catch(IOException e)
			{ return null; }
		}
	}
	
	public static ArrayList<String> listGradients()
	{
		return listResourceDirectory("/jfractus/gradients/");
	}
}
