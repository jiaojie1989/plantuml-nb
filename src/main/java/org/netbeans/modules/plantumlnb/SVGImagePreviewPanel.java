/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.util.XMLResourceDescriptor;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author venkat
 */
public class SVGImagePreviewPanel extends JPanel {
    
    private JSVGCanvas canvas;
    private String currentImageContent = "";
    private SVGDocument currentDocument = null;
    private pumlDataObject currentDataObject;
    private double zoomOutFactor = 0.1;
    private static final Logger logger = Logger.getLogger(SVGImagePreviewPanel.class.getName());
    
    /**
     * 
     * @param svgFile 
     */
    public SVGImagePreviewPanel() {
        canvas = new JSVGCanvas();

        //http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/200811.mbox/%3C82615BD530B1FA449BAEB584F01FFDBA0169E9FE@UHQEX30.ad.jfcom.mil%3E
        //http://mcc.id.au/2007/09/batik-course/
        
        
        canvas.addGVTTreeRendererListener(new ResizeGVTTreeRendererListener());       
        addComponentListener(new ResizeListener());
        add("Center", canvas);        
    }          
    
    /**
     * 
     * @param svgFile 
     */
    public AffineTransform renderSVGFile(@NonNull String imageContent) {    
        if(!"".equals(imageContent)) {
            currentImageContent = imageContent;
            canvas.setSize(getSize());
            canvas.setSVGDocument(createSVGDocument(new StringReader(imageContent)));       
        }
        
        return canvas.getRenderingTransform();
    }
    
    public SVGDocument createSVGDocument(StringReader sr) {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);                     
               
        try {
            currentDocument = f.createSVGDocument("http://www.w3.org/2000/svg",sr);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return currentDocument;
    }
    
    private StringReader readInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);        
        
        String puml = "";
        String sCurrentLine = "";
        try {
            while ((sCurrentLine = br.readLine()) != null) {
                    puml += sCurrentLine;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                is.close();
                isr.close();
                br.close(); 
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }            
        }
        
        return new StringReader(puml);
    }

    public JSVGCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(JSVGCanvas canvas) {
        this.canvas = canvas;
    }
        
    public pumlDataObject getCurrentDataObject() {
        return currentDataObject;
    }

    public void setCurrentDataObject(pumlDataObject currentDataObject) {
        this.currentDataObject = currentDataObject;
    }   
    
    //============================================================================
    // Listeners and actions.
    //============================================================================
    
    /**
     * 
     */
    private class ResizeListener implements ComponentListener {
        
        @Override
        public void componentResized(ComponentEvent evt) {
            renderSVGFile(currentImageContent);
        }
        
        
        @Override
        public void componentHidden(ComponentEvent evt) {}
        
        @Override
        public void componentShown(ComponentEvent evt) {}
        
        @Override
        public void componentMoved(ComponentEvent evt) {}
    }
    
    /**
      * A swing action to reset the rendering transform of the canvas.
      */
     public class ResetTransformAction extends AbstractAction {
         @Override
         public void actionPerformed(ActionEvent evt) {
             canvas.setFragmentIdentifier(null);
             canvas.resetRenderingTransform();
             currentDataObject.getCurrentAT().setToIdentity();
         }
     }
 
     /**
      * A swing action to append an affine transform to the current
      * rendering transform. Before the rendering transform is
      * applied the method translates the center of the display to
      * 0,0 so scale and rotate occur around the middle of
      * the display.
      */
     public class AffineAction extends AbstractAction {
         
         private AffineTransform at;
         
         public AffineAction(AffineTransform at) {
             this.at = at;
         }
 
         @Override
         public void actionPerformed(ActionEvent evt) {
//             if (canvas.getgvtRoot == null) {
//                 return;
//             }
             AffineTransform rat = canvas.getRenderingTransform();
             if (at != null) {
                 Dimension dim = canvas.getSize();
                 int x = dim.width / 2;
                 int y = dim.height / 2;
                 AffineTransform t = AffineTransform.getTranslateInstance(x, y);
                 t.concatenate(at);
                 t.translate(-x, -y);
                 t.concatenate(rat);
                 canvas.setRenderingTransform(t);
                 if(SVGImagePreviewPanel.this.currentDataObject != null ) {
                     SVGImagePreviewPanel.this.currentDataObject.setCurrentAT(t);
                 }
             }            
         }
     }
 
     /**
      * A swing action to apply a zoom factor to the canvas.
      * This can be used to zoom in (scale > 1) and out (scale <1).
      */
     public class ZoomAction extends AffineAction {
         public ZoomAction(double scale) {
             super(AffineTransform.getScaleInstance(scale, scale));
         }
         public ZoomAction(double scaleX, double scaleY) {
             super(AffineTransform.getScaleInstance(scaleX, scaleY));
         }
     }
 
     /**
      * A swing action to zoom in the canvas.
      */
     public class ZoomInAction extends ZoomAction {
         ZoomInAction() { super(1/zoomOutFactor); }
     }
 
     /**
      * A swing action to zoom out the canvas.
      */
     public class ZoomOutAction extends ZoomAction {
         ZoomOutAction() { super(zoomOutFactor); }
     }
 
     /**
      * A swing action to Rotate the canvas.
      */
     public class RotateAction extends AffineAction {
         public RotateAction(double theta) {
             super(AffineTransform.getRotateInstance(theta));
         }
     }
    
     /**
      * http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
      * http://www.coderanch.com/t/478152/java/java/Path-Temporary-Directory
      * http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
      */
     public class OpenInBrowserAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            File tmpFile = null;
            try {
                String tempDir = System.getProperty("java.io.tmpdir");  
                tmpFile = new File(tempDir, UUID.randomUUID().toString() + ".png");  
                PrintWriter writer = new PrintWriter(tmpFile, "UTF-8");
                
                writer.write(currentImageContent); 
                writer.close();
            } catch (    FileNotFoundException | UnsupportedEncodingException ex) {
                logger.log(Level.SEVERE, ex.getLocalizedMessage());// TODO: Add a user notification
            } 

            if(tmpFile != null) {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(Utilities.toURI(tmpFile));
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, e.getLocalizedMessage());
                    }
                }
            } else {
                // TODO: Add a user notification                
            }
        }
         
     }
     
     public class ResizeGVTTreeRendererListener implements GVTTreeRendererListener{

            @Override
            public void gvtRenderingPrepare(GVTTreeRendererEvent gvttre) {}

            @Override
            public void gvtRenderingStarted(GVTTreeRendererEvent gvttre) {}

            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent gvttre) {
                if (currentDataObject.getCurrentAT() != null) {
                    canvas.setRenderingTransform(currentDataObject.getCurrentAT());                    
                }
            }

            @Override
            public void gvtRenderingCancelled(GVTTreeRendererEvent gvttre) {}

            @Override
            public void gvtRenderingFailed(GVTTreeRendererEvent gvttre) {}
            
        }
    
     
     public ZoomInAction getZoomInActionInstance() {
         return new ZoomInAction();
     }
     
     public ZoomOutAction getZoomOutActionInstance() {
         return new ZoomOutAction();
     }
     
     public RotateAction getRotateActionInstance() {
         return new RotateAction(0.0872664626d);
     }
     
     public ResetTransformAction getResetTransformAction() {
         return new ResetTransformAction();
     }
     
     public OpenInBrowserAction getOpenInBrowserAction() {
         return new OpenInBrowserAction();
     }
}
