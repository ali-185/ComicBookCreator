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
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import comicBookModel.ComicBook;

@SuppressWarnings("serial")
abstract class ComicBookPanel extends JPanel {
    /***** The Comic Book Model *****/
    final ComicBook comic;       // Data
    final ComicBookState state;  // State
    /********************************/
    // Icon size
    private int iconWidth = 24;
    private int iconHeight = 24;
    // Color for selected components
    protected Color selected = new Color(100, 100, 255);
    // Horizontal/vertical gap between sub-components
    protected final int hgap = 6, vgap = 6;
    /***** Constructors *****/
    ComicBookPanel(ComicBook comic, ComicBookState state) {
        this.comic = comic;
        this.state = state;
    }
    /***** Abstract Methods *****/
    // Updates this ComicBookPanel components for the current Comic Book Model.
    abstract void update();
    /***** Inherited Methods *****/
    protected JButton standardButton(String name, ActionListener listener){
        String filename = name + ".png";
        return standardButton(name, filename, listener);
    }
    protected JButton standardButton(String name, String filename, ActionListener listener){
        JButton button;
        URL url = this.getClass().getClassLoader().getResource(filename);
        try {
            Image image = ImageIO.read(url);
            image = image.getScaledInstance(iconWidth, iconHeight, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            button = new JButton(icon);
        } catch (Exception e) {
            button = new JButton(name);
        } 
        button.setToolTipText(name);
        button.setActionCommand(name);
        button.addActionListener(listener);
        return button;
    }
}
