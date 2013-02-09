/**
 * GradientEditor.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Window;

import jfractus.app.Gradient;
import jfractus.app.Resources;

public class GradientEditor extends GenericDialog
{
    private static final long serialVersionUID = -2191995553331073004L;
	
    private GradientEditorPanel editorPanel;
    
    public GradientEditor(Window window)
	{	
		super(window, Resources.getString("GradientEditor"), ModalityType.MODELESS);
		Object[] buttons = { Stock.OK, Stock.CANCEL, Stock.APPLY };
		setButtons(buttons);
		editorPanel = new GradientEditorPanel(this);
		setContent(editorPanel);
		setSize(500, 200);
		
		addResponseListener(new ResponseEventListener());
	}
    
    public Gradient getGradient()
    {
    	return editorPanel.getGradient();
    }
    
    public void setGradient(Gradient gradient)
    {
    	editorPanel.setGradient(gradient);
    }
    
    public void appendPathToTitle(String path)
    {
    	String editorTitle = Resources.getString("GradientEditor"); 
    	if (path == null)
    		setTitle(editorTitle);
    	else
    		setTitle(editorTitle + " - " + path); 
    }
    
    private class ResponseEventListener implements ResponseListener
    {
        public void response(ResponseEvent e)
        {
        	if (e.getResponse() == Stock.CANCEL || e.getResponse() == Stock.OK)
        		setVisible(false);
        }
    }
}
