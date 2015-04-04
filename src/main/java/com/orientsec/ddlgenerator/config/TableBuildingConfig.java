package com.orientsec.ddlgenerator.config;

import java.io.Serializable;
import java.util.List;

import com.orientsec.ddlgenerator.Table;

public class TableBuildingConfig implements Serializable {
	private static final long serialVersionUID = 8973418987391349438L;

	private TableConfig config;

	private List<Table> table;

	public TableConfig getConfig() {
		return config;
	}

	public void setConfig(TableConfig config) {
		this.config = config;
	}

	public List<Table> getTable() {
		return table;
	}

	public void setTable(List<Table> table) {
		this.table = table;
	}

}
