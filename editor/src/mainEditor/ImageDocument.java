package mainEditor;
// $Id: TextDocument.java,v 1.0 2012/10/04 13:57:18 dalamb Exp $
import java.io.*;

//import java.util.*;
import javax.swing.JPanel;

// Import only those classes from edfmwk that are essential, for documentation purposes
import ca.queensu.cs.dal.edfmwk.doc.AbstractDocument;
// import ca.queensu.cs.dal.edfmwk.doc.DocumentType;
// import ca.queensu.cs.dal.edfmwk.doc.DocumentEvent;
import ca.queensu.cs.dal.edfmwk.doc.DocumentException;

/**
 * Implementation of an image document, which is (indirectly) defined in
 * terms of a Swing {@link javax.swing.Document}.
 * <p>
 * Copyright 2010 David Alex Lamb. (Edited by Joey Sun 2017-2018.) <p>
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public class ImageDocument extends AbstractDocument {
	
    private ImageContents contents;		// the contents of the image
    private AnimationPane animation;	// the animation pane
    /**
     * Constructs an image representation for viewing.
     * @param type The type of the document.
     * @throws IOException 
     */
    public ImageDocument(ImageType type) throws IOException {
    	super(type);
    	contents = new ImageContents();
    	animation = new AnimationPane();
    	
    	// connects the animation display pane to the contents of the image
    	// and the contents of the image to the animation display pane
    	contents.connectDisplay(animation);
		animation.setContents(contents);
		
		// the pane responsible for displaying the animation
		JPanel placeholder = new JPanel();
		placeholder.add(animation);
		window = placeholder;	
    } // end ImageDocument

    //================================================================================
    // Region: Document Notifiers (Not used)
    //================================================================================
    /**
     * Gives notification that an attribute or set of attributes changed.
     */
    /*public void changedUpdate(javax.swing.event.DocumentEvent e) {
    	setChanged();
    } // end changedUpdate
	*/
    
    /**
     * Gives notification that there was an insert into the document.
     */
    /*public void insertUpdate(javax.swing.event.DocumentEvent e) {
    	setChanged();
    } // end insertUpdate
	*/

    /**
     * Gives notification that a portion of the document has been removed.
     */
    /*public void removeUpdate(javax.swing.event.DocumentEvent e) {
    	setChanged();
    } // end removeUpdate
	*/

    //================================================================================
    // Region: I/O operations for the Image Contents
    //================================================================================
    /**
     * Saves the entire document.  After this operation completes
     * successfully, {@link #isChanged} returns <b>false</b>
     * @param out Where to write the document.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream; isChanged() is unchanged.
     */
    public void save(OutputStream out) throws IOException {
    	contents.save(out);
    	setChanged(false);
    } // save

    /**
     * Gets an input stream from which the document contents can be read as a
     *  stream of bytes.  This is required when running in a sandbox, where
     *  {@link javax.jnlp.FileSaveService#saveAsFileDialog} does not provide a
     *  means of supplying an output stream to which to write the internal
     *  representation. Document managers should avoid using this method
     *    wherever possible, preferring {@link #save} instead.
     * @throws IOException if such a stream cannot be created.
     */
    public InputStream getContentsStream() throws DocumentException {
    	return contents.getContentsStream();
    } // getContentsStream

    /**
     * Reads the entire document, and closes the stream from which it is read.
     * @param in Where to read the document from.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void open(InputStream in) throws IOException {
    	contents.open(in);
    	setChanged(false);
    } // open

    /**
     * Gets the contents of the image document, for those few methods within
     * this package that need direct access (such as actions).
     * @return the image Contents
     */
    public ImageContents getContents() { 
    	return contents;
    }
    
    /**
     * Allows access to the animation pane. Is used mainly to center input requests 
     * with respect to the animation pane.
     * @return A reference to the animation pane.
     */
    public AnimationPane getAnimationPane() {
		return animation;
    }
    
    //================================================================================
    // Region: Passive Observer To ImageContents (Never actually used)
    //================================================================================
    /**
     * Receives notification when a portion of the image has been changed.
     */
	/*@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ObserverNotice) {
			ObservedState os = ((ObserverNotice) arg).getObservedState();
			
			// finds out what needs to be updated based on the observed state
			switch (os) {
			case OPEN:
				break;
			case CHANGED_ADD:
				break;
			default:
				break;
			}
		}
	}*/
    
} // end class ImagetDocument

