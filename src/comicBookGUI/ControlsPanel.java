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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
import comicBookModel.ImageLayer;
import comicBookModel.Layer;
import comicBookModel.TextLayer;

@SuppressWarnings("serial")
class ControlsPanel extends ComicBookPanel {
    // Only update the components when they have changed
    String prevBorderMode = null;
    
    // The JLabels that make up a ControlsPanel
    private final JLabel moveImageAndBorder, moveImage;
    private final JLabel moveBorder, moveTextAndBorder;
    private final JLabel moveText, resizeImageAndBorder;
    private final JLabel resizeImage, resizeBorder;
    private final JLabel resizeTextAndBorder, resizeText;
    private final JLabel moveDeleteVertex, createVertex;
    
    ControlsPanel(ComicBook comic, ComicBookState state) {
        super(comic, state);
        // Setup the layout and border
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(null, "Controls", 2, 0));
        // Create the JLabels used in this ControlsPanael
        BufferedImage move = null;
        BufferedImage resize = null;
        BufferedImage grabVertex = null;
        BufferedImage grabEdge = null;
        try {
            move = ImageIO.read(this.getClass().getClassLoader().getResource("Move.png"));
            resize = ImageIO.read(this.getClass().getClassLoader().getResource("Resize.png"));
            grabVertex = ImageIO.read(this.getClass().getClassLoader().getResource("Grab Vertex.png"));
            grabEdge = ImageIO.read(this.getClass().getClassLoader().getResource("Grab Edge.png"));
        } catch (IOException e) {}    
        // Move image labels
        moveImageAndBorder = createJLabel("Left drag the image to move the image and border.", move);
        moveImage = createJLabel("Left drag the image to move the image.", move);
        moveBorder = createJLabel("Left drag the image to move the border.", move);
        moveTextAndBorder = createJLabel("Left drag the image to move the text and border.", move);
        moveText = createJLabel("Left drag the image to move the text.", move);
        // Resize image labels
        resizeImageAndBorder = createJLabel("Right drag the image to resize the image and border.", resize);
        resizeImage = createJLabel("Right drag the image to resize the image.", resize);
        resizeBorder = createJLabel("Right drag the image to resize the border.", resize);
        resizeTextAndBorder = createJLabel("Right drag the image to resize the text and border.", resize);
        resizeText = createJLabel("Right drag the image to resize the text.", resize);
        // Grab vertex label
        moveDeleteVertex = createJLabel("Left drag the vertex to move it or double click to delete it.", grabVertex);
        // Grab edge label
        createVertex = createJLabel("Double click the edge to add a new vertex.", grabEdge);
        
        // Update to reflect the current state
        update();
    }

    void update() {
        // Update the border buttons if the border mode state has changed
        if(state.getBorderMode() != prevBorderMode){
            prevBorderMode = state.getBorderMode();
            ComicPage page = comic.getPage(state.getPage());
            Layer layer = page.getLayer(state.getLayer());
            // Filler boxs between the panels
            Dimension dim = new Dimension(hgap, vgap);
            Box.Filler box1 = new Box.Filler(dim, dim, dim);
            Box.Filler box2 = new Box.Filler(dim, dim, dim);
            Box.Filler box3 = new Box.Filler(dim, dim, dim);
            if(state.getBorderMode() == ComicBookState.CONTENTS_MODE && layer instanceof ImageLayer){
                removeAll();
                add(moveImage);            // Move
                add(box1);
                add(resizeImage);          // Resize
                add(box2);
                add(moveDeleteVertex);     // Grab Vertex
                add(box3);
                add(createVertex);         // Grab Edge
            }else if(state.getBorderMode() == ComicBookState.CONTENTS_MODE && layer instanceof TextLayer){           
                removeAll();
                add(moveText);             // Move
                add(box1);
                add(resizeText);           // Resize
                add(box2);
                add(moveDeleteVertex);     // Grab Vertex
                add(box3);
                add(createVertex);         // Grab Edge           
            }else if(state.getBorderMode() == ComicBookState.BORDER_MODE){
                removeAll();
                add(moveBorder);           // Move
                add(box1);
                add(resizeBorder);         // Resize
                add(box2);
                add(moveDeleteVertex);     // Grab Vertex
                add(box3);
                add(createVertex);         // Grab Edge
            }else if(state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE && layer instanceof ImageLayer){
                removeAll();
                add(moveImageAndBorder);   // Move
                add(box1);
                add(resizeImageAndBorder); // Resize
                add(box2);
                add(moveDeleteVertex);     // Grab Vertex
                add(box3);
                add(createVertex);         // Grab Edge
            }else if(state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE  && layer instanceof TextLayer){
                removeAll();
                add(moveTextAndBorder);    // Move
                add(box1);
                add(resizeTextAndBorder);  // Resize
                add(box2);
                add(moveDeleteVertex);     // Grab Vertex
                add(box3);
                add(createVertex);         // Grab Edge
            }
            // Resize for the boxes
            setSize(getPreferredSize());
            // This component is no longer valid
            validate();
        }
    }
    public void paint(Graphics g){
        update();
        super.paint(g);
    }
    
    private JLabel createJLabel(String text, BufferedImage image){
        JLabel jlabel;
        String html = "<html><div style=\"text-align: justify;\">" +
                text + "</html>";
        if(image == null){
            jlabel = new JLabel(html);
        }else{
            ImageIcon icon = new ImageIcon(image);
            jlabel = new JLabel(html, icon, JLabel.LEFT);
        }             
        jlabel.setBorder(BorderFactory.createTitledBorder(null, null, 2, 0));
        return jlabel;
    }
}
