package mainEditor;

import java.awt.Window;

import javax.swing.JOptionPane;

public final class TipBox {
	
	/**
	 * Notifies User of Some limit that has been reached.
	 * @param message Message to be displayed.
	 * @param win Window where this tip is coming from.
	 */
	public static void limitReached(String message, Window win) {
		JOptionPane.showMessageDialog(win, message, "Attention!", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Notifies User that the image has been successfully saved.
	 * @param win Window where this tip is coming from.
	 */
	public static void saved(Window win) {
		JOptionPane.showMessageDialog(win, "Your image has been successfully saved!");
	}
	
}
