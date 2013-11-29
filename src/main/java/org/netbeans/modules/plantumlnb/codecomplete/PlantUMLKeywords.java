/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.codecomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.openide.util.Exceptions;

/**
 *
 * @author VAKKIVE
 */
public class PlantUMLKeywords {

    public static final List<String> keywords = Arrays.asList(
            "@startuml",
            "@enduml",
            "as",
            "also",
            "autonumber",
            "title",
            "newpage",
            "box",
            "alt",
            "else",
            "opt",
            "loop",
            "par",
            "break",
            "critical",
            "note left",
            "note left of",
            "note left on link",
            "note right",
            "note right of",
            "note right on link",
            "note top",
            "note top of",
            "note top on link",
            "note bottom",
            "note bottom of",
            "note bottom on link",
            "note over",
            "note on",
            "end note",
            "ref over",
            "...",
            "group",
            "left",
            "right",
            "of",
            "on",
            "link",
            "over",
            "end",
            "activate",
            "deactivate",
            "destroy",
            "create",
            "footbox",
            "skinparam",
            "skin",
            "top",
            "bottom",
            "top to bottom direction",
            "package",
            "namespace",
            "page",
            "up",
            "down",
            "if",
            "endif",
            "partition",
            "footer",
            "header",
            "center",
            "rotate",
            "ref",
            "return",
            "is",
            "repeat",
            "start",
            "stop",
            "while",
            "endwhile",
            "fork",
            "again",
            /* TYPES */
            "actor",
            "participant",
            "usecase",
            "class",
            "interface",
            "abstract",
            "annotation",
            "enum",
            "component",
            "state",
            "object",
            /* PARTS */
            /* http://plantuml.sourceforge.net/qa/?qa=1015/what-do-they-keywords-fall-under */
            "artifact",
            "folder",
            "rect",
            "node",
            "frame",
            "cloud",
            "database",
            "storage",
            "agent",
            "boundary",
            "control",
            "entity",
            /* VISIBILITY */
            "show",
            "hide",
            "empty members"
    );


    /* OPERATORS */
//    < FORWARD_CALL_MESSAGE : "->",
//    | < FORWARD_CALL_DOTTED_MESSAGE : "-->",
//    | < REVERSE_CALL_MESSAGE : "<-",
//    | < REVERSE_CALL_DOTTED_MESSAGE : "<--",
//    | < FORWARD_ASYNCHRONOUS_CALL_MESSAGE : "->>",
//    | < REVERSE_ASYNCHRONOUS_CALL_MESSAGE : "<<-",  
//    | < DOTS_DASHES : (" ") ( "." | "-" )+ (" ")+,
//    | < LEFT_AGGREGATION :  (" ")+ "o" ( "." | "-" )+ (" ")+,
//    | < LEFT_EXTENSION :  (" ")+ "<|"  ( "." | "-" )+ (" ")+,
//    | < LEFT_COMPOSITION : (" ")+ "*" ( "." | "-" )+ (" ")+,
//    | < RIGHT_AGGREGATION : (" ")+ ( "." | "-" )+ "o" (" ")+,
//    | < RIGHT_EXTENSION : (" ")+ ( "." | "-" )+ "*" (" ")+,
//    | < RIGHT_COMPOSITION : (" ")+ ( "." | "-" )+ "|>" (" ")+,
//
//}
    public static List<String> find(String input) {
        List<String> allMatches = new ArrayList<>();

        for (String keyword : keywords) {
            if (keyword.contains(input)) {
                allMatches.add(keyword);
            }
        }

        return allMatches;
    }

    public static CompletionResultSet find(CompletionResultSet crs, int caretOffset, Document document) {

        String filter = null;
        int startOffset = caretOffset - 1;

        try {
            final StyledDocument bDoc = (StyledDocument) document;
            final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
            final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
            final int whiteOffset = indexOfWhite(line);
            filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
            if (whiteOffset > 0) {
                startOffset = lineStartOffset + whiteOffset + 1;
            } else {
                startOffset = lineStartOffset;
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }

        for (String keyword : keywords) {
            if (keyword.contains(filter)) {
                crs.addItem(new PlantUMLKeywordCompletionItem(keyword, startOffset, caretOffset));
            }
        }

        return crs;
    }

    static int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while (start + 1 < lineElement.getEndOffset()) {
            try {
                if (doc.getText(start, 1).charAt(0) != ' ') {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException(
                        "calling getText(" + start + ", " + (start + 1)
                        + ") on doc of length: " + doc.getLength(), start
                ).initCause(ex);
            }
            start++;
        }
        return start;
    }

    static int indexOfWhite(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return -1;
    }

}
