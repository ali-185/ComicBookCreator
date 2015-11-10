 /*
  * Comic Book Creator - A program for creating a comic book photo album.
  * Copyright (C) 2013  Alastair Crowe
  *
  * This code is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version 2 only, as
  * published by the Free Software Foundation.
  *
  * This code is distributed in the hope that it will be useful, but WITHOUT
  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
  * version 2 for more details.
  
  * You should have received a copy of the GNU General Public License version
  * 2 along with this work; if not, write to the Free Software Foundation,
  * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
  *
  * Please contact comicbookhelp@gmail.com if you need additional information
  * or have any questions.
  */
package comicBookModel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * An ImageLayer is a Layer which has an PositionedImage for it's contents. 
 * The PositionedImage can have greyscale and/or halftone effects applied.
 * @see Layer
 * @see PositionedImage
 * @see PositionedImage#toGreyscale()
 * @see PositionedImage#toHalftone(int)
 * 
 * @author Alastair Crowe
 */
public class ImageLayer extends Layer {
    /* The ImageLayer may hold up to 4 different copies of this image
     * internally, each with different effects applied. This is to
     * ensure the effect is only recalculated when it is changed.
     */
    
    // Types of effects that can be applied to the PositionedImage
    public static final String GREYSCALE = "Grey Scale Image";
    public static final String RGB_HALFTONE = "RGB Halftone Image";
    public static final String BW_HALFTONE = "BW Halftone Image";
    
