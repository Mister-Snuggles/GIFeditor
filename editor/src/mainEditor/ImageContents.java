package mainEditor;

import java.awt.Dimension;
import java.awt.Window;
// $Id: ImageContents.java,v 1.0 2012/10/04 13:57:18 dalamb Exp $
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

//import java.util.*;
import ca.queensu.cs.dal.edfmwk.doc.DocumentException;
import mainEditor.GIFInputOutput.GIFSequenceReader;
import mainEditor.GIFInputOutput.GIFSequenceWriter;
import mainEditor.observerObject.ObservedState;
import mainEditor.observerObject.ObserverNotice;

/**
 * Internal representation of an image document.
 * <p>
 * Copyright 2010-2011 David Alex Lamb. (Edited by Joey Sun 2017-2018.)<p>
 * See the <a href="../doc-files/copyright.html">copyright notice</a> for details.
 */
public class ImageContents extends Observable{
    private ArrayList<ImageFrameData> imageData;		// The array list of original image frame data 
    private ArrayList<ImageFrameData> changedImageData;	// The array list of changed image frame data 
	private String disposalMethod = "none";				// The way the GIF handles frame animation
    private String currExtension = "gif";				// extension of the file originally from source
    
    private int DEFAULT_WIDTH = 500;					// default width of blank image
    private int DEFAULT_HEIGHT = 500;					// default height of blank image
    

