package com.lifeonwalden.codeGenerator.mybatis.impl.select;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class SelectAllElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SELECT.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.SELECT_ALL));
        element.addAttribute(new Attribute(XMLAttribute.RESULT_MAP.getName(), DefinedMappingID.BASE_RESULT_MAP));

        XmlElement includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), DefinedMappingID.QUERY_SQL));
        element.addElement(includeElement);

        return element;
    }
}
