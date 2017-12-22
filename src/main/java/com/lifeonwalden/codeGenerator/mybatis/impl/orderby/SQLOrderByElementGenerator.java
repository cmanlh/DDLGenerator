package com.lifeonwalden.codeGenerator.mybatis.impl.orderby;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLOrderByElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.ORDER_BY_SQL));

        String orderByProperty = "orderBy";

        XmlElement foreachElement = new XmlElement(XMLTag.FOR_EACH.getName());
        foreachElement.addAttribute(new Attribute(XMLAttribute.ITEM.getName(), "item"));
        foreachElement.addAttribute(new Attribute(XMLAttribute.COLLECTION.getName(), orderByProperty));
        foreachElement.addAttribute(new Attribute(XMLAttribute.OPEN.getName(), " ORDER BY "));
        foreachElement.addAttribute(new Attribute(XMLAttribute.SEPARATOR.getName(), ","));
        foreachElement.addElement(new TextElement("${item.columnName} ${item.order}"));

        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), orderByProperty.concat(" != null and ").concat(orderByProperty).concat(".size() > 0")));
        ifElement.addElement(foreachElement);

        element.addElement(ifElement);

        return element;
    }
}
