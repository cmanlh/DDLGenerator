package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "beanInfo")
public class BeanInfo implements Serializable {
    private static final long serialVersionUID = -3788453383700103003L;

    @XStreamAsAttribute
    private String packageName;

    @XStreamAsAttribute
    private String folderName;

    @XStreamAsAttribute
    private String generator;

    @XStreamAsAttribute
    private String resultNamePattern;

    @XStreamAsAttribute
    private String paramNamePattern;

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

    public String getResultNamePattern() {
        return resultNamePattern;
    }

    public void setResultNamePattern(String resultNamePattern) {
        this.resultNamePattern = resultNamePattern;
    }

    public String getParamNamePattern() {
        return paramNamePattern;
    }

    public void setParamNamePattern(String paramNamePattern) {
        this.paramNamePattern = paramNamePattern;
    }
}
