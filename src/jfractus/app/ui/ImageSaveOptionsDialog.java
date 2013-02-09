/**
 * ImageSaveOptionsDialog.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.util.Dictionary;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageWriteParam;

import javax.swing.*;

import jfractus.app.Resources;

public class ImageSaveOptionsDialog extends GenericDialog
{
    private static final long serialVersionUID = 7175669593298352239L;
    
    private JCheckBox progressiveCheckBox;
    private JComboBox comprTypeComboBox;
    private JLabel comprQualityLabel;
    private JSlider comprQualitySlider;
    
    private ImageWriteParam imageWriteParam;

	public ImageSaveOptionsDialog(Window owner, ImageWriteParam imageWriteParam)
	{
		super(owner, Resources.getString("ImageSaveOptions"), ModalityType.APPLICATION_MODAL);
		createGUI(imageWriteParam);
	}
	
	private void createGUI(ImageWriteParam param)
	{
		final Object[] buttons = { new StockButton(Stock.OK), new StockButton(Stock.CANCEL) };
		addResponseListener(new ResponseEventListener());
		setButtons(buttons);
		setSize(300, 300);
		
		imageWriteParam = param;
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		
		gbc.gridy = 0;
		if (param.canWriteProgressive())
		{
			imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
			
			progressiveCheckBox = new JCheckBox(Resources.getString("ProgressiveMode"));
			progressiveCheckBox.setSelected(true);
			
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			panel.add(progressiveCheckBox, gbc);
			
			gbc.gridy++;
		}
		
		if (param.canWriteCompressed())
		{
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			
			JLabel comprTypeLabel = new JLabel(Resources.getString("CompressionTypeLabel"),
					SwingConstants.RIGHT);
			comprTypeComboBox = new JComboBox();
			comprTypeComboBox.addItem("");
			String[] supportedComprTypes = param.getCompressionTypes();
			for (String s: supportedComprTypes)
				comprTypeComboBox.addItem(s);
			
			comprQualitySlider = new JSlider(0, 100);
			
			Dictionary<Integer, JComponent> comprQualityLabelTable =
				new Hashtable<Integer, JComponent>();
			comprQualityLabelTable.put(0, new JLabel("0"));
			comprQualityLabelTable.put(100, new JLabel("100"));
			
			comprQualitySlider.setPaintLabels(true);
			comprQualitySlider.setPaintTicks(true);
			comprQualitySlider.setPaintTrack(true);
			comprQualitySlider.setLabelTable(comprQualityLabelTable);
			
			comprQualityLabel = new JLabel(
					Resources.getString("CompressionQualityLabel"), SwingConstants.RIGHT);
			
			comprTypeComboBox.addActionListener(new CompressionTypeComboActionListener());
			if (supportedComprTypes.length != 0)
				comprTypeComboBox.setSelectedIndex(1);
			
			gbc.gridwidth = 1;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.weightx = 0.0;
			panel.add(comprTypeLabel, gbc);
			
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.weightx = 1.0;
			panel.add(comprTypeComboBox, gbc);
			
			gbc.gridy++;
			
			gbc.gridwidth = 1;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.weightx = 0.0;
			panel.add(comprQualityLabel, gbc);
			
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.weightx = 1.0;
			panel.add(comprQualitySlider, gbc);
			
			gbc.gridy++;
		}
		
		setContent(panel);
	}
	
	private void setupImageWriteParam()
	{
		if (imageWriteParam.canWriteProgressive())
		{
			if (progressiveCheckBox.isSelected())
				imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
			else
				imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
		}
		if (imageWriteParam.canWriteCompressed())
		{
			String comprType = (String)(comprTypeComboBox.getSelectedItem());
			if (comprType.equals(""))
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);
			else
			{
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionType(comprType);
				imageWriteParam.setCompressionQuality
						(0.01f * (float)(comprQualitySlider.getValue()));
			}
		}
	}
	
	public ImageWriteParam getImageWriteParam()
	{
		return imageWriteParam;
	}
	
	private class CompressionTypeComboActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String comprType = (String)comprTypeComboBox.getSelectedItem();
			if(comprType.equals(""))
			{
				comprQualityLabel.setEnabled(false);
				comprQualitySlider.setEnabled(false);
			}
			else
			{
				comprQualityLabel.setEnabled(true);
				comprQualitySlider.setEnabled(true);
			}
		}
	}

	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			setupImageWriteParam();
			setVisible(false);
		}
	}
}
