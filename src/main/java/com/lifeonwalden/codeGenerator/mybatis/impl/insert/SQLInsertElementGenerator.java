package com.lifeonwalden.codeGenerator.mybatis.impl.insert;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.BatisMappingUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLInsertElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.INSERT_SQL));

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
            BatisMappingUtil.valueFragment(sb, column, StringUtil.removeUnderline(column.getName()));
            sb.append(",");
        }

        if (sb.length() > tmpSize) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        sb.append(")");

        element.addElement(new TextElement(sb.toString()));

        return element;
    }
}
