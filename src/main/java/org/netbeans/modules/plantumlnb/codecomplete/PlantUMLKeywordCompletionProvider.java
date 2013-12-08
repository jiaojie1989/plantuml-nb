/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.plantumlnb.codecomplete;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author VAKKIVE
 */
//@MimeRegistration(mimeType = "text/x-puml", service = CompletionProvider.class)
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
                PlantUMLKeywords.find(crs, dcmnt, caretOffset).finish();
            }

//            @Override
//            protected boolean canFilter(JTextComponent component) {
////                int currentCaretPosition = component.getCaretPosition();
////                System.out.println(currentCaretPosition);
//                return true;
//            }
//
//            @Override
//            protected void filter(CompletionResultSet resultSet) {
//                super.filter(resultSet); //To change body of generated methods, choose Tools | Templates.
//            }
//            
//            private int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
//                Element lineElement = doc.getParagraphElement(offset);
//                int start = lineElement.getStartOffset();
//                while (start + 1 < lineElement.getEndOffset()) {
//                    try {
//                        if (doc.getText(start, 1).charAt(0) != ' ') {
//                            break;
//                        }
//                    } catch (BadLocationException ex) {
//                        throw (BadLocationException) new BadLocationException(
//                                "calling getText(" + start + ", " + (start + 1)
//                                + ") on doc of length: " + doc.getLength(), start
//                        ).initCause(ex);
//                    }
//                    start++;
//                }
//                return start;
//            }
//
//            private int indexOfWhite(char[] line) {
//                int i = line.length;
//                while (--i > -1) {
//                    final char c = line[i];
//                    if (Character.isWhitespace(c)) {
//                        return i;
//                    }
//                }
//                return -1;
//            }
//            
        });
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        return 1;
    }
    
}
