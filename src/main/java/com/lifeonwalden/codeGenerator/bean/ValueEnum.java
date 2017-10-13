package com.lifeonwalden.codeGenerator.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias(value = "valueEnum")
public class ValueEnum implements Serializable {
    private static final long serialVersionUID = -3675888410042396179L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private int value;

    @XStreamAsAttribute
    private String alias;

    @XStreamAsAttribute
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
