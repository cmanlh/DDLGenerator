package com.lifeonwalden.codeGenerator.mybatis.impl;

import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.StringUtil;

public class RootElementGenerator implements TableElementGenerator {

	public XmlElement getElement(Table table, Config config) {
		XmlElement element = new XmlElement(XMLTag.ROOT.getName());

		String namespace = config.getDaoInfo().getPackageName() + "." + StringUtil.firstAlphToUpper(table.getName())
				+ "Mapper";

		element.addAttribute(new Attribute(XMLAttribute.NAMESPACE.getName(), namespace));

		return element;
	}
}
