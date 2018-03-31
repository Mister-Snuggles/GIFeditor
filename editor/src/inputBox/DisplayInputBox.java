package inputBox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * A helper class which contains a display method of some component, by means
 * of a dialog box.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
final class DisplayInputBox {
    /**
     * A display method for a dialog box. By default, blocks all input to 
     * initial parent window until this dialog is closed. Allows the addition
     * of one component.
     * 
     * @param title The title of the input box.
     * @param win The parent window.
     * @param comp Component to be added to the dialog box
     * @return A reference to the dialog box created
     */
    public static JDialog display(String title, Window win, Component comp) {
        // Create dialog box
        JDialog inputBox = new JDialog(win, Dialog.ModalityType.DOCUMENT_MODAL);
        inputBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputBox.setResizable(false);
        inputBox.setTitle(title);
        
        // Set window content and validate
        inputBox.getContentPane().setLayout(new BorderLayout());
        inputBox.getContentPane().add(comp, BorderLayout.CENTER);
        inputBox.pack();
        
        // set window location
        inputBox.setLocationRelativeTo(win);
        
        return inputBox;
    }
}
