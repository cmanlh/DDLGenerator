package com.lifeonwalden.codeGenerator.constant;

import java.util.HashMap;
import java.util.Map;

public enum ColumnConstraintEnum {
  PRIMARY_KEY(1, "PRIMARY KEY", "主键约束"), UNION_PRIMARY_KEY(2, "UNION PRIMARY KEY", "联合主键约束"), UNIQUE_KEY(3,
      "UNIQUE KEY", "唯一键约束"), FOREIGN_KEY(4, "FOREIGN KEY", "外键约束");

  public final int value;

  public final String alias;

  public final String desc;

  private ColumnConstraintEnum(int value, String alias, String desc) {
    this.value = value;
    this.alias = alias;
    this.desc = desc;
  }

  public static ColumnConstraintEnum forAlias(String alias) {
    return aliasMapping.get(alias);
  }

  private static Map<String, ColumnConstraintEnum> aliasMapping = new HashMap<String, ColumnConstraintEnum>();

  static {
    for (ColumnConstraintEnum constraint : ColumnConstraintEnum.values()) {
      aliasMapping.put(constraint.alias, constraint);
    }
  }
}
