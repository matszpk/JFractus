/**
 * PrefsGeneralTab.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;

import javax.swing.*;

import jfractus.app.FractusPreferencesFactory;
import jfractus.app.Resources;

public class PrefsGeneralTab extends JPanel implements PreferencesTab
{
    private static final long serialVersionUID = -2799336096658495976L;

    private JSpinner threadsSpinner;
    
    public PrefsGeneralTab()
    {
    	setLayout(new GridBagLayout());
    	
    	JLabel label = new JLabel(Resources.getString("ThreadsNumberLabel"));
    	threadsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 256, 1));
    	threadsSpinner.setEditor(new JSpinner.NumberEditor(threadsSpinner, "###"));
    	
    	JPanel threadsPanel = new JPanel();
    	threadsPanel.setLayout(new GridBagLayout());
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.anchor = GridBagConstraints.NORTH;
    	gbc.gridwidth = gbc.gridheight = 1;
    	gbc.weighty = 1.0;
    	
    	{
    		GridBagConstraints gbc2 = new GridBagConstraints();
    		gbc2.fill = GridBagConstraints.BOTH;
        	gbc2.gridwidth = gbc.gridheight = 1;
        	gbc2.weighty = 1.0;
        	
        	gbc2.weightx = 0.0;
        	gbc2.gridx = 0;
        	gbc2.gridy = 0;
        	threadsPanel.add(label, gbc2);
        	
        	gbc2.weightx = 1.0;
        	gbc2.gridx = 1;
        	threadsPanel.add(threadsSpinner, gbc2);
    	}
    	
    	gbc.gridy = 0;
    	gbc.gridx = 0;
    	gbc.weightx = 1.0;
    	add(threadsPanel, gbc);
    }
    
    public int getThreadsNumber()
    {
    	return ((Number)threadsSpinner.getValue()).intValue();
    }
    public void setThreadsNumber(int threadsNum)
    {
    	threadsSpinner.setValue(threadsNum);
    }
    
    public void applyPreferences()
    {
    	int oldThreadsNumber = FractusPreferencesFactory.prefs.getThreadsNumber();
    	int newThreadsNumber = getThreadsNumber();
    	if (oldThreadsNumber != newThreadsNumber)
    		FractusPreferencesFactory.prefs.setThreadsNumber(newThreadsNumber);
    }
    public void fetchPreferences()
    {
    	setThreadsNumber(FractusPreferencesFactory.prefs.getThreadsNumber());
    }
}
