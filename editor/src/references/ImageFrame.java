package references;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mainEditor.ImageFrameData;
/****			DEPRECATED (USED AS INITIAL IMPLEMENATION OF ANIMATION			***/
/**
 * Container that is used to display a single image frame in the GIF.
 *
 * Copyright 2017-2018 Joey Sun.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public class ImageFrame extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JLabel imageBox;			// container that holds the image of the image frame

	// default image frame is empty JPanel with no JLabel
	public ImageFrame() {
		super();
	}
	
    /**
     * Constructs an empty image frame object, which contains a JLabel that holds some image.
     * @param img An image that is displayed by its JLabel.
     * @param dimensFF The dimensions of the first frame of the animation.
     * @param dimensCF The dimensions of the current frame of the animation.
     * @param offset A point which indicates the offset of this image frame.
     * @throws IOException 
     */
	public ImageFrame(ImageFrameData imgFrameData, Dimension dimensFF) {
		super();
		this.imageBox = new JLabel();
		this.imageBox.setIcon(new ImageIcon(imgFrameData.getImage()));
		
		// sets preferred size of JPanel to that of the first frame of GIF
		setPreferredSize(dimensFF);

		// offsets and dimensions of current frame of animation
		Point offset = imgFrameData.getOffset();
		int left = (int)offset.getX();
		int top = (int)offset.getY();
		
		// uses grid-bag constraints to handle images that are offset from their center
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(top, left, 0, 0);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
		add(this.imageBox, gbc);
	}
	
	//returns JLabel containing picture
	public JLabel getImageLabel() {
		return imageBox;
	}

}