    private PositionedImage IMAGE;
    private PositionedImage GREYSCALE_IMAGE;
    private PositionedImage RGB_HALFTONE_IMAGE;
    private PositionedImage BW_HALFTONE_IMAGE;
    private int HALFTONE_SIZE = -1;
    private String type;
    /**
     * Creates a ImageLayer with neither greyscale or halftone effects.
     * @see Layer#Layer(int, int)
     */
    ImageLayer(int width, int height) {
        super(width, height);
    }
    /**
     * Returns this ImageLayers images effects setting as a String. Null is 
     * returned if the image does not have any effects applied.
     */
    public String getType() {
        return this.type;
    }
    /**
     * Sets this ImageLayers images effects setting. Null indicates no effects 
     * are to be applied.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Sets this ImageLayers halftone size.
     * @param size Height and width of each halftone area
     * @see PositionedImage#toHalftone(int)
     */
    public void setHalftoneSize(int size){
        if(size < 2){
            throw new IllegalArgumentException();
        }
        if(size != this.HALFTONE_SIZE){
            this.RGB_HALFTONE_IMAGE = null;
            this.BW_HALFTONE_IMAGE = null;
        }
        this.HALFTONE_SIZE = size;
    }
    /**
     * @return This ImageLayers halftone size
     */
    public int getHalftoneSize(){
        return this.HALFTONE_SIZE;
    }
    /**
     * @return This ImageLayers PositionedImage with any effects applied if 
     * applicable.
     */
    public PositionedImage getImage(){
        if(this.type == null){
            return getDefault();
        }else if(this.type == GREYSCALE){
            return getGreyscale();
        }else if(this.type == RGB_HALFTONE){
            return getRGBHalftone();
        }else if(this.type == BW_HALFTONE){
            return getBWHalftone();
        }else{
            assert false;
            return null;
        }
    }
    /**
     * @return This ImageLayers PositionedImage with no effects applied.
     */
    public PositionedImage getDefault(){
        return this.IMAGE;
    }
    /**
     * @return This ImageLayers PositionedImage with the greyscale effect 
     * applied. Returns Null if no image has been set.
     * @see PositionedImage#toGreyscale()
     */
    public PositionedImage getGreyscale(){
        if(this.IMAGE == null){
            return null;
        }else if(this.GREYSCALE_IMAGE == null){
            this.GREYSCALE_IMAGE = this.IMAGE.toGreyscale();
        }
        return this.GREYSCALE_IMAGE;
    }
    /**
     * @return This ImageLayers PositionedImage with the halftone effect 
     * applied. Returns Null if no image has been set
     * @see PositionedImage#toHalftone(int)
     */
    public PositionedImage getRGBHalftone(){
        if(this.IMAGE == null){
            return null;
        }else if(this.RGB_HALFTONE_IMAGE == null){
            this.RGB_HALFTONE_IMAGE = this.IMAGE.toHalftone(this.HALFTONE_SIZE);
        }
        return this.RGB_HALFTONE_IMAGE;
    }
    public void setRGBHalftone(PositionedImage rgbHalftoneImage, int halftoneSize){
        this.setHalftoneSize(halftoneSize);    // This nulls current images if different
        this.RGB_HALFTONE_IMAGE = rgbHalftoneImage;
    }
    public PositionedImage getBWHalftone(){
        if(this.IMAGE == null){
            return null;
        }else if(this.BW_HALFTONE_IMAGE == null){
            this.BW_HALFTONE_IMAGE = this.getGreyscale().toHalftone(this.HALFTONE_SIZE);
        }
        return this.BW_HALFTONE_IMAGE;
    }
    public void setBWHalftone(PositionedImage bwHalftoneImage, int halftoneSize){
        this.setHalftoneSize(halftoneSize);    // This nulls current images if different
        this.BW_HALFTONE_IMAGE = bwHalftoneImage;
    }
    /**
     * Returns true if this ImageLayers PositionedImage has already been set. 
     */
    public boolean imageSet(){
        return this.IMAGE == null;
    }
    /**
     * Returns true if the greyscale copy of this ImageLayers
     * PositionedImage has already been calculated. 
     */
    public boolean greyscaleSet(){
        return this.GREYSCALE_IMAGE == null;
    }
    /**
     * Returns true if the rgb halftone copy of this ImageLayers
     * PositionedImage has already been calculated. 
     */
    public boolean rgbHalftoneSet(){
        return this.RGB_HALFTONE_IMAGE == null;
    }
    /**
     * Returns true if the black and white halftone copy of this ImageLayers
     * PositionedImage has already been calculated. 
     */
    public boolean bwHalftoneSet(){
        return this.BW_HALFTONE_IMAGE == null;
    }
    /**
     * Sets the PositionedImage for this ImageLayers contents.
     */
    public void setImage(PositionedImage image){
        // Set the image
        IMAGE = image;
        GREYSCALE_IMAGE = null;
        RGB_HALFTONE_IMAGE = null;
        BW_HALFTONE_IMAGE = null;
        // Set the type as default
        this.type = null;
        // Scale the image to fit inside the border without stretching
        Rectangle rect = BORDER.getBounds();
        double wFactor = (double) rect.width / (double) image.getWidth(); 
        double hFactor = (double) rect.height / (double) image.getHeight(); 
        double factor = (wFactor > hFactor) ? wFactor : hFactor;
        CONTENTS_WIDTH = (int) (image.getWidth() * factor);
        CONTENTS_HEIGHT = (int) (image.getHeight() * factor);
        // Set the image to be centred within border
        int x = (int) (rect.getLocation().x + (rect.width - CONTENTS_WIDTH)/2d);
        int y = (int) (rect.getLocation().y + (rect.height - CONTENTS_HEIGHT)/2d);
        CONTENTS_POSITION = new Point(x, y);
    }
    /**
     * Returns a BufferedImage of this ImageLayer.
     */
    public BufferedImage toImage(){
        // Create the image to return as an alpha mask
        BufferedImage newImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        // Clip to the border
        g.setClip(BORDER);
        // Fill the background
        g.setPaint(Color.WHITE);
        g.fill(BORDER);
        // Draw the requested image inside the border
        if(IMAGE != null){
            g.drawImage(getImage(), CONTENTS_POSITION.x, CONTENTS_POSITION.y, CONTENTS_WIDTH, CONTENTS_HEIGHT, null);
        }
        // Reset the clipping region
        g.setClip(0, 0, getWidth(), getHeight());
        // Draw the border
        drawBorderGrid(g);
        g.dispose();
        return newImage;
    }
}
