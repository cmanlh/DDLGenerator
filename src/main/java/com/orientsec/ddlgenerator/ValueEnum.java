package com.orientsec.ddlgenerator;

import java.io.Serializable;

public class ValueEnum implements Serializable {
    private static final long serialVersionUID = -6209268326720898242L;

    private String value;

    private String desc;

    private String note;

    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
