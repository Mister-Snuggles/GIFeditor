package mainEditor.imageAction;

/**
 * Used to pass a range defined by 2 numbers to some object that implements this, which are assumed 
 * to be indices of some sort to indicate a range. First number is smaller than second number. The object that
 * implements this also has some way of passing the maximum and minimum number of that range to others.
 *
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public interface RangeReceiver {
    /**
     * Used to receive the start and end of the range specified by the indices.
     * @param start The beginning of the range.
     * @param end	One more past the end of the range.
     */
	public void setIndices(int start, int end);
	
    /**
     * Returns one more than the maximum of the range.
     * @return The maximum index of the range.
     */
	public int getRangeMax();
	
    /**
     * Returns the minimum of the range.
     * @return The minimum index of the range.
     */
	public int getRangeMin();
} //end range receiver
