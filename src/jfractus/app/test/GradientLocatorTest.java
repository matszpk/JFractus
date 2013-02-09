/**
 * GradientLocatorTest.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import jfractus.app.GradientLocator;

public class GradientLocatorTest
{
	private GradientLocator gradientLocator;
	
	
	@Before
	public void setUp() throws Exception
	{
		gradientLocator = new GradientLocator();
	}

	@Test
	public void testSetFromSpec()
	{
		gradientLocator.setFromSpec("Default.grad");
		assertEquals(new GradientLocator(true, "Default.grad") ,gradientLocator);
		gradientLocator.setFromSpec("builtin:Default.grad");
		assertEquals(new GradientLocator(true, "Default.grad") ,gradientLocator);
		gradientLocator.setFromSpec("fromfile:Default.grad");
		assertEquals(new GradientLocator(false, "Default.grad") ,gradientLocator);
	}

	@Test
	public void testSetFromFilePath()
	{
		gradientLocator.setFromFilePath("Default.grad");
		assertEquals(new GradientLocator(false, "Default.grad") ,gradientLocator);
	}

	@Test
	public void testIsBuiltin()
	{
		gradientLocator.setFromSpec("Default.grad");
		assertTrue(gradientLocator.isBuiltin());
		gradientLocator.setFromSpec("fromfile:Default.grad");
		assertFalse(gradientLocator.isBuiltin());
	}

	@Test
	public void testGetFilePath()
	{
		String path = new String("directory/Default.grad");
		gradientLocator.setFromSpec("fromfile:directory/Default.grad");
		assertEquals(path.replace('/', File.separatorChar),
				gradientLocator.getFilePath());
	}
	
	@Test
	public void testListFiles()
	{
		gradientLocator.set(false, "/home/mat/docs/fractals/jfractus");
		GradientLocator[] locators = gradientLocator.listFiles();
		for (int i = 0; i < locators.length; i++)
			System.out.println(locators[i].toString());
		
		gradientLocator.set(true, "");
		locators = gradientLocator.listFiles();
		for (int i = 0; i < locators.length; i++)
			System.out.println(locators[i].toString());
	}
	
	@Test
	public void testGetName()
	{
		gradientLocator.set(false, "Ok/thisfile");
		assertEquals("thisfile", gradientLocator.getName());
		gradientLocator.set(false, "Ok/");
		assertEquals("", gradientLocator.getName());
		gradientLocator.set(false, "");
		assertEquals("", gradientLocator.getName());
	}
	
	@Test
	public void testGetParent()
	{
		gradientLocator.set(false, "Ok/thisfile");
		assertEquals("Ok", gradientLocator.getParentPath());
		gradientLocator.set(false, "Ok/dir/");
		assertEquals("Ok", gradientLocator.getParentPath());
		gradientLocator.set(false, "Ok/dir");
		assertEquals("Ok", gradientLocator.getParentPath());
		gradientLocator.set(false, "Ok/");
		assertNull(gradientLocator.getParentPath());
		gradientLocator.set(false, "");
		assertNull(gradientLocator.getParentPath());
		gradientLocator.set(false, "../");
		//System.out.println(gradientLocator.getParentPath());
	}
	
	@Test
	public void testGetAbsoluteFrom()
	{
		gradientLocator.set(false, "simple.grad");
		assertEquals(new GradientLocator(false, "/home/mat/docs/simple.grad"),
				gradientLocator.getAbsoluteFrom("/home/mat/docs"));
		gradientLocator.set(false, "/openware/simple.grad");
		assertEquals(new GradientLocator(false, "/openware/simple.grad"),
				gradientLocator.getAbsoluteFrom("/home/mat/docs"));
		gradientLocator.set(true, "simple.grad");
		assertEquals(new GradientLocator(true, "simple.grad"),
				gradientLocator.getAbsoluteFrom("/home/mat/docs"));
	}
	
	@Test
	public void testGetRelativeTo()
	{
		gradientLocator.set(false, "/home/mat/docs/devel/xx.grad");
		assertEquals(new GradientLocator(false, "xx.grad"),
				gradientLocator.getRelativeTo("/home/mat/docs/devel"));
		assertEquals(new GradientLocator(false, "../xx.grad"),
				gradientLocator.getRelativeTo("/home/mat/docs/devel/mydir"));
		assertEquals(new GradientLocator(false, "../../xx.grad"),
				gradientLocator.getRelativeTo("/home/mat/docs/devel/mydir/open"));
		assertEquals(new GradientLocator(false, "../devel/xx.grad"),
				gradientLocator.getRelativeTo("/home/mat/docs/devel2"));
		assertEquals(new GradientLocator(false, "../../docs/devel/xx.grad"),
				gradientLocator.getRelativeTo("/home/mat/doc2/ddr"));
		assertEquals(new GradientLocator(false, "../../devel/xx.grad"),
				gradientLocator.getRelativeTo("/home/mat/docs/system/one"));
	}
}
