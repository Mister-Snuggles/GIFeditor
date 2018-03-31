package mainEditor.imageAction;

import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;

import mainEditor.ImageContents;
import mainEditor.ImageFrameData;
import mainEditor.history.ImageFilterHistory;
import mainEditor.history.ImageHistory;

/**
 * {@link javax.swing.Action} for implementing image "Focus" functionality.
 *
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public class FocusAction extends ImageActionRange {
    private final BufferedImageOp FOCUS;    

	/**
	 * Constructs an image focusing action -- sharpens/blurs the GIF animation
	 */
	public FocusAction(String name, float [] data) {
		super(name);
		this.FOCUS = new ConvolveOp(new Kernel (3, 3, data));
	}

    /**
     * Changes to focus of a continuous set of frames using a buffered image filter.
     * @param cont Image contents to change.
     * @param range The range of the frames to be changed.
     */
	@Override
	protected void changeImage(ImageContents cont, ChangeParam range) {
		try {
    		if(range.hasValidRange()) {
    			ArrayList<ImageFrameData> imgData = cont.getData();

    			ImageHistory filterChange;	// THIS IS AN OBJECT DO NOT BIND UNTIL IN LOOP
		
    			// ensures we don't change frames that are beyond the animation
    			int len = imgData.size();
		
    			// adds the changes to all the frames that need them
    			for (int i=0; i<len; i++) {
    				filterChange = new ImageFilterHistory(null, FOCUS);
    				if(range.inRange(i)) {
    					imgData.get(i).addChange(filterChange);
    					//System.err.println("Mods Added at : " + i);
    				}
    				else {
    					imgData.get(i).addChange(null);
    					//System.err.println("Blanks Added at : " + i);
					}
				}
    		}
    		else {
    			throw new IllegalArgumentException("Arguments passed to changeImage method "+
													"not of expected form in Focus Action.");
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}

}	// end of Focus Action
