package com.lifeonwalden.codeGenerator.mybatis;

import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface ColumnElementGenerator {

    XmlElement getElement(Column column, Config config);

}
