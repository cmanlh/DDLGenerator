package com.lifeonwalden.codeGenerator.mybatis.impl;

import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.javaClass.impl.BeanGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;

public class BaseResultMapElementGenerator implements TableElementGenerator {

  public XmlElement getElement(Table table, Config config) {
    XmlElement element = new XmlElement(XMLTag.RESULT_MAP.getName());

    String className = config.getBeanInfo().getPackageName() + "." + BeanGeneratorImpl.getBeanName(table, config);

    element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "baseResultMap"));
    element.addAttribute(new Attribute(XMLAttribute.TYPE.getName(), className));

    IdElementGenerator idGenerator = new IdElementGenerator();
    ResultElementGenerator resultGenerator = new ResultElementGenerator();

    for (Column column : table.getColumns()) {
      if (ColumnConstraintEnum.PRIMARY_KEY == column.getConstraintType() || ColumnConstraintEnum.UNION_PRIMARY_KEY == column.getConstraintType()) {
        element.addElement(idGenerator.getElement(column, config));
      } else {
        element.addElement(resultGenerator.getElement(column, config));
      }
    }

    return element;
  }
}
