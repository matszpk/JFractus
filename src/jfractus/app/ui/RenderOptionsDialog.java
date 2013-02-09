/**
 * RenderOptionsDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

import jfractus.app.AntialiasConfig;
import jfractus.app.FractusPreferencesFactory;
import jfractus.app.Resources;

public class RenderOptionsDialog extends GenericDialog
{
	private static final long serialVersionUID = 3277953451540552747L;

	private FractalPanel currentPanel;
	
	private JSpinner imageWidthSpinner;
	private JSpinner imageHeightSpinner;
	private JComboBox aaMethodCombo;
	private JLabel sampWidthLabel;
	private JSpinner sampWidthSpinner;
	private JLabel sampHeightLabel;
	private JSpinner sampHeightSpinner;
	
	public RenderOptionsDialog(Window owner)
	{
		super(owner, Resources.getString("RenderOptions"), ModalityType.APPLICATION_MODAL);
		createGUI();
	}
	
	private void createGUI()
	{
		final Object[] buttons = { new StockButton(Stock.OK),
				new StockButton(Stock.CANCEL), new StockButton(Stock.APPLY) };
		setButtons(buttons);
		
		setSize(400, 300);
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		
		JLabel imageWidthLabel = new JLabel(Resources.getString("ImageWidthLabel"));
		JLabel imageHeightLabel = new JLabel(Resources.getString("ImageHeightLabel"));
		JLabel aaMethodLabel = new JLabel(Resources.getString("AntialiasMethodLabel"));
		sampWidthLabel = new JLabel(Resources.getString("SamplingWidthLabel"));
		sampHeightLabel = new JLabel(Resources.getString("SamplingHeightLabel"));
		
		imageWidthSpinner = new JSpinner(new SpinnerNumberModel(640, 1, 1000000000, 10));
		imageWidthSpinner.setEditor(new JSpinner.NumberEditor
				(imageWidthSpinner, "###########"));
		imageHeightSpinner = new JSpinner(new SpinnerNumberModel(480, 1, 1000000000, 10));
		imageHeightSpinner.setEditor(new JSpinner.NumberEditor
				(imageHeightSpinner, "###########"));
		
		String[] antialiasMethodNames = { Resources.getString("AANone"),
				Resources.getString("AANormal") };
		aaMethodCombo = new JComboBox(antialiasMethodNames);
		aaMethodCombo.addActionListener(new AntialiasMethodActionListener());
		
		sampWidthSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 1000000000, 1));
		sampWidthSpinner.setEditor(new JSpinner.NumberEditor
				(sampWidthSpinner, "###########"));
		sampHeightSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 1000000000, 1));
		sampHeightSpinner.setEditor(new JSpinner.NumberEditor
				(sampHeightSpinner, "###########"));

		sampWidthLabel.setEnabled(false);
		sampHeightSpinner.setEnabled(false);
		sampWidthLabel.setEnabled(false);
		sampHeightSpinner.setEnabled(false);
		
		JPanel imageSizePanel = new JPanel();
		imageSizePanel.setBorder(new TitledBorder(Resources.getString("ImageSize")));
		imageSizePanel.setLayout(new GridBagLayout());
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weighty = 1.0;
			
			gbc.gridy = 0;
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			imageSizePanel.add(imageWidthLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			imageSizePanel.add(imageWidthSpinner, gbc);
			
			gbc.gridy = 1;
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			imageSizePanel.add(imageHeightLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			imageSizePanel.add(imageHeightSpinner, gbc);
		}
		
		JPanel antialiasPanel = new JPanel();
		antialiasPanel.setBorder(new TitledBorder(Resources.getString("Antialias")));
		antialiasPanel.setLayout(new GridBagLayout());
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weighty = 1.0;
			
			gbc.gridy = 0;
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			antialiasPanel.add(aaMethodLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			antialiasPanel.add(aaMethodCombo, gbc);
			
			gbc.gridy = 1;
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			antialiasPanel.add(sampWidthLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			antialiasPanel.add(sampWidthSpinner, gbc);
			
			gbc.gridy = 2;
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			antialiasPanel.add(sampHeightLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			antialiasPanel.add(sampHeightSpinner, gbc);
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(imageSizePanel, gbc);
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		panel.add(antialiasPanel, gbc);
		
		setContent(panel);
		
		addResponseListener(new ResponseEventListener());
	}
	
	public void addRenderOptionsChangeListener(RenderOptionsChangeListener l)
	{
		listenerList.add(RenderOptionsChangeListener.class, l);
	}
	public void fireRenderOptionsChange()
	{
		Object[] listeners = listenerList.getListenerList();
		RenderOptionsChangeEvent event = new RenderOptionsChangeEvent
			(this, getImageWidth(), getImageHeight(), getAntialiasConfig());
    	
    	for (int i = listeners.length-2; i >= 0; i -= 2)
    		if (listeners[i] == RenderOptionsChangeListener.class)
    			((RenderOptionsChangeListener)(listeners[i+1])).renderOptionsChanged(
    					(event));
	}
	public void removeRenderOptionsChangeListener(RenderOptionsChangeListener l)
	{
		listenerList.remove(RenderOptionsChangeListener.class, l);
	}
	
	private class AntialiasMethodActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (aaMethodCombo.getSelectedIndex() == 0)
			{
				sampWidthLabel.setEnabled(false);
				sampHeightLabel.setEnabled(false);
				sampWidthSpinner.setEnabled(false);
				sampHeightSpinner.setEnabled(false);
			}
			else
			{
				sampWidthLabel.setEnabled(true);
				sampHeightLabel.setEnabled(true);
				sampWidthSpinner.setEnabled(true);
				sampHeightSpinner.setEnabled(true);
			}
		}	
	}
	
	public int getImageWidth()
	{
		return ((Number)imageWidthSpinner.getValue()).intValue();
	}
	public int getImageHeight()
	{
		return ((Number)imageHeightSpinner.getValue()).intValue();
	}
	
	public void setImageWidth(int width)
	{
		imageWidthSpinner.setValue(width);
	}
	public void setImageHeight(int height)
	{
		imageHeightSpinner.setValue(height);
	}
	
	public AntialiasConfig getAntialiasConfig()
	{
		AntialiasConfig.Method method = (aaMethodCombo.getSelectedIndex() == 0) ?
				AntialiasConfig.Method.NONE : AntialiasConfig.Method.NORMAL;
		
		return new AntialiasConfig(method,
				((Number)sampWidthSpinner.getValue()).intValue(),
				((Number)sampHeightSpinner.getValue()).intValue());
	}
	public void setAntialiasConfig(AntialiasConfig aaConfig)
	{
		aaMethodCombo.setSelectedIndex(
				aaConfig.getMethod() == AntialiasConfig.Method.NONE ? 0 : 1);
		sampWidthSpinner.setValue(aaConfig.getSamplingWidth());
		sampHeightSpinner.setValue(aaConfig.getSamplingHeight());
		
	}
	
	public void fetchSettings()
	{
		if (currentPanel != null)
		{
			setImageWidth(currentPanel.getImageWidth());
			setImageHeight(currentPanel.getImageHeight());
			setAntialiasConfig(currentPanel.getAntialiasConfig());
		}
		else
		{
    		setImageWidth(FractusPreferencesFactory.prefs.getDefaultImageWidth());
    		setImageHeight(FractusPreferencesFactory.prefs.getDefaultImageHeight());
    		setAntialiasConfig(FractusPreferencesFactory.prefs.
    				getDefaultAntialiasConfig());
		}
	}
	
	public void applySettings()
	{
		AntialiasConfig oldAAConfig;
		int oldImageWidth, oldImageHeight;
		if (currentPanel != null)
		{
			oldImageWidth = currentPanel.getImageWidth();
			oldImageHeight = currentPanel.getImageHeight();
			oldAAConfig = currentPanel.getAntialiasConfig();
		}
		else
		{
    		oldImageWidth = FractusPreferencesFactory.prefs.getDefaultImageWidth();
    		oldImageHeight = FractusPreferencesFactory.prefs.getDefaultImageHeight();
    		oldAAConfig = FractusPreferencesFactory.prefs.getDefaultAntialiasConfig();
		}
		
		AntialiasConfig newAAConfig = getAntialiasConfig();
		int newImageWidth = getImageWidth();
		int newImageHeight = getImageHeight();
		
		if (!oldAAConfig.equals(newAAConfig) || oldImageWidth != newImageWidth ||
				oldImageHeight != newImageHeight)
		{
    		if (currentPanel != null)
    			fireRenderOptionsChange();
    		
    		FractusPreferencesFactory.prefs.setDefaultImageSize
    				(getImageWidth(), getImageHeight());
    		FractusPreferencesFactory.prefs.setDefaultAntialiasConfig(getAntialiasConfig());
		}
	}
	
	public void runOptionsDialog(FractalPanel panel)
	{
		currentPanel = panel;
		fetchSettings();
		setVisible(true);
	}
	
	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			Object response = e.getResponse();
        	if (response == Stock.APPLY || response == Stock.OK)
        		applySettings();
        	if (response == Stock.CANCEL || response == Stock.OK)
        		setVisible(false);
		}
	}
}
