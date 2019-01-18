/*
 * Copyright 2005 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.lifeonwalden.codeGenerator.mybatis.constant;


public enum DatabaseUserTable {
    SQLSERVER("select name from sys.objects where type_desc='USER_TABLE' or type_desc='VIEW' order by name"), HSQLDB(
            "SELECT table_name FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'"),ORACLE(""),MYSQL("");

    private String userTableRetrievalStatement;

    private DatabaseUserTable(String userTableRetrievalStatement) {
        this.userTableRetrievalStatement = userTableRetrievalStatement;
    }

    public String getUserTableRetrievalStatement() {
        return userTableRetrievalStatement;
    }

    public static DatabaseUserTable getDatabaseUserTable(String database) {
        DatabaseUserTable returnValue = null;

        if ("sqlServer".equalsIgnoreCase(database) || "jtds".equalsIgnoreCase(database)) {
            returnValue = SQLSERVER;
        } else if ("hsqldb".equalsIgnoreCase(database)) {
            return returnValue = HSQLDB;
        } else if ("oracle".equalsIgnoreCase(database)) {
            return returnValue = ORACLE;
        } else if ("mysql".equalsIgnoreCase(database)) {
            return returnValue = MYSQL;
        }

        return returnValue;
    }
}
