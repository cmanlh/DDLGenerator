package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "indexColumn")
public class IndexColumn implements Serializable {
	private static final long serialVersionUID = 3289465936180763099L;

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String order = "ASC";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
