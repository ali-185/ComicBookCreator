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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import comicBookModel.PositionedImage;
/**
 * Creates a JDialog for previewing and applying halftone to an image. The 
 * JDialog containing 2 images, a slider, OK button, and Cancel button. The 
 * left image is a selector image where the user can select the area that is
 * previewed in the right image. The right image is the preview image and
 * will have the selected amount of halftone applied. The slider is for
 * selecting the amount of halftone to be applied.
 */
@SuppressWarnings("serial")
class HalftoneChooser extends JDialog implements MouseListener, MouseMotionListener, ChangeListener, ActionListener{
    /**
     * The width of the preview window
     */
    private int previewWidth = 100;
    /**
     * The height of the preview window
     */
    private int previewHeight = 100;
    /**
     * The width of the selector window
     */
    private int selectorWidth = 100;
    /**
     * The height of the selector window
     */
    private int selectorHeight = 100;
    /**
     * The scaling required such that the image fits inside the selector window
     */
    private double scale;
    /**
     * A X coord on the image where the preview is currently centered
     */
    private int centreX;
    /**
     * A Y coord on the image where the preview is currently centered
     */
    private int centreY;
    /**
     * The image to be halftoned.
     */
    private PositionedImage image;
    /**
     * A sample of the image, centred on centreX/Y and has the preview dimensions.
     */
    private PositionedImage previewImage;
    /**
     * A smaller version of image which is within selector dimensions
     */
    private BufferedImage selectorImage;
    /**
     * The previewImage with halftone applied.
     */
    private PositionedImage halftone;    
    /**
     * The minimum selectable halftone (less than 2 wouldn't make sense)
     */
    private final int minHalftoneSize = 2;
    /**
     * The maximum selectable halftone (24 is large even on high def. pictures)
     */
    private final int maxHalftoneSize = 24;
    /**
     * The currently selected halftone size
     */
    private int halftoneSize = -1;
    /**
     * The JLabel which holds the selector image, for selecting the current 
     * preview area
     */
    private JLabel selector;
    /**
     * The JLabel which holds the preview image
     */
    private JLabel preview;
    /**
     * The Jslider for selecting the halftone
     */
    private JSlider slider;
    /**
     * OK Button for accepting current halftone
     */
    private JButton ok;
    /**
     * Cancel Button for rejecting any change
     */
    private JButton cancel;
    /**
     * Flag to indicate if the user has OK'd the selected halftone
     */
    private boolean success = false;
    
