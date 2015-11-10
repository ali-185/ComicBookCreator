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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * An ImageLayer is a Layer which has an text for it's contents. 
 * @see Layer
 * 
 * @author Alastair Crowe
 */
public class TextLayer extends Layer {
    private String TEXT = null;
    public Paint TEXT_PAINT = Color.BLACK;
    Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    public Paint BACKGROUND_PAINT = Color.WHITE;
    /**
     * Creates a TextLayer of specified height and width.
     * @see Layer#Layer(int, int)
     */
    TextLayer(int width, int height) {
        super(width, height);
    }
    /**
     * Appends the char to the end of this TextLayers text.
     */
    public void appendText(char c){
        String s;
        if(c == (char) 10 || c == (char) 13){
            s = "\n";
        }else if(c == (char) 8){
            s = "";
            if(TEXT.length() > 0){
                TEXT = TEXT.substring(0, TEXT.length() - 1);
            }
        }else{
            s = c + "";
        }
        if(TEXT == null){
            // Set the text area to the bounding area when TEXT initialized
            Rectangle rect = BORDER.getBounds();
            CONTENTS_POSITION = rect.getLocation();
            CONTENTS_POSITION.x += 3;    // Create a gap between border and letters
            CONTENTS_WIDTH = rect.width;
            CONTENTS_HEIGHT = rect.height;
            // Write the text
            TEXT = s;
        }else{
            TEXT += s;
        }
    }
    /**
     * Scales the contents by the specified factor.
     * @see Layer#scaleContents(double)
     */
    public void scaleContents(double factor){
        super.scaleContents(factor);
        TEXT_FONT = TEXT_FONT.deriveFont((float)(factor * TEXT_FONT.getSize2D()));
    }
    /**
     * Returns a BufferedImage of this TextLayer.
     */
    BufferedImage toImage(){
        // Create alpha image
        BufferedImage newImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        // Clip to the border
        g.setClip(BORDER);
        // Fill the background
        g.setPaint(BACKGROUND_PAINT);
        g.fill(BORDER);
        // Write the text
        if(TEXT != null){
            g.setPaint(TEXT_PAINT);
            g.setFont(TEXT_FONT);
            // One line at a time
            int x = CONTENTS_POSITION.x;
            int y = CONTENTS_POSITION.y;
            int n = 1;
            String s = wrapToBorder(g);
            for (String line : s.split("\n")){
                g.drawString(line, x, y + n++ * g.getFontMetrics().getHeight());
            }
        }
        // Reset the clipping region
        g.setClip(0, 0, getWidth(), getHeight());
        // Draw the border
        drawBorderGrid(g);
        return newImage;
    }
    /**
     * Text is written within the bounding rectangle of the border. This method
     * inserts whitespace such that all the written text is within the border.
     */
    private String wrapToBorder(Graphics2D g){
        String newText = "";
        // Starting position
        int x = CONTENTS_POSITION.x;
        int y = CONTENTS_POSITION.y;
        // Get border bounding properties
        Rectangle rect = BORDER.getBounds();
        int bWidth = x + rect.width;
        int bHeight = y + rect.height;
        // Letter bounding properties
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight();
        // Padding attributes
        String pad = " ";
        int padWidth = fm.stringWidth(pad);
        // Go through each letter of TEXT
        for(int i = 0; i < TEXT.length(); i++){
            char c = TEXT.charAt(i);
            String s = String.valueOf(c);
            // Check if it's a new line
            if(s.equals("\n")){
                newText += "\n";
                x = CONTENTS_POSITION.x;
                y += height;
                continue;
            }
            // Get bounding rectangle of letter
            int width = fm.stringWidth(s);
            Rectangle r = new Rectangle(x, y, width, height);
            // Check if it is within the border
            if(BORDER.contains(r)){
                // If so add the letter
                newText += s;
                x += width;
            }else{
                // Otherwise pad
                newText += pad;
                x += padWidth;
                i--;
            }
            // Make sure it is within the border bounds
            if(x > bWidth){
                newText += "\n";
                x = CONTENTS_POSITION.x;
                y += height;
            }
            if(y > bHeight){
                break;
            }
        }
        return newText;
    }
}
