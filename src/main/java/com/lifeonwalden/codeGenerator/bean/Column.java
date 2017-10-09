package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;

import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias(value = "column")
public class Column implements Serializable {
    private static final long serialVersionUID = -6061857023223962144L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String type;

    @XStreamAsAttribute
    private String javaType;

    @XStreamAsAttribute
    private String typeHandler;

    @XStreamAsAttribute
    private String length;

    @XStreamAsAttribute
    private boolean required = false;

    @XStreamAsAttribute
    private String defaultVal;

    @XStreamAsAttribute
    private String note;

    @XStreamAsAttribute
    private String optionRef;

    @XStreamOmitField
    private EnumConst optionRefObj;

    @XStreamOmitField
    private ColumnConstraintEnum constraintType;

    private List<ValueEnum> options;

    @XStreamOmitField
    private Table table;

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

    public List<ValueEnum> getOptions() {
        return options;
    }

    public void setOptions(List<ValueEnum> options) {
        this.options = options;
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public String getOptionRef() {
        return optionRef;
    }

    public void setOptionRef(String optionRef) {
        this.optionRef = optionRef;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public ColumnConstraintEnum getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ColumnConstraintEnum constraintType) {
        this.constraintType = constraintType;
    }

    public EnumConst getOptionRefObj() {
        return optionRefObj;
    }

    public void setOptionRefObj(EnumConst optionRefObj) {
        this.optionRefObj = optionRefObj;
    }

    public Column copy() {
        Column column = new Column();
        column.setConstraintType(this.getConstraintType());
        column.setDefaultVal(this.getDefaultVal());
        column.setJavaType(this.getJavaType());
        column.setLength(this.getLength());
        column.setName(this.getName());
        column.setNote(this.getNote());
        column.setOptionRef(this.getOptionRef());
        column.setOptionRefObj(this.getOptionRefObj());
        column.setOptions(this.getOptions());
        column.setRequired(this.isRequired());
        column.setType(this.getType());
        column.setTypeHandler(this.getTypeHandler());

        return column;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
