package com.lifeonwalden.codeGenerator.mybatis.impl.delete;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class RemoveElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.DELETE.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.REMOVE));
        element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), NameUtil.getClassName(table, config)));

        XmlElement deleteSQL = new XmlElement(XMLTag.INCLUDE.getName());
        deleteSQL.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), DefinedMappingID.DELETE_SQL));
        element.addElement(deleteSQL);

        XmlElement queryCondition = new XmlElement(XMLTag.INCLUDE.getName());
        queryCondition.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), DefinedMappingID.QUERY_CONDITION));
        element.addElement(queryCondition);

        return element;
    }
}
