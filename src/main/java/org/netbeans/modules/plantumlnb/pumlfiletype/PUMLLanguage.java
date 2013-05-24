/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.pumlfiletype;

import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.plantumlnb.lexer.PUMLTokenId;

/**
 *
 * @author venkat
 */
@LanguageRegistration(mimeType = "text/x-puml")
public class PUMLLanguage extends DefaultLanguageConfig {

    @Override
    public Language getLexerLanguage() {
        return PUMLTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "Plant UML";
    }

}
