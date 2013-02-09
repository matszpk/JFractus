package jfractus.app.ui;

import java.awt.Window;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jfractus.app.Resources;

public class KeysListDialog extends GenericDialog
{
	private static final long serialVersionUID = -6483481917209316359L;

	public KeysListDialog(Window owner)
	{
		super(owner, Resources.getString("KeyList"), ModalityType.APPLICATION_MODAL);
		final Object[] buttons = { new StockButton(Stock.OK) };
		setButtons(buttons);
		setSize(400, 400);
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		textArea.setText(
				Resources.getString("KeyTranslate1") + "\n" +
				Resources.getString("KeyRotate1") + "\n" +
				Resources.getString("KeyRectZoomIn1") + "\n" +
				Resources.getString("KeyRectZoomOut1") + "\n" +
				Resources.getString("KeyNormalZoomIn1") + "\n" +
				Resources.getString("KeyNormalZoomOut1") + "\n" +
				Resources.getString("KeyTransform1") + "\n" +
				Resources.getString("KeyInverseTransform1") + "\n" +
				Resources.getString("KeyTranslate2") + "\n" +
				Resources.getString("KeyRotate2") + "\n" +
				Resources.getString("KeyRectZoomIn2") + "\n" +
				Resources.getString("KeyRectZoomOut2") + "\n" +
				Resources.getString("KeyNormalZoomIn2") + "\n" +
				Resources.getString("KeyNormalZoomOut2") + "\n" +
				Resources.getString("KeyTransform2") + "\n" +
				Resources.getString("KeyInverseTransform2") + "\n"
				);
		
		setContent(scrollPane);
		
		addResponseListener(new ResponseEventListener());
	}
	
	private class ResponseEventListener implements ResponseListener
	{
		public void response(ResponseEvent e)
		{
			setVisible(false);
		}
	}
}
