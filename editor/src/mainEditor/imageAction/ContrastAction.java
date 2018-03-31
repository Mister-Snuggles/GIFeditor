package mainEditor.imageAction;
// $Id: BrightenAction.java,v 1.0 2012/10/04 13:57:18 dalamb Exp $

import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.util.ArrayList;

import mainEditor.ImageContents;
import mainEditor.ImageFrameData;
import mainEditor.history.ImageFilterHistory;
import mainEditor.history.ImageHistory;

// For documentation purposes, import only edfmwk classes actually used.
/**
 * {@link javax.swing.Action} for implementing "Contrast" adjusting functionality.
 *
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public class ContrastAction extends ImageActionRange {
    private final BufferedImageOp CONTRAST;    
    
    /**
     * Constructs an image contrast action -- brightens/darkens the GIF animation.
     */
    public ContrastAction(String name, float contrastAdjust) {
    	super(name);
    	float[] scales = {contrastAdjust, contrastAdjust, contrastAdjust, 1f};
        float[] offsets = new float[4];
    	this.CONTRAST = new RescaleOp(scales, offsets, null);
    } // end constructor CapitalizeAction

    /**
     * Changes to contrast of a continuous set of frames using a buffered image filter.
     * @param cont Image contents to change.
     * @param range The range of the frames to be changed.
     */
    protected void changeImage(ImageContents cont, ChangeParam range) {
    	try {
    		if(range.hasValidRange()) {
    			ArrayList<ImageFrameData> imgData = cont.getData();

    			ImageHistory filterChange;	// THIS IS AN OBJECT DO NOT BIND UNTIL IN LOOP
		
    			// ensures we don't change frames that are beyond the animation
    			int len = imgData.size();
		
    			// adds the changes to all the frames that need them
    			for (int i=0; i<len; i++) {
    				filterChange = new ImageFilterHistory(null, CONTRAST);
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
													"not of expected form in Contrast Action.");
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    } // end changeImage

} // end class Contrast Action