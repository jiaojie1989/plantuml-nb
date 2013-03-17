/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.sourceforge.plantuml.SourceStringReader;
import org.openide.awt.QuickSearch.Callback;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author venkat
 */
public class PUMLGenerator {
    
    private static String genericUrl = "file://${path}/${name}.png";

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
            Exceptions.printStackTrace(ex);
        }
        return pumlImageInputStream;
    }
    
}
