package com.orientsec.ddlgenerator.config;

import java.io.Serializable;

public class TableConfig implements Serializable {
	private static final long serialVersionUID = 8905001963315346061L;

	private String database;

	private String schema;

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
}
