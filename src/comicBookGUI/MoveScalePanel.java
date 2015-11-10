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

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
import comicBookModel.ImageLayer;
import comicBookModel.Layer;
import comicBookModel.TextLayer;

/**
 * A panel with 3 buttons for selecting which area is active for the move/scale
 * actions. Either the border is active, the contents is active, or the border 
 * and contents are both active. This panel will displace differently depending
 * if the currently selected layer is an image or text layer.
 */
@SuppressWarnings("serial")
class MoveScalePanel extends ComicBookPanel implements ActionListener{
    /*
     * This panel is only updated when the components change
     */
    private boolean prevLayerImage;
    /**
     * The previous border mode, so we can tell when the components have changed
     */
    private String prevBorderMode;
    
    private final JButton imageMode, borderMode, imageBorderMode, textMode, textBorderMode;
    
    /**
     * Creates a MoveScalePanel for the comic book model and current state.
     * @param comic The comic book model
     * @param state The current state of the GUI
     */
    public MoveScalePanel(ComicBook comic, ComicBookState state) {
        super(comic, state);
        // Setup the layout and border
        setLayout(new GridLayout(1, 3, hgap, vgap));
        setBorder(BorderFactory.createTitledBorder(null, "Move/Scale", 2, 0));
        // Create the buttons
        imageMode = standardButton(ComicBookState.CONTENTS_MODE, this);
        borderMode = standardButton(ComicBookState.BORDER_MODE, this);
        imageBorderMode = standardButton(ComicBookState.CONTENTS_BORDER_MODE, this);
        textMode = standardButton(ComicBookState.CONTENTS_MODE, "Text Mode.png", this);
        textBorderMode = standardButton(ComicBookState.CONTENTS_BORDER_MODE, "Text and Border Mode.png", this);
        // Set current state as incorrect so it will be correct when updated
        ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        prevLayerImage = !(layer instanceof ImageLayer);
        prevBorderMode = null;
        update();
    }

    void update() {
        boolean validate = false;
        // Update ControlsPanel depending on the states current border mode
        ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        if(layer instanceof ImageLayer != prevLayerImage){
            prevLayerImage = layer instanceof ImageLayer;
            validate = true;
            // Add buttons
            removeAll();
            if(layer instanceof ImageLayer){
                add(imageMode);
                add(borderMode);
                add(imageBorderMode);
            }else if(layer instanceof TextLayer){
                add(textMode);
                add(borderMode);
                add(textBorderMode);
            }else{
                assert false;
            }
        }
        if(state.getBorderMode() != prevBorderMode){
            prevBorderMode = state.getBorderMode();
            validate = true;
            // Highlight selected
            imageMode.setBackground(null);
            textMode.setBackground(null);
            borderMode.setBackground(null);
            imageBorderMode.setBackground(null);
            textBorderMode.setBackground(null);
            if(state.getBorderMode() == ComicBookState.CONTENTS_MODE){
                imageMode.setBackground(selected);
                textMode.setBackground(selected);
            }else if(state.getBorderMode() == ComicBookState.BORDER_MODE){
                borderMode.setBackground(selected);
            }else if(state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE){
                imageBorderMode.setBackground(selected);
                textBorderMode.setBackground(selected);
            }
        }
        if(validate){
            validate();
        }
    }
    public void paint(Graphics g){
        update();
        super.paint(g);
    }
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();    
        if(command.equals(ComicBookState.CONTENTS_MODE)) {
            state.setBorderMode(ComicBookState.CONTENTS_MODE);
        }else if(command.equals(ComicBookState.BORDER_MODE)) {
            state.setBorderMode(ComicBookState.BORDER_MODE);
        }else if(command.equals(ComicBookState.CONTENTS_BORDER_MODE)) {
            state.setBorderMode(ComicBookState.CONTENTS_BORDER_MODE);
        }
        state.repaint();  // Repaint this component and the ControlsPanel
    }
}
