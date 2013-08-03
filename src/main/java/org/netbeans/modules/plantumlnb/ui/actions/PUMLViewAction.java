/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.plantumlnb.ui.PUMLTopComponent;

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
