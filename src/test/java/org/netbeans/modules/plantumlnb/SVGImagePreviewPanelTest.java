/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openide.util.Exceptions;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author venkat
 */
public class SVGImagePreviewPanelTest {
    
    SVGImagePreviewPanel svgImagePreviewPanel;
    
    public SVGImagePreviewPanelTest() {}
    
    @Before
    public void setUp() {
        svgImagePreviewPanel = new SVGImagePreviewPanel();
    }
    
    @After
    public void tearDown() {}
    
    
    @Test
    public void testCreateSVGDocument() {
        InputStream is = getClass().getResourceAsStream("./sequence.svg");       
                
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
        
        SVGDocument doc = svgImagePreviewPanel.createSVGDocument(new StringReader(puml));
        Assert.assertNotNull(doc);        
    }
    
    
    
}