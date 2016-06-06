package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "mybatisInfo")
public class MybatisInfo implements Serializable {
	private static final long serialVersionUID = -944107616662700183L;

	@XStreamAsAttribute
	private String namespace;

	@XStreamAsAttribute
	private String folderName;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}
