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
package hsbColorChooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeListener;
/**
 * Creates a customized JColorChooser with only Hue Saturation and Brightness 
 * selectors. The class creates a pane of controls to allow the user to
 * select a color.
 * 
 * @see javax.swing.JColorChooser
 * 
 * @author Alastair Crowe
 */
public class JHSBColorChooser{
    
    JColorChooser colorChooser;
    
    private Color initialColor = null;
    private Color finalColor = null;
    
    /**
     * Creates an HSB color chooser pane with the specified initial color.
     * @params initialColor the initial color set in the chooser
     * @see javax.swing.JColorChooser#JColorChooser(Color)
     */
    public JHSBColorChooser(Color initialColor) {
        // Create the color chooser
        HSBColorSelectionModel model = new HSBColorSelectionModel();
        colorChooser = new JColorChooser(model);
        // Remove the preview panel
        colorChooser.setPreviewPanel(new JPanel());
        // Override the chooser panels with my own
        HSBColorChooser chooser = new HSBColorChooser(model);
        AbstractColorChooserPanel panels[] = {chooser};
        colorChooser.setChooserPanels(panels);
        // Set the initial color
        this.initialColor = initialColor;
        colorChooser.setColor(initialColor);
    }
    /**
     * Shows a modal color-chooser dialog and blocks until the dialog is 
     * hidden. If the user presses the "OK" button, then this method 
     * hides/disposes the dialog and returns the selected color. If the user
     * presses the "Cancel" button or closes the dialog without pressing "OK",
     * then this method hides/disposes the dialog and returns null.
     * 
     * @param component - the parent Component for the dialog
     * @param title - the String containing the dialog's title
     * @param initialColor - the initial Color set when the color-chooser is shown
     * @return the selected color or null if the user opted out
     * @see javax.swing.JColorChooser#showDialog(Component, String, Color)
     */
    public Color showDialog(Component component, String title, ChangeListener listener){
        // Add Change Listener
        colorChooser.getSelectionModel().addChangeListener(listener);
           // Update the controller when returned
           ActionListener ok = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                finalColor = colorChooser.getColor();
            }
        };
           ActionListener cancel = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                finalColor = initialColor;
            }
        };
        // Create and set up the dialog pane.
        JDialog dialog = JColorChooser.createDialog(component, title, true, colorChooser, ok, cancel);
        dialog.getContentPane().add(colorChooser);
        // Display the window.
        dialog.pack();
        dialog.setVisible(true);
        // Return the selected color
        return finalColor;
    }
    /**
     * Gets the current color value from the color chooser. By default, this
     * delegates to the model.
     * @return the current color value of the color chooser
     * @see javax.swing.JColorChooser#getColor()
     */
    public Color getColor() {
        return colorChooser.getColor();
    }
}
