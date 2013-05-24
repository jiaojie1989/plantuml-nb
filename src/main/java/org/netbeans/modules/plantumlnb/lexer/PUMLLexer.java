/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.lexer;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.tutorial.pumllexer.JavaCharStream;
import org.tutorial.pumllexer.Token;
import org.tutorial.pumllexer.PUMLParserTokenManager;

/**
 *
 * @author venkat
 */
class PUMLLexer implements Lexer<PUMLTokenId> {

    private LexerRestartInfo<PUMLTokenId> info;
    private PUMLParserTokenManager javaParserTokenManager;

    PUMLLexer(LexerRestartInfo<PUMLTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream(info.input());
        javaParserTokenManager = new PUMLParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<PUMLTokenId> nextToken() {
        Token token = javaParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(PUMLLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}