package com.lifeonwalden.codeGenerator.mybatis.impl;

import java.util.List;

import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.StringUtil;

public class UpdateFullElementGenerator implements TableElementGenerator {

  public XmlElement getElement(Table table, Config config) {
    XmlElement element = new XmlElement(XMLTag.UPDATE.getName());

    String className =
        config.getDtoInfo().getPackageName() + "." + StringUtil.firstAlphToUpper(table.getName()) + "DTO";

    element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "update"));
    element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), className));

    XmlElement includeElement = new XmlElement(XMLTag.INCLUDE.getName());
    includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), "updateSQL"));
    element.addElement(includeElement);

    StringBuilder sb = new StringBuilder();
    List<Column> primaryKey = table.getPrimaryColumns();
    if (null != primaryKey && primaryKey.size() > 0) {
      sb.append("where ");
      for (Column column : primaryKey) {
        sb.append(column.getName()).append(" = ");
        sb.append("#{").append(column.getName()).append(", jdbcType=")
            .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
        if (null != column.getTypeHandler()) {
          sb.append(", typeHandler=").append(column.getTypeHandler());
        }
        sb.append("} AND ");
      }
      sb = sb.replace(sb.length() - 5, sb.length(), "");
    } else {
      throw new RuntimeException("Should not update one record without a primary key.");
    }

    TextElement fromElement = new TextElement(sb.toString());
    element.addElement(fromElement);

    return element;
  }
}
