package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "dbTable")
public class DBTable implements Serializable {
    private static final long serialVersionUID = 1524016033553064655L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String note;

    private List<Column> extProps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public List<Column> getExtProps() {
        return extProps;
    }

    public void setExtProps(List<Column> extProps) {
        this.extProps = extProps;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
