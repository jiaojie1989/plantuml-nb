/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author venkat
 */
public class PUMLTokenId implements TokenId {

    private final String name;
    private final String primaryCategory;
    private final int id;

    PUMLTokenId(
            String name,
            String primaryCategory,
            int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
    
    public static Language<PUMLTokenId> getLanguage() {
        return new PUMLLanguageHierarchy().language();
    }
}