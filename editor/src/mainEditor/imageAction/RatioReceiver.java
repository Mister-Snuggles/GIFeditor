package mainEditor.imageAction;

/**
 * Used to pass a ratio to some object that implements, usually as a float.
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public interface RatioReceiver {
    /**
     * Used to receive the ratio as a float.
     * @param ratio The ratio as a float.
     */
	public void setRatio(float ratio);
	
    /**
     * Returns the maximum ratio permitted.
     * @return The maximum ratio permitted.
     */
	public float getRatioMax();
	
    /**
     * Returns the minimum ratio permitted.
     * @return The minimum ratio permitted.
     */
	public float getRatioMin();
}
