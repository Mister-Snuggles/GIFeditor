package references;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Random debug class cause I have no more hair.
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class DebugWindow {
	// Create frame to display a buffered image
	public static void display(BufferedImage bi) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Test");
    
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(bi));
    
		// Set window content and validate
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
    
		// Set window location and display
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
