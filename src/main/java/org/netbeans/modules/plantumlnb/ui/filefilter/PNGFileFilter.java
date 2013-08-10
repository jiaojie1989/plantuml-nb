/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.filefilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import net.sourceforge.plantuml.FileFormat;
import org.netbeans.modules.plantumlnb.ui.FileFormatable;
import org.netbeans.modules.plantumlnb.ui.ImageUtils;

/**
 *
 * @author venkat
 */
public class PNGFileFilter extends FileFilter implements FileFormatable {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = ImageUtils.getExtension(f);
        if (extension != null) {
            if(extension.equals(ImageUtils.png)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }

    @Override
    public String getDescription() {
        return ImageUtils.png;
    }

    @Override
    public FileFormat getFileFormat() {
        return FileFormat.PNG;
    }
    
}
