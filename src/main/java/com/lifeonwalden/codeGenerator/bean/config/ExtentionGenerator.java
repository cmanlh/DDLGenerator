package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class ExtentionGenerator implements Serializable {
  private static final long serialVersionUID = -5514190451262434016L;

  @XStreamAsAttribute
  private String generator;

  private List<KeyValue> properties;

  public String getGenerator() {
    return generator;
  }

  public void setGenerator(String generator) {
    this.generator = generator;
  }

  public List<KeyValue> getProperties() {
    return properties;
  }

  public void setProperties(List<KeyValue> properties) {
    this.properties = properties;
  }
}
