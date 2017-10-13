package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias(value = "constraint")
public class Constraint implements Serializable {
    private static final long serialVersionUID = 2268185885474984309L;

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
