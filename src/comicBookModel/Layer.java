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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * A Layer has a final width and height upon construction. A Layer contains
 * a border, and contents within that border.
 * 
 * @author Alastair.Crowe
 */
public abstract class Layer {
    private final int WIDTH;
    private final int HEIGHT;
    // Border Properties
    protected Border BORDER;
    protected BasicStroke BORDER_STROKE = new BasicStroke(5f, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_MITER);
    public Color BORDER_COLOR = Color.BLACK;
    // Contents Properties
    protected Point CONTENTS_POSITION;
    protected int CONTENTS_WIDTH;
    protected int CONTENTS_HEIGHT;
    /**
     * Creates a Layer with the specified width and height. The border and
     * contents are an empty rectangle, half the height and width of the layer,
     * centered in the layer. 
     */
    Layer(int width, int height){
        WIDTH = width;
        HEIGHT = height;
        // Initialise remaining properties
        int contentsX = WIDTH / 4;
        int contentsY = HEIGHT / 4;
        CONTENTS_POSITION = new Point(contentsX, contentsY);
        CONTENTS_WIDTH = WIDTH / 2;
        CONTENTS_HEIGHT = HEIGHT / 2;
        int[] xpoints = {contentsX, contentsX + CONTENTS_WIDTH, contentsX + CONTENTS_WIDTH, contentsX};
        int[] ypoints = {contentsY, contentsY, contentsY + CONTENTS_HEIGHT, contentsY + CONTENTS_HEIGHT};
        int npoints = 4;
        BORDER = new Border(xpoints, ypoints, npoints, width, height);
    }
    /**
     * Returns the width of this Layer.
     */
    int getWidth() {
        return WIDTH;
    }
    /**
     * Returns the height of this Layer.
     */
    int getHeight() {
        return HEIGHT;
    }
    /**
     * Returns the width of this Layers contents.
     */
    public int getContentsWidth(){
        return CONTENTS_WIDTH;
    }
    /**
     * Returns the height of this Layers contents.
     */
    public int getContentsHeight(){
        return CONTENTS_HEIGHT;
    }
    /**
     * Returns the this Layers Border.
     */
    public Border getBorder(){
        return BORDER;
    }
    /**
     * Returns the stroke of the border.
     */
    public BasicStroke getStroke(){
        return BORDER_STROKE;
    }
    /**
     * Sets the stroke of the border.
     */
    public void setStroke(BasicStroke stroke){
        BORDER_STROKE = stroke;
    }
    /**
     * Translates the border and contents by the specified amounts.
     */
    void translate(int x, int y){
        translateBorder(x, y);
        translateContents(x, y);
    }
    /**
     * Scales the border and contents by the specified factor.
     */
    void scale(double factor){
        scaleBorder(factor);
        scaleContents(factor);
    }
    /**
     * Translates the border by the specified amounts.
     */
    public void translateBorder(int x, int y){
        BORDER.translate(x, y);
    }
    /**
     * Scales the border by the specified factor.
     */
    public void scaleBorder(double factor){
        BORDER.scale(factor);
    }    
    /**
     * Translates the contents by the specified amounts.
     */
    public void translateContents(int x, int y){
        CONTENTS_POSITION.x += x;
        CONTENTS_POSITION.y += y;
    }
    /**
     * Scales the contents by the specified factor.
     */
    public void scaleContents(double factor){
        // Get centre
        Rectangle rect = BORDER.getBounds();
        double x = rect.x + rect.width/2d;
        double y = rect.y + rect.height/2d;
        // And scale
        CONTENTS_POSITION.x = (int) Math.round((CONTENTS_POSITION.x - x) * factor + x);
        CONTENTS_POSITION.y = (int) Math.round((CONTENTS_POSITION.y - y) * factor + y);
        // And Scale
        CONTENTS_WIDTH = (int) Math.round(CONTENTS_WIDTH * factor);
        CONTENTS_HEIGHT = (int) Math.round(CONTENTS_HEIGHT * factor);
    }
    /**
     * Returns a BufferedImage of this Layer.
     */
    abstract BufferedImage toImage();
    /**
     * Draws this layers border onto the given Graphic2D. If the borders grid
     * is active, the grid is drawn too.
     */
    protected void drawBorderGrid(Graphics2D g){
        // This function draws the border/grid in layering levels of 
        // transparency from the centre to the edge, when the centre is 100%
        // opaque. This creates smooth edges of the border/grid stroke. 
        int steps = (int) Math.ceil(BORDER_STROKE.getLineWidth() * 2);
        // New Color properties
        int alphaStep = BORDER_COLOR.getAlpha() / steps;
        int red = BORDER_COLOR.getRed();
        int green = BORDER_COLOR.getGreen();
        int blue = BORDER_COLOR.getBlue();
        // New Stroke Properties
        int cap = BORDER_STROKE.getEndCap();
        int join = BORDER_STROKE.getLineJoin();
        float miterlimit = BORDER_STROKE.getMiterLimit();
        float[] dash = BORDER_STROKE.getDashArray();
        float dash_phase = BORDER_STROKE.getDashPhase();
        // Go through the levels
        for(int s = 0; s < steps; s++){
            int alpha = (s + 1) * alphaStep;
            Color color = new Color(red, green, blue, alpha);
            float width = BORDER_STROKE.getLineWidth() - (0.5f * s);
            BasicStroke stroke = new BasicStroke(width, cap, join, miterlimit, dash, dash_phase);
            // Draw the stroke
            g.setStroke(stroke);
            g.setPaint(color);
            g.draw(BORDER);
        }
        // Draw the grid if required
        if(BORDER.gridActive()){
            Color tBlack = new Color(0, 0, 0, 128);
            int grid = BORDER.getGrid();
            for(int i = grid/2; i < getWidth(); i += grid){
                for(int j = grid/2; j < getHeight(); j += grid){
                    g.setColor(tBlack);
                    g.fillOval(i-1, j-1, 3, 3);
                    g.setColor(Color.BLACK);
                    g.fillOval(i, j, 1, 1);
                }
            }
        }
    }
}
