/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.demo.viewer;

import org.jcurl.core.base.CenteredZoomer;
import org.jcurl.core.swing.PositionDisplay;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ViewerPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 7468246612242328793L;

    /** Creates new form ViewerPanel */
    public ViewerPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // ">//GEN-BEGIN:initComponents
    private void initComponents() {
        org.jcurl.core.swing.PositionDisplay birdsEyeDisplay;
        javax.swing.JPanel buttonPanel;
        javax.swing.JPanel controlPanel;
        PositionDisplay detailDisplay;
        javax.swing.JPanel detailPanel;
        javax.swing.JButton pauseButton;
        javax.swing.JButton startButton;
        javax.swing.JButton stopButton;
        javax.swing.JSlider timeSlider;

        detailPanel = new javax.swing.JPanel();
        detailDisplay = new PositionDisplay();
        birdsEyeDisplay = new org.jcurl.core.swing.PositionDisplay();
        controlPanel = new javax.swing.JPanel();
        timeSlider = new javax.swing.JSlider();
        buttonPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();

        detailPanel.setPreferredSize(new java.awt.Dimension(207, 200));
        detailDisplay.setZoom(CenteredZoomer.HOUSE);

        org.jdesktop.layout.GroupLayout detailPanelLayout = new org.jdesktop.layout.GroupLayout(
                detailPanel);
        detailPanel.setLayout(detailPanelLayout);
        detailPanelLayout.setHorizontalGroup(detailPanelLayout
                .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(detailDisplay,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 412,
                        Short.MAX_VALUE));
        detailPanelLayout.setVerticalGroup(detailPanelLayout
                .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(detailDisplay,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228,
                        Short.MAX_VALUE));

        birdsEyeDisplay.setZoom(CenteredZoomer.HOG2HACK);

        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel,
                javax.swing.BoxLayout.Y_AXIS));

        controlPanel.add(timeSlider);

        buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel,
                javax.swing.BoxLayout.X_AXIS));

        startButton.setText("Start");
        buttonPanel.add(startButton);

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        buttonPanel.add(stopButton);

        pauseButton.setText("Pause");
        pauseButton.setEnabled(false);
        buttonPanel.add(pauseButton);

        controlPanel.add(buttonPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
                this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                layout.createSequentialGroup().add(controlPanel,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 400,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)).add(detailPanel,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 412,
                Short.MAX_VALUE).add(birdsEyeDisplay,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 412,
                Short.MAX_VALUE));
        layout
                .setVerticalGroup(layout
                        .createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING)
                        .add(
                                layout
                                        .createSequentialGroup()
                                        .add(
                                                detailPanel,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                228,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(
                                                birdsEyeDisplay,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                30,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(
                                                controlPanel,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
