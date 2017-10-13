package com.lifeonwalden.codeGenerator.bean.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias(value = "daoInfo")
public class DAOInfo implements Serializable {
    private static final long serialVersionUID = 5664603834270051527L;

    @XStreamAsAttribute
    private String packageName;

    @XStreamAsAttribute
    private String folderName;

    @XStreamAsAttribute
    private String generator;

    @XStreamAsAttribute
    private String namePattern;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }
}
