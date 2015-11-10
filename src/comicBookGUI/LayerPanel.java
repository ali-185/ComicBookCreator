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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import comicBookModel.Border;
import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
import comicBookModel.Layer;

/**
 * A panel for selecting which Layer is currently active in the ComicBookPage,
 * and for creating and deleting layers. A LayerPanel has 7 buttons for 
 * selecting the next layer, previous layer, moving a layer up, moving a layer
 * down, adding an image layer, adding a text layer, and deleting a layer.
 * @see ComicBookModel.ComicBookPage
 * @see comicBookModel.Layer
 */
@SuppressWarnings("serial")
class LayerPanel extends ComicBookPanel implements ActionListener{

    private static final String PREV_LAYER = "Previous Layer";
    private static final String NEXT_LAYER = "Next Layer";
    private static final String MOVE_LAYER_UP = "Move Layer Up";
    private static final String MOVE_LAYER_DOWN = "Move Layer Down";
    private static final String ADD_IMAGE_LAYER = "Add Image Layer";
    private static final String ADD_TEXT_LAYER = "Add Text Layer";
    private static final String DEL_LAYER = "Delete Layer";
    
    /**
     * Creates a LayerPanel for the comic book model and current state.
     * @param comic The comic book model
     * @param state The current state of the GUI
     */
    public LayerPanel(ComicBook comic, ComicBookState state) {
        super(comic, state);
        // Setup the layout and border
        setLayout(new GridLayout(2, 4, hgap, vgap));
        setBorder(BorderFactory.createTitledBorder(null, "Layer", 2, 0));
        // Create the JButtons
        add(standardButton(PREV_LAYER, this));
        add(standardButton(NEXT_LAYER, this));
        add(standardButton(ADD_IMAGE_LAYER, this));
        add(standardButton(ADD_TEXT_LAYER, this));
        add(standardButton(MOVE_LAYER_UP, this));
        add(standardButton(MOVE_LAYER_DOWN, this));
        add(standardButton(DEL_LAYER, this));
    }
    /**
     * Not implemented
     */
    void update() {}
    /**
     * The button action listener. It updates the model and GUI.
     */
    public void actionPerformed(ActionEvent e) {
           ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        Border border = layer.getBorder();
        boolean active = border.gridActive();
        String command = e.getActionCommand();
        if (command.equals(PREV_LAYER)) {
            // Cycle backwards through the layers
            int prev = (state.getLayer() - 1 + page.noOfLayers()) % page.noOfLayers();
            state.setLayer(prev);
        }else if(command.equals(NEXT_LAYER)) {
            // Cycle forwards through the layers
            int next = (state.getLayer() + 1 + page.noOfLayers()) % page.noOfLayers();
            state.setLayer(next);
        }else if(command.equals(MOVE_LAYER_DOWN)) {
            int prev = (state.getLayer() - 1 + page.noOfLayers()) % page.noOfLayers();
            page.removeLayer(state.getLayer());
            page.addLayer(prev, layer);
            state.setLayer(prev);
        }else if(command.equals(MOVE_LAYER_UP)) {
            int next = (state.getLayer() + 1 + page.noOfLayers()) % page.noOfLayers();
            page.removeLayer(state.getLayer());
            page.addLayer(next, layer);
            state.setLayer(next);
        }else if(command.equals(ADD_IMAGE_LAYER)) {
            page.addImageLayer();
            state.setLayer(page.noOfLayers() - 1);
            if(active){
                page.getLayer(state.getLayer()).getBorder().setGrid(ComicBookState.GRID);
            }
        }else if(command.equals(ADD_TEXT_LAYER)) {
            page.addTextLayer();
            state.setLayer(page.noOfLayers() - 1);
            if(active){
                page.getLayer(state.getLayer()).getBorder().setGrid(ComicBookState.GRID);
            }
        }else if(command.equals(DEL_LAYER)) {
            page.removeLayer(state.getLayer());
            if(state.getLayer() == page.noOfLayers()){
                state.setLayer(state.getLayer() - 1);
            }
        }
        // Set state as updated
        state.repaint();
    }
}
