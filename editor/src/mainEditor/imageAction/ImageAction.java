package mainEditor.imageAction;

// $Id: ImageAction.java,v 1.1$
import java.awt.event.ActionEvent;
// import javax.swing.Action;
// import javax.swing.text.Keymap;
// import javax.swing.text.DefaultEditorKit;
// import javax.swing.KeyStroke;
import ca.queensu.cs.dal.edfmwk.Application;
import ca.queensu.cs.dal.edfmwk.act.DefaultAction;
import ca.queensu.cs.dal.edfmwk.win.CommonWindow;
import ca.queensu.cs.dal.edfmwk.win.WFrame;
import ca.queensu.cs.dal.flex.log.Log;
import mainEditor.ImageContents;
import mainEditor.ImageDocument;

/**
 * Parent for {@link javax.swing.Action Actions} for implementing changes to
 * some image frames; could be a range, or all the frames. Subclasses need only implement the
 * {@link #changeImage} method.
 *<p>
 * Copyright 2010-2011 David Alex Lamb.
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
@SuppressWarnings("serial")
public abstract class ImageAction extends DefaultAction{
	private ImageContents cont;					// the image contents to act upon by an action
	private CommonWindow win;					// keeps track of the common window in the application, when this action
												// was received

	static final int DEFAULT_MAX_FRAMES = 0;	// default max number of frames to apply change to
	static final int DEFAULT_MIN_FRAMES = 0;	// default min number of frames to apply change to
	
    /**
     * Constructs an image manipulation action
     * @param name Name of the action.
     */
    protected ImageAction(String name) {
    	super(name);
    } // end constructor ImageAction
    
    /**
     * Perform some appropriate change on all the frames by default, unless stated otherwise.
     * @param cont Image to change.
     * @param param An object that is passed to the class implementing the method, which contains
     * 			information on the range of frames to apply the change to, and another argument
     * 			if needed.
     */
    protected abstract void changeImage(ImageContents cont, ChangeParam param);

    /**
     * Perform the appropriate action (defined by {@link #changeImage}) after the 
     * user inputs certain data
     */
    public void actionPerformed(ActionEvent ae) {
    	try {
    		extractContents();
    	} catch (Exception ex) {
    		Log.error("Image action error: "+ex.getLocalizedMessage());
    	}
    }
    
    /** 
     * Extracts the contents of the active image document,
     * so that it can be later used.
     */
    private void extractContents() {
	    Application app = Application.getApplication();
	    this.win = app.getActiveWindow();

	    ImageDocument doc = (ImageDocument) app.getActiveDocument();
	    this.cont = (ImageContents) doc.getContents();
    }
    
    /**
     * Returns a reference to the image contents.
     * @return Reference to image contents.
     */
    protected ImageContents getContents() {
    	return this.cont;
    }
    
    /**
     * Returns a reference to the active window frame where this action originated.
     * @return Reference to the active window where this action originated.
     */
    protected WFrame getActiveWindowFrame() {
    	return (WFrame)this.win;
    }
    
    /**
     * Tells the contents that it has changed somehow. Also passes as an argument
     * the window that the change is being sent to, so that warnings/information dialogs
     * could be used.
     * @param isAdd Notes whether the change is a retraction or addition.
     */
    protected void changeDone(boolean isAdd) {
    	this.cont.changeDone(getActiveWindowFrame(), isAdd);
    }
    
    /**
     * Tells the contents that it has changed somehow. Also passes as an argument
     * the window that the change is being sent to, so that warnings/information dialogs
     * could be used. By default, the change is seen as an addition.
     */
    protected void changeDone() {
    	this.cont.changeDone(getActiveWindowFrame(), true);
    }
    
    /**
     * Returns the number of frames in the contents of the image
     * @return Number of frames in the image contents.
     */
    public int getNumberOfFrames() {
    	return this.cont.getData().size();
    }
    
    /**
     * Returns a pseudo-unique name for this action.
     * @return A pseudo-unique name for this action, based on the name of 
     * 		the action and the contents.
     */
    protected String getTitle() {
    	return this.getName() + ": " + this.getActiveWindowFrame().getTitle();
    }
    
    //================================================================================
    // Region: Leftover debug code from original
    //================================================================================
    /*
    // debugging
    /*
    private static JTextArea firstArea = null;
    private static void setArea(JTextArea area) {
	TextType.setActions(area);
	Keymap km = area.getKeymap();
	if (km==null) {System.out.println("No keymap"); return; }
	String actionName=DefaultEditorKit.pasteAction;
	Action ac = TextType.getNamedAction(actionName);
	TextType.debugAction(actionName, km, ac);
	debugStroke("ctrl pressed V",km);
	debugStroke("ctrl X",km);
	debugStroke("ctrl pressed C",km);
	firstArea = area;
    }
    private static void debugStroke(String stroke, Keymap km) {
	KeyStroke testChar = KeyStroke.getKeyStroke(stroke);
	if (testChar!=null) {
	    System.out.print("test='"+stroke+"' '"+testChar+"'");
	    while (km != null) {
		if (km.isLocallyDefined(testChar)) {
		    Action ac = km.getAction(testChar);
		    if (ac==null) System.out.print(" no action");
		    else System.out.print(" action "+ac);
		    break;
		} else {
		    System.out.println(" not in "+km);
		    km = km.getResolveParent();
		}
	    } // while 
	    System.out.println();
	} else System.out.println("No ctl-C keystroke");
    } // debugStroke
    */
} // end class ImageAction
