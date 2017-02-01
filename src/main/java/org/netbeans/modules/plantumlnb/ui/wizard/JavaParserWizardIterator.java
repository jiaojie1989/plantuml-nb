/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.wizard;

import java.awt.Component;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import net.sf.parserjavatoplant.ParserJavaToPlant;
import net.sf.parserjavatoplant.types.TypeProperties;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

// TODO define position attribute
@TemplateRegistration(folder = "PlantUML",
        displayName = "#JavaParserWizardIterator_displayName",
        iconBase = "org/netbeans/modules/plantumlnb/icon.png",
        description = "javaParser.html")
@Messages("JavaParserWizardIterator_displayName=Create Class diagram for project.")
public final class JavaParserWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private int index;

    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
            panels.add(new JavaParserWizardPanel1());
            String[] steps = createSteps();
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
        }
        return panels;
    }

    @Override
    public Set<?> instantiate() throws IOException {
        /**
         * initialize - Только когда выбрали Wizard
         * instantiate - Только когда нажали кнопку "Готово"
         * uninitialize - Всегда когда прекращает работать Wizard (можно узнать
         * как была закончена работа Готово или Отмена)
         */
        
        JavaParserVisualPanel1 firstPanel = (JavaParserVisualPanel1) panels.get(0).getComponent();
        
        Properties prop = TypeProperties.createDefaultProperties();
        prop.setProperty(TypeProperties.IMPLEMENTS.name(), 
                            Boolean.toString(firstPanel.isImplements()));
        prop.setProperty(TypeProperties.EXTENDS.name(), 
                            Boolean.toString(firstPanel.isExtends()));
        prop.setProperty(TypeProperties.FIELDS_PRIVATE.name(), 
                            Boolean.toString(firstPanel.isFieldsPrivate()));
        prop.setProperty(TypeProperties.FIELDS_PUBLIC.name(), 
                            Boolean.toString(firstPanel.isFieldsPublic()));
        prop.setProperty(TypeProperties.FIELDS_STATIC.name(), 
                            Boolean.toString(firstPanel.isFieldsStatic()));
        prop.setProperty(TypeProperties.FIELDS_FINAL.name(), 
                            Boolean.toString(firstPanel.isFieldsFinal()));
        prop.setProperty(TypeProperties.FIELDS_PROTECTED.name(), 
                            Boolean.toString(firstPanel.isFieldsProtected()));
        prop.setProperty(TypeProperties.METHODS_PRIVATE.name(), 
                            Boolean.toString(firstPanel.isMethodsPrivate()));
        prop.setProperty(TypeProperties.METHODS_PUBLIC.name(), 
                            Boolean.toString(firstPanel.isMethodsPublic()));
        prop.setProperty(TypeProperties.METHODS_STATIC.name(), 
                            Boolean.toString(firstPanel.isMethodsStatic()));
        prop.setProperty(TypeProperties.METHODS_FINAL.name(), 
                            Boolean.toString(firstPanel.isMethodsFinal()));
        prop.setProperty(TypeProperties.METHODS_PROTECTED.name(), 
                            Boolean.toString(firstPanel.isMethodsProtected()));
        
        
        prop.setProperty(TypeProperties.GENERAL.name(),
                            Boolean.toString(firstPanel.isImplements()));
        prop.setProperty(TypeProperties.PACKAGE.name(), 
                            Boolean.toString(firstPanel.isPackage()));
        prop.setProperty(TypeProperties.IMPORT.name(), 
                            Boolean.toString(firstPanel.isImport()));
        
        prop.setProperty(TypeProperties.PRJ_NAME.name(), 
                            firstPanel.getPrjName().getText());
        
        List<Path> paths = ParserJavaToPlant.parseJavaPrj(firstPanel.getPathSrc(), prop);
        
        Set<FileObject> fileSet = new HashSet<>();
        if (paths == null) return fileSet;
        for (Path path : paths)
            fileSet.add(FileUtil.toFileObject(path.toFile()));
        return fileSet;
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
        assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
        String[] res = new String[(beforeSteps.length - 1) + panels.size()];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
            }
        }
        return res;
    }

}
