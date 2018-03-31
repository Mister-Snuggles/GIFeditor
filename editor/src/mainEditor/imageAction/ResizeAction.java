package mainEditor.imageAction;

import java.awt.Dimension;
import java.util.ArrayList;

import mainEditor.ImageContents;
import mainEditor.ImageFrameData;
import mainEditor.ImageFrameDataHelper;
import mainEditor.TipBox;
import mainEditor.history.ImageHistory;
import mainEditor.history.ImageMetadataHistory;
import mainEditor.history.ImageMetadataHistoryType;

/**
 * An implemented action that will resize any GIF animation.
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public class ResizeAction extends ImageActionSingleAdjuster {

	//================================================================================
    // Region: Constructors
    //================================================================================
    /**
     * Constructs an image manipulation action.
     * @param name Name of the action.
     * @param prompt How to inform the user on what to do.
     */
	public ResizeAction(String name, String prompt) {
		super(name, prompt);
	}
	
    /**
     * Constructs an image manipulation action.
     * @param name Name of the action.
     * @param prompt How to inform the user on what to do.
     * @param minRatio Min ratio permitted.
     * @param maxRatio Max ratio permitted.
     */
	protected ResizeAction(String name, String prompt, float minRatio, float maxRatio) {
		super(name, prompt, minRatio, maxRatio);
	}
	
    /**
     * Constructs an image manipulation action.
     * @param name Name of the action.
     * @param minRatio Min ratio permitted.
     * @param maxRatio Max ratio permitted.
     */
	protected ResizeAction(String name, float minRatio, float maxRatio) {
		super(name, null, minRatio, maxRatio);
	}

    //================================================================================
    // Region: Image Content changing function
    //================================================================================
	@Override
	protected void changeImage(ImageContents cont, ChangeParam param) {
    	try {
    		// gets the ratio to re-scale the image frames by
    		float ratio = param.getValue();
    		
    		ArrayList<ImageFrameData> imgData = cont.getData();

    		ImageHistory resizeChange;	// THIS IS AN OBJECT DO NOT BIND UNTIL IN LOOP
    		
			// Notifies user if animation cannot be made any smaller.
			// [Uses size of first frame as an estimate]
    		Dimension dimens = ImageFrameDataHelper.getActualDimensions(imgData.get(0));
			if(ratio < 1 && (dimens.getWidth() <= 1 || dimens.getHeight() <= 1)) {
				TipBox.limitReached("You cannot shrink this animation any more!", getActiveWindowFrame());
			}
			else {
				// ensures we don't change frames that are beyond the animation
				int len = imgData.size();
			
				// adds the changes to all the frames that need them
				for (int i=0; i<len; i++) {
					resizeChange = new ImageMetadataHistory(null, ratio, ImageMetadataHistoryType.RESIZE);
					imgData.get(i).addChange(resizeChange);
					//System.err.println("History Added. For ratio " + ratio);
					//System.err.println("Mods Added at : " + i);
				}
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		
	}

}
