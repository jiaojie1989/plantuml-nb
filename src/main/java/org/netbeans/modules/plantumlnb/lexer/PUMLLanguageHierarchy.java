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

            new PUMLTokenId("EOF", "whitespace" ,0),
            new PUMLTokenId("WHITESPACE", "whitespace" ,1),

            new PUMLTokenId("SINGLE_LINE_COMMENT", "comment" ,2),

            new PUMLTokenId("MESSAGE", "message" ,3),
            new PUMLTokenId("START_UML", "keyword" ,4),
            new PUMLTokenId("END_UML", "keyword" ,5),
            new PUMLTokenId("AS", "keyword" ,6),
            new PUMLTokenId("ALSO", "keyword" ,7),
            new PUMLTokenId("AUTO_NUMBER", "keyword" ,8),
            new PUMLTokenId("TITLE", "keyword" ,9),
            new PUMLTokenId("NEW_PAGE", "keyword" ,10),
            new PUMLTokenId("BOX", "keyword" ,11),
            new PUMLTokenId("ALT", "keyword" ,12),
            new PUMLTokenId("ELSE", "keyword" ,13),
            new PUMLTokenId("OPT", "keyword" ,14),
            new PUMLTokenId("LOOP", "keyword" ,15),
            new PUMLTokenId("PAR", "keyword" ,16),
            new PUMLTokenId("BREAK", "keyword" ,17),
            new PUMLTokenId("CRITICAL", "keyword" ,18),
            new PUMLTokenId("NOTE", "keyword" ,19),
            new PUMLTokenId("GROUP", "keyword" ,20),
            new PUMLTokenId("LEFT", "keyword" ,21),
            new PUMLTokenId("RIGHT", "keyword" ,22),
            new PUMLTokenId("OF", "keyword" ,23),
            new PUMLTokenId("ON", "keyword" ,24),
            new PUMLTokenId("LINK", "keyword" ,25),
            new PUMLTokenId("OVER", "keyword" ,26),
            new PUMLTokenId("END", "keyword" ,27),
            new PUMLTokenId("ACTIVATE", "keyword" ,28),
            new PUMLTokenId("DEACTIVATE", "keyword" ,29),
            new PUMLTokenId("DESTROY", "keyword" ,30),
            new PUMLTokenId("CREATE", "keyword" ,31),
            new PUMLTokenId("FOOTBOX", "keyword" ,32),
            new PUMLTokenId("HIDE", "keyword" ,33),
            new PUMLTokenId("SHOW", "keyword" ,34),
            new PUMLTokenId("SKIN_PARAM", "keyword" ,35),
            new PUMLTokenId("SKIN", "keyword" ,36),
            new PUMLTokenId("TOP", "keyword" ,37),
            new PUMLTokenId("BOTTOM", "keyword" ,38),
            new PUMLTokenId("TOP_TO_BOTTOM_DIRECTION", "keyword" ,39),
            new PUMLTokenId("PACKAGE", "keyword" ,40),
            new PUMLTokenId("NAMESPACE", "keyword" ,41),
            new PUMLTokenId("PAGE", "keyword" ,42),
            new PUMLTokenId("UP", "keyword" ,43),
            new PUMLTokenId("DOWN", "keyword" ,44),
            new PUMLTokenId("IF", "keyword" ,45),
            new PUMLTokenId("ENDIF", "keyword" ,46),
            new PUMLTokenId("PARTITION", "keyword" ,47),
            new PUMLTokenId("FOOTER", "keyword" ,48),
            new PUMLTokenId("HEADER", "keyword" ,49),
            new PUMLTokenId("CENTER", "keyword" ,50),
            new PUMLTokenId("ROTATE", "keyword" ,51),
            new PUMLTokenId("REF", "keyword" ,52),
            new PUMLTokenId("RETURN", "keyword" ,53),
            new PUMLTokenId("IS", "keyword" ,54),
            new PUMLTokenId("REPEAT", "keyword" ,55),
            new PUMLTokenId("START", "keyword" ,56),
            new PUMLTokenId("STOP", "keyword" ,57),
            new PUMLTokenId("WHILE", "keyword" ,58),
            new PUMLTokenId("ENDWHILE", "keyword" ,59),
            new PUMLTokenId("FORK", "keyword" ,60),
            new PUMLTokenId("AGAIN", "keyword" ,61),

            new PUMLTokenId("FORWARD_CALL_MESSAGE", "operator" ,62),
            new PUMLTokenId("FORWARD_CALL_DOTTED_MESSAGE", "operator" ,63),
            new PUMLTokenId("REVERSE_CALL_MESSAGE", "operator" ,64),
            new PUMLTokenId("REVERSE_CALL_DOTTED_MESSAGE", "operator" ,65),
            new PUMLTokenId("FORWARD_ASYNCHRONOUS_CALL_MESSAGE", "operator" ,66),
            new PUMLTokenId("REVERSE_ASYNCHRONOUS_CALL_MESSAGE", "operator" ,67),            

            new PUMLTokenId("ACTOR", "type" ,68),
            new PUMLTokenId("PARTICIPANT", "type" ,69),
            new PUMLTokenId("USECASE", "type" ,70),
            new PUMLTokenId("CLASS", "type" ,71),
            new PUMLTokenId("INTERFACE", "type" ,72),
            new PUMLTokenId("ABSTRACT", "type" ,73),
            new PUMLTokenId("ENUM", "type" ,74),
            new PUMLTokenId("COMPONENT", "type" ,75),
            new PUMLTokenId("STATE", "type" ,76),
            new PUMLTokenId("OBJECT", "type" ,77),
            new PUMLTokenId("IDENTIFIER", "type" ,78),
            new PUMLTokenId("LETTER", "type" ,79),
            new PUMLTokenId("PART_LETTER", "type" ,80),
                      

            /** Lexical state. */
            new PUMLTokenId("DEFAULT", "whitespace" ,81)



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