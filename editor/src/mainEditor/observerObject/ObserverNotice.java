package mainEditor.observerObject;

import java.awt.Window;

/**
 * The enumeration of notices that can be passed from Observables to Observers. 
 * Includes the indices of the frames, as well as the base window of the active window
 * that this change originated from should there be a need to generate a warning or 
 * information dialog. Also contains a boolean for convenience which indicates whether the 
 * indexed frames need to change or if they should be unchanged while the others change.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */

public class ObserverNotice {
	
	private ObservedState os;			// the observed change
	private int[] indices;				// the indices of the frames to change (default is none)
	private Window win;					// the window where the change originated from
	private boolean inverseChange;		// indicates that the frames of indices given are to
										// not be changed while everything else is changed; default is false
	
	static final int [] DEFAULT_INDICES = {};	// default frame indices to change
	
    /**
     * Creates an Observer Notice which contains what change happened. 
     * No frame indices provided. No window origin. No inverse change.
     * @param os Observed state change.
     */
	public ObserverNotice(ObservedState os) {
		this(os, DEFAULT_INDICES, null, false);
	}
	
    /**
     * Creates an Observer Notice which contains what change happened. 
     * No frame indices provided. No window origin. Inverse change is given.
     * @param os Observed state change.
     * @param inverseChange Indicates that all indexed frames given need to change if false;
     * 		if true, indicates that all indexed frames given must be not changed while all other
     * 		frames must be changed.
     */
	public ObserverNotice(ObservedState os, boolean inverseChange) {
		this(os, DEFAULT_INDICES, null, inverseChange);
	}
	
    /**
     * Creates an Observer Notice which contains what change happened. 
     * Frames indices are included. No window origin. Inverse change is given.
     * @param os Observed state change.
     * @param indices Indices of frames.
     * @param inverseChange Indicates that all indexed frames given need to change if false;
     * 		if true, indicates that all indexed frames given must be not changed while all other
     * 		frames must be changed.
     */
	public ObserverNotice(ObservedState os, int [] indices, boolean inverseChange) {
		this(os, indices, null, inverseChange);
	}
	
    /**
     * Creates an Observer Notice which contains what change happened. 
     * No frame indices are included. The window that this change originated
     * from is also passed as a reference. Inverse change is given.
     * @param os Observed state change.
     * @param win The window where this change originated from.
     * @param inverseChange Indicates that all indexed frames given need to change if false;
     * 		if true, indicates that all indexed frames given must be not changed while all other
     * 		frames must be changed.
     */
	public ObserverNotice(ObservedState os, Window win, boolean inverseChange) {
		this(os, DEFAULT_INDICES, win, inverseChange);
	}
	
    /**
     * Creates an Observer Notice which contains what change happened. 
     * Frame indices are included. The window that this change originated
     * from is also passed as a reference. Inverse change is given.
     * @param os Observed state change.
     * @param indices Indices of frames.
     * @param win The window where this change originated from.
     * @param inverseChange Indicates that all indexed frames given need to change if false;
     * 		if true, indicates that all indexed frames given must be not changed while all other
     * 		frames must be changed.
     */
	public ObserverNotice(ObservedState os, int [] indices, Window win, boolean inverseChange) {
		this.os = os;
		this.indices = indices;
		this.inverseChange = inverseChange;
		this.win = win;
	}
	
	/**
	 * Returns observed state change.
	 * @return Observed state change.
	 */
	public ObservedState getObservedState() {
		return this.os;
	}
	
	/**
	 * Returns list of frame indices that need to be updated.
	 * @return List of frame indices that need to be updated.
	 */
	public int[] getFrameIndices() {
		return this.indices;
	}
	
	/**
	 * Returns window of active window where the change 
	 * originated from.in case of warnings/errors.
	 * @return Active window.
	 */
	public Window getActiveWindow() {
		return this.win;
	}
	
	/**
	 * Returns whether or not there is an active window where the
	 * change originated from.
	 * @return True if active window exists; false otherwise.
	 */
	public boolean hasActiveWindow() {
		return (this.win != null);
	}
	
	/**
	 * Returns whether if frame indices indicate what should remain or what should be changed.
	 * @return Active window.
	 */
	public boolean inverseChange() {
		return this.inverseChange;
	}

}


