package com.lifeonwalden.codeGenerator.bean.config;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias(value = "config")
public class Config implements Serializable {
  private static final long serialVersionUID = 1539262741181799480L;

  @XStreamOmitField
  private String outputLocation;

  private EnumInfo enumInfo;

  private DAOInfo daoInfo;

  private DTOInfo dtoInfo;

  private MybatisInfo mybatisInfo;

  public String getOutputLocation() {
    return outputLocation;
  }

  public void setOutputLocation(String outputLocation) {
    this.outputLocation = outputLocation;
  }

  public EnumInfo getEnumInfo() {
    return enumInfo;
  }

  public void setEnumInfo(EnumInfo enumInfo) {
    this.enumInfo = enumInfo;
  }

  public DAOInfo getDaoInfo() {
    return daoInfo;
  }

  public void setDaoInfo(DAOInfo daoInfo) {
    this.daoInfo = daoInfo;
  }

  public DTOInfo getDtoInfo() {
    return dtoInfo;
  }

  public void setDtoInfo(DTOInfo dtoInfo) {
    this.dtoInfo = dtoInfo;
  }

  public MybatisInfo getMybatisInfo() {
    return mybatisInfo;
  }

  public void setMybatisInfo(MybatisInfo mybatisInfo) {
    this.mybatisInfo = mybatisInfo;
  }
}
