package com.orientsec.ddlgenerator;

import java.io.Serializable;
import java.util.List;

public class Index implements Serializable {
	private static final long serialVersionUID = -6291502126054084649L;

	private String name;

	private String type;

	private List<IndexColumn> column;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<IndexColumn> getColumn() {
		return column;
	}

	public void setColumn(List<IndexColumn> column) {
		this.column = column;
	}
}
