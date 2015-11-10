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
package comicBookGUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
import comicBookModel.Layer;

@SuppressWarnings("serial")
class EditorPanel extends ComicBookPanel {
    /***** Controller *****/
    private final EditorControl editorControl = new EditorControl(comic, state);
    
    /***** Dashed Stroke *****/
    private static final float[] DASH = {10f};
    /***** Cursors *****/
    private Cursor pointer = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private Cursor move = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private Cursor resize = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
    private Cursor grab = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    
    /***** Constructor *****/
    EditorPanel(ComicBook comic, ComicBookState state) {
        super(comic, state);
        // Setup and border
        setBorder(BorderFactory.createTitledBorder(null, null, 2, 0));
        // Add the listeners
        addMouseListener(editorControl);
        addMouseMotionListener(editorControl);
        addKeyListener(editorControl);
        // Create custom cursors
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            BufferedImage image = ImageIO.read(this.getClass().getClassLoader().getResource("Move.png"));
            Point hotPoint = new Point(16, 16);
            Cursor move = toolkit.createCustomCursor(image, hotPoint, "Move");
            image = ImageIO.read(this.getClass().getClassLoader().getResource("Resize.png"));
            Cursor resize = toolkit.createCustomCursor(image, hotPoint, "Resize");
            image = ImageIO.read(this.getClass().getClassLoader().getResource("Grab.png"));
            hotPoint = new Point(16, 8);
            Cursor grab = toolkit.createCustomCursor(image, hotPoint, "Grab");
            setCursors(null, move, resize, grab);
        } catch (IOException e) {}
        
    }
    void setCursors(Cursor pointer, Cursor move, Cursor resize, Cursor grab){
        this.pointer = pointer;
        this.move = move;
        this.resize = resize;
        this.grab = grab;
    }
    void update() {}
    public void paint(Graphics g){
        super.paint(g);
        ComicPage page = comic.getPage(state.getPage());
        // Highlight a border
        Layer layer = page.getLayer(state.getLayer());
        BasicStroke stroke = layer.getStroke();
        BasicStroke newStroke = new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), 
                stroke.getLineJoin(), stroke.getMiterLimit(), DASH, 0f);
        layer.setStroke(newStroke);
        // Create the image
        Image image = page.toImage();
        // Undo border highlight
        layer.setStroke(stroke);
        // Draw the image
        g.drawImage(image, 0, 0, null);
        // Highlight a Point
        if(state.getActionMode() == ComicBookState.CREATE_ACTION){
            // Update the cursor
            this.setCursor(grab);
            // Highlight the action point
            Point point = state.getActionPoint();
            int size = 5;
            for(int i = 0; i < size; i++){
                g.setColor(new Color(0, 0, 0, (255*(size-i))/size));
                ((Graphics2D) g).setStroke(new BasicStroke(i));
                g.drawOval(point.x-7, point.y-7, 15, 15);
            }
        }else if(state.getActionMode() == ComicBookState.MODIFY_ACTION){
            // Update the cursor
            this.setCursor(grab);
            // Highlight the action point
            Point point = state.getActionPoint();
            int size = 15;
            for(int i = 0; i < size; i++){
                g.setColor(new Color(0, 0, 0, (255*(size-i))/size));
                g.fillOval(point.x-i/2, point.y-i/2, i, i);    
            }
        }else if(state.getActionMode() == ComicBookState.MOVE_ACTION){
            // Update the cursor
            this.setCursor(move);
        }else if(state.getActionMode() == ComicBookState.SCALE_ACTION){
            // Update the cursor
            this.setCursor(resize);
        }else{
            // Update the cursor
            this.setCursor(pointer);
        }
    }
}