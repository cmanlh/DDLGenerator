package com.lifeonwalden.codeGenerator.bean.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias(value = "mybatisInfo")
public class MybatisInfo implements Serializable {
    private static final long serialVersionUID = -944107616662700183L;

    @XStreamAsAttribute
    private String namespace;

    @XStreamAsAttribute
    private String folderName;

    @XStreamAsAttribute
    private String generator;

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

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }
}
