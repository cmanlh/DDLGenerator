package com.lifeonwalden.codeGenerator.mybatis.impl;

import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.mybatis.ColumnElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;

public class IdArgElementGenerator implements ColumnElementGenerator {

  public XmlElement getElement(Column column, Config config) {
    XmlElement element = new XmlElement(XMLTag.ID_ARG.getName());

    element.addAttribute(new Attribute(XMLAttribute.COLUMN.getName(), column.getName()));
    if (null != column.getJavaType()) {
      element.addAttribute(new Attribute(XMLAttribute.JAVA_TYPE.getName(), column.getJavaType()));
    } else {
      element.addAttribute(new Attribute(XMLAttribute.JAVA_TYPE.getName(),
          JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getJavaType()));
    }
    return element;
  }
}
