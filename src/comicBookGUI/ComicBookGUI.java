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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import comicBookModel.ComicBook;
@SuppressWarnings("serial")
class ComicBookGUI extends ComicBookPanel {
    /***** Editor JPanel *****/
    private EditorPanel editorPanel;
    private JPanel toolboxPanel;
    /***** Constructors *****/
    ComicBookGUI(ComicBook comic, ComicBookState state, int pageWidth, int pageHeight) {
        super(comic, state);
        // Setup the layout
        setLayout(new BorderLayout(3, 3));
        // Setup the editor panel
        editorPanel = new EditorPanel(comic, state);
        editorPanel.setPreferredSize(new Dimension(pageWidth, pageHeight));
        // Setup the panels and create the toolbox panel
        ComicBookPanel layerPanel = new LayerPanel(comic, state);
        ComicBookPanel borderPanel = new BorderPanel(comic, state);
        ComicBookPanel moveScalePanel = new MoveScalePanel(comic, state);
        ComicBookPanel imageTextPanel = new ImageTextPanel(comic, state, editorPanel);
        ComicBookPanel saveAsPanel = new SaveAsPanel(comic, state);
        ComicBookPanel controlsPanel = new ControlsPanel(comic, state);
        toolboxPanel = new JPanel();
        toolboxPanel.setLayout(new BoxLayout(toolboxPanel, BoxLayout.PAGE_AXIS));
        toolboxPanel.add(layerPanel);
        toolboxPanel.add(borderPanel);
        toolboxPanel.add(moveScalePanel);
        toolboxPanel.add(imageTextPanel);
        toolboxPanel.add(saveAsPanel);
        toolboxPanel.add(controlsPanel);
        Dimension dim = new Dimension(170, 500);
        Box.Filler filler= new Box.Filler(dim, dim, dim);
        toolboxPanel.add(filler);
        toolboxPanel.setPreferredSize(new Dimension(170, pageHeight));
        // Create this panel
        add(toolboxPanel, BorderLayout.EAST);
        add(editorPanel, BorderLayout.CENTER);
    }
    /***** Overwritten Paint Method *****/
    void update() {}
    public void paint(Graphics g){
           super.paint(g);
    }
}
    

