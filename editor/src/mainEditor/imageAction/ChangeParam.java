package mainEditor.imageAction;

/**
 * Used to pass parameters through the changeImage function. Can store a range of frames to 
 * apply a change to, as well as a specific value for cases where more arguments are needed.
 * Range of frames is denoted by index of frame at start of range, and one more than the index
 * of the frame at the end of the range. When the start and end values denoting the range are
 * equal, it is empty.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public class ChangeParam {
	private int [] range;				// range of frames to change
	private float value;				// other value to pass to the changeImage function
	
	static final int [] DEFAULT_RANGE = {0,0};
	static final float DEFAULT_VALUE = 0;
	
	/**
	 * Default empty constructor.
	 * @return A default change parameter object.
	 */
	public ChangeParam() {
		this.range = DEFAULT_RANGE;
		this.value = DEFAULT_VALUE;
	}
	
	/**
	 * Default constructor with a default start and end of range. Value is set.
	 * @param value	Used only when the changeImage function needs it.
	 * @return A change parameter object.
	 */
	public ChangeParam(float value) {
		this(DEFAULT_RANGE[0], DEFAULT_RANGE[1], value);
	}	
	
	/**
	 * Default constructor with a given start and end of range. Value is default;
	 * range is set if it the start and end values form a valid range. Otherwise,
	 * the range is default.
	 * @param start	The index of the first frame of the range.
	 * @param end	The index of one more than the last frame of the range.
	 * @return A change parameter object.
	 */
	public ChangeParam(int start, int end) {
		this(start, end, DEFAULT_VALUE);
	}
	
	/**
	 * Default constructor with a given start and end of range. Value is always set;
	 * range is set if it the start and end values form a valid range. Otherwise,
	 * the range is default.
	 * @param start	The index of the first frame of the range.
	 * @param end	The index of one more than the last frame of the range.
	 * @param value	Used only when the changeImage function needs it.
	 * @return A change parameter object.
	 */
	public ChangeParam(int start, int end, float value) {
		this.value = value;
		
		if (isValidRange(start, end)) {
			this.range = new int [] {start, end};
		}
		else {	// default range of frames
			this.range = DEFAULT_RANGE;
		}
	}

	
	/**
	 * Returns the specific value, which is only used in some instances
	 * of the changeImage function.
	 * @return The specific value.
	 */
	public float getValue() {
		return this.value;
	}
	
    //================================================================================
    // Region: Range checking
    //================================================================================
	/**
	 * Returns whether or not this contains a valid range of frames.
	 * @return	True if the range of frames is valid; false otherwise.
	 */
	public Boolean hasValidRange() {
		return isValidRange(this.range[0], this.range[1]);
	}
	
	/**
	 * Returns whether or not these 2 values could act as a valid range of frames.
	 * @return	True if the range of frames is valid; false otherwise.
	 */
	public static Boolean isValidRange(int start, int end) {
		return (start <= end && start >= 0);
	}
	
	/**
	 * Returns whether or not an index belongs in a range.
	 * @return	True if the index is within the range of frames; false otehrwise.
	 */
	public Boolean inRange(int i) {
		return (this.range[0] <= i && i < this.range[1]);
	}
}
