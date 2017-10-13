package com.lifeonwalden.codeGenerator.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@XStreamAlias(value = "table")
public class Table implements Serializable {
    private static final long serialVersionUID = 1698905977625460915L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String note;

    @XStreamAsAttribute
    private Boolean addDBFields = true;

    private List<Column> columns;

    private List<Column> extProps;

    @XStreamOmitField
    private Map<String, Column> columnMapping;

    @XStreamOmitField
    private List<Column> primaryColumns;

    private List<Constraint> constraints;

    private List<Index> indexs;

    private List<Option> options;

    @XStreamOmitField
    private Database database;

    public List<Column> getExtProps() {
        return extProps;
    }

    public void setExtProps(List<Column> extProps) {
        this.extProps = extProps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Map<String, Column> getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(Map<String, Column> columnMapping) {
        this.columnMapping = columnMapping;
    }

    public List<Column> getPrimaryColumns() {
        return primaryColumns;
    }

    public void setPrimaryColumns(List<Column> primaryColumns) {
        this.primaryColumns = primaryColumns;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public List<Index> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<Index> indexs) {
        this.indexs = indexs;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Boolean getAddDBFields() {
        return addDBFields;
    }

    public void setAddDBFields(Boolean addDBFields) {
        this.addDBFields = addDBFields;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
