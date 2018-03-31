package mainEditor.history;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import mainEditor.BufferedImageHelper;
import mainEditor.ImageFrameData;

/**
 * The enumeration of metadata changes that are permitted. 
 * 
 * Copyright 2017-2018 Joey Sun.
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public enum ImageMetadataHistoryType {
	RESIZE {
		@Override
		public ImageFrameData operate(ImageFrameData newData, float ratio) {
			// extract offset and dimension information from image frame data
			Dimension dimens = newData.getDimensions();
			Point offset = newData.getOffset();
			
			// creates rescaled dimensions and offset
			int newWidth = (int)(Math.ceil(dimens.getWidth()*ratio));
			int newHeight = (int)(Math.ceil(dimens.getHeight()*ratio));
			Dimension newDimens = new Dimension(newWidth, newHeight);
			Point newOffset = new Point((int)(offset.getX()*ratio), (int)(offset.getY()*ratio));
			
			// sets new dimensions and offset
			try {
				newData.setDimensions(newDimens);
				newData.setOffset(newOffset);
				
				// re-scales the image
				BufferedImage img = newData.getImage();
			    Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			    BufferedImage outputImg = BufferedImageHelper.getNewImageARGB(newWidth, newHeight);

			    Graphics2D g2d = outputImg.createGraphics();
			    g2d.drawImage(tmp, 0, 0, null);
			    g2d.dispose();

			    newData.setImage(outputImg);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		    
		    return newData;
		} // end of operate
	}, 
	RETIME {
		@Override
		public ImageFrameData operate(ImageFrameData newData, float ratio) {
			// extract delay time information from image frame data
			int delayTime = newData.getDelayTime();
			
			// sets new delayTime
			try {
				newData.setDelayTime((int)(delayTime*ratio), true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
			
			return newData;
			
		} // end of operate
	};

	public abstract ImageFrameData operate(ImageFrameData newData, float ratio);
}
