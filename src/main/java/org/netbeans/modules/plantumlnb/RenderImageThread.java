/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author venkat
 */
public class RenderImageThread extends Thread {
    
    PUMLTopComponent topComponent;
    InputStream inputStream;
    
    public RenderImageThread(PUMLTopComponent tc, InputStream is) {
        topComponent = tc;
        inputStream = is;
    }

    @Override
    public void run() {
        BufferedImage image = null;
        try {
            if (topComponent.getPanelUI() == null) {
                topComponent.getComponent();
            }
            image = ImageIO.read(inputStream);
        } catch (IllegalArgumentException iaex) {
            Logger.getLogger(PUMLTopComponent.class.getName()).info(NbBundle.getMessage(PUMLTopComponent.class, "ERR_IOFile"));
        } catch (IOException ex) {
            Logger.getLogger(PUMLTopComponent.class.getName()).info(NbBundle.getMessage(PUMLTopComponent.class, "ERR_IOFile"));
        } finally {

            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(PUMLTopComponent.class.getName()).info(NbBundle.getMessage(PUMLTopComponent.class, "ERR_IOFile"));
            }

            final BufferedImage fImage = image;
            // TODO: This line causes the problem below.
            // http://stackoverflow.com/questions/16502071/netbeans-save-hangs
//                    createNBImageIcon(currentDataObject);                    
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    topComponent.getPanelUI().setImage(fImage);
                }
            });
        }
    }
}
