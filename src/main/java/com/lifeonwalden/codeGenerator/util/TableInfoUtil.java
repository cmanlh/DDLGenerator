package com.lifeonwalden.codeGenerator.util;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.constant.BeanTypeEnum;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;

import java.util.SortedSet;
import java.util.TreeSet;

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

    static int getSerialVersionUID(Table table, BeanTypeEnum beanType) {
        SortedSet<String> sortedSet = new TreeSet<>();
        for (Column column : table.getColumns()) {
            sortedSet.add(StringUtil.removeUnderline(column.getName()));
        }

        StringBuilder hashString = new StringBuilder();
        for (String name : sortedSet) {
            hashString.append(name);
        }
        hashString.append(beanType.value);

        return hashString.hashCode();
    }

    static boolean allowedLike(Column column) {
        String javaType = column.getJavaType();
        if (!StringUtil.isNotBlank(javaType)) {
            javaType = JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getJavaType();
        }

        return "String".equalsIgnoreCase(javaType) || "java.lang.String".equalsIgnoreCase(javaType);
    }

    static boolean allowedDateRange(Column column) {
        JdbcTypeEnum jdbcType = JdbcTypeEnum.nameOf(column.getType().toUpperCase());
        return jdbcType.equals(JdbcTypeEnum.DATE) || jdbcType.equals(JdbcTypeEnum.DATETIME) || jdbcType.equals(JdbcTypeEnum.TIME)
                || jdbcType.equals(JdbcTypeEnum.TIMESTAMP);
    }
}
