/* 
 * The MIT License
 *
 * Copyright 2017 Venkat R Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
