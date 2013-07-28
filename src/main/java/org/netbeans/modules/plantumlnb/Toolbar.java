/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

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

    private Toolbar() {
    
    }
        
    public JToolBar createToolBar() {
        // Definition of toolbar.
        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); //NOI18N
        toolBar.setFloatable(false);
        toolBar.setName(NbBundle.getBundle(Toolbar.class).getString("ACSN_Toolbar"));

        JButton button;

        toolBar.add(getExportButton());

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

    /**
     * Gets zoom button.
     */
    private JButton getZoomButton(final int xf, final int yf) {
        // PENDING buttons should have their own icons.
        JButton button = new JButton("" + xf + ":" + yf); // NOI18N
        if (xf < yf) {
            button.setToolTipText(NbBundle.getBundle(Toolbar.class).getString("LBL_ZoomOut") + " " + xf + " : " + yf);
        } else {
            button.setToolTipText(NbBundle.getBundle(Toolbar.class).getString("LBL_ZoomIn") + " " + xf + " : " + yf);
        }
        button.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACS_Zoom_BTN"));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                customZoom(xf, yf);
            }
        });

        return button;
    }

    private JButton getZoomButton() {
        // PENDING buttons should have their own icons.
        JButton button = new JButton(NbBundle.getBundle(CustomZoomAction.class).getString("LBL_XtoY")); // NOI18N
        button.setToolTipText(NbBundle.getBundle(Toolbar.class).getString("LBL_CustomZoom"));
        button.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACS_Zoom_BTN"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CustomZoomAction sa = (CustomZoomAction) SystemAction.get(CustomZoomAction.class);
                sa.performAction();
            }
        });

        return button;
    }

    /**
     * Draws zoom in scaled image.
     */
    public void zoomIn() {
        scaleIn();
        resizePanel();
        panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
    }

    /**
     * Draws zoom out scaled image.
     */
    public void zoomOut() {
        double oldScale = scale;

        scaleOut();

        // You can't still make picture smaller, but bigger why not?
        if (!isNewSizeOK()) {
            scale = oldScale;

            return;
        }

        resizePanel();
        panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
    }

    /**
     * Change proportion "out"
     */
    private void scaleOut() {
        scale = scale / changeFactor;
    }

    /**
     * Change proportion "in"
     */
    private void scaleIn() {
        double oldComputedScale = getScale();

        scale = changeFactor * scale;

        double newComputedScale = getScale();

        if (newComputedScale == oldComputedScale) // Has to increase.
        {
            scale = newComputedScale + 1.0D;
        }
    }

    /**
     * Resizes panel.
     */
    private void resizePanel() {
        panel.setPreferredSize(new Dimension(
                (int) (getScale() * retrieveImage().getIconWidth()),
                (int) (getScale() * retrieveImage().getIconHeight())));
        panel.revalidate();
    }

    /**
     * Tests new size of image. If image is smaller than minimum size(1x1)
     * zooming will be not performed.
     */
    private boolean isNewSizeOK() {
        if (retrieveImage() == null) {
            return false;
        }

        if (((getScale() * retrieveImage().getIconWidth()) > 1)
                && ((getScale() * retrieveImage().getIconHeight()) > 1)) {
            return true;
        }
        return false;
    }

    /**
     * Return zooming factor.
     */
    private double getScale() {
        return scale;
    }

    /**
     * Perform zoom with specific proportion.
     *
     * @param fx numerator for scaled
     * @param fy denominator for scaled
     */
    public void customZoom(int fx, int fy) {
        double oldScale = scale;

        scale = (double) fx / (double) fy;
        if (!isNewSizeOK()) {
            scale = oldScale;

            return;
        }

        resizePanel();
        panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
    }

    /**
     * Gets grid button.
     */
    private JButton getGridButton() {
        // PENDING buttons should have their own icons.
        final JButton button = new JButton(" # "); // NOI18N
        button.setToolTipText(NbBundle.getBundle(Toolbar.class).getString("LBL_ShowHideGrid"));
        button.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACS_Grid_BTN"));
        button.setMnemonic(NbBundle.getBundle(Toolbar.class).getString("ACS_Grid_BTN_Mnem").charAt(0));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showGrid = !showGrid;
                panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
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
    
}
