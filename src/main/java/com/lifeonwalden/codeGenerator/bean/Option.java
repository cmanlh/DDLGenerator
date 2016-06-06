package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "option")
public class Option implements Serializable {
  private static final long serialVersionUID = -4913742886844635630L;

  @XStreamAsAttribute
  private String option;

  @XStreamAsAttribute
  private String value;

  public String getOption() {
    return option;
  }

  public void setOption(String option) {
    this.option = option;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
