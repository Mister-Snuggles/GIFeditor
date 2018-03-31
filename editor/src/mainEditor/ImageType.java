package mainEditor;
// $Id: TextType.java,v 1.3 2012/10/24 17:06:40 dalamb Exp $
// Import only those classes from edfmwk that are essential, for documentation purposes
import java.awt.Component;
import java.io.IOException;
import javax.swing.JPanel;
// import javax.swing.text.DefaultEditorKit;
// import javax.swing.KeyStroke;
import ca.queensu.cs.dal.edfmwk.doc.Document;
import ca.queensu.cs.dal.edfmwk.doc.DocumentType;
import ca.queensu.cs.dal.edfmwk.menu.MenuDescriptor;
// import ca.queensu.cs.dal.data.tree.TreeException;
import ca.queensu.cs.dal.edfmwk.menu.MenuElement;
import ca.queensu.cs.dal.flex.log.Log;
import mainEditor.imageAction.ContrastAction;
import mainEditor.imageAction.FocusAction;
import mainEditor.imageAction.ResizeAction;
import mainEditor.imageAction.TimeDilationAction;
import mainEditor.imageAction.UndoAction;

/**
 * <a href="http://en.wikipedia.org/wiki/Factory_(software_concept)">Factory</a>
 * for representations of image files.
 *<p>
 * Copyright 2010-2011 David Alex Lamb. (Adapted by 2017 Joey Sun)<p>
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public class ImageType implements DocumentType {

    private static MenuDescriptor menu; 							// The descriptor for the editor's main menu.
    private static String[] extensions = { "gif", "png", "jpg" };   // The expected extensions for files the application can edit.
    
    // Hash Map from action names to text-component-specific actions.
    // It should only be considered valid within a single call to {@link #getMenu}
    // private HashMap<String, Action> actions;
	
    /**
     * Construct a new factory for {@link ca.queensu.cs.dal.txt.ImageContents}
     * objects.
     */
    public ImageType() {} //default

    // returns name of what data type is being read
    public String getName() { return "Image File"; }
    
    /**
     * Create and initialize a new representation for an image.
     * @return the new image contents.
     */
    public Document newDocument() {
    	try {
			return new ImageDocument(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Get the descriptor for the menu items appropriate for this type of
     * document.  For example, <code>"Image/Resize"</code> could be one such
     * menu element for an image processing program.  The descriptor must
     * not include type-independent menu items, such as
     * <code>"File/Exit"</code>
     * 
     * Normally the actual {@link ca.queensu.cs.dal.edfmwk.menu.MenuDescriptor}
     * would have been static, but specific actions such as Cut and Paste
     * must be fetched from the {@link javax.swing.JTextArea} embedded in
     * the frame displaying the document.
     * @param doc Document whose state or GUI representation might influence the initial state of the menu.
     */
    public MenuDescriptor getMenu(Document doc) {
    	MenuDescriptor desc = getStaticMenu().copy();
    	Component comp = doc.getWindow();
    	JPanel img = null;
   
    	// makes sure the viewing window is not empty (should be JPanel)
    	if (comp instanceof JPanel) {
    		img = (JPanel) comp;
    	}
    	
    	// if component is not a JLabel, output unexpected message
    	if (img == null) {
    		// an internal error
    		System.err.println("Got unexpected " + comp);
    		return desc;
    	}

	return desc;
    } // getMenu
    
    private MenuDescriptor getStaticMenu() {
    	if (menu==null) {
    	    menu = new MenuDescriptor();
    	    try {
    	    	menu.addElement(new MenuElement("Edit/Contrast/Darken", new ContrastAction("Darken", 0.9f)));
    	    	menu.addElement(new MenuElement("Edit/Contrast/Brighten", new ContrastAction("Brighten", 1.1f)));
    	    	menu.addElement(new MenuElement("Edit/Focus/Sharpen", new FocusAction("Sharpen", 
    	    									new float[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 })));
    	    	menu.addElement(new MenuElement("Edit/Focus/Blur", new FocusAction("Blur", 
    	    			new float[] { 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f })));
    	    	menu.addElement(new MenuElement("Edit/Resize", new ResizeAction("Resize", 
    	    			"Move the slider left to make the image smaller; move the slider right to make it bigger.")));
    	    	menu.addElement(new MenuElement("Edit/Time Dilate/Timey-Wimey", new TimeDilationAction("Timey-Wimey", 
    	    			"Move the slider left to make the animation faster; move the slider right to make it slower")));
    	    	menu.addElement(new MenuElement("Edit/Undo Change", new UndoAction("Undo Change")));
    	    } catch (Exception e) {
    		Log.internalError("Menu element error "+e.getLocalizedMessage());
    	    }
    	}
    	return menu;
        }
        
    /**
     * Get the filename extensions appropriate for this kind of document.
     * @return Array of filename extensions that can be opened.
     */
    public String[] getExtensions() {
        return extensions;
    }

    //================================================================================
    // Region: Leftover debug code from original
    //================================================================================
    /*
    static void debugAction(Object name, Keymap km, Action ac) {
	System.out.print(name);
	if (km!=null) {
	    KeyStroke strokes[] = km.getKeyStrokesForAction(ac);
	    if (strokes == null) {
		System.out.print(" no keystrokes");
	    } else if (strokes.length==0) {
		System.out.print(" zero-length keystrokes");
	    } else {
		for(int j=0; j<strokes.length; j++) {
		    System.out.print(" "+strokes[j]);
		}
	    }
	}
	System.out.println();
    }
    */

    /**
     * Gets the action with a specific name.
     * @param name Name of action to be added.
     */
    /*private Action getNamedAction(String name) {
    	return actions.get(name);
    }*/

    /**
     * Set the {@link #actions} member to contain a list of the actions
     * allowed on the current image component. The actions (might?) embed
     * references to the specific text component, which is why we have to do
     * it over again for each document.
     * @param img The text component from which to retrieve actions.
     */
    /*private void setActions(JLabel img) {
        actions = new HashMap<Object, Action>();
        Action[] actionsArray = img.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
    } // setActions
 	*/
 
    /**
     * Get the descriptor for the menu items appropriate for this type of
     * document.  For example, <code>"Image/Resize"</code> could be one such
     * menu element for an image processing program.  The descriptor must
     * not include type-independent menu items, such as
     * <code>"File/Exit"</code>
     */

} // end class ImageType
