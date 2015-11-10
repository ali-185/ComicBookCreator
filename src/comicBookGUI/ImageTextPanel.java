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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import comicBookModel.ComicBook;
import comicBookModel.ComicPage;
import comicBookModel.ImageLayer;
import comicBookModel.Layer;
import comicBookModel.PositionedImage;
import comicBookModel.TextLayer;
import hsbColorChooser.JHSBColorChooser;

@SuppressWarnings("serial")
class ImageTextPanel extends ComicBookPanel implements ActionListener{
    // Flag to only update components when the layer changed
    private boolean prevLayerImage;
    private Layer prevLayer;
    private boolean highlightsChanged;
    
    private static final String LOAD_IMAGE = "Load Image";
    private static final String COLOR_BW = "Color Mode";
    private static final String COMIC_EFFECT = "Comic Effect";
    
    private static final String INSERT_TEXT = "Insert Text";
    private static final String TEXT_COLOR = "Text Color";
    private static final String BACKGROUND_COLOR = "Background Color";
    
    private final JButton loadImage, colorImage, comicImage;
    private final JButton insertText, colorText, backgroundText;
    
    private final Component textComponent;
    
    public ImageTextPanel(ComicBook comic, ComicBookState state, Component textComponent) {
        super(comic, state);
        // Setup the layout and border
        setLayout(new GridLayout(1, 3, hgap, vgap));
        setBorder(BorderFactory.createTitledBorder(null, "Image", 2, 0));
        // Create the buttons
        loadImage = standardButton(LOAD_IMAGE, this);
        colorImage = standardButton(COLOR_BW, this);
        comicImage = standardButton(COMIC_EFFECT, this);
        insertText = standardButton(INSERT_TEXT, this);
        colorText = standardButton(TEXT_COLOR, this);
        backgroundText = standardButton(BACKGROUND_COLOR, this);
        // Setup the text component (for when the insert text button is pressed)
        this.textComponent = textComponent;
        // Set prev layer as not current so it will update when painted
        ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        prevLayerImage = !(layer instanceof ImageLayer);
        update();
    }

    void update() {
        ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        
        if(prevLayerImage != layer instanceof ImageLayer 
                || highlightsChanged 
                || layer != prevLayer){
            prevLayerImage = layer instanceof ImageLayer;
            highlightsChanged = false;
            removeAll();
            if(layer instanceof ImageLayer){
                // Add image buttons
                setBorder(BorderFactory.createTitledBorder(null, "Image", 2, 0));
                add(loadImage);
                add(colorImage);
                add(comicImage);
                ImageLayer iLayer = (ImageLayer) layer;
                // Update color button
                if(iLayer.getType() == null || iLayer.getType() == ImageLayer.RGB_HALFTONE){
                    colorImage.setBackground(null);
                }else{
                    colorImage.setBackground(selected);
                }
                // Update comic effect button
                if(iLayer.getType() == null || iLayer.getType() == ImageLayer.GREYSCALE){
                    comicImage.setBackground(null);
                }else{
                    comicImage.setBackground(selected);
                }
            }else if(layer instanceof TextLayer){
                // Add text buttons
                setBorder(BorderFactory.createTitledBorder(null, "Text", 2, 0));
                add(insertText);
                add(colorText);
                add(backgroundText);
               }else{
                   assert false;
               }
               validate();
        }
    }
    public void paint(Graphics g){
        update();
        super.paint(g);
    }

