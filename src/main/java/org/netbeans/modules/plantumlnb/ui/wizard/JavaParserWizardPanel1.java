/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.wizard;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class JavaParserWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private JavaParserVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public JavaParserVisualPanel1 getComponent() {
        if (component == null) {
            component = new JavaParserVisualPanel1();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        // Если ждем, что кто то что то сказал.
        // Например произошла смена проекта.
//        System.out.println("---------------------------------------------------");
        Project project = Templates.getProject(wiz);
        Sources sources = (Sources) ProjectUtils.getSources(project);
        SourceGroup[] groups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        
        String name = project.getProjectDirectory().getName();
        String path = project.getProjectDirectory().getPath();
//        System.out.println("PrjName:"+name +";\nPrjPath:"+path+";");
        component.getPrjName().setText(name);
        component.getPrjSrc().setText("");
        
        for (SourceGroup group : groups){
            System.out.println("Group");
            if ( (group.getName().endsWith("SourceRoot")) || 
                 (group.getName().endsWith("${src.dir}")) ) {
                String src = group.getRootFolder().getPath();
                Path pathSRC = Paths.get(src);
                src = src.substring(path.length());
                component.getPrjSrc().setText(src);
                component.setPathSrc(pathSRC);
//                System.out.println(group.getRootFolder().getExt());
//                System.out.println(group.getRootFolder().getName());
//                System.out.println(group.getRootFolder().getNameExt());
//                System.out.println(group.getRootFolder().getPath());
//                System.out.println(group.getRootFolder().getSize());
            }
//            System.out.println("\t"+group.getName());
//            if ( (group.getName().endsWith("TestSourceRoot")) ||
//                 (group.getName().endsWith("${test.src.dir}")) ) {
//                
//            }
        }
        
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        // Если хотим, что бы узнал об изменениях кто то еще кроме самой панели
        // use wiz.putProperty to remember current panel state
    }

}
