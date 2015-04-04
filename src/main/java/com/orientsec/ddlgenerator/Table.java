package com.orientsec.ddlgenerator;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {
	private static final long serialVersionUID = 1698905977625460915L;

	private String name;

	private List<Column> column;

	private List<Constraint> constraint;

	private List<Index> index;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumn() {
		return column;
	}

	public void setColumn(List<Column> column) {
		this.column = column;
	}

	public List<Constraint> getConstraint() {
		return constraint;
	}

	public void setConstraint(List<Constraint> constraint) {
		this.constraint = constraint;
	}

	public List<Index> getIndex() {
		return index;
	}

	public void setIndex(List<Index> index) {
		this.index = index;
	}

}
