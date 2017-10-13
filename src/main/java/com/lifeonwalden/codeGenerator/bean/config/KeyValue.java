package com.lifeonwalden.codeGenerator.bean.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias(value = "keyValue")
public class KeyValue implements Serializable {
    private static final long serialVersionUID = -7862910746690794765L;
    @XStreamAsAttribute
    private String key;

    @XStreamAsAttribute
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
