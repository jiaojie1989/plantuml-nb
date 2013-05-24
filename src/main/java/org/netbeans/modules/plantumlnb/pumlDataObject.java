/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_PUML_LOADER=Files of PUML"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_PUML_LOADER",
mimeType = "text/x-puml",
extension = {"puml", "PUML"})
@DataObject.Registration(
    mimeType = "text/x-puml",
iconBase = "org/netbeans/modules/plantumlnb/icon.png",
displayName = "#LBL_PUML_LOADER",
position = 300)
@ActionReferences({
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
    position = 100,
    separatorAfter = 200),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
    position = 300),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
    position = 400,
    separatorAfter = 500),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
    position = 600),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
    position = 700,
    separatorAfter = 800),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
    position = 900,
    separatorAfter = 1000),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
    position = 1100,
    separatorAfter = 1200),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
    position = 1300),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
    position = 1400)
})
public class pumlDataObject extends MultiDataObject implements FileChangeListener {
    private String content;
    
    private FileObject fileObject;
//    private final Saver saver = new Saver();

    public pumlDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        fileObject = pf;
        pf.addFileChangeListener(this);
        registerEditor("text/x-puml", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }
    
    @MultiViewElement.Registration(
        displayName = "#LBL_puml_EDITOR",
    iconBase = "org/netbeans/modules/plantumlnb/icon.png",
    mimeType = "text/x-puml",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
    preferredID = "puml",
    position = 1200)
    @Messages("LBL_puml_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return ((MultiViewEditorElement ) new pumlVisualElement(lkp));
    }
    
    
    
    // Michael Wever 26/09/2001
    /** Gets image for the image data 
     * @return the image or <code>null</code> if image could not be created
     * @return  java.io.IOException  if an error occurs during reading
     */
    public Image getImage() throws IOException {
        InputStream inputStream = getPrimaryFile().getInputStream();
        BufferedImage image = null;
        try {            
            image = ImageIO.read(inputStream);        
        } catch(IOException e){
            Logger.getLogger(pumlDataObject.class.getName()).info(e.getMessage());
        }
        
        return image;       

        
//        InputStream input = getPrimaryFile().getInputStream();
//        try {
//            return javax.imageio.ImageIO.read(input);
//        } catch (IndexOutOfBoundsException ioobe) {
//            return null;
//        } finally {
//            input.close();
//        }
    }
    
//    synchronized void setContent(String text) {
//        this.content = text;
//        if (text != null) {
//            setModified(true);
//            getCookieSet().add(saver);
//        } else {
//            setModified(false);
//            getCookieSet().remove(saver);
//        }
//    }
//    
//    private class Saver extends AbstractSavable {
//
//        @Override
//        public void save() throws IOException {
//            String txt;
//            synchronized (pumlDataObject.this) {
//                //synchronize access to the content field
//                txt = content;
//                setContent(null);
//            }
//            FileObject fo = getPrimaryFile();
//            OutputStream out = new BufferedOutputStream(fo.getOutputStream());
//            PrintWriter writer = new PrintWriter(out);
//            try {
//                writer.print(txt);
//            } finally {
//                writer.close();
//                out.close();
//            }
//        }
//    }

    @Override
    public void fileFolderCreated(FileEvent fe) {}

    @Override
    public void fileDataCreated(FileEvent fe) {}

    @Override
    public void fileChanged(FileEvent fe) {
        
        DataObject.Registry registries = DataObject.getRegistry();
        PUMLTopComponent tc = PUMLTopComponent.getInstance();

        DataObject[] objects = registries.getModified();

        tc.setCurrentDataObject(this);

        tc.setNewContent((InputStream) tc.getPumlGenerator().generate(fileObject));
        
    }

    @Override
    public void fileDeleted(FileEvent fe) {}

    @Override
    public void fileRenamed(FileRenameEvent fre) {}

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {}
}
