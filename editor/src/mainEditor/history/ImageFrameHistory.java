package mainEditor.history;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import mainEditor.BufferedImageHelper;
import mainEditor.ImageFrameData;

/**
 * Class that extends the image history class to guarantee that records are buffered images.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class ImageFrameHistory extends ImageHistory {
	private BufferedImage frameRecord;			// the image to add to a frame image data
	
	/** 
	 * Constructs a buffered image historical record which points to an earlier historical record.
	 * If null, then there is no earlier historical record to point to.
	 * @param prevHistory The earlier historical record to point to.
	 * @param frameRecord The buffered image to store in current node.
	 * @return A historical record with possible earlier historical records.
	 */
	public ImageFrameHistory(History prevHistory, BufferedImage frameRecord) {
		super(prevHistory);
		this.frameRecord = frameRecord;
	}
	
	@Override
	public ImageFrameData apply(ImageFrameData imgData) {
		BufferedImage changedFrame = BufferedImageHelper.deepCopy(imgData.getImage());
		
		if(isEmpty()) {} // if no image record, returns passed image data copy
		else { // if there is actually an image record, draw it over passed image copy
			
			Graphics g = changedFrame.getGraphics();

			// this is a new buffered image obtained from drawing the image record
			// over the passed image
			g.drawImage(frameRecord, 0, 0, null);

			g.dispose();
		}
		
		// copies the image frame data deeply and returns it
		ImageFrameData newData = imgData.noHistoryDeepCopy();
		newData.setImage(changedFrame);
		
		return newData;
	}

	@Override
	public void setChange(Object record) {
		if (record instanceof BufferedImage) {
			this.frameRecord = (BufferedImage) record;
		}
		else {
			throw new IllegalArgumentException("BufferedImage expected; did not get it");
		}
	}

	@Override
	public Object getChange() {
		return this.frameRecord;
	}

	@Override
	public boolean isEmpty() {
		return this.frameRecord == null;
	}

}
