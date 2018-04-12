package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLDynamicUpdateElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "dynamicUpdateSQL"));

        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(NameUtil.getTableName(table,config));

        element.addElement(new TextElement(sb.toString()));

        XmlElement trimElement = new XmlElement(XMLTag.TRIM.getName());
        trimElement.addAttribute(new Attribute(XMLAttribute.PREFIX.getName(), "set"));
        trimElement.addAttribute(new Attribute(XMLAttribute.SUFFIX_OVERRIDES.getName(), ","));
        element.addElement(trimElement);

        for (Column column : table.getColumns()) {
            if (column.getConstraintType() == ColumnConstraintEnum.PRIMARY_KEY
                    || column.getConstraintType() == ColumnConstraintEnum.UNION_PRIMARY_KEY) {
                continue;
            }
            XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
            ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), StringUtil.removeUnderline(column.getName()) + " != null"));

            StringBuilder setValueText = new StringBuilder();
            setValueText.append(column.getName()).append(" = ");
            setValueText.append("#{").append(StringUtil.removeUnderline(column.getName())).append(", jdbcType=")
                    .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
            if (null != column.getTypeHandler()) {
                setValueText.append(", typeHandler=").append(column.getTypeHandler());
            }
            setValueText.append("},");
            TextElement setValue = new TextElement(setValueText.toString());
            ifElement.addElement(setValue);

            trimElement.addElement(ifElement);
        }
        return element;
    }
}
