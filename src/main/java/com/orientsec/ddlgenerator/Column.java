package com.orientsec.ddlgenerator;

import java.io.Serializable;
import java.util.List;

public class Column implements Serializable {
    private static final long serialVersionUID = 2831098838858706104L;

    private String name;

    private String type;

    private boolean required;

    private String defaultVal;

    private String note;

    private List<ValueEnum> valueEnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ValueEnum> getValueEnum() {
        return valueEnum;
    }

    public void setValueEnum(List<ValueEnum> valueEnum) {
        this.valueEnum = valueEnum;
    }

}
