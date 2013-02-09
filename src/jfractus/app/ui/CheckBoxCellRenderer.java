/**
 * CheckBoxCellRenderer.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class CheckBoxCellRenderer extends JPanel
		implements TableCellRenderer
{
    private static final long serialVersionUID = -2940736883022675518L;
    
    private JCheckBox checkBox;
    
    public CheckBoxCellRenderer()
    {
    	checkBox = new JCheckBox();
    	setLayout(new GridBagLayout());
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.NONE;
    	gbc.gridx = gbc.gridy = 0;
    	gbc.gridwidth = gbc.gridheight = 1;
    	gbc.weightx = gbc.weighty = 0.0;
    	add(checkBox, gbc);
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
		checkBox.setSelected(((Boolean)value).booleanValue());
		if (isSelected)
			setBackground(table.getSelectionBackground());
		else
			setBackground(table.getBackground());
		return this;
    }
	
	public void paint(Graphics gx)
	{
		Graphics2D g = (Graphics2D)gx;
		Dimension dim = getSize();
		g.setBackground(getBackground());
		g.clearRect(0, 0, dim.width, dim.height);
		
		super.paint(gx);
	}
}