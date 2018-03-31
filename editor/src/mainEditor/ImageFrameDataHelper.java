package mainEditor;

import java.awt.Dimension;

import mainEditor.history.ImageHistory;
import mainEditor.history.ImageMetadataHistory;

/**
 * A helper class that can manipulate the metadata of GIFS stored as ImageFrameData.
 * Contains methods that applies all the metadata changes of some frame to its metadata so that
 * the true value of its metadata could be discerned.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public final class ImageFrameDataHelper {
    
    /**
     * Applies all metadata histories to some image frame data in order to get it's actual delay time.
     * @param imageData A single image frame data.
     * @return The actual delay time of the image frame, after all metadata changes in its history are applied to it.
     */
    public static int getActualDelayTime(ImageFrameData imageData) {   	
    	// returns actual delay time
    	return collapseMetadata(imageData).getDelayTime();
    }
    
    /**
     * Applies all metadata histories to some image frame data in order to get it's actual dimensions.
     * @param imageData A single image frame data.
     * @return The actual dimensions of the image frame, after all metadata changes in its history are applied to it.
     */
    public static Dimension getActualDimensions(ImageFrameData imageData) {
    	// returns actual dimensions
    	return collapseMetadata(imageData).getDimensions();
    }
    
    /**
     * Applies all metadata histories to some image frame data.
     * @param imageData A single image frame data.
     * @return The image frame, after all metadata changes in its history are applied to it.
     */
    public static ImageFrameData collapseMetadata(ImageFrameData imageData) {
    	ImageHistory imgHist = imageData.getFirstHistory();
    	ImageFrameData finalFrame = imageData.noHistoryDeepCopy();
    	
    	// while there are still historical records, and only if the historical
    	// record has to do with metadata, apply the change to the frame
    	while(imgHist.hasNext()) {
			imgHist = ImageFrameData.castToImageHistory(imgHist.next());
			if (!imgHist.isEmpty() && imgHist instanceof ImageMetadataHistory) {
				finalFrame = imgHist.apply(finalFrame);
				//System.err.println("lol");
			}
		}
    	
    	// returns frame with finalized metadata
    	return finalFrame;
    }
}
