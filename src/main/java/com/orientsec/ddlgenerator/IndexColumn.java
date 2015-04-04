package com.orientsec.ddlgenerator;

import java.io.Serializable;

public class IndexColumn implements Serializable {
	private static final long serialVersionUID = 6105003221662256777L;

	private String name;

	private String order;

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
