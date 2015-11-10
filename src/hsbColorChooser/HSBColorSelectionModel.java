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

import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.*;
import java.awt.Color;
/**
 * A hue, saturation, and brightness model that supports selecting a Color.
 * @see javax.swing.colorchooser.ColorSelectionModel
 * 
 * @author Alastair Crowe
 */
public class HSBColorSelectionModel implements ColorSelectionModel{

    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    /**
     * The currently selected colors hue, saturation, and brightness.
     */
    private float hue, saturation, brightness;
    
    /**
     * Creates a HSBColorSelectionModel with the current color set to 
     * Color.white. This is the default constructor.
     */
    public HSBColorSelectionModel() {
        this(1f, 1f, 1f);
    }
    /**
     * Creates a HSBColorSelectionModel with the current color set to
     * color, which should be non-null. Note that setting the color to null is
     * undefined and may have unpredictable results.
     * @param color the new Color
     */
    public HSBColorSelectionModel(Color color) {
        setSelectedColor(color);
    }
    /**
     * Creates a HSBColorSelectionModel with the current color set to specified
     * hue, saturation, and brightness. Note that each of hue, saturation, or 
     * brightness must be between 0 and 1 inclusive, and setting outside this 
     * range is undefined and may have unpredictable results.
     * @param hue the new hue which must be in the range 0 to 1 inclusive
     * @param saturation the new hue which must be in the range 0 to 1
     * inclusive
     * @param brightness the new hue which must be in the range 0 to 1 
     * inclusive
     */
    public HSBColorSelectionModel(float hue, float saturation, float brightness) {
        setSelectedColor(hue, saturation, brightness);
    }
    
    /**
     * @return the currently selected colors hue
     */
    public float getHue(){
        return this.hue;
    }
    /**
     * @return the currently selected colors saturation
     */
    public float getSaturation(){
        return this.saturation;
    }
    /**
     * @return the currently selected colors brightness
     */
    public float getBrightness(){
        return this.brightness;
    }
    /**
     * Returns the selected Color which should be non-null.
     * 
     * @return the selected Color
     * @see javax.swing.colorchooser.ColorSelectionModel#getSelectedColor()
     */
    public Color getSelectedColor() {
        int rgb = Color.HSBtoRGB(hue, saturation, brightness);
        return new Color(rgb);
    }
    /**
     * Sets the selected color to color. Note that setting the color to null
     * is undefined and may have unpredictable results. This method fires a 
     * state changed event if it sets the current color to a new non-null
     * color; if the new color is the same as the current color, no event is 
     * fired.
     * 
     * @param color the new Color
     * @see javax.swing.colorchooser.ColorSelectionModel#setSelectedColor(Color)
     */
    public void setSelectedColor(Color color) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        setSelectedColor(hsb[0], hsb[1], hsb[2]);
    }
    /**
     * Sets the selected color to the specified hue, saturation, and 
     * brightness. Note that each hue, saturation, or brightness must be
     * between 0 and 1 inclusive, and setting outside this range is undefined 
     * and may have unpredictable results.This method fires a state changed 
     * event if it sets the current color to a new non-null color; if the new 
     * color is the same as the current color, no event is fired.
     * 
     * @param hue the new hue which must be in the range 0 to 1 inclusive
     * @param saturation the new hue which must be in the range 0 to 1
     * inclusive
     * @param brightness the new hue which must be in the range 0 to 1 
     * inclusive
     * 
     * @see javax.swing.colorchooser.ColorSelectionModel#setSelectedColor(Color)
     */
    public void setSelectedColor(float hue, float saturation, float brightness) {
        if(hue != this.hue || saturation != this.saturation || brightness != this.brightness){
            this.hue = hue;
            this.saturation = saturation;
            this.brightness = brightness;
            fireStateChanged();
        }
    }
    
    /**
     * Adds a ChangeListener to the model.
     * @param l the ChangeListener to be added
     * 
     * @see javax.swing.colorchooser.ColorSelectionModel#addChangeListener(ChangeListener)
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }
    /**
     * Removes listener as a listener to changes in the model.
     * @param listener the ChangeListener to be removed
     * 
     * @see javax.swing.colorchooser.ColorSelectionModel#removeChangeListener(ChangeListener)
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }
    /**
     * Returns an array of all the ChangeListeners added to this 
     * HSBColorSelectionModel with addChangeListener.
     * @return all of the ChangeListeners added, or an empty array if no 
     * listeners have been added
     */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }
    /**
     * Runs each ChangeListener's stateChanged method.
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
}
