package mainEditor.GIFInputOutput;

//
// GifSequenceWriter.java
//
// Created by Elliot Kroo on 2009-04-25.
// Adapted by Joey Sun on 2017-11-18
//
// This work is licensed under the Creative Commons Attribution 3.0 Unported
// License. To view a copy of this license, visit
// http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
// Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;

import java.awt.Point;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

/**
 * A GIF sequence writer.
 *
 * @author Elliot Kroo (elliot[at]kroo[dot]net)
 */
public class GIFSequenceWriter {
	private ImageWriter gifWriter;				//the GIF writer being used (default GIF writer)
	private ImageWriteParam imageWriteParam;	//the description on how a writing stream should be encoded
	private IIOMetadata imageMetaData;			//the image meta data from the image reader

	/**
	* Creates a new GifSequenceWriter
	* 
	* @param outputStream			The ImageOutputStream to be written to
	* @param imageType 				One of the imageTypes specified in BufferedImage
	* @param disposalMethod			How the GIF should deal with frame animation
	* @param loopContinuously 		Whether the gif should loop repeatedly
	* @throws IIOException if no gif ImageWriters are found
	*/
	public GIFSequenceWriter(	ImageOutputStream outputStream,
								int imageType, 
								String disposalMethod,
								boolean loopContinuously) throws IIOException, IOException {
		// the method to create a writer
		gifWriter = getWriter(); 
		
		// gets the default metadata of the GIF writer and the image type we are using to create the GIF.
		// also details how the output stream will be encoded (as a GIF)
		imageWriteParam = gifWriter.getDefaultWriteParam();
		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
		
		// gets the image metadata for every frame in the GIF
		imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

		String metaFormatName = imageMetaData.getNativeMetadataFormatName();

		// gets image metadata as a tree and that obtains the Graphics Control Extension Node if it exists
		IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
		IIOMetadataNode graphicsControlExtensionNode = GIFInputOutputHelper.getNode(root, "GraphicControlExtension");

		// sets some metadata attributes
		graphicsControlExtensionNode.setAttribute("disposalMethod", disposalMethod);
		graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("transparentColorFlag", "TRUE");
		graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");
		
		// places comments on what created the gif and other things
		IIOMetadataNode commentsNode = GIFInputOutputHelper.getNode(root, "CommentExtensions");
		commentsNode.setAttribute("CommentExtension", "Created by MAH");

		// gets the Applications Extensions Node if it exists
		IIOMetadataNode appExtensionsNode = GIFInputOutputHelper.getNode(root, "ApplicationExtensions");
		IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

		child.setAttribute("applicationID", "NETSCAPE");
		child.setAttribute("authenticationCode", "2.0");

		// notifies gif to either run continuously or not continuously
		int loop = loopContinuously ? 0 : 1;

		// does something magical beyond my knowledge to tell GIF to loop
		child.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
		appExtensionsNode.appendChild(child);

		// sets the changed tree metadata data back into the image metadata
		imageMetaData.setFromTree(metaFormatName, root);
		
		//the output stream which to output the GIF to
		gifWriter.setOutput(outputStream);
		gifWriter.prepareWriteSequence(null);
	}

	/**
	 * Writes an image frame to the GIF with no thumbnail and the metadata defined at beginning of the
	 * creation of this writer.
	 * @param img Image to write as next frame in GIF.
	 * @throws IOException if there is no way to write to the output stream
	 */
	public void writeToSequence(RenderedImage img) throws IOException {
		gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
	}
	
	/**
	 * Sets the metadata for the offset of some frame and its delay in the GIF.
	 * @param offset The offset of the frame from top left corner.
	 * @param delayTime The number of milliseconds this frame is to remain on-screen.
	 * @throws IOException if there is no way to write to the output stream
	 */
	public void setImageOffsetDelay(Point offset, int delayTime) throws IIOException {
		String metaFormatName = imageMetaData.getNativeMetadataFormatName();

		// gets image metadata as a tree and that obtains the Image Descriptor Extension Node if it exists
		IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
		IIOMetadataNode imageDescriptorNode = GIFInputOutputHelper.getNode(root, "ImageDescriptor");

		imageDescriptorNode.setAttribute("imageLeftPosition", String.valueOf((int)offset.getX()));
		imageDescriptorNode.setAttribute("imageTopPosition", String.valueOf((int)offset.getY()));
		
		// obtains the Graphics Control Extension Node if it exists
		IIOMetadataNode graphicsControlExtensionNode = GIFInputOutputHelper.getNode(root, "GraphicControlExtension");
		
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delayTime / 10));
		
		// sets the changed tree metadata data back into the image metadata
		imageMetaData.setFromTree(metaFormatName, root);
		
		//System.err.println(imageDescriptorNode.getAttribute("imageLeftPosition"));
	}

	/**
	 * Close this GifSequenceWriter object. This does not close the underlying
	 * stream, just finishes off the GIF.
	 */
	public void close() throws IOException {
		gifWriter.endWriteSequence();    
	}

	/**
	 * Returns the first available GIF ImageWriter using ImageIO.getImageWritersBySuffix("gif").
	 * 
	 * @return a GIF ImageWriter object
	 * @throws IIOException if no GIF image writers are returned
	 */
	private static ImageWriter getWriter() throws IIOException {
		Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
		if(!iter.hasNext()) { // makes sure a GIF Image Write exists before returning it
			throw new IIOException("No GIF Image Writers Exist");
		} else {
			return iter.next();
		}
	}
	
}
