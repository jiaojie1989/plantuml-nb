/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

/**
 *
 * @author venkat
 */
public class StringUtils {

    public static boolean isEmpty(final String string) {
        return string == null || string.trim().equals("");
    }
    
    public static boolean isNotEmpty(final String string) {
        return !isEmpty(string);
    }
    
}
