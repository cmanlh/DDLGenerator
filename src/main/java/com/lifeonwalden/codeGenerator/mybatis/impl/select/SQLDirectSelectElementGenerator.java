package com.lifeonwalden.codeGenerator.mybatis.impl.select;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLDirectSelectElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.DIRECT_QUERY_SQL));

        TextElement selectElement = new TextElement("SELECT");
        element.addElement(selectElement);

        XmlElement includeColumnList = new XmlElement(XMLTag.INCLUDE.getName());
        includeColumnList.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), NameUtil.getNamespace(table, config).concat(".").concat(DefinedMappingID.FIELD_PICK)));
        element.addElement(includeColumnList);

        StringBuilder sb = new StringBuilder();
        sb.append("FROM ").append(NameUtil.getTableName(table,config));

        TextElement fromElement = new TextElement(sb.toString());
        element.addElement(fromElement);

        return element;
    }
}
