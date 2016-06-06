package com.lifeonwalden.codeGenerator.mybatis;

import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface TableElementGenerator {

	public XmlElement getElement(Table table, Config config);

}
