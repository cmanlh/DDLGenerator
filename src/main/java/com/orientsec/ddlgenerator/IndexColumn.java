package com.orientsec.ddlgenerator;

import java.io.Serializable;

public class IndexColumn implements Serializable {
	private static final long serialVersionUID = 6105003221662256777L;

	private String value;

	private String desc;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