    /**
     * Constructor for HalftoneChooser, use showDiaglog to display the HalftoneChooser
     * @param image The image to be halftoned
     * @param initialHalftone The initial halftone to be applied in the preview
     * window
     * @see HalftoneChooser
     * @see showDiaglog
     */
    public HalftoneChooser(PositionedImage image, int initialHalftone){
        super();
        this.image = image;
        // Check initial halftone
        if(initialHalftone < this.minHalftoneSize){initialHalftone =  this.minHalftoneSize;}
        if(initialHalftone > this.maxHalftoneSize){initialHalftone = this.maxHalftoneSize;}
        this.halftoneSize = initialHalftone;
        // Initialize the centre as the centre of the image
        this.centreX = image.getWidth() / 2;
        this.centreY = image.getHeight() / 2;
        // Calculate the scaling for the small image
        double wScale = (double) selectorWidth / (double) image.getWidth();
        double hScale = (double) selectorHeight / (double) image.getHeight();
        this.scale = wScale < hScale ? wScale : hScale;
        // Create the selector label
        this.selector = new JLabel();        
        this.selector.setPreferredSize(new Dimension(selectorWidth, selectorHeight));
        this.selector.setHorizontalAlignment(JLabel.CENTER);
        updateSelector();
        this.selector.addMouseListener(this);
        this.selector.addMouseMotionListener(this);
        // Create the preview window
        this.preview = new JLabel();
        this.preview.setPreferredSize(new Dimension(previewWidth, previewHeight));
        this.preview.setHorizontalAlignment(JLabel.CENTER);
        updatePreviewImage();
        // Create the slider and get it to update the preview window
        this.slider = new JSlider(JSlider.HORIZONTAL, this.minHalftoneSize, this.maxHalftoneSize, this.halftoneSize);
        this.slider.addChangeListener(this);
        // Turn on labels at major tick marks.
        this.slider.setMajorTickSpacing(2);
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        // Create the dynamicPreview label
        JPanel dynamicPreview = new JPanel(new BorderLayout(6, 6));
        dynamicPreview.setBorder(BorderFactory.createTitledBorder(null, "Select Halftone Size", 2, 0));
        dynamicPreview.add(this.selector, BorderLayout.WEST);
        dynamicPreview.add(this.preview, BorderLayout.EAST);
        dynamicPreview.add(this.slider, BorderLayout.SOUTH);    
        // Create the OK and Cancel pane
        this.ok = new JButton("OK");
        this.ok.addActionListener(this);
        this.cancel = new JButton("Cancel");
        this.cancel.addActionListener(this);
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        closePanel.add(this.ok);
        closePanel.add(this.cancel);
        // Setup this JDialog
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(dynamicPreview, BorderLayout.NORTH);
        this.getContentPane().add(closePanel, BorderLayout.SOUTH);
        this.pack();
        this.setModalityType(ModalityType.APPLICATION_MODAL);
    }
    /**
     * @return The halftoned preview image
     */
    PositionedImage getHalftone(){
        return this.halftone;
    }
    /**
     * @return The currently selected halftone size
     */
    int getHalftoneSize(){
        return this.halftoneSize;
    }
    /** 
     * Updates the selector JPanel based on the current scale, windowWidth, 
     * windowHeight, centreX and centreY.
     */
    private void updateSelector(){
        // Create the selectorImage image
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);
        selectorImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g = selectorImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        // Highlight the preview area
        g.setXORMode(Color.WHITE);
        g.setColor(Color.BLACK);
        // Calculate sample window
        int startX = this.centreX - previewWidth / 2;
        int startY = this.centreY - previewHeight / 2;
        g.drawRect((int) (startX * scale), (int) (startY * scale), (int) (previewWidth * scale), (int) (previewHeight * scale));
        g.dispose();
        // Draw preview area 
        ImageIcon selectorIcon = new ImageIcon(selectorImage);
        selector.setIcon(selectorIcon);
        selector.validate();
    }
    /**
     * Updates the halftone on the preview image to the currently selected 
     * halftone size.
     */
    private void updatePreviewHalftone(){
        PositionedImage halftone = previewImage.toHalftone(halftoneSize);
        ImageIcon icon = new ImageIcon(halftone);
        preview.setIcon(icon);
        preview.validate();
    }
    /**
     * Updates the preview image to the currently selected position on the 
     * selector window, and the currently selected halftone size.
     */
    private void updatePreviewImage(){
        // Update the preview image
        int startX = this.centreX - previewWidth / 2;
        int startY = this.centreY - previewHeight / 2;
        // Check within bounds
        if(startX < 0){startX = 0;}
        if(startY < 0){startY = 0;}
        if(startX + previewWidth > image.getWidth()){previewWidth = image.getWidth() - startX;}
        if(startY + previewHeight > image.getHeight()){previewHeight = image.getHeight() - startY;}
        // Create the sample image
        BufferedImage sample = image.getSubimage(startX, startY, previewWidth, previewHeight);
        this.previewImage = new PositionedImage(sample, sample.getType());
        updatePreviewHalftone();
    }
    /**
     * Displays the HalftoneChooser window
     */
    public boolean showDiaglog(){
        this.setVisible(true);
        return this.success;
    }
    /**
     *  Listener method for the halftone slider.  
     */
    public void stateChanged(ChangeEvent e) {
        // Update current halftoneSize
        halftoneSize = slider.getValue();
        // Update the preview window
        updatePreviewHalftone();
        preview.repaint();
    }
    /**
     *  @see mouseReleased(MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        mouseReleased(e);
    }
    /**
     * No action
     */
    public void mouseMoved(MouseEvent e) {}
    /**
     * No action
     */
    public void mouseClicked(MouseEvent e) {}
    /**
     * No action
     */
    public void mouseEntered(MouseEvent e) {}
    /**
     * No action
     */
    public void mouseExited(MouseEvent e) {}
    /**
     * No action
     */
    public void mousePressed(MouseEvent e) {}
    /**
     *  Listener method for mouse selecting a position in the the selector 
     *  image.
     */
    public void mouseReleased(MouseEvent e) {
        // Get the selector labels and images centre (as they align)
        int csx = selector.getWidth()/2;
        int csy = selector.getHeight()/2;
        int cix = selectorImage.getWidth()/2;
        int ciy = selectorImage.getHeight()/2;
        // Get the coords on the selector
        int sx = e.getX();
        int sy = e.getY();
        // Transform to coords on the selector image
        int cx = sx - csx + cix;
        int cy = sy - csy + ciy;
        // Scale to coords on the image
        int x = (int) (cx / scale);
        int y = (int) (cy / scale);
        // Make sure they are within bounds
        if(x < previewWidth/2){x = previewWidth/2;}
        if(y < previewHeight/2){y = previewHeight/2;}
        if(x + previewWidth/2 + 1 > image.getWidth()){x = image.getWidth() - previewWidth/2 - 1;}
        if(y + previewHeight/2 + 1 > image.getHeight()){y = image.getHeight() - previewHeight/2 - 1;}
        // Update centre and selector
        centreX = x;
        centreY = y;
        updateSelector();
        updatePreviewImage();
        repaint();
    }
    /**
     *  Listener method for the OK and Cancel buttons.  
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.cancel){
            this.halftoneSize = -1;
            this.success = false;
        }else if(e.getSource() == this.ok){
            // Update label to please wait
            final JDialog busy = new JDialog();
            final JOptionPane optionPane = new JOptionPane("Calculating, please wait.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            busy.setTitle("Message");
            busy.setModal(true);
            busy.setContentPane(optionPane);
            busy.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            busy.pack();
            
            class DoHalftoneThread extends Thread {
                public void run() {
                    halftone = image.toHalftone(halftoneSize);
                    busy.dispose();
                }
            }
            (new DoHalftoneThread()).start();
            busy.setVisible(true);
            this.success = true;
        }
        this.setVisible(false);
    }
}
