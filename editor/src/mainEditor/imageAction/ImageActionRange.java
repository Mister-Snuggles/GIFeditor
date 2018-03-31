package mainEditor.imageAction;

// $Id: ImageActionRange.java,v 1.1$
import java.awt.event.ActionEvent;

import ca.queensu.cs.dal.flex.log.Log;
import inputBox.RangeSliderBox;
import mainEditor.ImageContents;

/**
 * Parent for {@link javax.swing.Action Actions} for implementing changes to
 * a range of image frames. The argument of integers passed to the changeImage
 * function is a 2-element array indicating range of frames.
 * Subclasses need only implement the * {@link #changeImage} method.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public abstract class ImageActionRange extends ImageAction implements RangeReceiver{
	
    /**
     * Constructs an image manipulation action that needs the input as a range.
     * @param name Name of the action.
     */
    protected ImageActionRange(String name) {
    	super(name);
    } // end constructor ImageAction
    
    /**
     * Perform some appropriate change on a selected region of frames;
     * subclasses must implement this method. If <code>start</code> and 
     * <code>end</code> are equal, the operation will do nothing.
     * @param cont Image to change.
     * @param range The range of the frames to be changed.
     */
    protected abstract void changeImage(ImageContents cont, ChangeParam range);

    /**
     * Perform the appropriate action (defined by {@link #changeImage}) by asking the 
     * user to input the starting and ending frames 
     */
    public void actionPerformed(ActionEvent ae) {
    	try {
    		super.actionPerformed(ae);
    		
    		// title for the input box
    		String title = this.getTitle();
	    
    		// uses range slider to get frames we want to operate the change on
    		new RangeSliderBox(null, this).display(title, getActiveWindowFrame());
    		
    	} catch (Exception ex) {
    		Log.error("Image action error: "+ex.getLocalizedMessage());
    	}
    }

    @Override
    public void setIndices(int start, int end) {
	    changeImage(getContents(), new ChangeParam(start, end));
	    changeDone();
    }
    
    @Override
    public int getRangeMax() {
	    if (getContents()!=null) {
	    	return getContents().getData().size();
	    }
	    return DEFAULT_MAX_FRAMES;
    }
    
    @Override
    public int getRangeMin()  {
    	return DEFAULT_MIN_FRAMES;
    }
    
} // end class ImageActionRange