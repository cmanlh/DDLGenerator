package com.orientsec.ddlgenerator.config;

import java.io.Serializable;

public class IndexConfig implements Serializable {
	private static final long serialVersionUID = -4561705064729660741L;

	private String table;

	private TableConfig tableConfig;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public TableConfig getTableConfig() {
		return tableConfig;
	}

	public void setTableConfig(TableConfig tableConfig) {
		this.tableConfig = tableConfig;
	}
}
