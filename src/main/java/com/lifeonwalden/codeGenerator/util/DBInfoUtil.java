package com.lifeonwalden.codeGenerator.util;

import com.lifeonwalden.codeGenerator.bean.config.JDBCConnectionConfiguration;

public class DBInfoUtil {
    public static String getDBType(JDBCConnectionConfiguration config) {
        String[] metadatas = config.getConnectionURL().split(":");

        return metadatas[1];
    }
}
