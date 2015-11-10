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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * HSBColorChoose is a customized panel for JHSBColorChooser.
 * @see javax.swing.colorchooser.AbstractColorChooserPanel
 * 
 * @author Alastair Crowe
 */
@SuppressWarnings("serial")
public class HSBColorChooser extends AbstractColorChooserPanel {
    /**
     * The HSBColorSelectionModel corresponding to the current state of this
     * color chooser.
     */
    HSBColorSelectionModel model;
    /**
     * A SliderComponent for selecting hue
     */
    private final SliderComponent slider;
    /**
     * A SliderComponent for selecting saturation and brightness.
     */
    private final DiagramComponent diagram;
    
    /**
     * Creates a HSBColorChooser using the specified model
     * @param model The model used to store the current selection of this 
     * HSBColorChooser
     */
    public HSBColorChooser(HSBColorSelectionModel model){
        this.model = model;
        this.slider = new SliderComponent(model);
        this.diagram = new DiagramComponent(model);
    }
    /**
     * Builds a new chooser panel.
     */
    public void buildChooser() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        diagram.setPreferredSize(new Dimension(200,200));
        add(diagram, BorderLayout.CENTER);
        slider.setPreferredSize(new Dimension(30,200));
        add(slider, BorderLayout.EAST);
    }
    /**
     * Redraws this HSBColorChooser.
     */
    public void updateChooser() {
        this.slider.repaint();
        this.diagram.repaint();
    }
    /**
     * Not implemented.
     */
    public String getDisplayName() {
        return "HSBColorChooser";
    }
    /**
     * Not implemented.
     */
    public Icon getSmallDisplayIcon() {
        return null;
    }
    /**
     * Not implemented.
     */
    public Icon getLargeDisplayIcon() {
        return null;
    }
}
    