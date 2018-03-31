package mainEditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * A helper class that contains some operations on buffered images.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public final class BufferedImageHelper {
	
    static final int ARGB = BufferedImage.TYPE_INT_ARGB;		// default image type for buffered images
    static final int RGB = BufferedImage.TYPE_INT_RGB;			// default image type for buffered images
	
    //================================================================================
    // Region: Clean Buffered Image of any Type
    //================================================================================
	/**
	 * Returns a new clean buffered image with the given image dimensions and type.
	 * @param img The buffered image to get the width, height, and type from for the clean buffered image.
	 * @return A clean buffered image with the same dimensions and type as given image.
	 */
	public static BufferedImage getNewImage(BufferedImage img) {
		return new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
	}
	
	/**
	 * Returns a new clean buffered image with the given dimensions and type.
	 * @param width The width of the clean buffered image
	 * @param height The height of the clean buffered image
	 * @param imgType The type of buffered image this new image should be.
	 * @return A clean buffered image with the same dimensions and type as given image.
	 */
	public static BufferedImage getNewImage(int width, int height, int imgType) {
		return new BufferedImage(width, height, imgType);
	}
	
    //================================================================================
    // Region: Clean Buffered Images of Other Types
    //================================================================================
	/**
	 * Returns a new clean buffered image with the given image dimensions and RGB color model.
	 * @param img The buffered image to get the width, and height from for the clean buffered image.
	 * @return A clean buffered image with the same dimensions and type as given image.
	 */
	public static BufferedImage getNewImageRGB(BufferedImage img) {
		return new BufferedImage(img.getWidth(), img.getHeight(), RGB);
	}	
	
	/**
	 * Returns a new clean buffered image with the given image dimensions and ARGB color model.
	 * @param img The buffered image to get the width, and height from for the clean buffered image.
	 * @return A clean buffered image with the same dimensions and type as given image.
	 */
	public static BufferedImage getNewImageARGB(BufferedImage img) {
		return new BufferedImage(img.getWidth(), img.getHeight(), ARGB);
	}
	
	/**
	 * Returns a new clean buffered image with the given image dimensions and ARGB color model.
	 * @param width The width of the clean buffered image
	 * @param height The height of the clean buffered image
	 * @return A clean buffered image with the same dimensions and type as given image.
	 */
	public static BufferedImage getNewImageARGB(int width, int height) {
		return new BufferedImage(width, height, ARGB);
	}
	
	
    //================================================================================
    // Region: Deep copy of Images
    //================================================================================
	/**
	 * Returns a deep copy of a buffered image.
	 * @return A deep copy of a buffered image.
	 * 
	 * @see also https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	 */
	public static BufferedImage deepCopy(BufferedImage img) {
		 ColorModel cm = img.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = img.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	/**
	 * Returns a deep copy of a buffered image with ARGB color model.
	 * @return A deep copy of a buffered image with ARGB color model.
	 * 
	 * @see also https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	 */
	public static BufferedImage deepCopyARGB(BufferedImage img) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), ARGB);
		
		Graphics g = image.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		return image;
	}
	
	/**
	 * Returns a deep copy of a buffered image with RGB color model.
	 * @return A deep copy of a buffered image with RGB color model.
	 * 
	 * @see also https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	 */
	public static BufferedImage deepCopyRGB(BufferedImage img) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), RGB);
		
		Graphics g = image.getGraphics();
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		return image;
	}
}
