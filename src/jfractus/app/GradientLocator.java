/**
 * GradientLocator.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import jfractus.generic.Copyable;

public class GradientLocator implements Serializable, Comparable<GradientLocator>,
		Copyable<GradientLocator>
{
	private static final long serialVersionUID = 8963969369638981365L;
    
	private boolean builtin;
	private String filePath;
	
	/* spec format:
	 * [builtin:]path - builtin gradient
	 * fromfile:path - gradient from file
	 */
	
	private static GradientLocator[] builtinGradients = new GradientLocator[0];
	
	public GradientLocator()
	{
		builtin = false;
		filePath = null;
	}
	public GradientLocator(String spec)
	{
		setFromSpec(spec);
	}
	
	public GradientLocator(boolean builtin, String path)
	{
		this.builtin = builtin;
		filePath = path;
	}
	
	protected GradientLocator(GradientLocator loc)
	{
		builtin = loc.builtin;
		filePath = new String(loc.filePath);
	}
	
	public GradientLocator copy()
	{
		return new GradientLocator(this);
	}
	
	public void setFromSpec(String spec)
	{
		if (spec.startsWith("builtin:"))
		{
			builtin = true;
			filePath = spec.substring(8);
		}
		else if (spec.startsWith("fromfile:"))
		{
			builtin = false;
			filePath = spec.substring(9);
		}
		else
		{
			builtin = true;
			filePath = spec;
		}
	}
	
	public void set(boolean builtin, String path)
	{
		this.builtin = builtin;
		filePath = path;
	}
	
	public void setFromFilePath(String filePath)
	{
		builtin = false;
		this.filePath = filePath.replace(File.separatorChar, '/');
	}
	
	public boolean isBuiltin()
	{
		return builtin;
	}
	/* get system-dependent file path */
	public String getFilePath()
	{
		return filePath.replace('/', File.separatorChar);
	}
	
	public String getName()
	{
		if (builtin)
			return new String(filePath);
		else
		{
			int index = filePath.lastIndexOf('/');
			if (index < 0)
				return new String(filePath);
			else
				return filePath.substring(index+1);
		}
	}
	
	/* get parent path (in gradient locator format) */
	public String getParentPath()
	{
		if (builtin)
			return null;
		else
		{
			int index = filePath.lastIndexOf('/');
			if (index < 0)
				return null;
			else
			{
				if (index == filePath.length()-1)
				{
					index = filePath.lastIndexOf('/', index-1);
					if (index < 0)
						return null;
				}
				if (index == 0 && filePath.charAt(0) == '/')
					index = 1;
				return filePath.substring(0, index);
			}
		}
	}
	
	public GradientLocator getParentLocator()
	{
		String parent = getParentPath();
		if (parent != null)
			return new GradientLocator(false, parent);
		else return null;
	}
	
	public GradientLocator getCanonicalLocator() throws IOException
	{
		if (builtin)
			return this;
		else
		{
			File canonical = new File(getFilePath()).getCanonicalFile();
			GradientLocator newLoc = new GradientLocator();
			newLoc.setFromFilePath(canonical.getPath());
			return newLoc;
		}
	}
	
	public GradientLocator getAbsoluteLocator()
	{
		if (builtin)
			return this;
		else
		{
			File absolute = new File(getFilePath()).getAbsoluteFile();
			GradientLocator newLoc = new GradientLocator();
			newLoc.setFromFilePath(absolute.getPath());
			return newLoc;
		}
	}
	
	public GradientLocator getAbsoluteFrom(String absoluteFrom)
	{
		if (isAbsolute())
			return this;
		else
		{
			GradientLocator newLocator = new GradientLocator();
			newLocator.setFromFilePath
				(new File(absoluteFrom + File.separator + getFilePath()).
						getAbsolutePath());
			return newLocator;
		}
	}
	
	public GradientLocator getRelativeTo(String relativeTo)
	{
		if (builtin)
			return this;
		else
		{
			GradientLocator absLocator = null;
			try
			{ absLocator = getCanonicalLocator(); }
			catch(IOException e)
			{ absLocator = getAbsoluteLocator(); }
			
			String absRelativeTo;
			try
			{ absRelativeTo = new File(relativeTo).getCanonicalPath(); }
			catch(IOException e)
			{ absRelativeTo = new File(relativeTo).getAbsolutePath(); } 

			Pattern pattern = Pattern.compile
					((File.separatorChar == '\\') ? "\\" : File.separator);
			
			String[] absLocatorComps = pattern.split(absLocator.getFilePath());
			String[] absRelativeToComps = pattern.split(absRelativeTo);
			
			int lastEqualComp = 0;
			for (; lastEqualComp < absRelativeToComps.length &&
						lastEqualComp < absLocatorComps.length; lastEqualComp++)
				if (!absRelativeToComps[lastEqualComp].
						equals(absLocatorComps[lastEqualComp]))
					break;
			
			StringBuilder destPath = new StringBuilder();
			for (int i = lastEqualComp; i < absRelativeToComps.length; i++)
				destPath.append(".." + File.separator);
			
			for (int i = lastEqualComp; i < absLocatorComps.length; i++)
			{
				destPath.append(absLocatorComps[i]);
				if (i + 1 != absLocatorComps.length)
					destPath.append(File.separatorChar);
			}
			
			GradientLocator newLocator = new GradientLocator();
			newLocator.setFromFilePath(destPath.toString());
			return newLocator;
		}
	}
	
	public InputStream getAsInputStream()
	{
		if (builtin)
			return Resources.getGradientAsStream(filePath);
		else
		{
			FileInputStream iStream = null;
			try
			{ iStream = new FileInputStream(new File(getFilePath())); }
			catch (FileNotFoundException e)
			{ }
			return iStream;
		}
	}
	
	public OutputStream getAsOutputStream()
	{
		if (builtin)
			return null;
		else
		{
			FileOutputStream oStream = null;
			try
			{ oStream = new FileOutputStream(new File(getFilePath())); }
			catch (FileNotFoundException e)
			{ }
			return oStream;
		}
	}
	
	public void appendName(String name)
	{
		if (filePath.length() == 0)
			filePath += name;
		else if (filePath.charAt(filePath.length()-1) != '/')
			filePath += "/" + name;
		else
			filePath += name;
	}
	
	public void changeName(String name)
	{
		filePath = getParentPath();
		appendName(name);
	}
	
	public void createNewFile() throws IOException
	{
		if (builtin)
			throw new IOException("Builtin directory only for read-only");
		else
		{
			File file = new File(getFilePath());
			file.createNewFile();
		}
	}
	
	public void mkdir()
	{
		
	}
	
	public boolean isAbsolute()
	{
		if (builtin)
			return true;
		else
			return new File(getFilePath()).isAbsolute();
	}
	
	public boolean isFile()
	{
		if (builtin)
			return !filePath.equals("");
		else
			return new File(getFilePath()).isFile();
	}
	
	public boolean canWrite()
	{
		if (builtin)
			return false;
		else
			return (new File(getFilePath())).canWrite();
	}
	
	public boolean isDirectory()
	{
		if (builtin)
			return filePath.equals("");
		else
			return new File(getFilePath()).isDirectory();
	}
	
	public boolean exists()
	{
		if (builtin)
		{
			ArrayList<String> gradients = Resources.listGradients();
			return gradients.contains(filePath);
		}
		else
			return new File(getFilePath()).exists();
	}
	
	private class GradientFileFilter implements FileFilter
	{
		public GradientFileFilter()
		{ }

        public boolean accept(File file)
        {
        	if (!file.isFile())
        		return false;
	        return file.getName().endsWith(".grad");
        }
	}
	
	public GradientLocator[] listFiles()
	{
		if (builtin && filePath.equals(""))
		{
			synchronized(builtinGradients)
			{
				if (builtinGradients.length != 0)
					return builtinGradients;
    			ArrayList<String> gradients = Resources.listGradients();
    			if (gradients != null)
    			{
    				GradientLocator[] locators = new GradientLocator[gradients.size()];
    				int i = 0;
    				for(Iterator<String> iter = gradients.iterator();
    						iter.hasNext(); i++)
    					locators[i] = new GradientLocator(true, iter.next());
    				
    				builtinGradients = locators;
    				return locators;
    			}
    			else return null;
			}
		}
		else
		{
			File[] files = new File(getFilePath()).listFiles(new GradientFileFilter());
			if (files == null)
				return new GradientLocator[0];
			GradientLocator[] locators = new GradientLocator[files.length];
			for (int i = 0; i < files.length; i++)
			{
				locators[i] = new GradientLocator();
				locators[i].setFromFilePath(files[i].getPath());
			}
			return locators;
		}
	}
	
	public static GradientLocator[] listRoots()
	{
		File[] roots = File.listRoots();
		GradientLocator[] destRoots = new GradientLocator[roots.length+1];
		for(int i = 0; i < roots.length; i++)
		{
			destRoots[i] = new GradientLocator();
			destRoots[i].setFromFilePath(roots[i].getPath());
		}
		destRoots[roots.length] = new GradientLocator(true, "");
		return destRoots;
	}
	
	private class GradientDirectoryFilter implements FileFilter
	{
		public GradientDirectoryFilter()
		{ }

        public boolean accept(File file)
        {
        	return file.isDirectory();
        }
	}
	
	public GradientLocator[] listDirectories()
	{
		if (builtin)
			return new GradientLocator[0];
		else
		{
			File[] files = new File(getFilePath()).listFiles
					(new GradientDirectoryFilter());
			if (files == null)
				return new GradientLocator[0];
			GradientLocator[] locators = new GradientLocator[files.length];
			for (int i = 0; i < files.length; i++)
			{
				locators[i] = new GradientLocator();
				locators[i].setFromFilePath(files[i].getPath());
			}
			return locators;
		}
	}
	
	public String toString()
	{
		StringBuilder sB = new StringBuilder();
		if (builtin)
			sB.append("builtin:");
		else
			sB.append("fromfile:");
		sB.append(filePath);
		return sB.toString();
	}
	
	public int compareTo(GradientLocator loc)
	{
		if (builtin != loc.builtin)
			return (builtin) ? -1 : 1;
		return filePath.compareTo(loc.filePath);
	}
	
	public boolean equals(Object ob)
	{
		if (ob == this)
			return true;
		else if (ob instanceof GradientLocator)
		{
			GradientLocator loc = (GradientLocator)ob;
			return builtin == loc.builtin && filePath.equals(loc.filePath);
		}
		else return false;
	}
	
	/* compare by canonical paths */
	public boolean canonicallyEquals(Object ob)
	{
		if (ob == this)
			return true;
		else if (ob instanceof GradientLocator)
		{
			GradientLocator loc = (GradientLocator)ob;
			if (builtin != loc.builtin)
				return false;
			if (builtin)
				return filePath.equals(loc.filePath);
			else
			{
				try
				{
					File file1 = new File(getFilePath()).getCanonicalFile();
					File file2 = new File(loc.getFilePath()).getCanonicalFile();
					return file1.equals(file2);
				}
				catch(IOException e)
				{ return filePath.equals(loc.filePath); }
			}
		}
		else return false;
	}
}
