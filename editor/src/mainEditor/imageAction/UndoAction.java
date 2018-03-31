package mainEditor.imageAction;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import ca.queensu.cs.dal.flex.log.Log;
import mainEditor.ImageContents;
import mainEditor.TipBox;

/**
 * Implements the capability to undo any action or change in the history of an animation.
 *<p>
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
@SuppressWarnings("serial")
public class UndoAction extends ImageAction{

    /**
     * Constructs an image manipulation action that needs an input from a set of options.
     * @param name Name of the action.
     */
	public UndoAction(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Undoes the history of the animation by 1 step backwards if the user the confirms
     * that this is indeed the decision that they want.
     */
    public void actionPerformed(ActionEvent ae) {
    	try {
    		super.actionPerformed(ae);
    		
    		// title for the input box
    		String title = this.getTitle();
	    
    		// option pane which confirms that the user wants to undo their history
    		int answer = JOptionPane.showOptionDialog(getActiveWindowFrame(),
    			    		"Are you sure you want to undo a change?\n\nThis may be a cost expensive action.",
    			    			title, JOptionPane.DEFAULT_OPTION,
    			    			JOptionPane.QUESTION_MESSAGE,
    			    			null,     							// do not use a custom Icon
    			    			UndoHistoryOptions.values(),  		// the titles of buttons
    			    			UndoHistoryOptions.values()[1]); 	// default selected button
    		
    		// undoes the change and tells the imageContents that a change has been done.
    		changeImage(getContents(), new ChangeParam(answer));
        	changeDone(false);
    		
    	} catch (Exception ex) {
    		Log.error("Image action error: "+ex.getLocalizedMessage());
    	}
    }


	@Override
	protected void changeImage(ImageContents cont, ChangeParam param) {
		int ans = (int)param.getValue();		// answer to whether or not we want to undo a change
		//System.err.println(JOptionPane.CLOSED_OPTION);
		
		// if the answer is yes, and the first frame has changes to remove
		switch(UndoHistoryOptions.values()[ans]) {
			case YES_OPTION:
				if(cont.getData().get(0).hasChange()) {
					int len = cont.getNumberOfFrames();
			
					// Undoes one history node from all the frames
					for(int i = 0; i < len; i++) {
						cont.getData().get(i).undoChange();
					}
				}
				else {
					// informs the user that there are no changes to remove, based on the fact
					// that the first frame has no historical records
					TipBox.limitReached("There are likely no changes to remove!", getActiveWindowFrame());
				}
				break;
			case EASTER_EGG: // random easter egg
				JOptionPane.showMessageDialog(getActiveWindowFrame(),
					    "Congrats, you completed the game in 2.3 seconds!\n > Oh wait, wrong application. \n" 
					    		+" > Re-routing the server ... Connection lost.",
					    "Congratulations! -or not-",
					    JOptionPane.INFORMATION_MESSAGE);
				break;
			case NO_OPTION: 		// all other options are lumped into do nothing
			case CANCEL_OPTION:
			default:
				break;
		}
	
	}

}
