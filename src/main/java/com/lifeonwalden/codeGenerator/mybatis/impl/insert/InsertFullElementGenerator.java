package com.lifeonwalden.codeGenerator.mybatis.impl.insert;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class InsertFullElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.INSERT.getName());
        String className = config.getBeanInfo().getPackageName() + "." + NameUtil.getResultBeanName(table, config);

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.INSERT));
        element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), className));

        XmlElement insertSQL = new XmlElement(XMLTag.INCLUDE.getName());
        insertSQL.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), DefinedMappingID.INSERT_SQL));
        element.addElement(insertSQL);

        return element;
    }
}
