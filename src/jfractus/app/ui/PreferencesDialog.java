/**
 * PreferencesDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.Window;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import jfractus.app.Resources;

public class PreferencesDialog extends GenericDialog
{
	private static final long serialVersionUID = 9023611120249995607L;

	private JTabbedPane tabPane;
	private PrefsGeneralTab generalTab;
	private PrefsFunctionsTab functionsTab;
	
	public PreferencesDialog(Window owner)
	{
		super(owner, Resources.getString("Preferences"), ModalityType.APPLICATION_MODAL);
		createGUI();
	}
	
	private void createGUI()
	{
		final Object[] buttons = { Stock.OK, Stock.CANCEL, Stock.APPLY,  };
		setButtons(buttons);
		setSize(400, 400);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		tabPane = new JTabbedPane();
		panel.add(tabPane, BorderLayout.CENTER);
		setContent(panel);
		generalTab = new PrefsGeneralTab();
		generalTab.fetchPreferences();
		tabPane.addTab(Resources.getString("General"), generalTab);
		functionsTab = new PrefsFunctionsTab(this);
		functionsTab.fetchPreferences();
		tabPane.addTab(Resources.getString("Functions"), functionsTab);
		
		addResponseListener(new ResponseEventListener());
	}
	
	public void runPreferences()
	{
		setVisible(true);
		generalTab.fetchPreferences();
		functionsTab.fetchPreferences();
	}
	
	private class ResponseEventListener implements ResponseListener
    {
        public void response(ResponseEvent e)
        {
        	Object response = e.getResponse();
        	if (response == Stock.APPLY || response == Stock.OK)
        	{
        		generalTab.applyPreferences();
        		functionsTab.applyPreferences();
        	}
        	if (response == Stock.CANCEL || response == Stock.OK)
        		setVisible(false);
        }
    }
}
