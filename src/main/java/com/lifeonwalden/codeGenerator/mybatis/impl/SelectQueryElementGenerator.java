package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.javaClass.impl.BeanGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class SelectQueryElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SELECT.getName());
        String className = config.getBeanInfo().getPackageName() + "." + BeanGeneratorImpl.getResultBeanName(table, config);

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "select"));
        element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), className));
        element.addAttribute(new Attribute(XMLAttribute.RESULT_MAP.getName(), "baseResultMap"));

        XmlElement includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), "querySQL"));
        element.addElement(includeElement);

        includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), "queryCondition"));
        element.addElement(includeElement);

        return element;
    }
}
