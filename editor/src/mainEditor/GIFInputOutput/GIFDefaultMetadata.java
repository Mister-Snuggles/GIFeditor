package mainEditor.GIFInputOutput;

/**
 * A helper class that contains some default values for some default metadata variables for a GIF animation.
 * Also contains a function which calculates the metadata delay time so that it doesn’t go below the minimum delay time.
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public final class GIFDefaultMetadata {
	public static final String DISPOSAL_METHOD = "none";		// the default disposal method
	public static final int DELAY_TIME = 100;					// the default frame delay time in ms
	public static final int X_OFFSET = 0;		 				// the default frame x-offset
	public static final int Y_OFFSET = 0;		 				// the default frame y-offset
	public static final int WIDTH = 0;		 					// the default frame width
	public static final int HEIGHT = 0;		 					// the default frame height
	public static final int MIN_DELAY_TIME = 10;				// the minimal delay time possible between frames in ms
	public static final int DIS_DELAY_TIME = 2*MIN_DELAY_TIME;	// the minimal delay time that will display properly in ms
	
	/**
	 * Rounds the delay time to the nearest unit of minimal delay time.
	 * If this is less than the displayable delay time, returns displayable
	 * delay time.
	 * @return Delay time in units of minimal delay time.
	 */
	public static int roundDelayTime(int delayTime) {
		int time = ((delayTime+MIN_DELAY_TIME/2)/MIN_DELAY_TIME)*MIN_DELAY_TIME;
		if (time < DIS_DELAY_TIME){
			return DIS_DELAY_TIME;
		}
		
		return time;
	}
} // end GIF Default tMetadata values
