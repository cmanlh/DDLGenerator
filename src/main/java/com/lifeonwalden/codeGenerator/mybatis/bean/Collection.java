package com.lifeonwalden.codeGenerator.mybatis.bean;

public class Collection {

	private String select;

	private String javaType;

	private String ofType;

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getOfType() {
		return ofType;
	}

	public void setOfType(String ofType) {
		this.ofType = ofType;
	}
}
