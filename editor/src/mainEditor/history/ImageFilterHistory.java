package mainEditor.history;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import mainEditor.BufferedImageHelper;
import mainEditor.ImageFrameData;

/**
 * Class that extends the image history class to guarantee that records are buffered image filters.
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class ImageFilterHistory extends ImageHistory {
	public BufferedImageOp filterRecord;
	
	/** 
	 * Constructs a buffered image filter historical record which points to an earlier historical record.
	 * If null, then there is no earlier historical record to point to.
	 * @param prevHistory The earlier historical record to point to.
	 * @param filterRecord The buffered image filter to store in current node.
	 * @return A historical record with possible earlier historical records.
	 */
	public ImageFilterHistory(History prevHistory, BufferedImageOp filterRecord) {
		super(prevHistory);
		this.filterRecord = filterRecord;
	}
	
	@Override
	public ImageFrameData apply(ImageFrameData imgData) {
		BufferedImage changedFrame = BufferedImageHelper.deepCopyARGB(imgData.getImage());
		//System.err.println("Applying changes...");
		BufferedImage temp = null;
		
		if(isEmpty()) {} // if no filter, returns image copy
		else { // if there is actually a filter, apply filter to image
			 temp= filterRecord.filter(changedFrame, null);
			 changedFrame = temp;
		}
		
		// copies the image frame data deeply and returns it
		ImageFrameData newData = imgData.noHistoryDeepCopy();
		newData.setImage(changedFrame);
		
		return newData;
	}

	@Override
	public void setChange(Object record) {
		if (record instanceof BufferedImageOp) {
			this.filterRecord = (BufferedImageOp) record;
		}
		else {
			throw new IllegalArgumentException("BufferedImageOp expected; did not get it");
		}
	}

	@Override
	public Object getChange() {
		return this.filterRecord;
	}

	@Override
	public boolean isEmpty() {
		return this.filterRecord == null;
	}

}