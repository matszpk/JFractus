/**
 * FunctionsLoader.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.jar.*;
import java.util.prefs.*;
import javax.swing.event.EventListenerList;

import jfractus.api.FractalFormula;
import jfractus.api.InnerFunction;
import jfractus.api.OuterFunction;
import jfractus.api.OutputFilter;
import jfractus.api.PlaneTransform;

public class FunctionsLoader
{
	private String[] classPaths;
	private ClassLoader classLoader;
	
	private Class<?>[] fractalFormulas;
	private Class<?>[] innerFunctions;
	private Class<?>[] outerFunctions;
	private Class<?>[] planeTransforms;
	private Class<?>[] outputFilters;

	private Preferences prefs;
	
	private EventListenerList listenerList = new EventListenerList(); 
	
	public void setClassPathsFromString(String s)
	{
		int length = s.length();
		ArrayList<String> strings = new ArrayList<String>();
		StringBuilder current = new StringBuilder(); 
		for (int i = 0; i < length; i++)
		{
			char c = s.charAt(i);
			if (c == '\\' && i + 1 < length)
			{
				current.append(s.charAt(i+1));
				i++;
			}
			else if (c == ';')
			{
				strings.add(current.toString());
				current = new StringBuilder();
			}
			else
				current.append(c);
		}
		if (length != 0)
			strings.add(current.toString());
		
		classPaths = strings.toArray(new String[0]);
		updateFunctions();
	}
	
	private void getClassPathsFromPrefs()
	{
		setClassPathsFromString(prefs.get("ClassPaths", ""));
	}
	
	public void putClassPathsToPrefs()
	{
		StringBuilder toOut = new StringBuilder();
		for (int k = 0; k < classPaths.length; k++)
		{
			String s = classPaths[k];
			int length = s.length();
			for (int i = 0; i < length; i++)
			{
				char c = s.charAt(i);
				if (c == ';')
					toOut.append('\\');
				toOut.append(c);
			}
			
			if (k+1 < classPaths.length)
				toOut.append(';');
		}
		
		prefs.put("ClassPaths", toOut.toString());
	}
	
	public FunctionsLoader()
	{
		prefs = Preferences.userNodeForPackage(FunctionsLoader.class);
		getClassPathsFromPrefs();
	}
	
	public void addClassPathsListener(ClassPathsListener l)
	{
		listenerList.add(ClassPathsListener.class, l);
	}
	public void fireClassPathsChange()
	{
		Object[] listeners = listenerList.getListenerList();
		ClassPathsEvent event = new ClassPathsEvent(this);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == ClassPathsListener.class)
    			((ClassPathsListener)(listeners[i+1])).classPathsChanged(event);
	}
	public void removeClassPathsListener(ClassPathsListener l)
	{
		listenerList.remove(ClassPathsListener.class, l);
	}
	
	
	private static boolean isFunction(Class<?> cls)
	{
		return (!cls.isInterface() &&
			!Modifier.isAbstract(cls.getModifiers()) &&
			(FractalFormula.class.isAssignableFrom(cls) ||
			 InnerFunction.class.isAssignableFrom(cls) ||
			 OuterFunction.class.isAssignableFrom(cls) ||
			 PlaneTransform.class.isAssignableFrom(cls) ||
			 OutputFilter.class.isAssignableFrom(cls)));
	}
	
	protected ArrayList<Class<?>> getFunctionsFromDirectory(File dir,
			String packagePath)
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		File[] entries = dir.listFiles();
		
		for(File entry: entries)
		{
			String entryName = entry.getName();
			if (entry.isDirectory())
			{
				if (packagePath.length() != 0)
					classes.addAll(getFunctionsFromDirectory(entry,
						packagePath + "." + entryName));
				else
					classes.addAll(getFunctionsFromDirectory(entry,
						entryName));
			}
			else if (entryName.endsWith(".class"))
			{
				String currentName = null;
				if (packagePath != null && packagePath.length() != 0)
					currentName =packagePath + "." +
						entryName.substring(0, entryName.length()-6);
				else
					currentName = entryName.substring(0, entryName.length()-6);
				
				Class<?> currentClass = null;
				try
				{ currentClass = Class.forName(currentName, true, classLoader); }
				catch(ClassNotFoundException e)
				{ continue; }
				
				//System.out.println("Analized: " + currentName);
				
				if (isFunction(currentClass))
					classes.add(currentClass);
			}
		}
		
		return classes;
	}
	
	protected ArrayList<Class<?>> updateFunctionsFromURL(URL url)
	{
		if (url.getProtocol().equals("file"))
		{
			File dir = new File(url.getPath().replace('/', File.separatorChar));
			return getFunctionsFromDirectory(dir, "");
		}
		else
		{
			URLConnection connection = null;
			try
			{
				connection = url.openConnection();
				if (!JarURLConnection.class.isAssignableFrom(connection.getClass()))
					return null;
				
				JarURLConnection jarConn = (JarURLConnection)connection;
				
				JarFile jarFile = jarConn.getJarFile();
				
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
				
				while(jarEntries.hasMoreElements())
				{
					JarEntry entry = jarEntries.nextElement();
					String entryName = entry.getName();
					if (entryName.endsWith(".class"))
					{
						Class<?> currentClass = null;
						try
						{
							currentClass = Class.forName(entryName.replace('/', '.').
									substring(0, entryName.length()-6),
									true, classLoader);
						}
						catch(ClassNotFoundException e)
						{
							continue; }
						
						if (isFunction(currentClass))
							classes.add(currentClass);
					}
				}
				return classes;
			}
			catch(IOException e)
			{ return null; }
		}
	}
	
	private class ClassNameComparator implements Comparator<Class<?>>
	{
		public int compare(Class<?> c1, Class<?> c2)
		{
			return c1.getName().compareTo(c2.getName());
		}
		public boolean equals(Class<?> c1, Class<?> c2)
		{
			return c1.getName().equals(c2.getName());
		}
	}
	
	public void updateFunctions()
	{
		synchronized (classPaths)
        {
			URL[] urls = new URL[classPaths.length+1];
			{
				String tempURLString = FunctionsLoader.class.
						getResource("/jfractus").toString();
				try
				{
					urls[0] = new URL(tempURLString.substring
							(0, tempURLString.lastIndexOf('/') + 1));
				}
				catch(MalformedURLException e)
				{ }
			}
			
			for (int i = 0; i < classPaths.length; i++)
			{
				File file =  new File(classPaths[i]);
				try
				{
					if (file.isDirectory())
						urls[i+1] = file.toURI().toURL();
					else
					{
						urls[i+1] = new URL("jar:" +
								file.toURI().toURL().toString() + "!/");
					}
				}
				catch (MalformedURLException e)
				{ }
			}
			
			classLoader = new URLClassLoader(urls);
			ArrayList<Class<?>> allClasses = new ArrayList<Class<?>>();
			for (URL url: urls)
				allClasses.addAll(updateFunctionsFromURL(url));
			
			int fractalFormulasCount = 0;
			int innerFunctionsCount = 0;
			int outerFunctionsCount = 0;
			int planeTransformsCount = 0;
			int outputFiltersCount = 0;
			
			/* count functions */
			for (Class<?> cls: allClasses)
			{
				if (FractalFormula.class.isAssignableFrom(cls))
					fractalFormulasCount++;
				else if (InnerFunction.class.isAssignableFrom(cls))
					innerFunctionsCount++;
				else if (OuterFunction.class.isAssignableFrom(cls))
					outerFunctionsCount++;
				else if (PlaneTransform.class.isAssignableFrom(cls))
					planeTransformsCount++;
				else if (OutputFilter.class.isAssignableFrom(cls))
					outputFiltersCount++;
			}
			
			/* allocate */
			fractalFormulas = new Class<?>[fractalFormulasCount];
			innerFunctions = new Class<?>[innerFunctionsCount];
			outerFunctions = new Class<?>[outerFunctionsCount];
			planeTransforms = new Class<?>[planeTransformsCount];
			outputFilters = new Class<?>[outputFiltersCount];
			
			int fractalFormulasIndex = 0;
			int innerFunctionsIndex = 0;
			int outerFunctionsIndex = 0;
			int planeTransformsIndex = 0;
			int outputFiltersIndex = 0; 
			
			/* assign */
			for (Class<?> cls: allClasses)
			{
				if (FractalFormula.class.isAssignableFrom(cls))
					fractalFormulas[fractalFormulasIndex++] = cls;
				else if (InnerFunction.class.isAssignableFrom(cls))
					innerFunctions[innerFunctionsIndex++] = cls;
				else if (OuterFunction.class.isAssignableFrom(cls))
					outerFunctions[outerFunctionsIndex++] = cls;
				else if (PlaneTransform.class.isAssignableFrom(cls))
					planeTransforms[planeTransformsIndex++] = cls;
				else if (OutputFilter.class.isAssignableFrom(cls))
					outputFilters[outputFiltersIndex++] = cls;
			}
			
			ClassNameComparator comparator = new ClassNameComparator();
			
			Arrays.sort(fractalFormulas, comparator);
			Arrays.sort(innerFunctions, comparator);
			Arrays.sort(outerFunctions, comparator);
			Arrays.sort(planeTransforms, comparator);
			Arrays.sort(outputFilters, comparator);
        }
	}
	
	public Class<?> forName(String name) throws ClassNotFoundException
	{
		return Class.forName(name, true, classLoader);
	}
	
	public ClassLoader getClassLoader()
	{
		synchronized (classPaths)
		{ return classLoader; }
	}
	
	public String[] getClassPaths()
	{
		synchronized (classPaths)
        { 
			String[] dest = new String[classPaths.length];
			System.arraycopy(classPaths, 0, dest, 0, classPaths.length);
			return dest;
		}
	}
	
	public void setClassPaths(String[] newClassPaths)
	{
		synchronized (classPaths)
        {
    	    classPaths = newClassPaths;
    	    
    	    putClassPathsToPrefs();
    	    
    	    updateFunctions();
    	    fireClassPathsChange();
        }
	}
	
	public Class<?>[] getFractalFormulas()
	{
		synchronized(classPaths)
		{ return fractalFormulas; }
	}
	public Class<?>[] getInnerFunctions()
	{
		synchronized(classPaths)
		{ return innerFunctions; }
	}
	public Class<?>[] getOuterFunctions()
	{
		synchronized(classPaths)
		{ return outerFunctions; }
	}
	public Class<?>[] getPlaneTransforms()
	{
		synchronized(classPaths)
		{ return planeTransforms; }
	}
	public Class<?>[] getOutputFilters()
	{
		synchronized(classPaths)
		{ return outputFilters; }
	}
	
	public Class<?>[] getFunctionsByType(Class<?> classType)
	{
		if (FractalFormula.class.isAssignableFrom(classType))
			return getFractalFormulas();
		else if (InnerFunction.class.isAssignableFrom(classType))
			return getInnerFunctions();
		else if (OuterFunction.class.isAssignableFrom(classType))
			return getOuterFunctions();
		else if (PlaneTransform.class.isAssignableFrom(classType))
			return getPlaneTransforms();
		else if (OutputFilter.class.isAssignableFrom(classType))
			return getOutputFilters();
		return null;
	}
}
