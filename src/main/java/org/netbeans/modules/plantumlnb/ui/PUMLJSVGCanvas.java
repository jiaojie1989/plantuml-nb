/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.plantumlnb.ui;

import java.awt.geom.AffineTransform;
import org.apache.batik.swing.JSVGCanvas;

/**
 *
 * @author Venkat Akkineni <sriguru@users.sourceforge.net>
 */
public class PUMLJSVGCanvas extends JSVGCanvas {
    
    public static final String renderingTransformPropertyName = "renderingTransform";
     
    @Override
    public void setRenderingTransform(AffineTransform newAt) {
        AffineTransform oldAt = getRenderingTransform();
        super.setRenderingTransform(newAt);
        pcs.firePropertyChange(renderingTransformPropertyName, oldAt, newAt);
    }
    
}
