package com.lifeonwalden.codeGenerator.mybatis.impl.column;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.SpecialInnerSuffix;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLFieldPickWithPrefixElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "fieldPick"));

        XmlElement trimElement = new XmlElement(XMLTag.TRIM.getName());
        trimElement.addAttribute(new Attribute(XMLAttribute.SUFFIX_OVERRIDES.getName(), ","));
        element.addElement(trimElement);

        String alias = table.getAlias();
        for (Column column : table.getColumns()) {
            String columnName = StringUtil.removeUnderline(column.getName());
            XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
            ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), columnName.concat(SpecialInnerSuffix.PICKED).concat(" != null")));

            StringBuilder fieldText = new StringBuilder();
            if (alias != null && alias.length() > 0) {
                fieldText.append(alias).append(".").append(column.getName()).append(",");
            } else {
                fieldText.append("pre_").append(table.getName().toLowerCase()).append(".").append(column.getName()).append(",");
            }
            TextElement fieldTextElement = new TextElement(fieldText.toString());
            ifElement.addElement(fieldTextElement);

            trimElement.addElement(ifElement);
        }
        return element;
    }
}
