/* 
 * The MIT License
 *
 * Copyright 2013 Venkat Ram Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.netbeans.modules.plantumlnb;

import javax.swing.SwingUtilities;
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
        pumlDataObject dataObject = (pumlDataObject) topComponent.getCurrentDataObject();

        if(dataObject == null) {
            return;
        }

        SVGImagePreviewPanel panelUI = SVGImagePreviewPanel.getInstance();
        panelUI.setCurrentDataObject(dataObject);
        panelUI.renderSVGFileOnTabSwitch(imageContent);
        Toolbar.instance().setSvgImagePreviewPanel(panelUI);

        /**
         * This needs to be in awt thread, otherwise netbeans complains.
         * 
         * java.lang.IllegalStateException: Problem in some module which uses 
         * Window System: Window System API is required to be called from AWT 
         * thread only, see http://core.netbeans.org/proposals/threading/
         *
         * @todo: All Swing Interaction should be on the EDT, so calling into
         * repaint from another threads looks doubious
         */
        SwingUtilities.invokeLater(() -> {
            topComponent.setDisplayName(dataObject.getPrimaryFile().getName());
        });

        topComponent.repaint();
    }

}
