package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.javaClass.impl.DAOGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLSelectElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "querySQL"));

        TextElement selectElement = new TextElement("SELECT");
        element.addElement(selectElement);

        XmlElement includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        String namespace = config.getDaoInfo().getPackageName() + "." + DAOGeneratorImpl.getDaoName(table, config);
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), namespace + ".baseColumnList"));
        element.addElement(includeElement);

        StringBuilder sb = new StringBuilder();
        sb.append("FROM ").append(NameUtil.getTableName(table,config));

        TextElement fromElement = new TextElement(sb.toString());
        element.addElement(fromElement);

        return element;
    }
}
