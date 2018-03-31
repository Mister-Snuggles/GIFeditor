package mainEditor.GIFInputOutput;

/**
 *  A GiF reader which is able to extract metadata from GIF animations.
 *  <p>
 *  Adapted by Joey Sun from Elliot Kroo on 2017-11-10<p>
 *   This work is licensed under the Creative Commons Attribution 3.0 Unported
 *   License. To view a copy of this license, visit
 *   http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *   Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */

import javax.imageio.*;
import javax.imageio.metadata.*;

import java.awt.Dimension;
import java.awt.Point;
import java.io.*;

public class GIFSequenceReader {
	
	private IIOMetadata imageMetaData;	//the image meta data from the image reader
	private ImageReader ir;				//the image reader from which metadata is being extracted
	private IIOMetadataNode root;		//the image metadata as a tree
	private int imageNum = 0;			//the indexed image we are looking at in the image reader (starts at 0)
	
	/**
	* Creates a new GifSequenceReader
	* 
	* @param ir The ImageReader which contains info about the reader.
	* @throws IIOException if no gif ImageWriters are found
	*
	* @author Joey Sun (adapted from Ellis Kroo)
	*/
	public GIFSequenceReader(ImageReader ir) throws IIOException, IOException {
		this.ir = ir;
		updateMetaData(0);
	}
	
    /**
     * Updates the metadata structure to indicate which image we want to look at now.
     * @param imageNum The index of the image we wish to look at now.
     * @throws IOException If any I/O errors occur, such as the fact the image at the given index
     * 		cannot be found.
     */
    public void updateMetaData(int imageNum) throws IOException {
    	
    	if((this.imageNum = imageNum) >= 0) {
        	this.imageMetaData =  ir.getImageMetadata(imageNum);
            String metaFormatName = imageMetaData.getNativeMetadataFormatName();

            // gets image metadata as a tree
            this.root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
    	}
    	else {
    		new IOException("This image number requested cannot be negative.");
    	}
    }
	
    /**
     * Gets metadata delay time in milliseconds between frames of the GIF.
     * If delay time is <= 0, which is nonsensical, returns 0.
     * @return the delay time between gifs in milliseconds
     * @throws IOException if any I/O errors occur.
     */
    public int getDelayTime() throws IOException {
        // obtains the Graphics Control Extension Node if it exists from the metadate node
        IIOMetadataNode graphicsControlExtensionNode = GIFInputOutputHelper.getNode(root, "GraphicControlExtension");

        // if delay time is greater than 0, return it. Otherwise, returns a default delay time.
        int delayTime = GIFDefaultMetadata.DELAY_TIME;
        
        try {
        	delayTime = Integer.parseInt(graphicsControlExtensionNode.getAttribute("delayTime"));
        	delayTime = delayTime > 0 ? delayTime : GIFDefaultMetadata.DELAY_TIME;
        }
        catch (NumberFormatException e) {	
        	//System.err.println("No delay time");        	
        }
        return delayTime*10;
    }
    
    /**
     * Gets metadata disposal method for frames of the GIF.
     * If no disposal method, default disposal method.
     * @return the disposal method for gif frames
     */
    public String getDisposalMethod() {
        // obtains the Graphics Control Extension Node if it exists from the metadata node
        IIOMetadataNode graphicsControlExtensionNode = GIFInputOutputHelper.getNode(root, "GraphicControlExtension");

        // return the Disposal Method if it exists. If not, return "none"
        String disposalMethod;
        
        disposalMethod = graphicsControlExtensionNode.getAttribute("disposalMethod");
        disposalMethod = disposalMethod != "" ? disposalMethod : GIFDefaultMetadata.DISPOSAL_METHOD;
        
        return disposalMethod;
    }
    
    /**
     * Gets metadata for image offset of the current frame of the GIF. 
     * If they do not exist, returns default x and y offsets.
     * Any offsets less than 0 are also set to their default values, since
     * no DIF can have a negative offset. 
     * @return The image offset for current GIF frame as a point.
     */
    public Point getImageOffset() {
        // obtains the Image Descriptor Node if it exists from the metadata node
        IIOMetadataNode imageDescriptorNode = GIFInputOutputHelper.getNode(root, "ImageDescriptor");

        // return the current image's x, y offset if it exists. If not, returns default values
        int x=GIFDefaultMetadata.X_OFFSET, y=GIFDefaultMetadata.Y_OFFSET;
        
        try {	//x-offset
        	x = Integer.parseInt(imageDescriptorNode.getAttribute("imageLeftPosition"));
        	x = x >= 0 ? x : (int)GIFDefaultMetadata.X_OFFSET;
            //System.err.println("a:" + imageDescriptorNode.getAttribute("imageLeftPosition"));
        }
        catch (NumberFormatException e) {	
        	//System.err.println("No x-offset");
        }
        
        try {	//y-offset
        	y = Integer.parseInt(imageDescriptorNode.getAttribute("imageTopPosition"));
        	y = y >= 0 ? y : (int)GIFDefaultMetadata.Y_OFFSET;
        	//System.err.println(imageDescriptorNode.getAttribute("imageTopPosition"));
        }
        catch (NumberFormatException e) {	
        	//System.err.println("No y-offset");
        }
        
        return new Point(x,y);
    }

    /**
     * Gets metadata for the size of the current frame of the GIF.
     * If no dimensions, returns default height and widths.;
     * @return The dimensions for current GIF frame.
     */
	public Dimension getImageDimension() {
        // obtains the Image Descriptor Node if it exists from the metadata node
        IIOMetadataNode imageDescriptorNode = GIFInputOutputHelper.getNode(root, "ImageDescriptor");

        // return the current image's dimensions if it exists. If not, returns default values.
        int width=GIFDefaultMetadata.WIDTH, height=GIFDefaultMetadata.HEIGHT;
        
        try {	//width
        	width = Integer.parseInt(imageDescriptorNode.getAttribute("imageWidth"));
        	width = width >= 0 ? width : GIFDefaultMetadata.WIDTH;
            //System.err.println("a:" + imageDescriptorNode.getAttribute("imageLeftPosition"));
        }
        catch (NumberFormatException e) {	
        	//System.err.println("No x-offset");
        }
        
        try {	//height
        	height = Integer.parseInt(imageDescriptorNode.getAttribute("imageHeight"));
        	height = height >= 0 ? height : GIFDefaultMetadata.HEIGHT;
        	//System.err.println(imageDescriptorNode.getAttribute("imageTopPosition"));
        }
        catch (NumberFormatException e) {	
        	//System.err.println("No y-offset");
        }
        
        return new Dimension(width, height);
	}
    
	/**
	 * Returns an existing child node, or creates and returns a new child node (if 
	 * the requested node does not exist).
	 * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
	 * @param nodeName the name of the child node.
	 * @return the child node, if found or a new node created with the given name.
	 */
    public static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) { // traverses metadata tree
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)== 0) { // node is found
            	return((IIOMetadataNode) rootNode.item(i));
            }
        }
        
        // if a node with the given name cannot be found, create one, append it to the root node, and return it.
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }
    
	/**
	 * Returns the index of the image whose metadata we are looking at in the image reader.
	 * 
	 * @return the index of the current image's metadata we are looking at.
	 */
    public int getImageNum() {
        return this.imageNum;
    }

}
