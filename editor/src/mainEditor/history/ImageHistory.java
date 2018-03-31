package mainEditor.history;

import mainEditor.ImageFrameData;

/**
 * Abstract class that would be used to record image changes for some image type.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public abstract class ImageHistory implements History {
	private History next;		// previous earlier historical record
	private History prev;		// next more recent historical record
	
	/** 
	 * Default image history object. Everything is null.
	 */
	public ImageHistory() {}
	
	/** 
	 * Constructs an image historical record which points to an earlier historical record.
	 * If null, then there is no earlier historical record to point to.
	 * @param prevHistory The earlier historical record to point to.
	 * @return A historical record with possible earlier historical records.
	 */
	public ImageHistory(History prevHist) {
		
		if(prevHist!=null) {
			this.prev = prevHist;
			this.prev.setNext(this);
		}
	}
	
	@Override
	public boolean hasNext() {
		return (this.next != null);
	}

	@Override
	public History next() {
		return this.next;
	}

	@Override
	public boolean hasPrev() {
		return (this.prev != null);
	}

	@Override
	public History prev() {
		return this.prev;
	}
	
	@Override
	public boolean isFirst() {
		if (hasPrev()) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isLast() {
		if (hasNext()) {
			return false;
		}
		
		return true;
	}

	@Override
	public History undo() {
		if(hasNext()) {	// if there is an earlier historical record
			this.next.setPrev(this.prev);
		}
		
		if(hasPrev()) { // if there is a more recent historical record
			this.prev.setNext(this.next);
			return this.prev;
		}
		
		// returns more recent historical record if no earlier record exists
		return this.next;
		
	}
	
	@Override
	public void setNext(History h) {
		this.next=h;
		
	}

	@Override
	public void setPrev(History h) {
		this.prev = h;
		
	}
	
	/**
	 * Applies the current record/change to some image frame data.
	 * @param imgData The image data to apply the change to.
	 * @return A new image with the recorded change applied to it, 
	 * 		if there is a change recorded down. Returns null if no change exists. 
	 * 		Always returns a copy of the image frame data. 
	 */
	public abstract ImageFrameData apply(ImageFrameData imgData);
	
	/**
	 * Records down a change in the historical record.
	 * @param e The type of change to record.
	 */
	public abstract void setChange(Object record);
	
	/**
	 * Returns the change in the historical records, without destroying it.
	 * @return The change that is currently being pointed to.
	 */
	public abstract Object getChange();
	
	/**
	 * Returns if the current historical record is empty.
	 * @return True if the current record is empty; false otherwise.
	 */
	public abstract boolean isEmpty();

}
