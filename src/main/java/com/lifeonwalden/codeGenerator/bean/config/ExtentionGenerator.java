package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;
import java.util.List;

import com.lifeonwalden.codeGenerator.bean.Option;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "extentionGenerator")
public class ExtentionGenerator implements Serializable {
  private static final long serialVersionUID = -5514190451262434016L;

  @XStreamAsAttribute
  private String generator;

  private List<Option> options;

  public String getGenerator() {
    return generator;
  }

  public void setGenerator(String generator) {
    this.generator = generator;
  }

  public List<Option> getOptions() {
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

}
