/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

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
        JFileChooser fc = new JFileChooser();
        System.out.println(doa.getDataObject().toString());
        int returnVal = fc.showSaveDialog(panel);        

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println(fc.getSelectedFile());            
        }
    }
    
}
