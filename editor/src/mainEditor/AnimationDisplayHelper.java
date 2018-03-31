package mainEditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import mainEditor.GIFInputOutput.GIFDefaultMetadata;
import mainEditor.history.ImageHistory;

/**
 * A helper class that can render each frame of an animated GIF based on their history.
 * Contains the method "flatten" which returns an array list of all the frames of a GIF with 
 * all the changes applied to the frames. The frames are of the type ImageFrameData.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class AnimationDisplayHelper {
    //================================================================================
    // Region: Flattening (Applying all changes to image frames)
    //================================================================================	
	/**
	 * Applies changes in each frame's history to a copy of the base frame image, depending
	 * on the disposal method recorded in the image contents, and whether the change was an addition
	 * or a retraction. This operates on the knowledge that only the image frame data has a history.
	 * 
	 * For disposal methods that require each frame to overwrite (clean the slate) before displaying
	 * another frame, it just applies all changes to each frame as it is.
	 * 
	 * For disposal methods that have frames ignoring each other and lying on top of each other 
	 * (drawing over other frames), this function will first plot an independent ith frame using an 
	 * "active" method, and then apply changes to that ith frame.
	 * The active method consists of drawing frames 0 to i in quick succession onto one buffered image.
	 * Returns an array with the changes applied to all frames.
	 * 
	 * This is needed if one wants to apply filters/resizings to a GIF on a frame-to-frame basis, 
	 * so that it still looks good.
	 * 
	 * @param imageData The original image frame data in an array list, which also contains the 
	 * 				history of changes of each frame.
	 * @param changedImageData The changed image frame data in an array list.
	 * @param disposalMethod The disposal method of frames in the animation.
	 * @param isAdd Notes if the change was a simply addition, (so we do less work),
	 * 				or if it was a retraction (all histories in each frame must be applied to itself).
	 * 
	 * @return The array list of final frames after all frames are rendered.
	 * 
	 * NOTE: THIS MAY GREATLY INCREASE THE SPACE THE GIF TAKES UP IN MEMORY. USE ONLY IF NEEDED.
	 * 		THIS ASSUMES THERE IS INFO ALREADY IN THE CHANGED IMAGE DATA.
	 * 		THIS ASSUMES THAT THE IMAGE FRAME DATA HAS A HISTORY.
	 */
	public static ArrayList<ImageFrameData> baseLineFlatten(ArrayList<ImageFrameData> imageData,
															ArrayList<ImageFrameData> changedImageData,
															String disposalMethod, boolean isAdd) {
	
		//obtains the frame drawing setting for the given disposal method
		FrameDrawSetting fds = disposalType(disposalMethod);
		
		ImageFrameData frameTracked; 			// pointer that references current frame
		ImageHistory imgHist;					// the historical record of changes of current frame
		ImageFrameData baseFrame;				// the copy of the current frame to be added	
		ArrayList<ImageFrameData> renderData;	// the array list of image frame data that will be used
												// to construct the final rendered frames
		
		// initializes array list of image frame data that will be returned
		int len = imageData.size();
		ArrayList<ImageFrameData> finalFrames = new ArrayList<ImageFrameData>(len);
		
		// if the change is an addition
		if (isAdd) {
			// render image frame data is changed image frame data
			renderData = changedImageData; 	
		}
		else { //if the change isn't an addition
			// render image frame data is original image frame data
			renderData = imageData;
		}
		
		// the dimensions of the first image frame
		Dimension dimens = ImageFrameDataHelper.getActualDimensions(imageData.get(0));
		int width = (int)dimens.getWidth();
		int height = (int)dimens.getHeight();
		
		// iterates over all frames in the image frame data
		for(int i=0; i<len; i++) {
			frameTracked = renderData.get(i);
			baseFrame = frameTracked.noHistoryDeepCopy();
			
			//**** first point where the disposal method matters ****//
			switch (fds) {
				case IGNORE:
					// apply all the historical record changes to the independent ith frame
					baseFrame.setImage(getIndependentFrame(renderData, fds, i));
					break;
				case OVERWRITE: // default case is lumped with when overwrite is true
				default:		// do not do anything (base frame needs no change)
					break;
			}
			
			// if the change is an addition
			if (isAdd) {
				// get current history and apply its recorded change
				// to the image frame data copy
				imgHist = imageData.get(i).getCurrHistory();
				baseFrame = imgHist.apply(baseFrame);
			}
			else { //if the change isn't an addition
				// get first history (recorded change is applied later)
				imgHist = imageData.get(i).getFirstHistory(); 	
			}
				
			while(imgHist.hasNext()) { // while there are still historical records
				imgHist = ImageFrameData.castToImageHistory(imgHist.next());
				if (!imgHist.isEmpty()) {
					baseFrame = imgHist.apply(baseFrame);		// applies historical record
					//System.err.print(i +" ");
				}
			}
			
			//**** second point where the disposal method matters ****//
			switch (fds) {
				case IGNORE:
					// resets the frame dimensions and image offsets so frames that had 
					// different sizes/offsets are all set to 0,0
					try {
						baseFrame.setDimensions(new Dimension(width, height));
						baseFrame.setOffset(new Point(0, 0));
						//System.err.println(baseFrame.getDimensions());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case OVERWRITE: // default case is lumped with when overwrite is true
				default:
					break;
			}
			
			//if (i%30 == 0) {DebugWindow.display(BufferedImageHelper.deepCopyRGB(baseFrame.getImage()));}
			
			finalFrames.add(i, baseFrame);	// adds processed animation frame to the array list
		}
		return finalFrames;		// returns processed array list of frames
	}

	/**
	 * Applies changes in each frame's history to a copy of the base frame image, depending
	 * on whether the change was an addition or a retraction.
	 * 
	 * Calls the baseLineFlatten method, and uses the default disposal method.
	 * (this currently treats each frame is if it were independent of others)
	 * 
	 * @param imageData The original image frame data in an array list, which also contains the 
	 * 				history of changes of each frame.
	 * @param changedImageData The changed image frame data in an array list.
	 * @param isAdd Notes if the change was a simply addition, (so we do less work),
	 * 				or if it was a retraction (all histories in each frame must be applied to itself)
	 * 
	 * @return The array list of final frames after all frames are rendered.
	 * 
	 * NOTE: THIS WILL ONLY INCREASE SPACE IF THE DEFAULT DISPOSAL METHOD IS CLASSIFIED AS AN OVERWRITE.
	 */
	public static ArrayList<ImageFrameData> flatten(ArrayList<ImageFrameData> imageData, 
													ArrayList<ImageFrameData> changedImageData, boolean isAdd) {
		return baseLineFlatten(imageData, changedImageData, GIFDefaultMetadata.DISPOSAL_METHOD, isAdd);
	}
	
	/**
     * Returns a FrameDrawSetting based on the string disposal method passed to it. Bundles how to treat
     * certain disposal methods.
     * @return A Frame Draw Setting that allows one to easier understand how to display/write frames
     */
    public static FrameDrawSetting disposalType (String disposalMethod) {
		// gets disposal method for the GIF animation
    	
		if (disposalMethod.equals("none") || disposalMethod.equals("doNotDispose")) {
			return FrameDrawSetting.IGNORE;
		}
		else {
			return FrameDrawSetting.OVERWRITE;
		}
    }
    
    /**
     * Returns a copy of an independent frame at index n, given the image frame data array list, and the frame
     * draw setting.
     * 
     * @return A copy of a frame independent of all the other frames.
     */
    public static BufferedImage getIndependentFrame (ArrayList<ImageFrameData> imageData, FrameDrawSetting fds, int n) {
    	
    	if(n >=0 && n < imageData.size()) { // ensures that the index is a valid frame number first
    		// the image frame to be returned
			BufferedImage bi = BufferedImageHelper.getNewImageARGB(imageData.get(0).getImage());
			Graphics g = bi.getGraphics();
			
			// dimension of the base image for the animation, and the offset of the
			// dependent nth frame of the animation
			Dimension dimens = imageData.get(0).getDimensions();
			Point offset = imageData.get(n).getOffset();

			
    		switch (fds) {
    			case IGNORE:
					for (int i = 0; i <= n; i++) {
						//gets offset of current frame index
						offset = imageData.get(i).getOffset();
						g.drawImage(imageData.get(i).getImage(), (int)offset.getX(), (int)offset.getY(), null);
					}
					break;
				case OVERWRITE:
					g.clearRect(0, 0, (int)dimens.getWidth(), (int)dimens.getHeight());
					g.drawImage(imageData.get(n).getImage(), (int)offset.getX(), (int)offset.getY(), null);
					break;
				default:
					break;
    		}
    		
        	//dispose of graphics and returns the image
        	g.dispose();
        	return bi;
    	}
    	else { // it is not a valid index
    		throw new ArrayIndexOutOfBoundsException("Index provided to get an independent frame for is out of range.");
    	}
    	
    }
}
