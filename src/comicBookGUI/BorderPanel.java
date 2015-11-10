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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import comicBookModel.Border;
import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
import comicBookModel.Layer;
import hsbColorChooser.JHSBColorChooser;

/**
 * A BorderPanel has 2 buttons. One to set choose the border color by displaying
 * a JHSBColorChooser, and the other to toggle the border grid.
 * @see comicBookModel.Border
 * 
 * @author Alastair Crowe
 */
@SuppressWarnings("serial")
class BorderPanel extends ComicBookPanel implements ActionListener{
    private static final String BORDER_COLOR = "Border Color";
    private static final String BORDER_GRID = "Snap to Grid";
    
    private boolean prevGridActive = false;
    private JButton gridButton;
    
    /**
     * Creates the BorderPanel with initial state that of the parameters
     * @param comic The model which this BorderPanel will update/reflect
     * @param state The state which this BorderPanel will update/reflect
     */
    public BorderPanel(ComicBook comic, ComicBookState state) {
        super(comic, state);
        // Setup the layout and border
        setLayout(new GridLayout(1, 2, hgap, vgap));
        setBorder(BorderFactory.createTitledBorder(null, "Border", 2, 0));
        // Create the buttons
        add(standardButton(BORDER_COLOR, this));
        gridButton = standardButton(BORDER_GRID, this);
        add(gridButton);
        // Update to reflect the current state
        update();
    }
    /**
     * Updates this BorderPanel if the user has selected a different button.
     */
    void update() {
        ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        Border border = layer.getBorder();
        // Update grid button if it's changed
        if(border.gridActive() != prevGridActive){
            prevGridActive = border.gridActive();
            if(border.gridActive()){
                gridButton.setBackground(selected);
            }else{
                gridButton.setBackground(null);
            }
            validate();
        }
    }
    /**
     * Redraws this BorderPanel
     */
    public void paint(Graphics g){
        update();
        super.paint(g);
    }
    /**
     * Called when the BorderPanel changes state, and updates the ComicBook
     * comic, the ComicBookState state, and this BorderPanel.
     */
    public void actionPerformed(ActionEvent e) {
        ComicPage page = comic.getPage(state.getPage());
        final Layer layer = page.getLayer(state.getLayer());
        Border border = layer.getBorder();
        boolean active = border.gridActive();
        String command = e.getActionCommand();
        if(command.equals(BORDER_COLOR)) {
            Color initial = layer.BORDER_COLOR instanceof Color ? layer.BORDER_COLOR : null;
            final JHSBColorChooser colorChooser = new JHSBColorChooser(initial); 
            ChangeListener listener = new ChangeListener(){
                   // Update the GUI with the selected border color
                public void stateChanged(ChangeEvent e) {
                    layer.BORDER_COLOR = colorChooser.getColor();
                    state.repaint();
                }
            };
            layer.BORDER_COLOR = colorChooser.showDialog(this, "Select Border Color", listener);
        }else if(command.equals(BORDER_GRID)) {
            // Update all the layers
            for(int i = 0; i < page.noOfLayers(); i++){
                border = page.getLayer(i).getBorder();
                if(active){
                    border.deactivateGrid();
                }else{
                    border.setGrid(ComicBookState.GRID);
                }
            }
            state.repaint();
        }
    }
}
