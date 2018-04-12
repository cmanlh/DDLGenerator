package com.lifeonwalden.codeGenerator.util;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface NameUtil {
    static String getTableName(Table table, Config config) {
        if (table.getDatabase().isWithSchema()) {
            String schema = table.getDatabase().getSchema();
            if (null == schema || schema.length() == 0) {
                throw new RuntimeException("Schema is required");
            } else {
                return schema.concat(".").concat(table.getName());
            }
        } else {
            return table.getName();
        }
    }
}
