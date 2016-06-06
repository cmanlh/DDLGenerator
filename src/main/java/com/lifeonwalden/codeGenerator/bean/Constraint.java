package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "constraint")
public class Constraint implements Serializable {
	private static final long serialVersionUID = 2268185885474984309L;

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
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
