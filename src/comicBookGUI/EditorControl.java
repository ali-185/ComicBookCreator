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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import comicBookModel.Border;
import comicBookModel.ComicBook;
import comicBookModel.Layer;
import comicBookModel.TextLayer;

class EditorControl implements MouseListener, MouseMotionListener, KeyListener{
    /***** Internal Variables *****/
    // Distance until mouse snaps to vertex/edge
    private static final double VERTEX_DISTANCE = 20d;
    private static final double EDGE_DISTANCE = 20d;
    /***** The Comic Book Model *****/
    private final ComicBook comic;       // Data
    private final ComicBookState state;  // State
    /***** Constructor *****/
    EditorControl(ComicBook comic, ComicBookState state){
        this.comic = comic;
        this.state = state;
    }
    /***** MouseMotionListener Methods *****/
    // A mouse drag can either be moving a vertex or moving the border
    public void mouseDragged(MouseEvent e) {
        Point mouse = new Point(e.getX(), e.getY());
        Layer layer = comic.getPage(state.getPage()).getLayer(state.getLayer());
        Border border = layer.getBorder();
        // If active, set the mouse point to the grid
        if(border.gridActive()){
            mouse = border.toGrid(mouse);
        }
        // Calculate which mouse button is pressed
        boolean left = false, right = false;
        if(SwingUtilities.isLeftMouseButton(e)){
            left = true;
        }else if(SwingUtilities.isRightMouseButton(e)){
            right = true;
        }
        if(state.getActionActive()){
            Point last = state.getActionPoint();
            if(state.getActionMode() == ComicBookState.MODIFY_ACTION && left){
                // Dragging a vertex
                // Update the model
                border.setPoint(border.getIndex(last), mouse);
                // Update state action
                state.setAction(ComicBookState.MODIFY_ACTION, mouse);
            }else if(state.getActionMode() == ComicBookState.MOVE_ACTION && left){
                // Moving a border
                // Update the model
                int xShift = mouse.x - last.x;
                int yShift = mouse.y - last.y;
                if(state.getBorderMode() == ComicBookState.BORDER_MODE || 
                        state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE){
                    layer.translateBorder(xShift, yShift);
                }
                if(state.getBorderMode() == ComicBookState.CONTENTS_MODE || 
                        state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE){
                    layer.translateContents(xShift, yShift);
                }
                // Update the state
                state.setAction(ComicBookState.MOVE_ACTION, mouse);
            }else if(state.getActionMode() == ComicBookState.SCALE_ACTION && right){
                // Scaling a border
                // Update the model
                double diff = mouse.y - last.y;
                double factor = Math.pow(2, diff/-200d);
                if(state.getBorderMode() == ComicBookState.BORDER_MODE || 
                        state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE){
                    layer.scaleBorder(factor);
                }
                if(state.getBorderMode() == ComicBookState.CONTENTS_MODE || 
                        state.getBorderMode() == ComicBookState.CONTENTS_BORDER_MODE){
                    layer.scaleContents(factor);
                }
                // Update the state
                state.setAction(ComicBookState.SCALE_ACTION, mouse);
            }else{
                // Nothing
                // Update the state
                state.setAction(null, null);
            }
        }else{
            // First point in drag
            Point closestVertex = border.closestVertexPoint(mouse);
            double shortestVertex = closestVertex.distance(mouse);
            if(shortestVertex < VERTEX_DISTANCE && left){
                // Dragging a vertex
                // Update the state
                state.setAction(ComicBookState.MODIFY_ACTION, closestVertex);
            }else if(border.contains(mouse) && left){
                // Moving a border
                // Update the state
                state.setAction(ComicBookState.MOVE_ACTION, mouse);
            }else if(border.contains(mouse) && right){
                // Scaling a border
                // Update the state
                state.setAction(ComicBookState.SCALE_ACTION, mouse);
            }else{
                // Nothing
                // Update the state
                state.setAction(null, null);
            }
        }
        state.setActionActive(true);
        e.consume();
        // Flag state as updated
        state.repaint();
    }
    public void mouseMoved(MouseEvent e) {
        Point mouse = new Point(e.getX(), e.getY());
        Layer layer = comic.getPage(state.getPage()).getLayer(state.getLayer());
        Border border = layer.getBorder();
        // Calculate the closest vertex
        Point closestVertex = border.closestVertexPoint(mouse);
        double shortestVertex = closestVertex.distance(mouse);
        // Calculate the closest edge point
        Point closestEdgePoint = border.closestEdgePoint(mouse);
        double shortestEdgePoint = closestEdgePoint.distance(mouse);
        // Update cursor and highlight variables
        if(shortestVertex < VERTEX_DISTANCE){
            // Update the state
            state.setAction(ComicBookState.MODIFY_ACTION, closestVertex);
        }else if(shortestEdgePoint < EDGE_DISTANCE){
            // Update the state
            state.setAction(ComicBookState.CREATE_ACTION, closestEdgePoint);
        }else if(border.contains(mouse)){
            // Update the state
            state.setAction(ComicBookState.MOVE_ACTION, mouse);
        }else{
            // Update the state
            state.setAction(null, null);
        }
        // All move events are passive
        state.setActionActive(false);
        // Flag state as updated
        e.consume();
        state.repaint();
    }
    /***** MouseListener Methods *****/
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {
        // Update the state
        state.setAction(null, null);
        e.consume();
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
        Point mouse = new Point(e.getX(), e.getY());
        Layer layer = comic.getPage(state.getPage()).getLayer(state.getLayer());
        Border border = layer.getBorder();
        // Calculate the closest vertex
        Point closestVertex = border.closestVertexPoint(mouse);
        double shortestVertex = closestVertex.distance(mouse);
        // Calculate the closest edge point
        Point closestEdgePoint = border.closestEdgePoint(mouse);
        double shortestEdgePoint = closestEdgePoint.distance(mouse);
        // If it's a double click
        boolean doubleClick = false;
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            doubleClick = true;
        }
        // Update cursor and highlight variables
        if(doubleClick && shortestVertex < VERTEX_DISTANCE){
            // Delete vertex
            int index = border.getIndex(closestVertex);
            border.removePoint(index);
        }else if(doubleClick && shortestEdgePoint < EDGE_DISTANCE){
            // Insert a vertex
            int index = border.closestEdgeIndex(closestEdgePoint) + 1;
            border.insertPoint(index, closestEdgePoint);
        }
        mouseMoved(e);
    }
    /***** KeyListener Methods *****/
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {
        Layer layer = comic.getPage(state.getPage()).getLayer(state.getLayer());
        if(layer instanceof TextLayer){
            ((TextLayer) layer).appendText(e.getKeyChar());
            state.repaint();
        }
        e.consume();
    }
}
