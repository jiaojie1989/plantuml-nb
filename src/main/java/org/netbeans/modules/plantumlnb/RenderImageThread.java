/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.modules.plantumlnb.ui.PUMLTopComponent;
import org.netbeans.modules.plantumlnb.ui.Toolbar;
import org.openide.util.NbBundle;

/**
 *
 * @author venkat
 */
public class RenderImageThread extends Thread {
    
    PUMLTopComponent topComponent;
    String imageContent;
    
    public RenderImageThread(PUMLTopComponent tc, String ic) {
        topComponent = tc;
        imageContent = ic;
    }

    @Override
    public void run() {
        BufferedImage image = null;
        try {
            if (topComponent.getPanelUI() == null) {
                topComponent.getComponent();
            }
        } catch (IllegalArgumentException iaex) {
            Logger.getLogger(PUMLTopComponent.class.getName()).info(NbBundle.getMessage(PUMLTopComponent.class, "ERR_IOFile"));
        } finally {

            final BufferedImage fImage = image;
            // TODO: This line causes the problem below.
            // http://stackoverflow.com/questions/16502071/netbeans-save-hangs
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    AffineTransform at = topComponent.getPanelUI().renderSVGFile(imageContent);
                    Toolbar.instance().setSvgImagePreviewPanel(topComponent.getPanelUI());
                    topComponent.repaint();
                }
            });
        }
    }
}
