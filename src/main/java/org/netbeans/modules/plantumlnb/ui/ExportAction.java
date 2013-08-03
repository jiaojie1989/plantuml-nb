/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.modules.plantumlnb.DataObjectAccess;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.netbeans.modules.plantumlnb.ui.io.PUMLGenerator;
import org.openide.util.Exceptions;

/**
 *
 * @author venkat
 */
public class ExportAction implements ActionListener {
    
    private JPanel panel;
    private PUMLGenerator pumlGenerator = new PUMLGenerator();
    private DataObjectAccess doa;
        
    public ExportAction(JPanel panel, DataObjectAccess doa) {
        this.panel = panel;
        this.doa = doa;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();     
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new SVGFileFilter());
        fc.addChoosableFileFilter(new PNGFileFilter());
        final pumlDataObject dataObject = pumlVisualElement.getActivePUMLEditorDataObject();
        
        int returnVal = fc.showSaveDialog(panel);        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
                        
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        File file = fc.getSelectedFile().getCanonicalFile();
                        if(file.getName().indexOf(".") == -1) {
                            file = new File(file.getCanonicalPath() + "." + fc.getFileFilter().getDescription());
                        }                                                
                        PUMLGenerator pumlGenerator = new PUMLGenerator();
                        FileFormatable f = ((FileFormatable) fc.getFileFilter());
                        pumlGenerator.generateFile(dataObject.getPrimaryFile(), f.getFileFormat(), file);
                       
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    
                }
            
            });
            
        }
    }
    
}
