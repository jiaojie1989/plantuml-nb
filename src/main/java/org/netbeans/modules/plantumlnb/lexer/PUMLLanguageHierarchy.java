/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author venkat
 */
public class PUMLLanguageHierarchy extends LanguageHierarchy<PUMLTokenId> {

    private static List<PUMLTokenId> tokens;
    private static Map<Integer, PUMLTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<PUMLTokenId>asList(new PUMLTokenId[]{            
            
             new PUMLTokenId("EOF", "whitespace", 0),            
             new PUMLTokenId("WHITESPACE", "whitespace", 1),
            
             new PUMLTokenId("SINGLE_LINE_COMMENT", "comment", 2),            
             new PUMLTokenId("FORMAL_COMMENT", "comment", 3),            
             new PUMLTokenId("MULTI_LINE_COMMENT", "comment", 4),     
             
             new PUMLTokenId("START_UML", "keyword", 6),            
             new PUMLTokenId("END_UML", "keyword", 7),            
             new PUMLTokenId("AS", "keyword", 8),            
             new PUMLTokenId("ALSO", "keyword", 9),            
             new PUMLTokenId("AUTO_NUMBER", "keyword", 10),            
             new PUMLTokenId("TITLE", "keyword", 11),            
             new PUMLTokenId("NEW_PAGE", "keyword", 12),            
             new PUMLTokenId("BOX", "keyword", 13),            
             new PUMLTokenId("ALT", "keyword", 14),            
             new PUMLTokenId("ELSE", "keyword", 15),            
             new PUMLTokenId("OPT", "keyword", 16),            
             new PUMLTokenId("LOOP", "keyword", 17),            
             new PUMLTokenId("PAR", "keyword", 18),            
             new PUMLTokenId("BREAK", "keyword", 19),            
             new PUMLTokenId("CRITICAL", "keyword", 20),            
             new PUMLTokenId("NOTE", "keyword", 21),            
             new PUMLTokenId("GROUP", "keyword", 22),            
             new PUMLTokenId("LEFT", "keyword", 23),            
             new PUMLTokenId("RIGHT", "keyword", 24),            
             new PUMLTokenId("OF", "keyword", 25),            
             new PUMLTokenId("ON", "keyword", 26),            
             new PUMLTokenId("LINK", "keyword", 27),            
             new PUMLTokenId("OVER", "keyword", 28),            
             new PUMLTokenId("END", "keyword", 29),            
             new PUMLTokenId("ACTIVATE", "keyword", 30),            
             new PUMLTokenId("DEACTIVATE", "keyword", 31),            
             new PUMLTokenId("DESTROY", "keyword", 32),            
             new PUMLTokenId("CREATE", "keyword", 33),            
             new PUMLTokenId("FOOTBOX", "keyword", 34),            
             new PUMLTokenId("HIDE", "keyword", 35),            
             new PUMLTokenId("SHOW", "keyword", 36),            
             new PUMLTokenId("SKIN_PARAM", "keyword", 37),            
             new PUMLTokenId("SKIN", "keyword", 38),            
             new PUMLTokenId("TOP", "keyword", 39),            
             new PUMLTokenId("BOTTOM", "keyword", 40),            
             new PUMLTokenId("TOP_TO_BOTTOM_DIRECTION", "keyword", 41),            
             new PUMLTokenId("PACKAGE", "keyword", 42),            
             new PUMLTokenId("NAMESPACE", "keyword", 43),            
             new PUMLTokenId("PAGE", "keyword", 44),            
             new PUMLTokenId("UP", "keyword", 45),            
             new PUMLTokenId("DOWN", "keyword", 46),            
             new PUMLTokenId("IF", "keyword", 47),            
             new PUMLTokenId("ENDIF", "keyword", 48),            
             new PUMLTokenId("PARTITION", "keyword", 49),            
             new PUMLTokenId("FOOTER", "keyword", 50),            
             new PUMLTokenId("HEADER", "keyword", 51),            
             new PUMLTokenId("CENTER", "keyword", 52),            
             new PUMLTokenId("ROTATE", "keyword", 53),            
             new PUMLTokenId("REF", "keyword", 54),            
             new PUMLTokenId("RETURN", "keyword", 55),            
             new PUMLTokenId("IS", "keyword", 56),            
             new PUMLTokenId("REPEAT", "keyword", 57),            
             new PUMLTokenId("START", "keyword", 58),            
             new PUMLTokenId("STOP", "keyword", 59),            
             new PUMLTokenId("WHILE", "keyword", 60),            
             new PUMLTokenId("ENDWHILE", "keyword", 61),            
             new PUMLTokenId("FORK", "keyword", 62),            
             new PUMLTokenId("AGAIN", "keyword", 63),
            
             new PUMLTokenId("FORWARD_CALL_MESSAGE", "operator", 64),            
             new PUMLTokenId("FORWARD_CALL_DOTTED_MESSAGE", "operator", 65),            
             new PUMLTokenId("REVERSE_CALL_MESSAGE", "operator", 66),            
             new PUMLTokenId("REVERSE_CALL_DOTTED_MESSAGE", "operator", 67),            
             new PUMLTokenId("FORWARD_ASYNCHRONOUS_CALL_MESSAGE", "operator", 68),            
             new PUMLTokenId("REVERSE_ASYNCHRONOUS_CALL_MESSAGE", "operator", 69),            
             new PUMLTokenId("COLON_MESSAGE", "operator", 70),
            
             new PUMLTokenId("ACTOR", "type", 71),            
             new PUMLTokenId("PARTICIPANT", "type", 72),            
             new PUMLTokenId("USECASE", "type", 73),            
             new PUMLTokenId("CLASS", "type", 74),            
             new PUMLTokenId("INTERFACE", "type", 75),            
             new PUMLTokenId("ABSTRACT", "type", 76),            
             new PUMLTokenId("ENUM", "type", 77),            
             new PUMLTokenId("COMPONENT", "type", 78),            
             new PUMLTokenId("STATE", "type", 79),            
             new PUMLTokenId("OBJECT", "type", 80),   
             
             new PUMLTokenId("IDENTIFIER", "identifier", 81),            
             
             new PUMLTokenId("LETTER", "literal", 82),            
             new PUMLTokenId("PART_LETTER", "literal", 83),

            /** Lexical state. */
//             new PUMLTokenId("DEFAULT", "whitespace", 0),
            /** Lexical state. */
             new PUMLTokenId("IN_FORMAL_COMMENT", "comment", 84),
            /** Lexical state. */
             new PUMLTokenId("IN_MULTI_LINE_COMMENT", "comment", 85)
            
            
            
            /** Lexical state. */
//            new PUMLTokenId("DEFAULT", " = 0;            

        });
        idToToken = new HashMap<Integer, PUMLTokenId>();
        for (PUMLTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized PUMLTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<PUMLTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<PUMLTokenId> createLexer(LexerRestartInfo<PUMLTokenId> info) {
        return new PUMLLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-puml";
    }

}