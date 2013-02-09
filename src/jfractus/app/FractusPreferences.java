/**
 * FractusPreferences.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.Serializable;

import java.util.prefs.*;

import javax.swing.event.EventListenerList;

public class FractusPreferences implements Serializable
{
	private static final long serialVersionUID = 8213290611830867895L;
	
	private Preferences prefs;
	private EventListenerList listenerList = new EventListenerList(); 
	
	public FractusPreferences()
	{
		prefs = Preferences.userNodeForPackage(FractusPreferences.class);
	}
	
	public void addPreferencesListener(FractusPreferencesListener l)
	{
		listenerList.add(FractusPreferencesListener.class, l);
	}
	public void firePreferencesChange(FractusPreferencesEvent.Change change)
	{
		Object[] listeners = listenerList.getListenerList();
		FractusPreferencesEvent event = new FractusPreferencesEvent(this, change);
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == FractusPreferencesListener.class)
    			((FractusPreferencesListener)(listeners[i+1])).preferencesChanged(event);
	}
	public void removePreferencesListener(FractusPreferencesListener l)
	{
		listenerList.remove(FractusPreferencesListener.class, l);
	}
	
	public int getThreadsNumber()
	{
		return prefs.getInt("ThreadsNumber", 1);
	}
	public void setThreadsNumber(int threadsNum)
	{
		prefs.putInt("ThreadsNumber", threadsNum);
		firePreferencesChange(FractusPreferencesEvent.Change.THREAD_NUMBER);
	}
	
	public void setDefaultImageWidth(int width)
	{
		prefs.putInt("DefaultImageWidth", width);
		firePreferencesChange(FractusPreferencesEvent.Change.DEFAULT_IMAGE_SIZE);
	}
	public void setDefaultImageHeight(int height)
	{
		prefs.putInt("DefaultImageHeight", height);
		firePreferencesChange(FractusPreferencesEvent.Change.DEFAULT_IMAGE_SIZE);
	}
	public void setDefaultImageSize(int width, int height)
	{
		prefs.putInt("DefaultImageWidth", width);
		prefs.putInt("DefaultImageHeight", height);
		firePreferencesChange(FractusPreferencesEvent.Change.DEFAULT_IMAGE_SIZE);
	}
	
	public int getDefaultImageWidth()
	{
		return prefs.getInt("DefaultImageWidth", 640);
	}
	public int getDefaultImageHeight()
	{
		return prefs.getInt("DefaultImageHeight", 480);
	}
	
	public AntialiasConfig getDefaultAntialiasConfig()
	{
		String methodName = prefs.get("DefaultAAMethod", "none");
		AntialiasConfig.Method method = (methodName.equals("normal")) ?
				AntialiasConfig.Method.NORMAL : AntialiasConfig.Method.NONE;
		int sampWidth = prefs.getInt("DefaultSampWidth", 2);
		int sampHeight = prefs.getInt("DefaultSampHeight", 2);
		return new AntialiasConfig(method, sampWidth, sampHeight);
	}
	
	public void setDefaultAntialiasConfig(AntialiasConfig aaConfig)
	{
		prefs.put("DefaultAAMethod",
				(aaConfig.getMethod() == AntialiasConfig.Method.NONE) ? "none" : "normal");
		prefs.putInt("DefaultSampWidth", aaConfig.getSamplingWidth());
		prefs.putInt("DefaultSampHeight", aaConfig.getSamplingHeight());
		firePreferencesChange(FractusPreferencesEvent.Change.DEFAULT_AA_CONFIG);
	}
}
