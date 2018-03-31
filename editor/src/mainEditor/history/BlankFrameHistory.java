package mainEditor.history;

import mainEditor.ImageFrameData;

/**
 * Class that extends the image history class. Stores no records, but does store the pointers
 * which point to an earlier historical record and a later one.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class BlankFrameHistory extends ImageHistory {
	
	/** 
	 * Constructs a default blank frame history node. Stores no records.
	 * @return A history node that serves as a placeholder in list of historical records.
	 */
	public BlankFrameHistory() {
		super();
	}
	
	/** 
	 * Constructs a blank frame history node, with a previous historical record. Stores no records.
	 * @return A history node that serves as a placeholder in list of historical records.
	 */
	public BlankFrameHistory(History prevHistory) {
		super(prevHistory);
	}
	
	@Override
	public ImageFrameData apply(ImageFrameData img) {
		return img.noHistoryDeepCopy();
	}

	/**
	 * This method does nothing in this class.
	 */
	@Override
	public void setChange(Object record) {	}

	/**
	 * This method returns null in this class.
	 */
	@Override
	public Object getChange() {
		return null;
	}

	/**
	 * This method always returns false in this class.
	 */
	@Override
	public boolean isEmpty() {
		return true;
	}

}