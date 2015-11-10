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

import java.awt.Point;

import javax.swing.JPanel;

/**
 * This class holds the current state of the ComicBookGUI (e.g. which buttons
 * are pressed, if the user is currently dragging a component)
 * 
 * @author Alastair Crowe
 */
class ComicBookState {
    // Editor Action States
    static final String MODIFY_ACTION = "Modify Vertex";
    static final String CREATE_ACTION = "Create Vertex";
    static final String SCALE_ACTION = "Scale Border/Contents";
    static final String MOVE_ACTION = "Move Border/Contents";
    // Editor Border States
    static final String CONTENTS_MODE = "Image Mode";
    static final String BORDER_MODE = "Border Mode";
    static final String CONTENTS_BORDER_MODE = "Image and Border Mode";
    // The Grid Spacing 
    static final int GRID = 20;
    // The GUI
    private JPanel gui;
    // Current State Variables
    /**
     * The currently selected page, -1 indicates no page is selected.
     */
    private int page;
    /**
     * The currently selected layer, -1 indicates no layer is selected.
     */
    private int layer;
    /**
     * The current border mode.
     */
    private String borderMode;
    private String actionMode;
    private Point actionPoint = null;
    private boolean actionActive = false;
    /***** Constructor *****/
    ComicBookState(JPanel gui, int page, int layer, String borderMode){
        this.gui = gui;
        this.setPage(page);
        this.setLayer(layer);
        this.setBorderMode(borderMode);
    }
    /**
     * Updates the current page index.
     */
    void setPage(int page) {
        this.page = page;
    }
    /**
     * Returns the current page index.
     */
    int getPage() {
        return this.page;
    }
    /**
     * Updates the current layer index.
     */
    void setLayer(int layer) {
        this.layer = layer;
    }
    /**
     * Returns the current layer index.
     */
    int getLayer() {
        return this.layer;
    }
    /**
     * Sets the current borderMode.
     * @param borderMode Must be either ComicBookState.CONTENTS_MODE 
     * ComicBookState.BORDER_MODE, ComicBookState.CONTENTS_BORDER_MODE.
     * It is the currently selected mode for selecting between manipulation 
     * of the border, the contents, or the border and contents.
     */
    void setBorderMode(String borderMode) {
        this.borderMode = borderMode;
    }
    /**
     * Returns the currently selected borderMode.
     * @see setBorderMode(String)
     */
    String getBorderMode() {
        return this.borderMode;
    }
    /**
     * Sets the current action.
     * @param borderMode Must be either ComicBookState.MODIFY_ACTION 
     * ComicBookState.CREATE_ACTION, ComicBookState.SCALE_ACTION, 
     * ComicBookState.MOVE_ACTION. It is the currently selected action for
     * manipulation of the borders vertices.
     */
    void setAction(String actionMode, Point actionPoint) {
        this.actionMode = actionMode;
        this.actionPoint = actionPoint;
    }
    /**
     * Returns the currently selected actionMode.
     * @see setAction(String)
     */
    String getActionMode() {
        return this.actionMode;
    }

    
    Point getActionPoint() {
        return this.actionPoint;
    }
    
    /**
     * @return True if the ActionMode is active.
     * @see setAction(String)
     */
    boolean getActionActive() {
        return this.actionActive;
    }
    /**
     * Sets the ActionMode as active or inactive.
     * @see setAction(String)
     */
    void setActionActive(boolean actionActive) {
        this.actionActive = actionActive;
    }    
    /**
     * Repaints the entire GUI
     */
    void repaint(){
        gui.repaint();
    }
}
