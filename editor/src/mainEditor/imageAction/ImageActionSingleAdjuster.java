package mainEditor.imageAction;

import java.awt.event.ActionEvent;

import ca.queensu.cs.dal.flex.log.Log;
import inputBox.SliderBox;
import mainEditor.ImageContents;

/**
 * Parent for {@link javax.swing.Action Actions} for implementing changes to
 * all image frames; the user input is received using a slider. 
 * Subclasses need only implement the {@link #changeImage} method.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public abstract class ImageActionSingleAdjuster extends ImageAction implements RatioReceiver {
	static final float DEFAULT_MAX_RATIO = 2;			// default max ratio permitted
	static final float DEFAULT_MIN_RATIO = 0.5f;		// default min ratio permitted
	
	private float minRatio;		// minimum ratio permitted
	private float maxRatio;		// maximum ratio permitted
	private String prompt;		// prompt to tell the user what to do
	
    /**
     * Constructs an image manipulation action that needs a single value input, 
     * within some range.
     * @param name Name of the action.
     * @param prompt How to inform the user on what to do.
     */
    protected ImageActionSingleAdjuster(String name, String prompt) {
    	super(name);
    	
    	// default values for the ratios
    	this.maxRatio = DEFAULT_MAX_RATIO;			
    	this.minRatio = DEFAULT_MIN_RATIO;
    	
    	this.prompt= prompt;
    	
    } // end constructor ImageAction
	
    /**
     * Constructs an image manipulation action.
     * @param name Name of the action.
     * @param prompt How to inform the user on what to do.
     * @param minRatio Min ratio permitted.
     * @param maxRatio Max ratio permitted.
     */
    protected ImageActionSingleAdjuster(String name, String prompt, float minRatio, float maxRatio) {
    	super(name);
    	
    	// default values for the ratios
    	this.maxRatio = DEFAULT_MAX_RATIO;			
    	this.minRatio = DEFAULT_MIN_RATIO;
    	
    	this.prompt= prompt;
    	
    	// sets the ratios passed in as arguments
    	if(maxRatio >= minRatio) {
        	if(minRatio > 0) {
        		this.minRatio = minRatio;
        	}
        	if(maxRatio > 0) {
        		this.maxRatio = maxRatio;
        	}
    	} 	
    	
    } // end constructor ImageAction
    
    /**
     * Perform some appropriate change on all the frames.
     * @param cont Image to change.
     * @param start Index of the first frame to change.
     * @param end Index one beyond the last frame to change.
     */
    protected abstract void changeImage(ImageContents cont, ChangeParam param);

    /**
     * Perform the appropriate action (defined by {@link #changeImage}) by asking the 
     * user to input the starting and ending frames 
     */
    public void actionPerformed(ActionEvent ae) {
    	try {
    		super.actionPerformed(ae);
    		
    		// title for the input box
    		String title = this.getTitle();
    		
    		// uses slider to get ratio we want to use for data manipulation
        	new SliderBox(prompt, this, minRatio, maxRatio).display(title, getActiveWindowFrame());
    	} catch (Exception ex) {
    		Log.error("Image action error: "+ex.getLocalizedMessage());
    	}
    }

    
    @Override
	public void setRatio(float ratio) {
    	changeImage(getContents(), new ChangeParam(ratio));
    	changeDone();
    }

    @Override
	public float getRatioMax() {
		return this.maxRatio;
	}
	
	@Override
	public float getRatioMin() {
		return this.minRatio;
	}
}
