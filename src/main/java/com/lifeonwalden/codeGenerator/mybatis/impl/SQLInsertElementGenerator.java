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

public class SQLInsertElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "insertSQL"));

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(table.getName()).append(" (");

        int tmpSize = sb.length();
        for (Column column : table.getColumns()) {
            sb.append(column.getName()).append(",");
        }

        if (sb.length() > tmpSize) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }

        sb.append(") values(");

        tmpSize = sb.length();
        for (Column column : table.getColumns()) {
            sb.append("#{").append(StringUtil.removeUnderline(column.getName())).append(", jdbcType=")
                            .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());

            if (null != column.getTypeHandler()) {
                sb.append(", typeHandler=").append(column.getTypeHandler());
            }
            sb.append("},");
        }

        if (sb.length() > tmpSize) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        sb.append(")");

        TextElement sqlElement = new TextElement(sb.toString());
        element.addElement(sqlElement);

        return element;
    }
}
