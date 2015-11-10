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
import java.awt.Point;
import java.awt.image.BufferedImage;
/**
 * A PositionedImage is an BufferedImage with an additional origin property.
 * The origin is always non-negative, and is initially the point (0,0), however
 * it may move with rotations and shearing of the image (but will correspond
 * to the pixel that was originally at (0,0). This makes rotations and shears
 * @see java.awt.image.BufferedImage
 * 
 * @author Alastair.Crowe
 */
public class PositionedImage extends BufferedImage {
    // Origin is always non-negative
    private Point origin;
    
    /**
     * Creates a new blank PositionedImage with the origin at (0,0).
     * @param imageType PositionedImage.TYPE_BYTE_GRAY for greyscale images or 
     * PositionedImage.TYPE_INT_RGB or RGB
     * @throws IllegalArgumentException If the imageType is neither 
     * PositionedImage.TYPE_BYTE_GRAY or PositionedImage.TYPE_INT_RGB
     * image The BufferedImage this PositionedImage will be a copy of.
     */
    public PositionedImage(int width, int height, int imageType) {
        super(width, height, imageType);
        if(imageType != PositionedImage.TYPE_BYTE_GRAY &&
           imageType != PositionedImage.TYPE_INT_RGB){
            throw new IllegalArgumentException();
        }
        this.origin = new Point(0, 0);
    }
    /**
     * Creates a new PositionedImage copy of a BufferedImage with the origin at (0,0).
     * @param image The BufferedImage this PositionedImage will be a copy of.
     * @param imageType PositionedImage.TYPE_BYTE_GRAY for greyscale images or 
     * PositionedImage.TYPE_INT_RGB or RGB
     * @throws IllegalArgumentException If the imageType is niether 
     * PositionedImage.TYPE_BYTE_GRAY or PositionedImage.TYPE_INT_RGB
     * image The BufferedImage this PositionedImage will be a copy of.
     */
    public PositionedImage(BufferedImage image, int imageType) {
        this(image.getWidth(), image.getHeight(), imageType);
        this.addImage(0, 0, image);
    }
    /**
     * Creates a new PositionedImage copy of this PositionedImage image.
     * @param image The PositionedImage this PositionedImage will be a copy of.
     * @param imageType PositionedImage.TYPE_BYTE_GRAY for greyscale images or 
     * PositionedImage.TYPE_INT_RGB or RGB
     * @throws IllegalArgumentException If the imageType is niether 
     * PositionedImage.TYPE_BYTE_GRAY or PositionedImage.TYPE_INT_RGB
     * image The BufferedImage this PositionedImage will be a copy of.
     */
    public PositionedImage(PositionedImage image, int imageType) {
        this(image.getWidth(), image.getHeight(), imageType);
        this.addImage(0, 0, image);
        int x = ((PositionedImage) image).origin.x;
        int y = ((PositionedImage) image).origin.y;
        this.origin = new Point(x, y);
    }
    
