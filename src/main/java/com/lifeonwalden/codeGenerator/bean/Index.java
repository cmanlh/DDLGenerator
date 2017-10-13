package com.lifeonwalden.codeGenerator.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.util.List;

@XStreamAlias(value = "index")
public class Index implements Serializable {
    private static final long serialVersionUID = -8939930815910361456L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String type;

    private List<IndexColumn> columns;

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

    public List<IndexColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<IndexColumn> columns) {
        this.columns = columns;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
