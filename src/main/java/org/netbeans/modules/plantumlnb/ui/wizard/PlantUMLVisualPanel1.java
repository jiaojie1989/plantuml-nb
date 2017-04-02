/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.ui.wizard;

import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.NbBundle;
import static org.netbeans.modules.plantumlnb.ui.wizard.Bundle.*;
import org.netbeans.spi.java.project.support.ui.PackageView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.RequestProcessor;

public final class PlantUMLVisualPanel1 extends JPanel {
    
    private PlantUMLWizardPanel1 plantUMLWizardPanel1;
    private JFileChooser fileChooser = null;
    private SourceGroup[] sourceGroups;
    private static final Logger LOG = Logger.getLogger(PlantUMLVisualPanel1.class.getName());

    /**
     * Creates new form PlantUMLVisualPanel1
     */
    public PlantUMLVisualPanel1(PlantUMLWizardPanel1 plantUMLWizardPanel1) {
        initComponents();
        
        this.plantUMLWizardPanel1 = plantUMLWizardPanel1;
        
        sourceGroupsComboBox.setRenderer(new SourceGroupListCellRenderer());        
        packageSelectionComboBox.setRenderer(PackageView.listRenderer());
        
        getPlantumlFileNameTextField().getDocument().addDocumentListener(new GenericDocumentListener());
        getPackageSelectionInputDirectory().getDocument().addDocumentListener(new GenericDocumentListener());
        getDestinationDirectoryTextField().getDocument().addDocumentListener(new GenericDocumentListener());
    }

    @Override
    public String getName() {
        return "Choose a source package";
    }

    @Override
    public void validate() {
        super.validate();         
    }
    
    public void updateUI(DocumentEvent e) {
        if (getPlantumlFileNameTextField().getDocument() == e.getDocument()) {
            firePropertyChange(DESTINATION_DIRECTORY, null, destinationDirectoryTextField.getText());
        }
        if (getPackageSelectionInputDirectory().getDocument() == e.getDocument()) {
            firePropertyChange(PLANTUML_FILE_NAME, null, plantumlFileNameTextField.getText());
        }
        if (getPackageSelectionInputDirectory().getDocument() == e.getDocument()) {
            firePropertyChange(PACKAGE_SELECTION, null, packageSelectionInputDirectory.getText());
        }
    }

    public void initValues( FileObject template, FileObject preselectedFolder, SourceGroup[] sourceGroups ) {
        this.sourceGroups = sourceGroups;
        
        sourceGroupsComboBox.setModel(new DefaultComboBoxModel(sourceGroups));
        SourceGroup preselectedGroup = getPreselectedGroup(preselectedFolder);
        sourceGroupsComboBox.setSelectedItem(preselectedGroup);
        sourceGroupsComboBox.setToolTipText(preselectedGroup.getDisplayName());
        updatePackages(preselectedGroup);

        setSelectedPackage(preselectedFolder);
    }
    
    private String showOpenDialog(String dialogTitle) {
        fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle(dialogTitle);
        Project mainProject = OpenProjects.getDefault().getMainProject();
        File currentDirectory = new File(System.getProperty("user.home"));
        
        if(mainProject != null) {
            currentDirectory = FileUtil.toFile(mainProject.getProjectDirectory());
        } 
        
        fileChooser.setCurrentDirectory(currentDirectory);        
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //This is where a real application would open the file.
            LOG.log(Level.INFO, "Opening: {0}.", new Object[]{file.getName()});
            return fileChooser.getSelectedFile().getAbsolutePath();
        } 
        
