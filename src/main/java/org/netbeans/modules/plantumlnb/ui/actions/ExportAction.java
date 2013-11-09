/* 
 * The MIT License
 *
 * Copyright 2013 Venkat Ram Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.netbeans.modules.plantumlnb.ui.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.modules.plantumlnb.DataObjectAccess;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.netbeans.modules.plantumlnb.ui.FileFormatable;
import org.netbeans.modules.plantumlnb.ui.filefilter.EPSFileFilter;
import org.netbeans.modules.plantumlnb.ui.filefilter.PNGFileFilter;
import org.netbeans.modules.plantumlnb.ui.filefilter.SVGFileFilter;
import org.netbeans.modules.plantumlnb.ui.io.PUMLGenerator;
import org.netbeans.modules.plantumlnb.ui.pumlVisualElement;
import org.openide.awt.NotificationDisplayer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author venkat
 */
public class ExportAction implements ActionListener {
    
    private JPanel panel;
    private PUMLGenerator pumlGenerator = new PUMLGenerator();
    private DataObjectAccess doa;
    private static final Logger LOG = Logger.getLogger(ExportAction.class.getName());
    
        
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
        fc.addChoosableFileFilter(new EPSFileFilter());
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
                        
                        final String filePath = file.getAbsolutePath();
                        String notificationText = "File " + filePath+ " successully exported";
                        StatusDisplayer.getDefault().setStatusText("File saved to " + notificationText);
                        Icon successIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/plantumlnb/ui/actions/check.png", true);
                        NotificationDisplayer.getDefault().notify("File Export", successIcon, notificationText, new ActionListener(){

                            @Override
                            public void actionPerformed(ActionEvent e) {                                
                                try {
                                    Desktop.getDesktop().browse(new URI("file://" + filePath));
                                } catch ( IOException | URISyntaxException ex) {
                                    LOG.log(Level.INFO, ex.getMessage());
                                }
                            }
                        
                        });
                       
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        String failNotificationText = "File saved failed. ";
                        StatusDisplayer.getDefault().setStatusText(failNotificationText);
                        Icon failIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/plantumlnb/ui/actions/close_delete.png", true);
                        NotificationDisplayer.getDefault().notify("File Export", failIcon, failNotificationText, new ActionListener(){

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                
                            }
                        
                        });
                    }
                    
                }
            
            });
            
        }
    }
    
}
