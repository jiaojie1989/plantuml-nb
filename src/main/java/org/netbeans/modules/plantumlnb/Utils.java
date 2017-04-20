package org.netbeans.modules.plantumlnb;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Utils {

    private Utils() {
    }

    public static boolean isAntStylePathPattern(final String path) {
        return (path.indexOf("*") != -1 || path.indexOf("?") != -1);
    }

    public static boolean isRegexPattern(final String regexArg) {
        try {
            Pattern.compile(regexArg);
        } catch (final PatternSyntaxException exception) {
            return false;
        }

        return true;
    }

}
