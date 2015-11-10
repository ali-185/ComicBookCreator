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
package hsbColorChooser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
/**
 * A diagram component for changing the saturation and brightness in a HSBColorSelectionModel.
 * 
 * @author Alastair Crowe
 */
@SuppressWarnings("serial")
final class DiagramComponent extends JComponent implements MouseListener, MouseMotionListener {

    private int[] rgb;
    private BufferedImage image;
    HSBColorSelectionModel model;
    
    private int width, height;
    
    /**
     * Creates a DiagramComponent to update the models saturatioon and 
     * brightness.
     * @param model The model to be updated by this SliderComponent
     */
    DiagramComponent(HSBColorSelectionModel model) {
        this.model = model;
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    /**
     * Redraws this DiagramComponent showing the selection of saturation and 
     * brightness available with a dot indicating the current selection. 
     */
    protected void paintComponent(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
        if (this.image == null) {
            this.rgb = new int[this.width * this.height];
            this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        }
        // Step per iteration for saturation and brightness
        float sStep = 1.0f / (float) (this.width - 1);
        float bStep = 1.0f / (float) (this.height - 1);
        // rgb array position
        int offset = 0;
        float brightness = 0.0f;
        // Create rgb array
        float hue = model.getHue();
        for (int h = 0; h < this.height; h++, brightness += bStep) {
            float saturation = 0.0f;
            for (int w = 0; w < this.width; w++, saturation += sStep, offset++) {
                this.rgb[offset] = Color.HSBtoRGB(hue, saturation, brightness);
            }
        }
        this.image.setRGB(0, 0, this.width, this.height, this.rgb, 0, this.width);
        g.drawImage(this.image, 0, 0, this.width, this.height, this);
        // Draw color selection lines
        if (isEnabled()) {
            int x = (int) (model.getSaturation() * (float) (this.width - 1));
            int y = (int) (model.getBrightness() * (float) (this.height - 1));
            g.setXORMode(Color.WHITE);
            g.setColor(Color.BLACK);
            g.drawLine(x - 8, y, x + 8, y);
            g.drawLine(x, y - 8, x, y + 8);
            g.setPaintMode();
        }
    }    
    /**
     * Updates the HSBColorSelectionModel to the currently selected
     * saturation and brightness.
     */
    public void mouseDragged(MouseEvent e) {
        // Update the mouse position to within bounds
        int mouseX = e.getX();
        int mouseY = e.getY();
        if(mouseX < 0){mouseX = 0;}
        if(mouseX > this.width - 1){mouseX = this.width - 1;}
        if(mouseY < 0){mouseY = 0;}
        if(mouseY > this.height - 1){mouseY = this.height - 1;}
        e.consume();
        // Update the color model
        float hue = model.getHue();
        float saturation = (float) mouseX / (float) (this.width - 1);
        float brightness = (float) mouseY / (float) (this.height - 1);
        // Set model color
        model.setSelectedColor(hue, saturation, brightness);
    }
    /**
     * No action.
     */
    public void mouseMoved(MouseEvent e) {}
    /**
     * No action.
     */
    public void mouseClicked(MouseEvent e) {}
    /**
     * No action.
     */
    public void mouseEntered(MouseEvent e) {}
    /**
     * No action.
     */
    public void mouseExited(MouseEvent e) {}
    /**
     * @see mouseDragged
     */
    public void mousePressed(MouseEvent e) {
        mouseDragged(e);
    }
    /**
     * No action.
     */
    public void mouseReleased(MouseEvent e) {}
}
