/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.geom.AffineTransform;
import org.netbeans.modules.plantumlnb.ui.PUMLTopComponent;
import org.netbeans.modules.plantumlnb.ui.Toolbar;

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
        // TODO: This line causes the problem below.
        // http://stackoverflow.com/questions/16502071/netbeans-save-hangs
        
        SVGImagePreviewPanel panelUI = topComponent.getPanelUI();
        panelUI.setCurrentDataObject((pumlDataObject) topComponent.getCurrentDataObject());
        AffineTransform at = panelUI.renderSVGFile(imageContent);
        Toolbar.instance().setSvgImagePreviewPanel(panelUI);
        topComponent.repaint();
    }

}
