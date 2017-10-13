package com.lifeonwalden.codeGenerator.bean.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@XStreamAlias(value = "config")
public class Config implements Serializable {
    private static final long serialVersionUID = 1539262741181799480L;

    @XStreamAsAttribute
    private String outputLocation;

    @XStreamAsAttribute
    private String encoding;

    private ConstInfo constInfo;

    private DAOInfo daoInfo;

    private BeanInfo beanInfo;

    private MybatisInfo mybatisInfo;

    private List<ExtentionGenerator> extentions;

    @XStreamOmitField
    private Map<String, ExtentionGenerator> extentionMapping;

    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public String getEncoding() {
        return null == encoding ? "UTF-8" : encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public ConstInfo getConstInfo() {
        return constInfo;
    }

    public void setConstInfo(ConstInfo constInfo) {
        this.constInfo = constInfo;
    }

    public DAOInfo getDaoInfo() {
        return daoInfo;
    }

    public void setDaoInfo(DAOInfo daoInfo) {
        this.daoInfo = daoInfo;
    }

    public BeanInfo getBeanInfo() {
        return beanInfo;
    }

    public void setBeanInfo(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

    public MybatisInfo getMybatisInfo() {
        return mybatisInfo;
    }

    public void setMybatisInfo(MybatisInfo mybatisInfo) {
        this.mybatisInfo = mybatisInfo;
    }

    public List<ExtentionGenerator> getExtentions() {
        return extentions;
    }

    public void setExtentions(List<ExtentionGenerator> extentions) {
        this.extentions = extentions;
    }

    public Map<String, ExtentionGenerator> getExtentionMapping() {
        return extentionMapping;
    }

    public void setExtentionMapping(Map<String, ExtentionGenerator> extentionMapping) {
        this.extentionMapping = extentionMapping;
    }
}
