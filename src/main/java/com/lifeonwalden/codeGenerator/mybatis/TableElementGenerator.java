package com.lifeonwalden.codeGenerator.mybatis;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import org.mybatis.generator.dom.xml.XmlElement;

public interface TableElementGenerator {

    XmlElement getElement(Table table, Config config);

}
