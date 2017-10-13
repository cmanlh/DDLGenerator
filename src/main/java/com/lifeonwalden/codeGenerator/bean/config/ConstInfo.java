package com.lifeonwalden.codeGenerator.bean.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias(value = "constInfo")
public class ConstInfo implements Serializable {
    private static final long serialVersionUID = 6729183562859310073L;

    @XStreamAsAttribute
    private String packageName;

    @XStreamAsAttribute
    private String folderName;

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

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }
}
