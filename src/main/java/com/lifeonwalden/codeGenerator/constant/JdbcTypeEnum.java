package com.lifeonwalden.codeGenerator.constant;

import java.util.HashMap;
import java.util.Map;

public enum JdbcTypeEnum {

    /**
     *
     */
    NCHAR("NCHAR", -15, "", "java.lang.String"),

    /**
     *
     */
    NVARCHAR("NVARCHAR", -9, "", "java.lang.String"),

    /**
     *
     */
    BIT("BIT", -7, "", "java.lang.Boolean"),

    /**
     *
     */
    TINYINT("TINYINT", -6, "", "java.lang.Byte"),

    /**
     *
     */
    BIGINT("BIGINT", -5, "", "java.lang.Long"),

    /**
     *
     */
    LONGVARBINARY("LONGVARBINARY", -4, "", "byte[]"),

    /**
     *
     */
    VARBINARY("VARBINARY", -3, "", "byte[]"),

    /**
     *
     */
    BINARY("BINARY", -2, "", "byte[]"),

    /**
     *
     */
    LONGVARCHAR("LONGVARCHAR", -1, "", "java.lang.String"),

    /**
     *
     */
    NULL("NULL", 0, "", "java.lang.Object"),

    /**
     *
     */
    CHAR("CHAR", 1, "", "java.lang.String"),

    /**
     *
     */
    NUMERIC("NUMERIC", 2, "", "java.math.BigDecimal"),

    /**
     *
     */
    DECIMAL("DECIMAL", 3, "", "java.math.BigDecimal"),

    /**
     *
     */
    INTEGER("INTEGER", 4, "", "java.lang.Integer"),

    /**
     *
     */
    SMALLINT("SMALLINT", 5, "", "java.lang.Short"),

    /**
     *
     */
    FLOAT("FLOAT", 6, "", "java.lang.Double"),

    /**
     *
     */
    REAL("REAL", 7, "", "java.lang.Float"),

    /**
     *
     */
    DOUBLE("DOUBLE", 8, "", "java.lang.Double"),

    /**
     *
     */
    VARCHAR("VARCHAR", 12, "", "java.lang.String"),

    /**
     *
     */
    BOOLEAN("BOOLEAN", 16, "", "java.lang.Boolean"),

    /**
     *
     */
    DATALINK("DATALINK", 70, "", "java.lang.Object"),

    /**
     *
     */
    DATE("DATE", 91, "", "java.util.Date"),

    /**
     *
     */
    TIME("TIME", 92, "", "java.util.Date"),

    /**
     *
     */
    TIMESTAMP("TIMESTAMP", 93, "", "java.util.Date"),

    /**
     *
     */
    DATETIME("TIMESTAMP", 94, "", "java.util.Date"),

    /**
     *
     */
    INT("INTEGER", 100, "", "java.lang.Integer"),

    /**
     *
     */
    OTHER("OTHER", 1111, "", "java.lang.Object"),

    /**
     *
     */
    JAVA_OBJECT("JAVA_OBJECT", 2000, "", "java.lang.Object"),

    /**
     *
     */
    DISTINCT("DISTINCT", 2001, "", "java.lang.Object"),

    /**
     *
     */
    STRUCT("STRUCT", 2002, "", "java.lang.Object"),

    /**
     *
     */
    ARRAY("ARRAY", 2003, "", "java.lang.Object"),

    /**
     *
     */
    BLOB("BLOB", 2004, "", "byte[]"),

    /**
     *
     */
    CLOB("CLOB", 2005, "", "java.lang.String"),

    /**
     *
     */
    REF("REF", 2006, "", "java.lang.Object"),

    /**
     *
     */
    NCLOB("NCLOB", 2011, "", "java.lang.String");

    private static final Map<Integer, JdbcTypeEnum> valueMapping = new HashMap<Integer, JdbcTypeEnum>();

    private static final Map<String, JdbcTypeEnum> nameMapping = new HashMap<String, JdbcTypeEnum>();

    static {
        JdbcTypeEnum[] enumArray = JdbcTypeEnum.values();
        for (JdbcTypeEnum _enum : enumArray) {
            valueMapping.put(_enum.getValue(), _enum);
            nameMapping.put(_enum.name(), _enum);
        }
    }

    private String name;

    private int value;

    private String desc;

    private String javaType;

    private JdbcTypeEnum(String name, int value, String desc, String javaType) {
        this.name = name;
        this.value = value;
        this.desc = desc;
        this.javaType = javaType;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getAlias() {
        return this.javaType;
    }

    public static JdbcTypeEnum valueOf(int value) {
        return valueMapping.get(value);
    }

    public static JdbcTypeEnum nameOf(String name) {
        return nameMapping.get(name);
    }
}
