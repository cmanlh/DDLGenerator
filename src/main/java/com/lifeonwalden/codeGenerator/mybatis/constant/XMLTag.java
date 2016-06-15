package com.lifeonwalden.codeGenerator.mybatis.constant;

public enum XMLTag {
  ROOT("mapper"), RESULT_MAP("resultMap"), RESULT("result"), ASSOCIATION("association"), SQL("sql"), SELECT("select"), INCLUDE("include"), INSERT(
      "insert"), UPDATE("update"), DELETE("delete"), COLLECTION("collection"), SET("set"), IF("if"), TRIM("trim"), CONSTRUCTOR("constructor"), ID_ARG(
      "idArg"), ID("id");

  private String name;

  XMLTag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
