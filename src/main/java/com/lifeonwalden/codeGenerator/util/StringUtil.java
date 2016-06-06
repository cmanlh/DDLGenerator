package com.lifeonwalden.codeGenerator.util;

public class StringUtil {
  public static String firstAlphToUpper(String string) {
    char[] buffer = string.toCharArray();
    buffer[0] = Character.toUpperCase(buffer[0]);

    return new String(buffer);
  }

  public static void main(String[] args) {
    System.out.println(firstAlphToUpper("aere123"));
    System.out.println(firstAlphToUpper("Aere123"));
    System.out.println(firstAlphToUpper("1ere123"));
  }
}
