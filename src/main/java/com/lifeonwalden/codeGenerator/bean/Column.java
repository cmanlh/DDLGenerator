package com.lifeonwalden.codeGenerator.bean;

import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.util.List;

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

    // support db extra features
    @XStreamAsAttribute
    private String extra;

    @XStreamAsAttribute
    private String note;

    @XStreamAsAttribute
    private String optionRef;

    @XStreamAsAttribute
    private boolean enableIn;

    @XStreamAsAttribute
    private boolean enableLike;

    @XStreamAsAttribute
    private boolean enableNotIn;

    @XStreamAsAttribute
    private boolean enableNotLike;

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

    public boolean isEnableIn() {
        return enableIn;
    }

    public void setEnableIn(boolean enableIn) {
        this.enableIn = enableIn;
    }

    public boolean isEnableLike() {
        return enableLike;
    }

    public void setEnableLike(boolean enableLike) {
        this.enableLike = enableLike;
    }

    public boolean isEnableNotIn() {
        return enableNotIn;
    }

    public void setEnableNotIn(boolean enableNotIn) {
        this.enableNotIn = enableNotIn;
    }

    public boolean isEnableNotLike() {
        return enableNotLike;
    }

    public void setEnableNotLike(boolean enableNotLike) {
        this.enableNotLike = enableNotLike;
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Column copy() {
        Column column = new Column();
        column.setConstraintType(this.getConstraintType());
        column.setDefaultVal(this.getDefaultVal());
        column.setJavaType(this.getJavaType());
        column.setLength(this.getLength());
        column.setName(this.getName());
        column.setExtra(this.getExtra());
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
