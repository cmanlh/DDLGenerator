package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class KeyValue implements Serializable {
  private static final long serialVersionUID = -7862910746690794765L;
  @XStreamAsAttribute
  private String key;

  @XStreamAsAttribute
  private String value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
