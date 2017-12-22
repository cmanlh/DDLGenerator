package com.lifeonwalden.codeGenerator.util;

public interface StringUtil {
    static String firstAlphToUpper(String string) {
        char[] buffer = string.toCharArray();
        buffer[0] = Character.toUpperCase(buffer[0]);

        return new String(buffer);
    }

    static String removeUnderline(String string) {
        String[] words = string.split("_");
        int length = words.length;

        if (1 == length) {
            return string;
        }
        StringBuilder builder = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < length; i++) {
            builder.append(firstAlphToUpper(words[i].toLowerCase()));
        }

        return builder.toString();
    }

    static boolean isNotBlank(String string) {
        int strLen;
        if (string == null || (strLen = string.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(string.charAt(i)) == false) {
                return true;
            }
        }
        return false;
    }
}
