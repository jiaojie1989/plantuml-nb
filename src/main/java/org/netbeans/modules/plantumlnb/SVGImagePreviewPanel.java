/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.swing.JPanel;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
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
    
    /**
     * 
     * @param svgFile 
     */
    public SVGImagePreviewPanel() {
//        super(new FlowLayout(FlowLayout.CENTER));       
        canvas = new JSVGCanvas();
        canvas.setDisableInteractions(false);
        add("Center", canvas);        
    }          
    
    /**
     * 
     * @param svgFile 
     */
    public void renderSVGFile(String imageContent) {            
        canvas.setSize(getSize());
        canvas.setSVGDocument(createSVGDocument(new StringReader(imageContent)));
        canvas.revalidate();
        canvas.repaint();
        revalidate();
        repaint();   
       
        
    }
    
    public SVGDocument createSVGDocument(StringReader sr) {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);                     
        
        SVGDocument doc = null;
        try {
            doc = f.createSVGDocument("http://www.w3.org/2000/svg",sr);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return doc;
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
    
}
