package com.lifeonwalden.codeGenerator.util;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

import java.util.List;

public interface BatisMappingUtil {
    static void valueFragment(StringBuilder sb, Column column, String propertyName) {
        sb.append("#{").append(propertyName).append(", jdbcType=")
                .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
        if (null != column.getTypeHandler()) {
            sb.append(", typeHandler=").append(column.getTypeHandler());
        }
        sb.append("}");
    }

    static String conditionFragment(Column column, String propertyName, String start, String end, String operation) {
        StringBuilder conditionBuffer = new StringBuilder();
        conditionBuffer.append(start).append(column.getName()).append(operation);
        valueFragment(conditionBuffer, column, propertyName);
        conditionBuffer.append(end);

        return conditionBuffer.toString();
    }

    static XmlElement setConditionFragment(Column column, String propertyName) {
        XmlElement foreachElement = new XmlElement(XMLTag.FOR_EACH.getName());
        foreachElement.addAttribute(new Attribute(XMLAttribute.ITEM.getName(), "item"));
        foreachElement.addAttribute(new Attribute(XMLAttribute.COLLECTION.getName(), propertyName));
        foreachElement.addAttribute(new Attribute(XMLAttribute.OPEN.getName(), "("));
        foreachElement.addAttribute(new Attribute(XMLAttribute.SEPARATOR.getName(), ","));
        foreachElement.addAttribute(new Attribute(XMLAttribute.CLOSE.getName(), ")"));
        StringBuilder sb = new StringBuilder();
        valueFragment(sb, column, "item");
        foreachElement.addElement(new TextElement(sb.toString()));

        return foreachElement;
    }

    static XmlElement ifConditionFragment(Column column, String propertyName, String start, String end, String operation) {
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), propertyName.concat(" != null")));
        TextElement condition = new TextElement(conditionFragment(column, propertyName, start, end, operation));
        ifElement.addElement(condition);

        return ifElement;
    }

    static XmlElement ifSetFragment(Column column, String propertyName) {
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), propertyName.concat(" != null")));
        StringBuilder valueText = new StringBuilder();
        valueText.append(column.getName()).append(" = ");
        valueFragment(valueText, column, propertyName);
        valueText.append(",");
        ifElement.addElement(new TextElement(valueText.toString()));

        return ifElement;
    }

    static XmlElement ifDirectSetFragment(Column column, String propertyName, String directFlagProperty) {
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), directFlagProperty.concat(" != null AND ").concat(directFlagProperty).concat(" == true")));
        StringBuilder valueText = new StringBuilder();
        valueText.append(column.getName()).append(" = ");
        valueFragment(valueText, column, propertyName);
        valueText.append(",");
        ifElement.addElement(new TextElement(valueText.toString()));

        return ifElement;
    }

    static XmlElement ifSetConditionFragment(Column column, String propertyName, String flagProperty, String start, String end, String operation) {
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), flagProperty.concat(" != null and ").concat(flagProperty).concat(".size() > 0")));
        ifElement.addElement(new TextElement(start.concat(propertyName).concat(operation)));
        ifElement.addElement(setConditionFragment(column, flagProperty));

        return ifElement;
    }

    static String primaryKeyFragment(Table table) {
        StringBuilder sb = new StringBuilder();
        List<Column> primaryKey = table.getPrimaryColumns();
        if (null != primaryKey && primaryKey.size() > 0) {
            sb.append(" where ");
            for (Column column : primaryKey) {
                sb.append(column.getName()).append(" = ");
                BatisMappingUtil.valueFragment(sb, column, StringUtil.removeUnderline(column.getName()));
                sb.append(" AND ");
            }
            sb = sb.replace(sb.length() - 5, sb.length(), "");
        } else {
            throw new RuntimeException("Should not delete one record without a primary key.");
        }

        return sb.toString();
    }
}