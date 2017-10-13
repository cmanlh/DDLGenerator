package com.lifeonwalden.codeGenerator.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias(value = "indexColumn")
public class IndexColumn implements Serializable {
    private static final long serialVersionUID = 3289465936180763099L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String order = "ASC";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
