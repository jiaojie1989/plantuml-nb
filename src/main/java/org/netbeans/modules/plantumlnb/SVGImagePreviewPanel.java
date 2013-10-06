/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.util.XMLResourceDescriptor;
import org.openide.util.Exceptions;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author venkat
 */
public class SVGImagePreviewPanel extends JPanel {

    private File svgFile;
    private JSVGCanvas canvas;
    private JSVGScrollPane scroller;
    private String currentImageContent = "";
    private SVGDocument currentDocument = null;
    
    /**
     * 
     * @param svgFile 
     */
    public SVGImagePreviewPanel() {
//        super(new FlowLayout(FlowLayout.CENTER));       
        canvas = new JSVGCanvas();
//        scroller = new JSVGScrollPane(canvas);      
        add("Center", canvas);        
    }          
    
    /**
     * 
     * @param svgFile 
     */
    public AffineTransform renderSVGFile(String imageContent) {    
        currentImageContent = imageContent;
        canvas.setSize(getSize());
        canvas.setSVGDocument(createSVGDocument(new StringReader(imageContent)));
                
//        canvas.addMouseListener(listener);
        
//        canvas.addComponentListener(new ResizeListener());
                
        canvas.revalidate();
        canvas.repaint();
        revalidate();
        repaint();   
       
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
    
    private final class ZoomMouseWheelListener extends MouseAdapter implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent evt) {
            int notches = evt.getWheelRotation();
            AffineTransform at = canvas.getRenderingTransform();
            if (at != null) {
                Point2D p2d = new Point2D.Double(evt.getX(), evt.getY());

                at.preConcatenate(AffineTransform.getTranslateInstance(-p2d.getX(), -p2d.getY()));
               
                // Mouse wheel moved up, so zoom in.
                if (notches < 0) {
                    at.preConcatenate(AffineTransform.getScaleInstance(1.25, 1.25));
                } else {
                    // Mouse wheel moved up, so zoom out.
                    at.preConcatenate(AffineTransform.getScaleInstance(.8, .8));
                }

                at.preConcatenate(AffineTransform.getTranslateInstance(p2d.getX(), p2d.getY()));
                canvas.setRenderingTransform(at);
            }
        }
    }
    
    private final class PanMouseMotionListener extends MouseAdapter implements MouseMotionListener {
        
        private Point2D startPoint = null;
        private Point2D endPoint = null;
        
        @Override
        public void mouseClicked(MouseEvent evt) {
            if(evt.getButton() == 3) {
                startPoint = new Point2D.Double(evt.getX(), evt.getY());
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent evt) {
            updateSize(evt);           
        }
        
        @Override
        public void mouseReleased(MouseEvent evt) {
            updateSize(evt);
        }
        
        private void updateSize(MouseEvent evt) {
            if(evt.getButton() == 3) {
                endPoint = new Point2D.Double(evt.getX(), evt.getY());
                
                AffineTransform at = canvas.getRenderingTransform();
            
                double dx = endPoint.getX() - startPoint.getX();
                double dy = endPoint.getY() - startPoint.getY();

                at.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
            }
                        
        }
                
    }
    
    
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
         AffineTransform at;
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
                 Dimension dim = getSize();
                 int x = dim.width / 2;
                 int y = dim.height / 2;
                 AffineTransform t = AffineTransform.getTranslateInstance(x, y);
                 t.concatenate(at);
                 t.translate(-x, -y);
                 t.concatenate(rat);
                 canvas.setRenderingTransform(t);
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
         ZoomInAction() { super(2); }
     }
 
     /**
      * A swing action to zoom out the canvas.
      */
     public class ZoomOutAction extends ZoomAction {
         ZoomOutAction() { super(.5); }
     }
 
     /**
      * A swing action to Rotate the canvas.
      */
     public class RotateAction extends AffineAction {
         public RotateAction(double theta) {
             super(AffineTransform.getRotateInstance(theta));
         }
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
}
