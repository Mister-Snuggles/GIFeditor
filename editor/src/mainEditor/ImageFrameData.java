package mainEditor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import mainEditor.GIFInputOutput.GIFDefaultMetadata;
import mainEditor.history.BlankFrameHistory;
import mainEditor.history.History;
import mainEditor.history.ImageHistory;

/**
 * Internal representation of the data relevant to that of an image frame in the GIF.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class ImageFrameData {
	private BufferedImage img;	// the buffered image of the frame
	private Point offset;		// the image offset from top left corner
	private Dimension dimens;	// the dimensions of the frame
	private int delayTime;		// the delay time of the frame (before another frame can be displayed)

	private final ImageHistory first = new BlankFrameHistory();		// the first history record 
	private ImageHistory current = first;							// the current history record
	
    /**
     * Creates an image frame data structure which contains a buffered image,
     * the offset from the image origin, the image dimensions, and the frame delay.
     * @param img The image of the frame.
     * @param offset The offset the image frame is from origin.
     * @param dimens The dimensions of the image.
     * @param delayTime The time the frame will remain on screen.
     * @return The image frame data representation.
     */
	public ImageFrameData(BufferedImage img, Point offset, Dimension dimens, int delayTime) {
		this.img = img;
		
		// makes sure frame has non-negative offset
		if (offset.getX() >= 0 && offset.getY() >= 0) {
			this.offset = offset;
		}
		else {
			this.offset = new Point(GIFDefaultMetadata.X_OFFSET, GIFDefaultMetadata.Y_OFFSET);
		}
		
		// makes sure frame has non-negative dimensions
		if (dimens.getHeight() >= 0 && dimens.getWidth() >= 0) {
			this.dimens = dimens;
		}
		else {
			this.dimens = new Dimension(GIFDefaultMetadata.WIDTH, GIFDefaultMetadata.HEIGHT);
		}
		
		// makes sure frame delay is positive
		this.delayTime = (delayTime > 0) ? delayTime : GIFDefaultMetadata.DELAY_TIME;
	}
	
    /**
     * Creates an image frame data structure which contains a buffered image 
     * with certain dimensions and frame delay but no offset. 
     * @param img The image of the frame.
     * @param dimens The dimensions of the image.
     * @param delayTime The time the frame will remain on screen.
     * @return The image frame data representation.
     */
	public ImageFrameData(BufferedImage img, Dimension dimens, int delayTime) {
		this(img, new Point(0,0), dimens, delayTime);
	}
	
    /**
     * Creates an image frame data structure which contains a buffered image 
     * with certain dimensions and frame delay but no offset and delay. 
     * @param img The image of the frame.
     * @param dimens The dimensions of the image.
     * @return The image frame data representation.
     */
	public ImageFrameData(BufferedImage img, Dimension dimens) {
		this(img, new Point(0,0), dimens, GIFDefaultMetadata.DELAY_TIME);
	}
	
    //================================================================================
    // Region: Attribute Getters and Setters
    //================================================================================
	/**
	 * Gets image of frame.
	 * @return Image of frame.
	 */
	public BufferedImage getImage() {
		return this.img;
	}

	/**
	 * Sets image of frame.
	 */
	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
	/**
	 * Gets offset for image in frame.
	 * @return Offset of image in frame.
	 */
	public Point getOffset() {
		return this.offset;
	}

	/**
	 * Sets offset for image in frame, if it is non-negative.
	 * @throws Exception 
	 */
	public void setOffset(Point offset) throws Exception {
		if(offset.getX() >= 0 && offset.getY()>=0) {
			this.offset = offset;
		}
		else {
			throw new Exception("Offset provided is not proper for frame data!");
		}
	}
	
	/**
	 * Gets dimensions for image in frame.
	 * @return Dimensions of image in frame.
	 */
	public Dimension getDimensions() {
		return this.dimens;
	}

	/**
	 * Sets dimensions for image in frame, if it is non-negative.
	 * @throws Exception 
	 */
	public void setDimensions(Dimension dimens) throws Exception {
		if(dimens.getWidth() >= 0 && dimens.getHeight()>=0) {
			this.dimens = dimens;
		}
		else {
			throw new Exception("Dimension provided is not proper for frame data!");
		}
	}
	
	/**
	 * Gets delay time for image in frame.
	 * @return Delay time of image in frame.
	 */
	public int getDelayTime() {
		return this.delayTime;
	}
	
	/**
	 * Sets delay time for image in frame, if it is positive
	 * and greater than 10 milliseconds. If skipError is true, all other
	 * times will automatically default to default delay time.
	 * @throws Exception 
	 */
	public void setDelayTime(int delayTime, boolean skipError) throws Exception {
		// rounds delay time to closest displayable minimal delay time unit (10 ms)
		delayTime = GIFDefaultMetadata.roundDelayTime(delayTime);
		
		if(delayTime > 0) { // if delay time is positive, then set it
			this.delayTime = delayTime;
		}
		else {
			if (skipError) { // skips error and sets delay time to default
				this.delayTime = GIFDefaultMetadata.DELAY_TIME;
			}
			else {
				throw new Exception("Delay time provided is not proper for frame data!");
			}	
		}
	}
	
	/**
	 * Returns a deep copy of this image frame data with no history.
	 * @return A deep copy of the information (without history) of the image frame data.
	 */
	public ImageFrameData noHistoryDeepCopy() {
		BufferedImage imgCopy = BufferedImageHelper.deepCopy(img);
		Point offsetCopy = new Point(offset);
		Dimension dimensCopy = new Dimension(dimens);
		return new ImageFrameData(imgCopy, offsetCopy, dimensCopy, this.delayTime);
	}
	
    //================================================================================
    // Region: History Manipulations
    //================================================================================
	/**
	 * Removes most recent change in the image frame. Most recent change is 
	 * pointed to by current pointer. Only removes change if there is
	 * a historical record to remove.
	 */
	public void undoChange() {
		if(hasChange()) {
			this.current = castToImageHistory(this.current.undo());
		}
	}
	
	/**
	 * Returns whether not this frame has any changes.
	 * @return True if there are historical records; false otherwise.
	 */
	public boolean hasChange() {
		return (this.current!=this.first);
	}
	
	/**
	 * Adds a change to the image frame as the most recent change. If change is null,
	 * creates a blank frame history and adds that as most recent historical record.
	 * @param change The change to apply to the image frame.
	 */
	public void addChange(ImageHistory change) {
		if(change == null) {
			change = new BlankFrameHistory();
		}
		
		this.current.setNext(change);
		change.setPrev(current);
		this.current = change;
	}

	/**
	 * Returns a pointer to the front of the list of historical records.
	 * @return A pointer to the front of the list of historical records.
	 */
	public ImageHistory getFirstHistory() {
		return first;
	}
	
	/**
	 * Returns a pointer to the current history node.
	 * @return A pointer to the current history node.
	 */
	public ImageHistory getCurrHistory() {
		return current;
	}
	
	/**
	 * Casts a history node to an image history node. If it is not, an
	 * exception is thrown.
	 * @param hist The history node that is to be cast to an image history node.
	 */
	public static ImageHistory castToImageHistory(History hist) {
		if (hist instanceof ImageHistory) {
			return (ImageHistory) hist;
		}
		
		// if the history node provided cannot be cast
		throw new IllegalArgumentException("History node provided is not of type image history.");
	}
}
