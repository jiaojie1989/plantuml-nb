/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
        
        /** Scale of image. */
        private double scale = 1.0D;
        
        /** Component showing image. */
        private JPanel panel;
        
        /** Viewed image. */
        private NBImageIcon storedImage; //TODO: Remove, not used anymore
    
        
        /** Increase/decrease factor. */
        private final double changeFactor = Math.sqrt(2.0D);
            
        /** On/off grid. */
        private boolean showGrid = false;
        
        private Toolbar(){};
        
        public JToolBar createToolBar() {
        // Definition of toolbar.
        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); //NOI18N
        toolBar.setFloatable (false);
        toolBar.setName (NbBundle.getBundle(Toolbar.class).getString("ACSN_Toolbar"));
        toolBar.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACSD_Toolbar"));
            JButton outButton = new JButton(SystemAction.get(ZoomOutAction.class));
            outButton.setToolTipText (NbBundle.getBundle(Toolbar.class).getString("LBL_ZoomOut"));
            outButton.setMnemonic(NbBundle.getBundle(Toolbar.class).getString("ACS_Out_BTN_Mnem").charAt(0));
            outButton.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACSD_Out_BTN"));
            outButton.setText("");
        toolBar.add(outButton);       
        toolbarButtons.add(outButton);
            JButton inButton = new JButton(SystemAction.get(ZoomInAction.class));
            inButton.setToolTipText (NbBundle.getBundle(Toolbar.class).getString("LBL_ZoomIn"));
            inButton.setMnemonic(NbBundle.getBundle(Toolbar.class).getString("ACS_In_BTN_Mnem").charAt(0));
            inButton.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACSD_In_BTN"));
            inButton.setText("");
        toolBar.add(inButton);
        toolbarButtons.add(inButton);
        toolBar.addSeparator(new Dimension(11, 0));
        
        JButton button;
        
        toolBar.add(button = getZoomButton(1,1));
        toolbarButtons.add(button);
        toolBar.addSeparator(new Dimension(11, 0));
        toolBar.add(button = getZoomButton(1,3));
        toolbarButtons.add(button);
        toolBar.add(button = getZoomButton(1,5));
        toolbarButtons.add(button);
        toolBar.add(button = getZoomButton(1,7));
        toolbarButtons.add(button);
        toolBar.addSeparator(new Dimension(11, 0));
        toolBar.add(button = getZoomButton(3,1));
        toolbarButtons.add(button);
        toolBar.add(button = getZoomButton(5,1));
        toolbarButtons.add(button);
        toolBar.add(button = getZoomButton(7,1));
        toolbarButtons.add(button);
        toolBar.addSeparator(new Dimension(11, 0));
//        SystemAction sa = SystemAction.get(CustomZoomAction.class);
//        sa.putValue (Action.SHORT_DESCRIPTION, NbBundle.getBundle(Toolbar.class).getString("LBL_CustomZoom"));
        toolBar.add (button = getZoomButton());
        toolbarButtons.add(button);
        toolBar.addSeparator(new Dimension(11, 0));
        toolBar.add(button = getGridButton());
        toolbarButtons.add(button);

        for (Iterator it = toolbarButtons.iterator(); it.hasNext(); ) {
            ((JButton) it.next()).setFocusable(false);
        }

        return toolBar;
    }
        
            /** Gets zoom button. */
    private JButton getZoomButton(final int xf, final int yf) {
        // PENDING buttons should have their own icons.
        JButton button = new JButton(""+xf+":"+yf); // NOI18N
        if (xf < yf)
            button.setToolTipText (NbBundle.getBundle(Toolbar.class).getString("LBL_ZoomOut") + " " + xf + " : " + yf);
        else
            button.setToolTipText (NbBundle.getBundle(Toolbar.class).getString("LBL_ZoomIn") + " " + xf + " : " + yf);
        button.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACS_Zoom_BTN"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                customZoom(xf, yf);
            }
        });
        
        return button;
    }
    
        
    private JButton getZoomButton() {
        // PENDING buttons should have their own icons.
        JButton button = new JButton(NbBundle.getBundle(CustomZoomAction.class).getString("LBL_XtoY")); // NOI18N
        button.setToolTipText (NbBundle.getBundle(Toolbar.class).getString("LBL_CustomZoom"));
        button.getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(Toolbar.class).getString("ACS_Zoom_BTN"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CustomZoomAction sa = (CustomZoomAction) SystemAction.get(CustomZoomAction.class);
                sa.performAction ();
            }
        });
        
        return button;
    }
    
     /** Draws zoom in scaled image. */
    public void zoomIn() {
        scaleIn();
        resizePanel();
        panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
    }
    
    /** Draws zoom out scaled image. */
    public void zoomOut() {
        double oldScale = scale;
        
        scaleOut();
        
         // You can't still make picture smaller, but bigger why not?
        if(!isNewSizeOK()) {
            scale = oldScale;
            
            return;
        }
        
        resizePanel();
        panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
    }
    
        
    /** Change proportion "out"*/
    private void scaleOut() {
        scale = scale / changeFactor;
    }
    
    /** Change proportion "in"*/
    private void scaleIn() {
        double oldComputedScale = getScale ();
        
        scale = changeFactor * scale;
        
        double newComputedScale = getScale();
        
        if (newComputedScale == oldComputedScale)
            // Has to increase.
            scale = newComputedScale + 1.0D;
    }
        
    /** Resizes panel. */
    private void resizePanel() {
        panel.setPreferredSize(new Dimension(
            (int)(getScale () * retrieveImage().getIconWidth ()),
            (int)(getScale () * retrieveImage().getIconHeight()))
        );
        panel.revalidate();
    }
    
    /** Tests new size of image. If image is smaller than  minimum
     *  size(1x1) zooming will be not performed.
     */
    private boolean isNewSizeOK() {
        if(retrieveImage() == null) { return false; }
        
        if (((getScale () * retrieveImage().getIconWidth ()) > 1) &&
            ((getScale () * retrieveImage().getIconHeight()) > 1)
        ) {
            return true;
        }
        return false;
    }
        
    /** Return zooming factor.*/
    private double getScale () {
        return scale;
    }
    
    /** Perform zoom with specific proportion.
     * @param fx numerator for scaled
     * @param fy denominator for scaled
     */
    public void customZoom(int fx, int fy) {
        double oldScale = scale;
        
        scale = (double)fx/(double)fy;
        if(!isNewSizeOK()) {
            scale = oldScale;
            
            return;
        }
        
        resizePanel();
        panel.repaint(0, 0, panel.getWidth(), panel.getHeight());
    }
    
    /** Gets grid button.*/
    private JButton getGridButton() {
        // PENDING buttons should have their own icons.
        final JButton button = new JButton(" # "); // NOI18N
        button.setToolTipText (NbBundle.getBundle(Toolbar.class).getString("LBL_ShowHideGrid"));
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
 
    private NBImageIcon retrieveImage(){
        return PUMLTopComponent.getNBImageIcon();
    }
    
    private static Toolbar instance;
    
    public static Toolbar instance(){
        if(instance == null) { instance = new Toolbar(); }
        
        return instance;
    }
}
