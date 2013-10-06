/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.apache.batik.swing.JSVGCanvas;
import org.netbeans.modules.plantumlnb.DataObjectAccess;
import org.netbeans.modules.plantumlnb.SVGImagePreviewPanel;
import org.netbeans.modules.plantumlnb.ui.actions.ExportAction;
import org.openide.util.NbBundle;

/**
 *
 * @author venkat
 */
public class Toolbar {

    private final Collection toolbarButtons = new ArrayList(11);
    /**
     * Scale of image.
     */
    private double scale = 1.0D;
    /**
     * Component showing image.
     */
    private JPanel panel;
    /**
     * Viewed image.
     */
    private NBImageIcon storedImage; //TODO: Remove, not used anymore
    /**
     * Increase/decrease factor.
     */
    private final double changeFactor = Math.sqrt(2.0D);
    /**
     * On/off grid.
     */
    private boolean showGrid = false;
    
    private DataObjectAccess dataObjectAccess;
    private AffineTransform currentAt = null;    
    private JSVGCanvas canvas = null;
    private SVGImagePreviewPanel svgImagePreviewPanel = null;

    private Toolbar() {}
        
    public JToolBar createToolBar() {
        // Definition of toolbar.
        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); //NOI18N
        toolBar.setFloatable(false);
        toolBar.setName(NbBundle.getBundle(Toolbar.class).getString("ACSN_Toolbar"));

        JButton button;

        toolBar.add(getExportButton());
        toolBar.add(getZoomInButton());
        toolBar.add(getZoomOutButton());
        toolBar.add(getResetButton());
        toolBar.add(getRotateButton());

        return toolBar;
    }
    
    public JToolBar createToolBar(DataObjectAccess doa) {
        this.dataObjectAccess = doa;
        return createToolBar();
    }

    public JButton getExportButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/save.png"));
            button.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace(); //TODO log this.
        }

        button.addActionListener(new ExportAction(panel, dataObjectAccess));

        return button;
    }
    
    private JButton getZoomInButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/zoom-in.png"));
            button.setIcon(new ImageIcon(img));
            button.setToolTipText("Zoom in");
        } catch (IOException e) {
            e.printStackTrace(); //TODO log this.
        }

        button.addActionListener(svgImagePreviewPanel.getZoomInActionInstance());

        return button;
    }
    
    private JButton getZoomOutButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/zoom-out.png"));
            button.setIcon(new ImageIcon(img));
            button.setToolTipText("Zoom Out");
        } catch (IOException e) {
            e.printStackTrace(); //TODO log this.
        }

        button.addActionListener(svgImagePreviewPanel.getZoomOutActionInstance());

        return button;
    }

    private JButton getRotateButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/rotate.png"));
            button.setIcon(new ImageIcon(img));
            button.setToolTipText("Rotate");
        } catch (IOException e) {
            e.printStackTrace(); //TODO log this.
        }

        button.addActionListener(svgImagePreviewPanel.getRotateActionInstance());

        return button;
    }

    private JButton getResetButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/zoom-fit.png"));
            button.setIcon(new ImageIcon(img));
            button.setToolTipText("Reset");
        } catch (IOException e) {
            e.printStackTrace(); //TODO log this.
        }

        button.addActionListener(svgImagePreviewPanel.getResetTransformAction());

        return button;
    }
     

    private NBImageIcon retrieveImage() {
        return PUMLTopComponent.getNBImageIcon();
    }
    private static Toolbar instance;

    public static Toolbar instance() {
        if (instance == null) {
            instance = new Toolbar();
        }

        return instance;
    }

    public DataObjectAccess getDataObjectAccess() {
        return dataObjectAccess;
    }

    public void setDataObjectAccess(DataObjectAccess dataObjectAccess) {
        this.dataObjectAccess = dataObjectAccess;
    }    

    public AffineTransform getCurrentAt() {
        return currentAt;
    }

    public void setCurrentAt(AffineTransform currentAt) {
        this.currentAt = currentAt;
    }

    public JSVGCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(JSVGCanvas canvas) {
        this.canvas = canvas;
    }

    public SVGImagePreviewPanel getSvgImagePreviewPanel() {
        return svgImagePreviewPanel;
    }

    public void setSvgImagePreviewPanel(SVGImagePreviewPanel svgImagePreviewPanel) {
        this.svgImagePreviewPanel = svgImagePreviewPanel;
    }
}
