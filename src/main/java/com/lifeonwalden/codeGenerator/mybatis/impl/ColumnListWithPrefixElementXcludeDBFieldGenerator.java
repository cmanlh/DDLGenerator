package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class ColumnListWithPrefixElementXcludeDBFieldGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "columnListWithPrefixXcludeDBField"));
        String alias = table.getAlias();
        StringBuilder sb = new StringBuilder();
        for (Column column : table.getColumns()) {
            if (column.getName().equalsIgnoreCase("createTime") || column.getName().equalsIgnoreCase("createUser")
                    || column.getName().equalsIgnoreCase("updateTime") || column.getName().equalsIgnoreCase("updateUser")
                    || column.getName().equalsIgnoreCase("logicalDel")) {
                continue;
            }

            if (alias != null && alias.length() >= 1) {
                sb.append(alias).append(".").append(column.getName()).append(",");
            }
            else {
                sb.append("pre_").append(table.getName().toLowerCase()).append(".").append(column.getName()).append(",");
            }
        
        }

        if (sb.length() > 0) {
            element.addElement((new TextElement(sb.substring(0, sb.length() - 1))));
        }

        return element;
    }
}
