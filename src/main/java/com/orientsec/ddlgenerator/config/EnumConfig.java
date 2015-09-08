package com.orientsec.ddlgenerator.config;

import java.io.Serializable;
import java.util.List;

import com.orientsec.ddlgenerator.ValueEnum;

public class EnumConfig implements Serializable {
    private static final long serialVersionUID = 6465057479151969210L;

    private String name;

    private String desc;

    private List<ValueEnum> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ValueEnum> getOptions() {
        return options;
    }

    public void setOptions(List<ValueEnum> options) {
        this.options = options;
    }
}
