package com.lifeonwalden.codeGenerator.constant;

public interface DefinedMappingID {
    String BASE_RESULT_MAP = "baseResultMap";
    String RESULT_MAP_XCLUDE_DB_FIELD = "resultMapXcludeDBField";

    String BASE_COLUMN_LIST = "baseColumnList";
    String BASE_COLUMN_LIST_WITH_PREFIX = "baseColumnListWithPrefix";
    String COLUMN_LIST_XCLUDE_DB_FIELD = "columnListXcludeDBField";
    String COLUMN_LIST_WITH_PREFIX_XCLUDE_DB_FIELD = "columnListWithPrefixXcludeDBField";
    String FIELD_PICK = "fieldPick";
    String FIELD_PICK_WITH_PREFIX = "fieldPickWithPrefix";

    String CONDITION = "condition";
    String WILD_CONDITION = "wildCondition";
    String DELETE_CONDITION = "deleteCondition";
    String QUERY_CONDITION = "queryCondition";
    String QUERY_WILD_CONDITION = "queryWildCondition";

    String QUERY_SQL = "querySQL";
    String DIRECT_QUERY_SQL = "directQuerySQL";
    String GET = "get";
    String DIRECT_GET = "directGet";
    String SELECT = "select";
    String DIRECT_SELECT = "directSelect";
    String SELECT_ALL = "selectAll";
    String DIRECT_SELECT_All = "directSelectAll";
    String SELECT_WILD = "selectWild";
    String DIRECT_SELECT_WILD = "directSelectWild";
    String ORDER_BY_SQL = "orderBySQL";

    String INSERT_SQL = "insertSQL";
    String INSERT = "insert";

    String DELETE_SQL = "deleteSQL";
    String LOGICAL_DELETE_SQL = "logicalDeleteSQL";
    String DELETE = "delete";
    String LOGICAL_DELETE = "logicalDelete";
    String REMOVE = "remove";
    String REMOVE_ALL = "removeAll";
    String LOGICAL_REMOVE = "logicalRemove";

    String UPDATE_SQL = "updateSQL";
    String UPDATE_DYNAMIC_SQL = "updateDynamicSQL";
    String DIRECT_UPDATE_SQL = "directUpdateSQL";
    String UPDATE = "update";
    String UPDATE_DYNAMIC = "updateDynamic";
    String DIRECT_UPDATE = "directUpdate";
}
