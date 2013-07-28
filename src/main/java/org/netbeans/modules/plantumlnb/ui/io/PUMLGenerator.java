/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.io;

import org.netbeans.modules.plantumlnb.ui.ImageUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.netbeans.modules.plantumlnb.PrettyPrinter;
import org.openide.filesystems.FileObject;
import org.xml.sax.InputSource;

/**
 *
 * @author venkat
 */
public class PUMLGenerator {
    
    private static String genericUrl = "file://${path}/${name}.png";
    
    private HashMap<String, Method> methodMap =  new HashMap<String, Method>();
    private static final Logger logger = Logger.getLogger(PUMLGenerator.class.getName());
    
    public PUMLGenerator() {
        Class c = getClass();

        try {
            methodMap.put(ImageUtils.png, c.getMethod("generatePNGFile"));
            methodMap.put(ImageUtils.svg, c.getMethod("generateSVGFile"));
        } catch (NoSuchMethodException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (SecurityException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
        
    }

    public ByteArrayInputStream generate(FileObject fo){
        ByteArrayInputStream pumlImageInputStream = null;
        
        try {
//                    System.out.println("\tset file object: " + fo.asText());
            SourceStringReader pumlReader = new SourceStringReader(fo.asText());
//            String name = fo.getName();
//            name = "file://" + fo.getParent().getPath() + "/" + name + ".png";
//            
//            name = genericUrl.replace("${path}", fo.getParent().getPath())
//                    .replace("${name}", name);
//
            ByteArrayOutputStream pumlImageOutputStream = new ByteArrayOutputStream();

//            File pumlImageFile = new File(name);
//            // if file doesnt exists, then create it
//            pumlImageFile.mkdirs();
//            if (!pumlImageFile.exists()) {
//                pumlImageFile.createNewFile();
//            }
//                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    bw.write(content);
//                    bw.close();
            pumlReader.generateImage(pumlImageOutputStream);
            pumlImageInputStream = new ByteArrayInputStream(pumlImageOutputStream.toByteArray());
//            System.out.println(name);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } 
        return pumlImageInputStream;
    }
    
    /**
     * As the name suggests reads the supplied inputStream and returns a string
     * representation of the contents of the inputStream.
     * 
     * @param is
     * @return 
     */
    public String stringify(InputStream is) {        
        InputStreamReader isr = new InputStreamReader(is);
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(isr);
        String read = null;
        try {
            read = br.readLine();
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                is.close();
                isr.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }            
        }

        try {
            while(read != null) {
                sb.append(read);            
                read = br.readLine();            
            }
        } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        }

        return sb.toString();
    }
    
    public void generateFile(InputStream is, FileFilter ftf) throws IIOException {
        try {
            String methodName = "generate" + ftf.getDescription().toUpperCase() + "File";
            Class c = getClass();            
            c.getMethod(methodName).invoke(is);            
        } catch (NoSuchMethodException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (SecurityException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }  catch (IllegalAccessException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (InvocationTargetException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
    
    public void generateFile(FileObject fo, FileFormat fileFormat, File file) {        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;        
        
        if(fileFormat == FileFormat.SVG) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                SourceStringReader reader = new SourceStringReader(fo.asText());
                // Write the first image to "os"
                String desc = reader.generateImage(os, new FileFormatOption(fileFormat));
                String svg = new String(os.toByteArray());

                file.createNewFile();
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(PrettyPrinter.formatXml(svg));
                bw.flush();

            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            } finally {
                try {
                    os.close();
                    if (fw != null) {
                        fw.close();
                    }
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                }
            }        
            
        } else if ( fileFormat == FileFormat.PNG) {
            try {
                SourceStringReader reader = new SourceStringReader(fo.asText());
                // Write the first image to "os"
                String desc = reader.generateImage(os, new FileFormatOption(fileFormat));

                file.createNewFile();
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(os.toByteArray());

            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            } finally {
                try {
                    os.close();
                    if (fos != null) {
                        fos.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                }
            }
        } 
    }     
            
    
//    public void generatePNGFile(InputStream is) {        
//        generateFile(is, FileFormat.PNG);
//    }
//    
//    public void generateSVGFile(InputStream is) {
//        generateFile(is, FileFormat.SVG);
//    }
//    
//    public void generatePDFFile(InputStream is) {
//        generateFile(is, FileFormat.PDF);
//    }
//    
//    public void generateHTMLFile(InputStream is) {
//        generateFile(is, FileFormat.HTML);
//    }
//    
//    public void generateHTML5File(InputStream is) {
//        generateFile(is, FileFormat.HTML5);
//    }
    
}
