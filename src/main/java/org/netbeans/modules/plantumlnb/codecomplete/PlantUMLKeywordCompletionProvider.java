/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.plantumlnb.codecomplete;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author VAKKIVE
 */
@MimeRegistration(mimeType = "text/x-puml", service = CompletionProvider.class)
public class PlantUMLKeywordCompletionProvider implements CompletionProvider {

    public PlantUMLKeywordCompletionProvider() {
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent jtc) {
        /**
         * We need to test whether the user pressed the keys applicable to the COMPLETION_QUERY_TYPE.
         */
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) return null;
        
        return new AsyncCompletionTask(new AsyncCompletionQuery(){
            @Override
            protected void query(CompletionResultSet crs, Document dcmnt, int caretOffset) {
                PlantUMLKeywords.find(crs, caretOffset, dcmnt).finish();
            }
        });
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        return 1;
    }
    
}
