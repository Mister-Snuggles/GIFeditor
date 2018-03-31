package mainEditor.history;

import mainEditor.ImageFrameData;

/**
 * Class that extends the image history class to guarantee that records are changes in the metadata.
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class ImageMetadataHistory extends ImageHistory {
	private float ratioRecord;						// the ratio which to multiply all metadata values by
	private ImageMetadataHistoryType typeChange;	// which metadata values to change
	
	/** 
	 * Constructs a metadata historical record which points to an earlier historical record.
	 * If null, then there is no earlier historical record to point to.
	 * @param prevHistory The earlier historical record to point to.
	 * @param ratioRecord The ratio to multiply a set of metadata values by.
	 * @param typeChange Which metadata values are to be changed?
	 * @return A historical record with possible earlier historical records.
	 */
	public ImageMetadataHistory(History prevHistory, float ratioRecord, 
								ImageMetadataHistoryType typeChange) {
		super(prevHistory);
		this.ratioRecord = ratioRecord;
		this.typeChange = typeChange;
	}
	
	@Override
	public ImageFrameData apply(ImageFrameData imgData) {
		// copies the image frame data deeply
		ImageFrameData newData = imgData.noHistoryDeepCopy();
		
		
		if(isEmpty()) {} // if no metadata record, returns passed image data copy
		else { // if there is actually a metadata record, alter the given metadata values
			// changes the new image frame metadata
			newData = this.typeChange.operate(newData, this.ratioRecord);
		}
		
		return newData;
	}

	@Override
	public void setChange(Object record) {
		if (record instanceof Float) {
			this.ratioRecord = (Float) record;
		}
		else {
			throw new IllegalArgumentException("Float expected; did not get it");
		}
	}

	@Override
	public Object getChange() {
		return this.ratioRecord;
	}

	@Override
	public boolean isEmpty() {
		return this.ratioRecord <= 0;
	}

}
