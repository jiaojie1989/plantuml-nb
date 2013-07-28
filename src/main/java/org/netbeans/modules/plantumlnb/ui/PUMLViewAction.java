/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.plantumlnb.PUMLTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

//@ActionID(
//        category = "Window",
//        id = "org.netbeans.modules.plantumlnb.PUMLViewAction")
//@ActionRegistration(
//        iconBase = "org/netbeans/modules/plantumlnb/icon.png",
//        displayName = "#CTL_PUMLViewAction")
//@ActionReferences({
//    @ActionReference(path = "Menu/Window/Other", position = 1050),
//    @ActionReference(path = "Shortcuts", name = "DS-P"),
//    @ActionReference(path = "Shortcuts", name = "DS-U")
//})
//@Messages("CTL_PUMLViewAction=Plant UML")
public final class PUMLViewAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        PUMLTopComponent pumlTopComponent = PUMLTopComponent.getInstance();
        pumlTopComponent.open();
    }
}
