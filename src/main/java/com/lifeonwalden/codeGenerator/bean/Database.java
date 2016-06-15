package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias(value = "database")
public class Database implements Serializable {
  private static final long serialVersionUID = 3039704951463778462L;

  @XStreamAsAttribute
  private String name;

  @XStreamAsAttribute
  private String schema;

  @XStreamAsAttribute
  private String note;

  @XStreamAsAttribute
  private String generator;

  private List<EnumConst> constPool;

  @XStreamOmitField
  private Map<String, EnumConst> constMapping;

  private List<Table> tables;

  private List<Column> dbFields;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public List<EnumConst> getConstPool() {
    return constPool;
  }

  public void setConstPool(List<EnumConst> constPool) {
    this.constPool = constPool;
  }

  public Map<String, EnumConst> getConstMapping() {
    return constMapping;
  }

  public void setConstMapping(Map<String, EnumConst> constMapping) {
    this.constMapping = constMapping;
  }

  public List<Table> getTables() {
    return tables;
  }

  public void setTables(List<Table> tables) {
    this.tables = tables;

  }

  public List<Column> getDbFields() {
    return dbFields;
  }

  public void setDbFields(List<Column> dbFields) {
    this.dbFields = dbFields;
  }

  public String getGenerator() {
    return generator;
  }

  public void setGenerator(String generator) {
    this.generator = generator;
  }
}
