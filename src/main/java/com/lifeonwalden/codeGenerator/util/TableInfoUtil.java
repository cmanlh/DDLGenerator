package com.lifeonwalden.codeGenerator.util;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLWildConditionElementGenerator;

public interface TableInfoUtil {
    static boolean checkWildConditionSupport(Table table) {
        for (Column column : table.getColumns()) {
            if (column.isEnableIn() || column.isEnableNotIn() || ((column.isEnableLike() || column.isEnableNotLike()) && allowedLike(column))) {
                return true;
            }
        }

        return false;
    }

    static boolean checkPrimaryKey(Table table) {
        return null != table.getPrimaryColumns() && table.getPrimaryColumns().size() > 0;
    }

    static boolean allowedLike(Column column) {
        String javaType = column.getJavaType();
        if (!StringUtil.isNotBlank(javaType)) {
            javaType = JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getJavaType();
        }

        return "String".equalsIgnoreCase(javaType) || "java.lang.String".equalsIgnoreCase(javaType);
    }
}
