/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.util.Iterator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;


/**
 * https://blogs.oracle.com/geertjan/entry/multiview_of_the_next_release
 * @author venkat
 * 
 * NOTE: We don't need VisualElement Registration here.MultiViewEditorElement displays two views already.
 */

public final class pumlVisualElement extends MultiViewEditorElement {
    
    private static final long serialVersionUID = -6918553698868162650L;

    private pumlDataObject obj;
    private JToolBar toolbar = new JToolBar();
//    private transient MultiViewElementCallback callback;
    private TopComponent.Registry topComponentRegistry = TopComponent.getRegistry();

    public pumlVisualElement(Lookup lkp) {
        super(lkp);
        obj = lkp.lookup(pumlDataObject.class);
        assert obj != null;
    }
    

    @Override
    public void componentActivated() {
        super.componentActivated(); 
        PUMLTopComponent pumltc = retrievePUMLTopComponent();
        if(pumltc != null) {
            pumltc.setNewContent(obj);
        }
    }

    /**
     * http://netbeans-org.1045718.n5.nabble.com/Open-TopComponent-Programatically-td3036307.html
     */
    @Override
    public void componentOpened() {
        super.componentOpened(); 
        if(retrievePUMLTopComponent() == null) {
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    PUMLTopComponent pumltc = PUMLTopComponent.getInstance();
                    
                    // Register the instantiated PUMLTopComponent with Netbeans.
                    pumltc.open();
                    pumltc.setNewContent(obj);
                }
            });
            
        }

    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }
        
    public PUMLTopComponent retrievePUMLTopComponent() {
        Iterator i = topComponentRegistry.getOpened().iterator();
        
        PUMLTopComponent pumltc = null;
        while(i.hasNext()){
            TopComponent tc = (TopComponent) i.next();
            if(tc instanceof PUMLTopComponent) {
                pumltc = (PUMLTopComponent) tc;
                break;
            }                
//            System.out.println(((TopComponent) i.next()).getClass());
        }
        
        return pumltc;
//        return obj.getLookup().lookup(PUMLTopComponent.class);
    }
    
    @Override
    public void componentDeactivated() {
        super.componentDeactivated();
    }
    
    @Override
    public UndoRedo getUndoRedo() {
        return super.getUndoRedo();
    }

//    @Override
//    public void setMultiViewCallback(MultiViewElementCallback callback) {
//        this.callback = callback;
//    }

}