    /**
     * Shifts this PositionedImage such that the point (0,0) will line on the
     * origin. The origin coordinates are not changed.
     */
    public PositionedImage reset(int width, int height){
        // Goes through the image, shifting each poing. Since the origin is 
        // always non-negative, this won't overwrite itself.
        PositionedImage image = new PositionedImage(width, height, this.getType());
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                image.setRGB(x, y, this.getRGB(x + origin.x, y + origin.y));
            }
        }
        return image;
    }
    /**
     * @return A new PositionedImage which is a greyscale (TYPE_BYTE_GRAY)
     * copy of this PositionedImage.
     */
    PositionedImage toGreyscale(){
        PositionedImage greyscale = new PositionedImage(this.getWidth(), this.getHeight(), PositionedImage.TYPE_BYTE_GRAY);
        greyscale.addImage(0, 0, this);
        return greyscale;
    }
    // Shape Drawing methods section
    /**
     * Sets the pixel at the specified coordinate (x,y) to the RGB Color.
     */
    private void addDot(int x, int y, Color RGB){
        // Get the corresponding Color on this
        Color tRGB = new Color(this.getRGB(x, y));
        // Get the new values
        int nRed = tRGB.getRed() + RGB.getRed();
        int nGreen = tRGB.getGreen() + RGB.getGreen();
        int nBlue = tRGB.getBlue() + RGB.getBlue();
        // Check they aren't maxed out
        if(nRed > 255){nRed = 255;}
        if(nGreen > 255){nGreen = 255;}
        if(nBlue > 255){nBlue = 255;}
        // Set the pixel
        Color nColor = new Color(nRed, nGreen, nBlue);
        int nRGB = nColor.getRGB();
        this.setRGB(x, y, nRGB);
    }
    /**
     * Adds the image ontop of this PositionedImage with it's top left corner 
     * at the specified coordinate (x,y). If the coordinate is not on this 
     * PositionedImage, or the images boundaries exceeds this one, then it is 
     * cut off at the boundaries of this PositionedImage.
     */
    private void addImage(int x, int y, BufferedImage image){
        // Go through the pixels one at a time
        for(int i = 0; i < image.getWidth() && x + i < this.getWidth(); i++){
            for(int j = 0; j < image.getHeight() && y + j < this.getHeight(); j++){
                // Add the pixel in the new image
                Color iRGB = new Color(image.getRGB(i, j));
                this.addDot(x+i, y+j, iRGB);
            }
        }
    }
    /**
     * Draws a circle, with radius r and centred at the specified 
     * coordinate (x,y), onto this PositionedImage.
     */
    void addCircle(int x, int y, double r, Color color){
        // Make sure region within bounds of image
        int startX = x - (int) r < 0 ? 0 : x - (int) r;
        int endX = x + (int) r + 1 < this.getWidth() ? x + (int) r + 1 : this.getWidth() - 1;
        int startY = y - (int) r < 0 ? 0 : y - (int) r;
        int endY = y + (int) r + 1 < this.getHeight() ? y + (int) r + 1 : this.getHeight() - 1;
        // Go through each pixel
        for(int i = startX; i <= endX; i++){
            for(int j = startY; j <= endY; j++){
                // If inside circle
                if((i-x)*(i-x) + (j-y)*(j-y) <= r*r){
                    this.addDot(i, j, color);
                }
            }
        }
    }
    // Halftone methods section
    /**
     * Applies a halftone effect to this PositionedImage. If this is an 
     * TYPE_BYTE_GRAY PositionedImage black and white halftone is applied
     * at 15 degrees, if this is a TYPE_INT_RGB then halftone for red, green,
     * and blue at angles -15, 7.5 and 30 degrees respectively.
     * @param size Height and width of each halftone area
     * @return A copy of this PositionedImage with halftone applied
     */
    public PositionedImage toHalftone(int size){
        if(this.getType() == PositionedImage.TYPE_BYTE_GRAY){
            return toHalftoneBW(size, 15d);
        }else{
            return toHalftoneRGB(size, -15d, 7.5d, 30d);
        }
    }
    /**
     * Returns a copy of this PositionedImage with black and white 
     * halftone effect applied at the specified angles.
     * @param size Height and width of each halftone area
     * @param angle The angle of the halftone effect in degrees
     * @return A copy of this PositionedImage with black and white halftone
     * applied
     */
    PositionedImage toHalftoneBW(int size, double angle){
        PositionedImage bw;
        bw = this.rotate(Math.toRadians(angle));
        bw = bw.toHalftone(size, true, true, true);
        bw = bw.rotate(Math.toRadians(-angle));
        bw = bw.reset(this.getWidth(), this.getHeight());
        return bw;
    }
    /**
     * Returns a copy of this PositionedImage with red, green, and blue 
     * halftone effect applied at varying angles.
     * @param size Height and width of each halftone area
     * @param rAngle The angle of the red halftone effect in degrees
     * @param gAngle The angle of the green halftone effect in degrees
     * @param bAngle The angle of the blue halftone effect in degrees
     * @return A copy of this PositionedImage with halftone applied
     */
    PositionedImage toHalftoneRGB(int size, double rAngle, double gAngle, double bAngle){
        PositionedImage red, green, blue;
        red = this.rotate(Math.toRadians(rAngle));
        red = red.toHalftone(size, true, false, false);
        red = red.rotate(Math.toRadians(-rAngle));
        red = red.reset(this.getWidth(), this.getHeight());
        
        green = this.rotate(Math.toRadians(gAngle));
        green = green.toHalftone(size, false, true, false);
        green = green.rotate(Math.toRadians(-gAngle));
        green = green.reset(this.getWidth(), this.getHeight());
        
        blue = this.rotate(Math.toRadians(bAngle));
        blue = blue.toHalftone(size, false, false, true);
        blue = blue.rotate(Math.toRadians(-bAngle));
        blue = blue.reset(this.getWidth(), this.getHeight());

        red.addImage(0, 0, green);
        red.addImage(0, 0, blue);
        
        return red;
    }
    /**
     * Returns a copy of this PositionedImage with halftone of the specified
     * size applied.
     * @param size Height and width of each halftone area
     * @param red True indicates this halftone channel contains red
     * @param green True indicates this halftone channel contains green
     * @param blue True indicates this halftone channel contains blue
     * @return A copy of this PositionedImage with 1 channel halftone applied
     */
    PositionedImage toHalftone(int size, boolean red, boolean green, boolean blue){
        /* Goes through the image a size step at a time, both horizontally and 
         * vertically. For that pixel the requested channels are taken. I then
         * create a circle with the same total amount of the requested channels,
         * however spread over a larger area. The circle is drawn with the
         * requested channel at half strength thus the circle had radius
         * size * sqrt(2 * channels fractional value / pi)
         * 
         * For example, suppose I request only the red channel. If the pixels value
         * is 1/4 of the max strength. If I were to draw a circle at full strength
         * it would have to have an area 1/4 * size * size. However since I am 
         * drawing the circle at half strength its area is 2 * 1/4 * size * size,
         * and hence it's radius is size * sqrt(2 * (1/4) / pi)
         * 
         * Note that since max radius > sqrt(2) * size that at high channel values
         * 3 circles can overlap. 2 circles overlapping maximizes the channel thus
         * the highest channel values will be dimmed. To calculate the non-linear
         * radius is impossible by analytical methods (I think) and I have decided
         * too computationally expensive to do.
         */
        PositionedImage pImage = new PositionedImage(this.getWidth(), this.getHeight(), this.getType());
        pImage.origin = this.origin;
        // The new Color and divisor = noOfColors * 255
        Color nColor = new Color(red ? 128 : 0, green ? 128 : 0, blue ? 128 : 0);
        double div = (red ? 255 : 0) + (green ? 255 : 0) + (blue ? 255 : 0);
        // Go through each map of length size
        for(int x = 0; x < this.getWidth(); x += size){
            for(int y = 0; y < this.getHeight(); y += size){
                Color c = new Color(this.getRGB(x, y));
                // Calculate the fraction of requested red, green, blue
                double frac = ((red ? c.getRed() : 0) + (green ? c.getGreen() : 0) + (blue ? c.getBlue() : 0)) / div;
                // Calculate the circles radius
                double radius = size*Math.sqrt(2 * frac / Math.PI);
                pImage.addCircle(x, y, radius, nColor);
            }
        } 
        return pImage;
    }    
    /**
     * Returns a new PositionedImage which is a copy of this PositionedImage
     * rotated by the specified number of radians. Note this inversion
     * method is perfectly invertible.
     * @param rads The rotation in radians. 
     * @return The rotated PositionedImage
     */
    PositionedImage rotate(double rads){
        /* Rotation by 3 shears as no rounding errors will cause 2 pixels to be 
         * mapped to the same pixel, thus making it perfectly invertible.
         */
        double alpha = -1*Math.tan(rads/2d);
        double beta  = Math.sin(rads);
        PositionedImage image = shearVert(alpha);
        image = image.shearHorz(beta); 
        image = image.shearVert(alpha);
        return image;
    }
    /**
     * Returns a new PositionedImage which is a copy of this PositionedImage
     * sheared vertically by the specified shear.
     * @param shear The number of pixels to vertically shift down each column of
     * pixels. This is cumulative, so if the first column is shifted down 2
     * pixels, the second would be 4, the third 6, etc. 
     */
    PositionedImage shearVert(double shear){
        /* Vertical shift (a horizontal is symmetric with x<->y and height<->width)
         * 
         * Note the sheared image are x' and y' and the unsheared are x, y:
         * 
         * x' = x, y' = y + shift
         * 
         * The shift for +ve and -ve shears are different, this is so that one can
         * invert the other (see Shearing methods inverse problem below):
         * 
         * shift = Math.floor(x * shear)    iff shear >= 0
         *          = Math.ceil(x * shear)        iff shear < 0
         * 
         * Also, as the origin may be moved by make shears/rotations, I adjust x
         * by the origin such that the steps always occur in the same place with
         * respect to x. There will always be a step right before the origin. 
         * Therefore the shift is actually:
         * 
         * shift = Math.floor((x - origin.x) * shear)    iff shear >= 0
         *          = Math.ceil((x - origin.x) * shear)    iff shear < 0
         * 
         * Negative shifts move the image to the left, however all images start at
         * 0 and cannot have negative values. Therefore the image is offset and the
         * actual equation for shearing is:
         * 
         * y' = y + shift - offset
         * 
         * The offset should be the minimum shift value (note shift <= 0 and x >= 0).
         * 
         * offset =  Math.floor(-1 * origin.x * shear)    iff shear >= 0
         *        =  Math.ceil((x - origin.x) * shear)     iff shear < 0
         *        
         * Finally the height of the sheared image is equal to the maximum y' value
         * (after we have offset it such that it starts at 0)
         * 
         * sheared height = height - offset + max y'
         * 
         * max y' = Math.floor((width - 1 - origin.x) * shear)    iff shear >= 0
         *        = Math.ceil((-1 * origin.x) * shear)            iff shear < 0
         *        
         */
        
        /* Shearing methods inverse problem:
         * Consider reversing a horizontal shear follows by a vertical shear. 
         * In this example the vertical shear will shift down every 2 and the 
         * horizontal shear will shift left every 1.
         * 
         * Step 1: The original
         * Step 2: Shear every second column down. - marks new pixels (to make rectangle)
         * Step 3: Shear every row backwards.   + marks new pixel
         * Step 4: Undo step 3, shear every row forwards. * marks new pixels
         * Step 5: Undo step 2, shear every second column up. . marks new pixels
         * 
         * The original image (1) orientation has been changed from the result of 
         * a number of shifts then their reversals. The problem is the original
         * shift down (2) started at the top left corner, however when undoing this
         * (5) the image was now in the third column due to steps (3) and (4) 
         * so the image column up shifts was offset from its down shifts. To 
         * compensate the shear method has an offset property, to indicate at
         * which row/col the off setting to count from.
         * 
         *                                             ........**
         *                                             ......-***
         *                                             ....0-0++* 
         *                                             ..+0000+++
         *    0000    00--    +++00--    +++00--***    +++0000+.. 
         * 1: 0000 2: 0000 3: ++0000+ 4: *++0000+** -5: *++0-0....
         *    0000    0000    +0000++    **+0000++*    ***-......
         *            --00    --00+++    ***--00+++    **........
         */    
        // Get this PositionedImages properties
        int width  = getWidth();
        int height = getHeight();
        // Calculate the offset such that image doesn't go to -ve coords
        int offset = shear < 0 ? (int) Math.ceil((width - 1 - origin.x) * shear)
                               : (int) Math.floor(-1 * origin.x * shear);
        /* Since images are rectangular, embedding a sheared image in a rectangle 
         * requires the new image be larger. */
        int sHeight = (shear < 0) ? height - offset + (int) Math.ceil((-1 * origin.x) * shear) 
                                  : height - offset + (int) Math.floor((width - 1 - origin.x) * shear); 
        PositionedImage sImage = new PositionedImage(width, sHeight, getType());
        // Go through current layer one pixel at a time
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                // Calculate the corresponding row in the new layer
                int shift = (shear < 0) ? (int)  Math.ceil((x - origin.x) * shear) 
                                        : (int) Math.floor((x - origin.x) * shear);
                // Copy to the new layer
                int sY = y - offset + shift;
                sImage.setRGB(x, sY, getRGB(x, y));
                // On origin loop, setup the new layers origin
                if(x == origin.x && y == origin.y){
                    sImage.origin = new Point(x, sY);
                }
            }
        }
        // Return the new layer
        return sImage;    
    }
    /**
     * The same as shearVert but applied horizontally.
     * @see shearVert
     */
    PositionedImage shearHorz(double shear){
        /* 
         * See shearVert's implementation comments
         */
        // Get this PositionedImages properties
        int width  = getWidth();
        int height = getHeight();
        // Calculate the offset such that image doesn't go to -ve coords
        int offset = shear < 0 ? (int) Math.ceil((height - 1 - origin.y) * shear)
                               : (int) Math.floor(-1 * origin.y * shear);
        /* Since images are rectangular, embedding a sheared image in a rectangle 
         * requires the new image be larger. */
        int sWidth = (shear < 0) ? width - offset + (int) Math.ceil((-1 * origin.y) * shear) 
                                 : width - offset + (int) Math.floor((height - 1 - origin.y) * shear); 
        PositionedImage sImage = new PositionedImage(sWidth, height, getType());
        // Go through current layer one pixel at a time
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                // Calculate the corresponding row in the new layer
                int shift = (shear < 0) ? (int)  Math.ceil((y - origin.y) * shear) 
                                        : (int) Math.floor((y - origin.y) * shear);
                // Copy to the new layer
                int sX = x - offset + shift;
                sImage.setRGB(sX, y, getRGB(x, y));
                // On origin loop, setup the new layers origin
                if(x == origin.x && y == origin.y){
                    sImage.origin = new Point(sX, y);
                }
            }
        }
        // Return the new layer
        return sImage;    
    }
}
