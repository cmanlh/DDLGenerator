package com.lifeonwalden.codeGenerator.mybatis.impl.condition;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLQueryConditionElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.QUERY_CONDITION));

        XmlElement trimElement = new XmlElement(XMLTag.TRIM.getName());
        trimElement.addAttribute(new Attribute(XMLAttribute.PREFIX.getName(), "WHERE"));
        trimElement.addAttribute(new Attribute(XMLAttribute.PREFIX_OVERRIDES.getName(), "AND"));
        element.addElement(trimElement);

        XmlElement includeCondition = new XmlElement(XMLTag.INCLUDE.getName());
        includeCondition.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), NameUtil.getNamespace(table, config).concat(".").concat(DefinedMappingID.CONDITION)));
        trimElement.addElement(includeCondition);

        return element;
    }
}
