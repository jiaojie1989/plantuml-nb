/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.wizard;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.modules.plantumlnb.StringUtils;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.filesystems.FileObject;
import org.openide.util.HelpCtx;

public class PlantUMLWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor>, WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the component from this class, just use
     * getComponent().
     */
    private PlantUMLVisualPanel1 component;
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public PlantUMLVisualPanel1 getComponent() {
        if (component == null) {
            component = new PlantUMLVisualPanel1(this);
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

    /**
     * If it is always OK to press Next or Finish, then:
     * @return 
     */
    @Override
    public boolean isValid() {
        
        String fileName = component.getPlantumlFileNameTextField().getText();
        String packageName = component.getPackageSelectionInputDirectory().getText();
        String destinationDirectory = component.getDestinationDirectoryTextField().getText();

        if(fileName.equals("") || fileName == null
                || packageName.equals("") || packageName == null
                || destinationDirectory.equals("") || destinationDirectory == null) {
            return false;
        }
        return true;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        synchronized(listeners) {
            listeners.add(l);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        synchronized(listeners) {
            listeners.remove(l);
        }
    }
    
    protected final void fireChangeEvent() {
        Set ls;
        synchronized(listeners) {
            ls = new HashSet();
        }
        ChangeEvent ev = new ChangeEvent(this);
        for(ChangeListener l: listeners) {
            l.stateChanged(ev);
        }        
    }

    /**
     * Following code was inspired from JavaTargetChooserPanel and JavaTargetChooserPanelGUI 
     * in java.project.ui (Java Project Support UI) module of the netbeans. Take a look at 
     * JavaTargetChooserPanel.readSettings and JavaTargetChooserPanelGUI.initValues for 
     * further help.
     * 
     * @param wiz 
     */
    @Override
    public void readSettings(WizardDescriptor wiz) {        
        component.getPlantumlFileNameTextField().setText((String) wiz.getProperty("filename"));
        component.getDestinationDirectoryTextField().setText((String) wiz.getProperty("destinationdirectory"));
        
        String selectedPackageName = (String) wiz.getProperty("packagename");
        
        if (component != null) {
            if (StringUtils.isNotEmpty(selectedPackageName)) {
                component.getPackageSelectionInputDirectory().setText(selectedPackageName);
            } else {
                // Try to preselect a folder
                FileObject preselectedFolder = Templates.getTargetFolder(wiz);
                Project project = Templates.getProject(wiz);
                if (project == null) throw new NullPointerException ("No project found for: " + wiz);
                if (preselectedFolder != null) {
                    component.getPackageSelectionInputDirectory().setText(preselectedFolder.getPath());
                    
                    component.getDestinationDirectoryTextField()
                            .setText(project.getProjectDirectory().getPath());
                    component.getDestinationDirectoryTextField()
                            .setToolTipText(project.getProjectDirectory().getPath());
                }

                Sources sources = ProjectUtils.getSources(project);
                SourceGroup[] groups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
                if(groups.length != 0) {
                    // Init values
                    component.initValues(Templates.getTemplate(wiz), preselectedFolder, groups);
                }
            }
        }
        
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty("filename", component.getPlantumlFileNameTextField().getText());
        wiz.putProperty("packagename", component.getPackageSelectionInputDirectory().getText());
        wiz.putProperty("destinationdirectory", component.getDestinationDirectoryTextField().getText());
    }

    @Override
    public void validate() throws WizardValidationException {
        component.validate();
    }

}
