package mainEditor.imageAction;

import java.util.ArrayList;

import mainEditor.ImageContents;
import mainEditor.ImageFrameData;
import mainEditor.ImageFrameDataHelper;
import mainEditor.TipBox;
import mainEditor.GIFInputOutput.GIFDefaultMetadata;
import mainEditor.history.ImageHistory;
import mainEditor.history.ImageMetadataHistory;
import mainEditor.history.ImageMetadataHistoryType;

/**
 * An implemented action that will slow down/speed up any GIF, by changing the
 * interval time between frames. Note that a delay time cannot go below 10 milliseconds.
 * <p>
 * Copyright 2017-2018 Joey Sun. <p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported License. 
 *  To view a copy of this license, visit 
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative 
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public class TimeDilationAction extends ImageActionSingleAdjuster {

	// ================================================================================
	// Region: Constructors
	// ================================================================================
	/**
	 * Constructs an image speed manipulation action.
	 * 
	 * @param name Name of the action.
	 * @param prompt How to inform the user on what to do.
	 */
	public TimeDilationAction(String name, String prompt) {
		super(name, prompt);
	}

	/**
	 * Constructs an image speed manipulation action.
	 * 
	 * @param name Name of the action.
	 * @param prompt How to inform the user on what to do.
	 * @param minRatio Min speed up ratio permitted.
	 * @param maxRatio Max speed up ratio permitted.
	 */
	protected TimeDilationAction(String name, String prompt, float minRatio, float maxRatio) {
		super(name, prompt, minRatio, maxRatio);
	} // end Time Dilation Action

	/**
	 * Constructs an image speed manipulation action.
	 * 
	 * @param name Name of the action.
	 * @param minRatio Min speed up ratio permitted.
	 * @param maxRatio Max speed up ratio permitted.
	 */
	protected TimeDilationAction(String name, float minRatio, float maxRatio) {
		super(name, null, minRatio, maxRatio);
	}

	// ================================================================================
	// Region: Image Content changing function
	// ================================================================================
	@Override
	protected void changeImage(ImageContents cont, ChangeParam param) {
		try {
			// gets the ratio to re-scale the image delay time by
			float ratio = param.getValue();

			ArrayList<ImageFrameData> imgData = cont.getData();

			ImageHistory timeChange; // THIS IS AN OBJECT DO NOT BIND UNTIL IN LOOP

			// ensures we don't change frames that are beyond the animation
			int len = imgData.size();

			// Notifies user if animation cannot be made any faster.
			// [Uses delay of first frame as an estimate]
			if(ratio < 1 && ImageFrameDataHelper.getActualDelayTime(imgData.get(0)) <= GIFDefaultMetadata.DIS_DELAY_TIME) {
				TipBox.limitReached("You very likely cannot make this animation go any faster!", getActiveWindowFrame());
				//System.err.println(ImageFrameDataHelper.getActualDelayTime(imgData.get(0)));
			}
			else {
				// adds the changes to all the frames that need them
				for (int i = 0; i < len; i++) {
					timeChange = new ImageMetadataHistory(null, ratio, ImageMetadataHistoryType.RETIME);
					imgData.get(i).addChange(timeChange);
					// System.err.println("History Added. For ratio " + ratio);
					// System.err.println("Mods Added at : " + i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
