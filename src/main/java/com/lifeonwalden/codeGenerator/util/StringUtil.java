package com.lifeonwalden.codeGenerator.util;

public class StringUtil {
    public static String firstAlphToUpper(String string) {
        char[] buffer = string.toCharArray();
        buffer[0] = Character.toUpperCase(buffer[0]);

        return new String(buffer);
    }

    public static String removeUnderline(String string) {
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

    public static void main(String[] args) {
        System.out.println(firstAlphToUpper("aere123"));
        System.out.println(firstAlphToUpper("Aere123"));
        System.out.println(firstAlphToUpper("1ere123"));

        System.out.println(removeUnderline("1ere123"));
        System.out.println(removeUnderline("1ere_123"));
        System.out.println(removeUnderline("ABC_DEFGHIJKLMN"));
        System.out.println(removeUnderline("ABC_DEFGHI_JKLMN"));
    }
}
