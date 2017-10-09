package com.lifeonwalden.codeGenerator.mybatis.impl;

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

public class SQLQueryConditionElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "queryCondition"));

        XmlElement trimElement = new XmlElement(XMLTag.TRIM.getName());
        trimElement.addAttribute(new Attribute(XMLAttribute.PREFIX.getName(), "WHERE"));
        trimElement.addAttribute(new Attribute(XMLAttribute.PREFIX_OVERRIDES.getName(), "AND"));
        element.addElement(trimElement);

        for (Column column : table.getColumns()) {
            XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
            ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), StringUtil.removeUnderline(column.getName()) + " != null"));

            StringBuilder setValueText = new StringBuilder();
            setValueText.append("AND ").append(column.getName()).append(" = ");
            setValueText.append("#{").append(StringUtil.removeUnderline(column.getName())).append(", jdbcType=")
                    .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
            if (null != column.getTypeHandler()) {
                setValueText.append(", typeHandler=").append(column.getTypeHandler());
            }
            setValueText.append("}");
            TextElement setValue = new TextElement(setValueText.toString());
            ifElement.addElement(setValue);

            trimElement.addElement(ifElement);

            JdbcTypeEnum jdbcType = JdbcTypeEnum.nameOf(column.getType().toUpperCase());
            if (jdbcType.equals(JdbcTypeEnum.DATE) || jdbcType.equals(JdbcTypeEnum.DATETIME) || jdbcType.equals(JdbcTypeEnum.TIME)
                    || jdbcType.equals(JdbcTypeEnum.TIMESTAMP)) {
                dateFieldExtension(column, trimElement);
            }
        }

        return element;
    }

    private void dateFieldExtension(Column column, XmlElement trimElement) {
        // start part
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), StringUtil.removeUnderline(column.getName()) + "Start != null"));

        StringBuilder setValueText = new StringBuilder();
        setValueText.append("<![CDATA[ AND ").append(column.getName()).append(" >= ");
        setValueText.append("#{").append(StringUtil.removeUnderline(column.getName())).append("Start, jdbcType=")
                .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
        if (null != column.getTypeHandler()) {
            setValueText.append(", typeHandler=").append(column.getTypeHandler());
        }
        setValueText.append("} ]]>");
        TextElement setValue = new TextElement(setValueText.toString());
        ifElement.addElement(setValue);

        trimElement.addElement(ifElement);

        // end part
        ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), StringUtil.removeUnderline(column.getName()) + "End != null"));

        setValueText = new StringBuilder();
        setValueText.append("<![CDATA[ AND ").append(column.getName()).append(" <= ");
        setValueText.append("#{").append(StringUtil.removeUnderline(column.getName())).append("End, jdbcType=")
                .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
        if (null != column.getTypeHandler()) {
            setValueText.append(", typeHandler=").append(column.getTypeHandler());
        }
        setValueText.append("} ]]>");
        setValue = new TextElement(setValueText.toString());
        ifElement.addElement(setValue);

        trimElement.addElement(ifElement);
    }
}
