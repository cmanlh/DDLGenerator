package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "enumConst")
public class EnumConst implements Serializable {
	private static final long serialVersionUID = 6370215234094447122L;

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String note;

	private List<ValueEnum> options;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<ValueEnum> getOptions() {
		return options;
	}

	public void setOptions(List<ValueEnum> options) {
		this.options = options;
	}

}
