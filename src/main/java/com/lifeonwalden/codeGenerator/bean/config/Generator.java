package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;
import java.util.List;

import com.lifeonwalden.codeGenerator.bean.Database;
import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value = "generator")
public class Generator implements Serializable {
  private static final long serialVersionUID = 9218661364872624451L;

  private Config config;

  private Database database;

  private List<EnumConst> constPool;

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  public Database getDatabase() {
    return database;
  }

  public void setDatabase(Database database) {
    this.database = database;
  }

  public List<EnumConst> getConstPool() {
    return constPool;
  }

  public void setConstPool(List<EnumConst> constPool) {
    this.constPool = constPool;
  }
}
