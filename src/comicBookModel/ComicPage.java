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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * A ComicPage is an ordered list of one or more Layers. The Layers will all be
 * the same width and height. When converted to an Image the Layers are layered
 * on top of each other with the higher index Layers on top.
 * 
 * @author Alastair Crowe
 */
public class ComicPage {
    // All layers must have the same width and height
    private final int WIDTH;
    private final int HEIGHT;
    private LinkedList<Layer> LAYERS = new LinkedList<Layer>();
    private Paint BACKGROUND;
    /**
     * Creates a ComicPage with the specified width and height, with an initial blank
     * ImageLayer at index 0. All Layers added to this ComicPage are of this width
     * and height.
     * @param width
     * @param height
     */
    ComicPage(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;
        addImageLayer();
    }
    /**
     * Returns the width of this ComicPage
     * @return
     */
    int getWidth() {
        return WIDTH;
    }
    /**
     * Returns the height of this ComicPage
     * @return
     */
    int getHeight() {
        return HEIGHT;
    }
    /**
     * Adds the new layer at the specified index.
     * @param index
     * @param layer
     */
    public void addLayer(int index, Layer layer){
        LAYERS.add(index, layer);
    }
    /**
     * Adds a new ImageLayer in front of all other Layers.
     */
    public void addImageLayer(){
        Layer layer = new ImageLayer(WIDTH, HEIGHT);
        LAYERS.add(layer);
    }
    /**
     * Adds a new TextLayer in front of all other Layers.
     */
    public void addTextLayer(){
        Layer layer = new TextLayer(WIDTH, HEIGHT);
        LAYERS.add(layer);
    }
    /**
     * Removes the Layer at the specified index.
     * @param index
     */
    public void removeLayer(int index){
        LAYERS.remove(index);
        if(noOfLayers() == 0){
            addImageLayer();
        }
    }
    /**
     * Returns the Layer at the specified index.
     * @param index
     * @return
     */
    public Layer getLayer(int index){
        return LAYERS.get(index);
    }
    /** 
     * Returns the number of Layers on this ComicPage.
     * @return
     */
    public int noOfLayers(){
        return LAYERS.size();
    }
    /**
     * Returns this ComicPage as a BufferedImage. 
     * @return
     */
    public BufferedImage toImage(){
        // Create white image
        BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, 
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setPaint(BACKGROUND);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // Draw the layers one at a time, the higher the index Layers on top of
        // the lower.
        for(int index = 0; index < noOfLayers(); index++){
            Layer layer = getLayer(index);
            g.drawImage(layer.toImage(), 0, 0, null);
        }
        // Return the image
        return newImage;
    }
}
