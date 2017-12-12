package com.lifeonwalden.codeGenerator.mybatis.constant;

public enum XMLAttribute {
    NAMESPACE("namespace"), ID("id"), TYPE("type"), COLUMN("column"), PROPERTY("property"), JDBC_TYPE(
            "jdbcType"), TYPE_HANDLER("typeHandler"), JAVA_TYPE("javaType"), SELECT("select"), RESULT_MAP(
            "resultMap"), REF_ID("refid"), PARAMETER_TYPE("parameterType"), OF_TYPE("ofType"), TEST(
            "test"), PREFIX("prefix"), PREFIX_OVERRIDES("prefixOverrides"), SUFFIX_OVERRIDES("suffixOverrides"), ITEM("item"), INDEX("index"), COLLECTION("collection"), OPEN("open"), SEPARATOR("separator"), CLOSE("close");

    private String name;

    XMLAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
