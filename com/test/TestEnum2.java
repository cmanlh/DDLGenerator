package com.test;

import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public enum TestEnum2 {
  /**
   * ${@com.test.TestEnum2@NIL.getValue()}
   *
   * 空值
   */
  NIL("NIL", 0, "空值", ""),

  /**
   * ${@com.test.TestEnum2@HELLO.getValue()}
   *
   * 描述
   */
  HELLO("HELLO", 1, "描述", "备注"),

  /**
   * ${@com.test.TestEnum2@WORLD.getValue()}
   *
   * 描述
   */
  WORLD("WORLD", 2, "描述", "");

  private static final Map<Integer, TestEnum2> valueMapping = new HashMap<Integer, TestEnum2>();

  private static final Map<String, TestEnum2> nameMapping = new HashMap<String, TestEnum2>();

  private static final Map<String, TestEnum2> descMapping = new HashMap<String, TestEnum2>();

  static {
    TestEnum2[] enumArray = TestEnum2.values();
    for (TestEnum2 _enum : enumArray) {
      valueMapping.put(_enum.getValue(), _enum);
      nameMapping.put(_enum.getName(), _enum);
      descMapping.put(_enum.getDesc(), _enum);
    }
  }

  private String name;

  private int value;

  private String desc;

  private String note;

  private TestEnum2(String name, int value, String desc, String note) {
    this.name = name;
    this.value = value;
    this.desc = desc;
    this.note = note;
  }

  public int getValue() {
    return this.value;
  }

  public String getName() {
    return this.name;
  }

  public String getDesc() {
    return this.desc;
  }

  public String getNote() {
    return this.note;
  }

  public static TestEnum2 valueOf(int value) {
    return valueMapping.get(value);
  }

  public static TestEnum2 nameOf(String name) {
    return nameMapping.get(name);
  }

  public static TestEnum2 descOf(String desc) {
    return descMapping.get(desc);
  }
}
