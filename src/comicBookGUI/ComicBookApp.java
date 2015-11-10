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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comicBookModel.ComicBook;

/**
 * A panel for creating a Comic Book.
 * 
 * @author Alastair Crowe
 *
 */
@SuppressWarnings("serial")
public class ComicBookApp extends JPanel {

    public ComicBookApp() {
        // A4 paper is 210mm x 297mm
        final int width = 210*2;
        final int height = 297*2;
        // Create the model
        final ComicBook comic = new ComicBook(width, height);
        final ComicBookState state = new ComicBookState(this, 0, 0, ComicBookState.CONTENTS_BORDER_MODE);
        // Create the GUI
        final ComicBookGUI comicBookGUI = new ComicBookGUI(comic, state, width, height);
        add(comicBookGUI);
    }
    /***** Main and GUI Methods *****/
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Comic Creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create and set up the content pane.
        ComicBookApp newContentPane = new ComicBookApp();
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
