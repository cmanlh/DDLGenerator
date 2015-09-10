package com.test;

import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试 */
public enum TestEnum {
  /**
   * ${@com.test.TestEnum@NIL.getValue()} */
  NIL("NIL", 0, "空值", ""),

  /**
   * ${@com.test.TestEnum@HELLO.getValue()} */
  HELLO("HELLO", 1, "描述", "备注"),

  /**
   * ${@com.test.TestEnum@WORLD.getValue()} */
  WORLD("WORLD", 2, "描述", "");

  private static final Map<Integer, TestEnum> valueMapping = new HashMap<Integer, TestEnum>();

  private static final Map<String, TestEnum> nameMapping = new HashMap<String, TestEnum>();

  private static final Map<String, TestEnum> descMapping = new HashMap<String, TestEnum>();

  static {
    TestEnum[] enumArray = TestEnum.values();
    for (TestEnum _enum : enumArray) {
      valueMapping.put(_enum.getValue(), _enum);
      nameMapping.put(_enum.getName(), _enum);
      descMapping.put(_enum.getDesc(), _enum);
    }
  }

  private String name;

  private int value;

  private String desc;

  private String note;

  private TestEnum(String name, int value, String desc, String note) {
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

  public static TestEnum valueOf(int value) {
    return valueMapping.get(value);
  }

  public static TestEnum nameOf(String name) {
    return nameMapping.get(name);
  }

  public static TestEnum descOf(String desc) {
    return descMapping.get(desc);
  }
}
