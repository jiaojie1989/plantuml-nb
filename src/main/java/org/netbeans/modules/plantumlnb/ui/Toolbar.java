/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui;

import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
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
     * Component showing image.
     */
    private JPanel panel;    

    private DataObjectAccess dataObjectAccess;
    private AffineTransform currentAt = null;
    private JSVGCanvas canvas = null;
    private SVGImagePreviewPanel svgImagePreviewPanel = null;
    private JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 10);
    
    private static final Logger logger = Logger.getLogger(SVGImagePreviewPanel.class.getName());

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
        toolBar.add(getOpenInBrowserButton());
        toolBar.add(getRealTimeZoomButton());

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
            logger.log(Level.SEVERE, e.getLocalizedMessage()); 
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
            logger.log(Level.SEVERE, e.getLocalizedMessage());
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
            logger.log(Level.SEVERE, e.getLocalizedMessage());
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
            logger.log(Level.SEVERE, e.getLocalizedMessage());
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
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }

        button.addActionListener(svgImagePreviewPanel.getResetTransformAction());

        return button;
    }

    private JButton getOpenInBrowserButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/browser_generic_16x.png"));
            button.setIcon(new ImageIcon(img));
            button.setToolTipText("Open in Browser");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }

        button.addActionListener(svgImagePreviewPanel.getOpenInBrowserAction());

        return button;
    }
    
    private JToggleButton getRealTimeZoomButton() {
        final JToggleButton button = new JToggleButton();

        try {
            Image img = ImageIO.read(getClass().getResource("/org/netbeans/modules/plantumlnb/magnifier_zoom.png"));
            button.setIcon(new ImageIcon(img));
            button.setToolTipText("Real Time Zoom");            
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }

        button.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(button.isSelected()) {
                    button.getParent().add(zoomSlider);
                } else {
                    button.getParent().remove(zoomSlider);   
                }
                button.getParent().repaint();
            }
            
        });
        
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
