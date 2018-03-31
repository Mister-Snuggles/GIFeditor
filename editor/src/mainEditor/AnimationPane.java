package mainEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.Timer;

import mainEditor.observerObject.ObservedState;
import mainEditor.observerObject.ObserverNotice;

/**
 * Container that is used to display an image, given the contents of an image.
 * Can be used to also display single images, but is meant usually for GIF animations.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public class AnimationPane extends JPanel implements Observer, ActionListener{
	
	private ImageContents contents;					// image contents
    private Timer animTimer;						// timer for the animation
    private int frameNum = 0; 						// the frame we are going to paint next (starts at 0)
    private int frames;								// the number of frames in the animation (related to image contents) 
    private ArrayList<ImageFrameData> frameData;	// the frame data for the animation (related to image contents)
    private Dimension dimens;						// the dimensions of the initial animation frame (related to image contents)
    private int timeTracker=0;						// keeps track of the seconds that have elapsed
    private int currDelay;							// the number of milliseconds the current frame is to remain on screen
    static final int BASE_DELAY = 10;				// the minimal number of milliseconds a frame must stay on screen for
	
    /**
     * Constructs a component which will continuously paint the animation, based
     * on the disposal method of the GIF frames.
     * @param contents The image contents.
     */
	public AnimationPane(ImageContents contents) {
		super();
		setContents(contents);
	}

	/**
	 * Default empty constructor. Does not initialize image contents.
	 */
    public AnimationPane() {
		super();
	}
    
    //================================================================================
    // Region: Coupled Extracted Data (Coupled to Image Contents)
    //================================================================================
    
    /**
     * Sets the image contents that are supposed to be in the panel.
     * @param contents The image contents.
     */
    public void setContents(ImageContents contents) {
		this.contents = contents;
		if (!this.contents.isEmpty()) {
			updateImageContents();
		}
	}

	/**
     * Updates information pertaining to image contents. Also resizes the animation.
     */
	public void updateImageContents() {
		// gets the frame data from the image contents
		this.frameData = contents.getChangedData();

		// gets the number of frames in animation
		this.frames = frameData.size();
		
		// gets the animation height and width
		this.dimens = frameData.get(0).getDimensions();
		
		// sets preferred size of JPanel to that of the first frame of GIF
		setPreferredSize(dimens);
		//System.err.println(dimens.getWidth() + " " + dimens.getHeight());
		
		// updates all the parents of this component
		Component p = getParent();
		while(p!=null) {
			p.revalidate();
			p = p.getParent();
		}
		
		//System.err.println(getPreferredSize());
		
	} // end updateContents
	

    //================================================================================
    // Region: Animation Timer
    //================================================================================
    /**
     * Tells the animation to start only if there is more than one frame in the image.
     * Otherwise, repaint the image.
     * Also sets delay between frames.
     */
	private void animationStart() {
		if(!animTimer.isRunning()) {
				updateAnimDelayTime();
				animTimer.start();
		}
	}
	
    /**
     * Tells the animation to stop.
     */
	/*private void animationStop() {
		if(animTimer.isRunning()) {
			animTimer.stop();
		}
	}*/
	
    /**
     * Restarts the animation from first frame.
     * --- Never used so far. ---
     */
	/*private void animationRestart() {
		animationStop();
		animTimer.restart();
	}*/
	
    /**
     * Initializes the animation timer. Also sets the initial display time of the first frame.
     */
	private void initAnimTimer() {
		animTimer = new Timer(BASE_DELAY, this);
		currDelay = this.frameData.get(0).getDelayTime();
	}
	
    /**
     * Updates the animation delay time between frames
     */
	private void updateAnimDelayTime() {
		animTimer.setDelay(BASE_DELAY);
		animTimer.setInitialDelay(BASE_DELAY);
	}
	
    //================================================================================
    // Region: Display and painting
    //================================================================================
	
	/**
	 * Paints animation. Not sure how.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// ensures that animation will only progress if time elapsed between 2 frames is 0
		if (timeTracker == 0) {
			if (frames > 1) { // only animate if there is more than one frame
			
				// draw frame 
				g.drawImage(frameData.get(frameNum).getImage(), 0, 0, null);
			
				//gets the number of milliseconds the current frame is supposed to be displayed for
				this.currDelay = this.frameData.get(frameNum).getDelayTime();
			
				//increments the frame number
				frameNum ++;
				frameNum %= frames;
			}
			else if (frames == 1) { // there is only one frame
				g.drawImage(frameData.get(0).getImage(), 0, 0, null);
			}
		}
		
	} // end paintComponent
	
    //================================================================================
    // Region: Animation Loop and Communications
    //================================================================================
	
    /**
     * The main animation loop.
     * @param arg The action event object received.
     * @Override
     */
	public void actionPerformed(ActionEvent arg) {
		// first make sure the source is the timer
		// before doing anything
	
		if(arg.getSource().equals(this.animTimer)){
			if(this.timeTracker >= this.currDelay) { // moves to next frame of animation
				this.timeTracker=0;
				repaint();
			}
			else {	// increments time tracker (time elapsed until next animation frame)
				this.timeTracker+=BASE_DELAY;
			}
		}

	}

    /**
     * Receives notification when a portion of the image has been changed.
     */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ObserverNotice) {
			ObserverNotice obsInfo = (ObserverNotice) arg;
			ObservedState os = obsInfo.getObservedState();
			Window win = obsInfo.getActiveWindow();
			
			// finds out what needs to be updated based on the observed state
			switch (os) {
			case OPEN:
				updateImageContents();
				initAnimTimer();
				animationStart();
				break;
			case CHANGED_ADD:
			case CHANGED_UNDO:
				//System.err.println("Refresh requested.");
				updateImageContents();
				
				// updates all components in the window
				if (obsInfo.hasActiveWindow()) {
					win.pack();
				}
				break;
			default:
				break;
			}
		}
	}
	
}