    public void actionPerformed(ActionEvent e) {
        ComicPage page = comic.getPage(state.getPage());
        Layer layer = page.getLayer(state.getLayer());
        String command = e.getActionCommand();
           if(command.equals(LOAD_IMAGE)) {
            if(layer instanceof ImageLayer){
                ImageLayer iLayer = (ImageLayer) layer;
                try {
                    // Open the image from selected file
                    FileOpenService fos = (FileOpenService)ServiceManager.
                            lookup("javax.jnlp.FileOpenService"); 
                    FileContents fileContents = fos.openFileDialog(null, null); 
                    if(fileContents != null){    
                        // User didn't cancel
                        fileContents.getInputStream();
                        BufferedImage image = ImageIO.read(fileContents.getInputStream());
                        iLayer.setImage(new PositionedImage(image, PositionedImage.TYPE_INT_RGB));
                        state.repaint();
                    }
                } catch (Exception ex) {
                    String error = "Error:\n" + ex.getMessage();
                    JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(command.equals(COLOR_BW)) {
            if(layer instanceof ImageLayer){
                ImageLayer iLayer = (ImageLayer) layer;
                if(iLayer.getType() == null || iLayer.getType() == ImageLayer.RGB_HALFTONE){
                    iLayer.setType(ImageLayer.GREYSCALE);
                }else{    
                    iLayer.setType(null);
                }
                highlightsChanged = true;
                state.repaint();
            }
        }else if(command.equals(COMIC_EFFECT)) {
            if(layer instanceof ImageLayer){
                ImageLayer iLayer = (ImageLayer) layer;
                if(iLayer.getType() == null && iLayer.getDefault() != null){
                    HalftoneChooser chooser = new HalftoneChooser(iLayer.getDefault(), iLayer.getHalftoneSize());
                    if(chooser.showDiaglog()){
                        iLayer.setRGBHalftone(chooser.getHalftone(), chooser.getHalftoneSize());
                        iLayer.setType(ImageLayer.RGB_HALFTONE);
                        highlightsChanged = true;
                        state.repaint();
                    }
                }else if(iLayer.getType() == ImageLayer.GREYSCALE){    
                    HalftoneChooser chooser = new HalftoneChooser(iLayer.getGreyscale(), iLayer.getHalftoneSize());
                    if(chooser.showDiaglog()){
                        iLayer.setBWHalftone(chooser.getHalftone(), chooser.getHalftoneSize());
                        iLayer.setType(ImageLayer.BW_HALFTONE);
                        highlightsChanged = true;
                        state.repaint();
                    }
                }else if(iLayer.getType() == ImageLayer.RGB_HALFTONE){
                    iLayer.setType(null);
                    highlightsChanged = true;
                    state.repaint();
                }else if(iLayer.getType() == ImageLayer.BW_HALFTONE){
                    iLayer.setType(ImageLayer.GREYSCALE);
                    highlightsChanged = true;
                    state.repaint();
                }
            }     
        }else if(command.equals(INSERT_TEXT)) {
            textComponent.requestFocus();
        }else if(command.equals(TEXT_COLOR)) {
            if(layer instanceof TextLayer){
                final TextLayer tLayer = (TextLayer) layer;
                Color initial = tLayer.TEXT_PAINT instanceof Color ? (Color) tLayer.TEXT_PAINT : null;
                final JHSBColorChooser colorChooser = new JHSBColorChooser(initial); 
                ChangeListener listener = new ChangeListener(){
                    // Update the GUI with the selected text color
                    public void stateChanged(ChangeEvent e) {
                        tLayer.TEXT_PAINT = colorChooser.getColor();
                        state.repaint();
                    }
                };
                tLayer.TEXT_PAINT = colorChooser.showDialog(this, "Select Text Color", listener);
            }
        }else if(command.equals(BACKGROUND_COLOR)) {
            if(layer instanceof TextLayer){
                final TextLayer tLayer = (TextLayer) layer;
                Color initial = tLayer.BACKGROUND_PAINT instanceof Color ? (Color) tLayer.BACKGROUND_PAINT : null;
                final JHSBColorChooser colorChooser = new JHSBColorChooser(initial); 
                ChangeListener listener = new ChangeListener(){
                    // Update the GUI with the selected background color
                    public void stateChanged(ChangeEvent e) {
                        tLayer.BACKGROUND_PAINT = colorChooser.getColor();
                        state.repaint();
                    }
                };
                tLayer.BACKGROUND_PAINT = colorChooser.showDialog(this, "Select Background Color", listener);
            }
        }
    }
}
