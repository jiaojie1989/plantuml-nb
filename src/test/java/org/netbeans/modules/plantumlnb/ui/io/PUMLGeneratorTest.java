package org.netbeans.modules.plantumlnb.ui.io;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;

/**
 *
 * @author venkat
 */
public class PUMLGeneratorTest {
    
    String sequenceUML;
    
    public PUMLGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        sequenceUML =   "@startuml" +
                            "actor Foo1" +
                            "boundary Foo2" +
                            "control Foo3" +
                            "entity Foo4" +
                            "database Foo5" +
                            "Foo1 -> Foo2 : To boundary" +
                            "Foo1 -> Foo3 : To control" +
                            "Foo1 -> Foo4 : To entity" +
                            "Foo1 -> Foo5 : To database" +
                        "@enduml";
    }
    
    @After
    public void tearDown() {
    }
    
    
    @Test
    public void smokeTestSVGGeneration() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(sequenceUML);
        try {
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Test
    public void smokeTestPNGGeneration() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(sequenceUML);
        try {
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.PNG));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Test
    public void smokeTestEPSGeneration() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(sequenceUML);
        try {
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.EPS));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
      
    @Test
    public void smokeTestATXTGeneration() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(sequenceUML);
        try {
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.ATXT));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Test
    public void smokeTestUTXTGeneration() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(sequenceUML);
        try {
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.UTXT));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
       
    
}