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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
/**
 * A SaveAsPanel has 3 buttons for selecting the format which you want to
 * save the file as, which are JPG, PNG, and GIF. When pressed a window for 
 * locating a save destination and filename appears then, upon selection, the 
 * file is created in the specified format.
 * 
 * @author Alastair Crowe
 */
@SuppressWarnings("serial")
class SaveAsPanel extends ComicBookPanel implements ActionListener {
    // The available file formats
    private static final String JPG = "JPG";
    private static final String PNG = "PNG";
    private static final String GIF = "GIF";
    /**
     * Creates a SaveAsPanel for the comic book model and current state.
     * @param comic The comic book model
     * @param state The current state of the GUI
     */
    public SaveAsPanel(ComicBook comic, ComicBookState state) {
        super(comic, state);
        // Setup the layout and border
        setLayout(new GridLayout(1, 3, hgap, vgap));
        setBorder(BorderFactory.createTitledBorder(null, "Save As", 2, 0));
        // Create the JButtons
        add(standardButton(JPG, this));
        add(standardButton(PNG, this));
        add(standardButton(GIF, this));
    }
    /**
     * Not implemented
     */
    void update() {}
    /**
     * Action listener for the buttons. When pressed a file/folder selection
     * window is displayed, then upon selection, the file created in the 
     * specified format.
     */
    public void actionPerformed(ActionEvent e) {
        String extension = null;
        if(e.getActionCommand() == JPG){
            extension = "jpg";
        }else if(e.getActionCommand() == PNG){
            extension = "png";
        }else if(e.getActionCommand() == GIF){
            extension = "gif";
        }
        try {
            // Convert the image to an input stream of requested extension
            ComicPage page = comic.getPage(state.getPage());
            BufferedImage image = page.toImage();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, extension, outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            // Save the image (unless the user cancels)
            String[] extensions = {extension};
            FileSaveService fss = (FileSaveService)ServiceManager.
                      lookup("javax.jnlp.FileSaveService"); 
            fss.saveFileDialog(null,
                    extensions, inputStream, null); 
        } catch (Exception ex) {
            String error = "Error:\n" + ex.getMessage();
            JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
