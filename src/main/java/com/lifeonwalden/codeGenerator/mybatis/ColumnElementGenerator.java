package com.lifeonwalden.codeGenerator.mybatis;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import org.mybatis.generator.dom.xml.XmlElement;

public interface ColumnElementGenerator {

    XmlElement getElement(Column column, Config config);

}
