package com.lifeonwalden.codeGenerator.constant;

import java.util.HashMap;
import java.util.Map;

public enum BeanTypeEnum {
    POJO(1, "BEAN", "pojo bean"), HASH(2, "MAP", "map bean"), HASH_PARAM(3, "PARAMMAP", "parameter map bean"), HASH_EXT_PARAM(4, "EXTPARAMMAP", "extension parameter map bean"), POJO_PARAM(5, "PARAMBEAN", "parameter pojo bean");

    private static Map<String, BeanTypeEnum> aliasMapping = new HashMap<String, BeanTypeEnum>();

    static {
        for (BeanTypeEnum constraint : BeanTypeEnum.values()) {
            aliasMapping.put(constraint.alias, constraint);
        }
    }

    public final int value;
    public final String alias;
    public final String desc;

    private BeanTypeEnum(int value, String alias, String desc) {
        this.value = value;
        this.alias = alias;
        this.desc = desc;
    }

    public static BeanTypeEnum forAlias(String alias) {
        return aliasMapping.get(alias);
    }
}
