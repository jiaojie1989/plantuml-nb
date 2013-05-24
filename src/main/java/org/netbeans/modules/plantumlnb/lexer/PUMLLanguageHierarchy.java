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
            new PUMLTokenId("WHITESPACE", "whitespace",  1),            
                        
            new PUMLTokenId("IN_FORMAL_COMMENT", "comment", 2),
            new PUMLTokenId("IN_MULTI_LINE_COMMENT", "comment", 3),
            new PUMLTokenId("SINGLE_LINE_COMMENT", "comment",  4),
            new PUMLTokenId("FORMAL_COMMENT", "comment", 5),            
            new PUMLTokenId("MULTI_LINE_COMMENT", "comment", 6),
            
            new PUMLTokenId("START_UML", "keyword", 8),            
            new PUMLTokenId("END_UML", "keyword", 9),            
            new PUMLTokenId("AS", "keyword", 10),            
            new PUMLTokenId("ALSO", "keyword", 11),            
            new PUMLTokenId("AUTO_NUMBER", "keyword", 12),            
            new PUMLTokenId("TITLE", "keyword", 13),            
            new PUMLTokenId("NEW_PAGE", "keyword", 14),            
            new PUMLTokenId("BOX", "keyword", 15),            
            new PUMLTokenId("ALT", "keyword", 16),            
            new PUMLTokenId("ELSE", "keyword", 17),            
            new PUMLTokenId("OPT", "keyword", 18),            
            new PUMLTokenId("LOOP", "keyword", 19),            
            new PUMLTokenId("PAR", "keyword", 20),            
            new PUMLTokenId("BREAK", "keyword", 21),            
            new PUMLTokenId("CRITICAL", "keyword", 22),            
            new PUMLTokenId("NOTE", "keyword", 23),
            new PUMLTokenId("GROUP", "keyword", 24),
            new PUMLTokenId("LEFT", "keyword", 25),
            new PUMLTokenId("RIGHT", "keyword", 26),
            new PUMLTokenId("OF", "keyword", 27),
            new PUMLTokenId("ON", "keyword", 28),
            new PUMLTokenId("LINK", "keyword", 29),
            new PUMLTokenId("OVER", "keyword", 30),
            new PUMLTokenId("END", "keyword", 31),
            new PUMLTokenId("ACTIVATE", "keyword", 32),
            new PUMLTokenId("DEACTIVATE", "keyword", 33),
            new PUMLTokenId("DESTROY", "keyword", 34),
            new PUMLTokenId("CREATE", "keyword", 35),
            new PUMLTokenId("FOOTBOX", "keyword", 36),
            new PUMLTokenId("HIDE", "keyword", 37),
            new PUMLTokenId("SHOW", "keyword", 38),
            new PUMLTokenId("SKIN_PARAM", "keyword", 39),
            new PUMLTokenId("SKIN", "keyword", 40),            
            new PUMLTokenId("TOP", "keyword", 41),            
            new PUMLTokenId("BOTTOM", "keyword", 42),            
            new PUMLTokenId("TOP_TO_BOTTOM_DIRECTION", "keyword", 43),
            new PUMLTokenId("PACKAGE", "keyword", 44),
            new PUMLTokenId("NAMESPACE", "keyword", 45),            
            new PUMLTokenId("PAGE", "keyword", 46),            
            new PUMLTokenId("UP", "keyword", 47),            
            new PUMLTokenId("DOWN", "keyword", 48),            
            new PUMLTokenId("IF", "keyword", 49),            
            new PUMLTokenId("ENDIF", "keyword", 50),            
            new PUMLTokenId("PARTITION", "keyword", 51),            
            new PUMLTokenId("FOOTER", "keyword", 52),            
            new PUMLTokenId("HEADER", "keyword", 53),            
            new PUMLTokenId("CENTER", "keyword", 54),            
            new PUMLTokenId("ROTATE", "keyword", 55),            
            new PUMLTokenId("REF", "keyword", 56),            
            new PUMLTokenId("RETURN", "keyword", 57),            
            new PUMLTokenId("IS", "keyword", 58),            
            new PUMLTokenId("REPEAT", "keyword", 59),            
            new PUMLTokenId("START", "keyword", 60),           
            new PUMLTokenId("STOP", "keyword", 61),            
            new PUMLTokenId("WHILE", "keyword", 62),            
            new PUMLTokenId("ENDWHILE", "keyword", 63),            
            new PUMLTokenId("FORK", "keyword", 64),            
            new PUMLTokenId("AGAIN", "keyword", 65),  
            
            new PUMLTokenId("IDENTIFIER", "identifier", 66),            
            
            new PUMLTokenId("LETTER", "literal", 67),            
            new PUMLTokenId("PART_LETTER", "literal", 68),            
            
            new PUMLTokenId("FORWARD_CALL_MESSAGE", "operator", 69),
            new PUMLTokenId("FORWARD_CALL_DOTTED_MESSAGE", "operator", 70),
            new PUMLTokenId("REVERSE_CALL_MESSAGE", "operator", 71),            
            new PUMLTokenId("REVERSE_CALL_DOTTED_MESSAGE", "operator", 72),
            new PUMLTokenId("FORWARD_ASYNCHRONOUS_CALL_MESSAGE","operator",  73),            
            new PUMLTokenId("REVERSE_ASYNCHRONOUS_CALL_MESSAGE", "operator", 74),
            new PUMLTokenId("COLON_MESSAGE", "operator", 75),
            
            new PUMLTokenId("ACTOR", "type", 76),
            new PUMLTokenId("PARTICIPANT", "type", 77),
            new PUMLTokenId("USECASE", "type", 78),
            new PUMLTokenId("CLASS", "type", 79),
            new PUMLTokenId("INTERFACE", "type", 80),
            new PUMLTokenId("ABSTRACT", "type", 81),
            new PUMLTokenId("ENUM", "type", 82),
            new PUMLTokenId("COMPONENT", "type", 83),
            new PUMLTokenId("STATE", "type", 84),
            new PUMLTokenId("OBJECT", "type", 85),
            
            
            
            
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