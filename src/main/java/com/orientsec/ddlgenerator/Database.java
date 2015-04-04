package com.orientsec.ddlgenerator;

import java.io.Serializable;
import java.util.List;

public class Database implements Serializable {
	private static final long serialVersionUID = 8973418987391349438L;

	private String database;

	private String schema;

	private List<Table> table;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<Table> getTable() {
		return table;
	}

	public void setTable(List<Table> table) {
		this.table = table;
	}

}
