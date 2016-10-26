package com.lifeonwalden.codeGenerator.mybatis.impl;

import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.javaClass.impl.BeanGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;

public class InsertFullElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.INSERT.getName());
        String className = config.getBeanInfo().getPackageName() + "." + BeanGeneratorImpl.getResultBeanName(table, config);

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "insert"));
        element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), className));

        XmlElement includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), "insertSQL"));
        element.addElement(includeElement);

        return element;
    }
}