        LOG.log(Level.INFO, "Open command cancelled by user.");
        return null;
               
    }
    
    private RequestProcessor.Task updatePackagesTask = null;
    
    private static final ComboBoxModel WAIT_MODEL = new DefaultComboBoxModel( 
        new String[] {
            NbBundle.getMessage( PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1_PackageName_PleaseWait" ) // NOI18N
        } 
    ); 
    
    private void setSelectedPackage(final FileObject selectedDirectory) {
        Optional<String> selectedPackage = getSelectedPackage(selectedDirectory);        
        packageSelectionComboBox.getEditor().setItem(selectedPackage.orElse(""));
        packageSelectionComboBox.setToolTipText(selectedPackage.orElse(""));
    }
    
    private void updatePackages(final SourceGroup selectedSourceGroup) {
        WAIT_MODEL.setSelectedItem( packageSelectionComboBox.getEditor().getItem() );
        packageSelectionComboBox.setModel( WAIT_MODEL );
        if ( updatePackagesTask != null) {
            updatePackagesTask.cancel();
        }
        
        if (sourceGroups != null && sourceGroups.length > 0) {
            SourceGroup sourceGroup = selectedSourceGroup == null ? sourceGroups[0] : selectedSourceGroup;
//                Optional<SourceGroup> javaSourceGroup = Arrays.asList(this.sourceGroups)
//                        .stream()
//                        .filter(sourceGroup -> sourceGroup.getName().equals("1SourceRoot")).findFirst();

            updatePackagesTask = new RequestProcessor("ComboUpdatePackages").post(() -> {
                final ComboBoxModel model = PackageView.createListView(sourceGroup);
                SwingUtilities.invokeLater(() -> {
                    model.setSelectedItem(packageSelectionComboBox.getEditor().getItem());
                    packageSelectionComboBox.setModel(model);
                });
            });
        }
    }
    
    private SourceGroup getPreselectedGroup(FileObject folder) {
        Optional<SourceGroup> selectedSourceGroup = Arrays.asList(sourceGroups).stream().filter(sourceGroup -> {
            FileObject root = ((SourceGroup) sourceGroup).getRootFolder();
            return root.equals(folder) || FileUtil.isParentOf(root, folder);
        }).findFirst();
        
        if (!selectedSourceGroup.isPresent()) {
            return Arrays.asList(sourceGroups).get(0);
        }
        
        return selectedSourceGroup.get();
    }
    
    /**
     * Get a package combo model item for the package the user selected before opening the wizard.
     * May return null if it cannot find it; or a String instance if there is a well-defined
     * package but it is not listed among the packages shown in the list model.
     */
    private Optional<String> getSelectedPackage(FileObject selectedDirectory) {
        SourceGroup selectedGroup = getPreselectedGroup( selectedDirectory );
        if ( selectedDirectory == null || selectedGroup == null ) {
            return Optional.empty();
        }
        FileObject root = selectedGroup.getRootFolder();
        String relPath = FileUtil.getRelativePath( root, selectedDirectory );
        
        if ( relPath != null ) {
            relPath = relPath.replace('/', '.');
        }        
        return Optional.ofNullable(relPath);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        existingSourcesPaneLabel = new javax.swing.JLabel();
        packageSelectionButton = new javax.swing.JButton();
        packageSelectionInputDirectory = new javax.swing.JTextField();
        plantumlFileNameTextField = new javax.swing.JTextField();
        destinationDirectoryTextField = new javax.swing.JTextField();
        destinationDirectoryButton = new javax.swing.JButton();
        pumlFileNameLabel = new javax.swing.JLabel();
        generatedFileNameLabel = new javax.swing.JLabel();
        packageSelectionLabel = new javax.swing.JLabel();
        destinationDirectoryLabel = new javax.swing.JLabel();
        generatedFileNameDisplayTextField = new javax.swing.JTextField();
        packageSelectionComboBox = new javax.swing.JComboBox<>();
        sourceGroupsComboBox = new javax.swing.JComboBox<>();

        org.openide.awt.Mnemonics.setLocalizedText(existingSourcesPaneLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.existingSourcesPaneLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(packageSelectionButton, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.packageSelectionButton.text")); // NOI18N
        packageSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                packageSelectionButtonActionPerformed(evt);
            }
        });

        packageSelectionInputDirectory.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.packageSelectionInputDirectory.text")); // NOI18N

        plantumlFileNameTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.plantumlFileNameTextField.text")); // NOI18N
        plantumlFileNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateGeneratedFileNameDisplayLabel(evt);
            }
        });

        destinationDirectoryTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(destinationDirectoryButton, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryButton.text")); // NOI18N
        destinationDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinationDirectoryButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(pumlFileNameLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.pumlFileNameLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(generatedFileNameLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.generatedFileNameLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(packageSelectionLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.packageSelectionLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(destinationDirectoryLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryLabel.text")); // NOI18N

        generatedFileNameDisplayTextField.setEditable(false);
        generatedFileNameDisplayTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.generatedFileNameDisplayTextField.text")); // NOI18N

        sourceGroupsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleSourceGroupChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(packageSelectionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generatedFileNameDisplayTextField)
                    .addComponent(existingSourcesPaneLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plantumlFileNameTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generatedFileNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(packageSelectionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(destinationDirectoryLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(destinationDirectoryTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(destinationDirectoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(packageSelectionInputDirectory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(packageSelectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(pumlFileNameLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(sourceGroupsComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(existingSourcesPaneLabel)
                .addGap(18, 18, 18)
                .addComponent(pumlFileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plantumlFileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(generatedFileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generatedFileNameDisplayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(destinationDirectoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationDirectoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(destinationDirectoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(packageSelectionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(packageSelectionInputDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(packageSelectionButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sourceGroupsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(packageSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents
   
    @NbBundle.Messages("PlantUMLVisualPanel1.destinationDirectoryButton.text=...")
    private void destinationDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationDirectoryButtonActionPerformed
        String returnVal = showOpenDialog(PlantUMLVisualPanel1_destinationDirectoryButton_text());
        if(returnVal != null) {
            destinationDirectoryTextField.setText(returnVal);
            destinationDirectoryTextField.setToolTipText(returnVal);
        }
    }//GEN-LAST:event_destinationDirectoryButtonActionPerformed

    @NbBundle.Messages("PlantUMLVisualPanel1.packageSelectionButton.text=...")
    private void packageSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_packageSelectionButtonActionPerformed
        String returnVal = showOpenDialog(PlantUMLVisualPanel1_packageSelectionButton_text());
        if(returnVal != null) {
            packageSelectionInputDirectory.setText(returnVal);
            packageSelectionInputDirectory.setToolTipText(returnVal);
        }
    }//GEN-LAST:event_packageSelectionButtonActionPerformed

    private void updateGeneratedFileNameDisplayLabel(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_updateGeneratedFileNameDisplayLabel
        generatedFileNameDisplayTextField.setText(plantumlFileNameTextField.getText() + ".puml");
    }//GEN-LAST:event_updateGeneratedFileNameDisplayLabel

    private void handleSourceGroupChange(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleSourceGroupChange
        SourceGroup selectedSourceGroup = (SourceGroup) ((JComboBox) evt.getSource()).getSelectedItem();
        updatePackages(selectedSourceGroup);
    }//GEN-LAST:event_handleSourceGroupChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton destinationDirectoryButton;
    private javax.swing.JLabel destinationDirectoryLabel;
    private javax.swing.JTextField destinationDirectoryTextField;
    private javax.swing.JLabel existingSourcesPaneLabel;
    private javax.swing.JTextField generatedFileNameDisplayTextField;
    private javax.swing.JLabel generatedFileNameLabel;
    private javax.swing.JButton packageSelectionButton;
    private javax.swing.JComboBox<String> packageSelectionComboBox;
    private javax.swing.JTextField packageSelectionInputDirectory;
    private javax.swing.JLabel packageSelectionLabel;
    private javax.swing.JTextField plantumlFileNameTextField;
    private javax.swing.JLabel pumlFileNameLabel;
    private javax.swing.JComboBox<String> sourceGroupsComboBox;
    // End of variables declaration//GEN-END:variables

    private static final String DESTINATION_DIRECTORY = "destinationDirectoryTextField";
    private static final String PLANTUML_FILE_NAME = "plantumlFileNameTextField"; 
    private static final String PACKAGE_SELECTION = "packageSelectionInputDirectory";
    
    
    public JTextField getDestinationDirectoryTextField() {
        return destinationDirectoryTextField;
    }

    public void setDestinationDirectoryTextField(JTextField destinationDirectoryTextField) {
        this.destinationDirectoryTextField = destinationDirectoryTextField;
    }

    public JTextField getPackageSelectionInputDirectory() {
        return packageSelectionInputDirectory;
    }

    public void setPackageSelectionInputDirectory(JTextField packageSelectionInputDirectory) {
        this.packageSelectionInputDirectory = packageSelectionInputDirectory;
    }

    public JTextField getPlantumlFileNameTextField() {
        return plantumlFileNameTextField;
    }

    public void setPlantumlFileNameTextField(JTextField plantumlFileNameTextField) {
        this.plantumlFileNameTextField = plantumlFileNameTextField;
    }

    public JComboBox<String> getPackageSelectionComboBox() {
        return packageSelectionComboBox;
    }

    public void setPackageSelectionComboBox(JComboBox<String> packageSelectionComboBox) {
        this.packageSelectionComboBox = packageSelectionComboBox;
    }

    public JComboBox<String> getSourceGroupsComboBox() {
        return sourceGroupsComboBox;
    }

    public void setSourceGroupsComboBox(JComboBox<String> sourceGroupsComboBox) {
        this.sourceGroupsComboBox = sourceGroupsComboBox;
    }
    
    class GenericDocumentListener implements DocumentListener {
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            plantUMLWizardPanel1.fireChangeEvent();
            updateUI();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            plantUMLWizardPanel1.fireChangeEvent();
            updateUI();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            plantUMLWizardPanel1.fireChangeEvent();
            updateUI();
        }
        
    }
    
     /**
     * Displays a {@link SourceGroup} in {@link #rootComboBox}.
     */
    private static final class SourceGroupListCellRenderer extends DefaultListCellRenderer/*<SourceGroup>*/ {
        
        public SourceGroupListCellRenderer() {}
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null && value instanceof SourceGroup) {
                SourceGroup g = (SourceGroup) value;
                String name = g.getDisplayName();
                Icon icon = g.getIcon(false);
                super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
                setIcon(icon);
            }
            return this;
        }
        
    }

}