    /**
     * Constructs an empty image contents class, which is by default only able to open "GIF" files.
     * @param iv The image document that will be notified when the contents change.
     */
    public ImageContents() {
    	super();
    	this.imageData = new ArrayList<ImageFrameData>();
    	BufferedImage temp = BufferedImageHelper.getNewImageARGB(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    	imageData.add(0, new ImageFrameData(temp, new Dimension (DEFAULT_WIDTH, DEFAULT_HEIGHT)));
    	this.changedImageData = this.imageData;
    	
    	setChanged();
    } // end constructor

    //================================================================================
    // Region: Readers and Writers
    //================================================================================
    /**
     * Reads the entire document, and closes the stream from which it is read.
     * @param in Where to read the document from.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void open(InputStream in) throws IOException
    {
    	//System.err.println("Open...");	
    	this.currExtension = URLConnection.guessContentTypeFromStream(in);
    	
    	if (this.currExtension == null) {
    		//    throw new IOException(e);
    		throw new IOException("CANNOT GUESS/GET IMAGE EXTENSION");
    	}
    	
    	//truncates file extension to remove image
    	this.currExtension = this.currExtension.substring(this.currExtension.indexOf("/")+1);	
    	
    	//System.err.println(this.currExtension);
    	
    	//image reader for doing imagey things (reading frames of gif, etc)
    	ImageReader ir = (ImageReader)ImageIO.getImageReadersByFormatName(this.currExtension).next();
    	ir.setInput(ImageIO.createImageInputStream(in));
        
    	// number of frames in the GIF
        int numImages = ir.getNumImages(true);
        ImageFrameData frameData;
        
        // if there are more than 0 frames, create new image frame data array
        if (numImages > 0) {
        	this.imageData = new ArrayList<ImageFrameData>();
        }
        
        //if there is less than one image, there is no need for a GIF reader
        if (numImages < 2) {
        	BufferedImage temp = ir.read(0);
        	
        	frameData = new ImageFrameData(temp, new Dimension(temp.getWidth(), temp.getHeight()));
        	this.imageData.add(frameData);
        }
        else { // otherwise, there is a need for a GIF reader
        	// GIF reader to help extract metadata from the GIF
        	GIFSequenceReader gifReader = new GIFSequenceReader(ir);
        	
        	// extracts delay time between frames and the disposal method for frames of the GIF, if it exists
        	this.disposalMethod = gifReader.getDisposalMethod();
        	
        	for(int i = 0; i < numImages; i++) {
        		gifReader.updateMetaData(i);
        		frameData = new ImageFrameData(ir.read(i), gifReader.getImageOffset(),
        										gifReader.getImageDimension(), gifReader.getDelayTime());
            	this.imageData.add(frameData);
            	//System.err.println("Reading frame...");
            }
        }
        
        //System.err.println(in.available());
        
        // if the image read is empty, throw an error
    	if(this.imageData.isEmpty()) {
    		//		throw new IOException(e);
    		throw new IOException("NO SUCH FILE / CANNOT READ IMAGE FROM INPUT SOURCE " + in);
    	}
    	
    	this.changedImageData = this.imageData;
    	this.updateChangedImageFrameData(false);
    	setChanged();
    	notifyObservers(new ObserverNotice(ObservedState.OPEN));
    	//System.err.println("Done open");
    } // end method open
    
    /**
     * Writes the entire document to some output stream. output stream is only closed if an error is thrown.
     * @param out Where to write the document to.
     * @param ext The extension of the image file type to save as.
     * @throws IOException 
     */
    private void write(OutputStream out, String ext) throws IOException
    {	//System.err.println("Writing to " + ext + "...");
    
    	//if there are several images, create a GIF
    	if (this.imageData.size() > 1) {
    		GIFSequenceWriter gsw = new GIFSequenceWriter( ImageIO.createImageOutputStream(out), 
    														BufferedImageHelper.ARGB,
    														this.disposalMethod, true);
    		
    		// finalizes all changes to the frames and then saves it
    	    writeToGIF(gsw);
    	    
    	    gsw.close();
    	}
    	else if (this.imageData.size() == 1) { //creates a single image if there is just one image
    		ImageFrameData endFrame = this.changedImageData.get(0);
    		ImageIO.write(endFrame.getImage(), ext, out);
    	}
    	else { // throws exception, since no image to save
    		throw new IOException ("There is no image to save! >:)");
    	}
    	
    	setChanged();
    	notifyObservers(new ObserverNotice(ObservedState.WRITE));
    	//System.err.println("Written.");
    } // end method write

	/**
	 * Writes an entire animation GIF with no thumbnail, which uses the offset of
	 * each frame in its construction
	 * @param gsw The GIF sequence writer, which is going to write images to some output stream.
	 * @throws IOException if there is no way to write to the output stream
	 */
	public void writeToGIF(GIFSequenceWriter gsw) throws IOException {
		int numFrames = this.getNumberOfFrames();
		
		for(int i = 0; i < numFrames; i++) {	// writes to GIF output location using GIF writer
			ImageFrameData singleFrameImage = changedImageData.get(i);
			gsw.setImageOffsetDelay(singleFrameImage.getOffset(), singleFrameImage.getDelayTime());
			gsw.writeToSequence(singleFrameImage.getImage());
		}
	} // end method write to GIF
    
    /**
     * Saves the entire document.
     * @param out Where to write the document.
     * @throws IOException if any I/O errors occur, in which case it will have
     * closed the stream.
     */
    public void save(OutputStream out) throws IOException {
    	try {
    		write(out, this.currExtension);
    	} catch (Exception e) {
    		out.close();
    		//	    throw new IOException(e);
    		throw new IOException(e.getLocalizedMessage());
    	}
    	
    	setChanged();
    	notifyObservers(new ObserverNotice(ObservedState.SAVE));
		TipBox.saved(null);
    } // end save

    /**
     * Gets an input stream from which the document contents can be read as a
     *  stream of bytes.  This is required when running in a sandbox, where
     *  {@link javax.jnlp.FileSaveService#saveAsFileDialog} does not provide a
     *  means of supplying an output stream to which to write the internal
     *  representation. Document managers should avoid using this method
     *    wherever possible, preferring {@link #save} instead.
     * @throws DocumentException if such a stream cannot be created.
     */
    public InputStream getContentsStream() throws DocumentException
    {
    	try {
    		// return new StringBytesInputStream(this);
    		byte[] buffer = ((DataBufferByte)(this.imageData.get(0).getImage()).getRaster().getDataBuffer()).getData();
    		return new ByteArrayInputStream(buffer);
    	} catch (Exception e) {
    		throw new DocumentException(e);
    	}
    } // end getContentStream
    
    //================================================================================
    // Region: Connectors for communication
    //================================================================================
    /**
     * Adds an Animation Pane as an observer to the contents of the image.
     * @param iv The image viewer that will be notified when the contents change.
     */
    public void connectDisplay(AnimationPane animPane) {
    	addObserver(animPane);
    } // end connect display
    
    /**
     * Notifies the image contents object that it has been changed somehow. Receives
     * window that the change is being sent to, so that warnings/information dialogs
     * could be used.
     * @param win The window that the change is being sent to.
     * @param isAdd Notes whether the change is a retraction or addition.
     */
    public void changeDone(Window win, boolean isAdd) {
    	updateChangedImageFrameData(isAdd);
    	setChanged();
    	
    	// keeps track of whether the change is an add or an undo
    	ObservedState os;
    	if (isAdd) {
    		os = ObservedState.CHANGED_ADD;
    	}
    	else {
    		os = ObservedState.CHANGED_UNDO;
    	}
    	
    	notifyObservers(new ObserverNotice(os, win, false));
    }

    //================================================================================
    // Region: Attribute Getters and Setters
    //================================================================================
    /**
     * Gets disposal method for animation frames in GIF.
     * @return The disposal method for treating frames in the animation.
     */
    public String getDisposalMethod() {
        return this.disposalMethod;
    }
	
    /**
     * Gets changed image frame data as an array list of image frame data objects.
     * @return The array list of changed image frame datas.
     */
    public ArrayList<ImageFrameData> getChangedData() {
    	return this.changedImageData;
    } // end getData
    
    /**
     * Updates the changed image frame data, depending on whether there was an addition or undo.
     * @param isAdd Notes if the change was a simply addition, (so we do less work),
	 * 				or if it was a retraction (all histories in each frame must be applied to itself)
     */
    private void updateChangedImageFrameData(boolean isAdd) {
    	this.changedImageData = AnimationDisplayHelper.baseLineFlatten(this.imageData, this.changedImageData,
    																disposalMethod, isAdd);
    }
    
    /**
     * Gets original image frame data as an array list of image frame data objects.
     * @return The array list of original image frame datas.
     */
    public ArrayList<ImageFrameData> getData() {
    	return this.imageData;
    } // end getData
    
    /**
     * Gets the number of frames of the animation/image.
     * @return The number of frames of the animation/image.
     */
    public int getNumberOfFrames() {
    	return this.changedImageData.size();
    } // end getData
    
    
    /**
     * Returns if the image frame data is empty.
     * @return Boolean which details if there are frames in the image.
     */
    public Boolean isEmpty() {
    	return this.imageData.isEmpty();
    } // end getData



} // end TextContents
