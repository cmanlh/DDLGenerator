package com.lifeonwalden.codeGenerator.constant;

import java.util.HashMap;
import java.util.Map;

public enum ColumnIndexPropertyEnum {
  UNIQUE_INDEX(1, "UNIQUE INDEX", "唯一性索引"), INDEX(2, "INDEX", "索引");

  public final int value;

  public final String alias;

  public final String desc;

  private ColumnIndexPropertyEnum(int value, String alias, String desc) {
    this.value = value;
    this.alias = alias;
    this.desc = desc;
  }

  public static ColumnIndexPropertyEnum forAlias(String alias) {
    return aliasMapping.get(alias);
  }

  private static Map<String, ColumnIndexPropertyEnum> aliasMapping = new HashMap<String, ColumnIndexPropertyEnum>();

  static {
    for (ColumnIndexPropertyEnum constraint : ColumnIndexPropertyEnum.values()) {
      aliasMapping.put(constraint.alias, constraint);
    }
  }
}
