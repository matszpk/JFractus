/**
 * StockButton.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import jfractus.app.Resources;

public class StockButton extends JButton
{
    private static final long serialVersionUID = 4086573207024353813L;
    
    private Stock stock;
    
    public StockButton(Stock stock)
    {
    	this.stock = stock;
    	switch(stock)
    	{
    	case OK:
    		setText(Resources.getString("OK"));
    		setMnemonic(Resources.getString("OKMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-ok.png")));
    		break;
    	case CANCEL:
    		setText(Resources.getString("Cancel"));
    		setMnemonic(Resources.getString("CancelMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-cancel.png")));
    		break;
    	case APPLY:
    		setText(Resources.getString("Apply"));
    		setMnemonic(Resources.getString("ApplyMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-apply.png")));
    		break;
    	case YES:
    		setText(Resources.getString("Yes"));
    		setMnemonic(Resources.getString("YesMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-yes.png")));
    		break;
    	case NO:
    		setText(Resources.getString("No"));
    		setMnemonic(Resources.getString("NoMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-no.png")));
    		break;
    	case OPEN:
    		setText(Resources.getString("Open"));
    		setMnemonic(Resources.getString("OpenMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-open.png")));
    		break;
    	case SAVE:
    		setText(Resources.getString("Save"));
    		setMnemonic(Resources.getString("SaveMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-save.png")));
    		break;
    	case ADD:
    		setText(Resources.getString("Add"));
    		setMnemonic(Resources.getString("AddMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-add.png")));
    		break;
    	case REMOVE:
    		setText(Resources.getString("Remove"));
    		setMnemonic(Resources.getString("RemoveMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-remove.png")));
    		break;
    	case EDIT:
    		setText(Resources.getString("Edit"));
    		setMnemonic(Resources.getString("EditMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-edit.png")));
    		break;
    	case NEW:
    		setText(Resources.getString("New"));
    		setMnemonic(Resources.getString("NewMnemonic").charAt(0));
    		setIcon(new ImageIcon(Resources.getGraphics("icon-new.png")));
    		break;
    	default:
    		break;
    	}
    }
    
    public Stock getStock()
    {
    	return stock;
    }
}
