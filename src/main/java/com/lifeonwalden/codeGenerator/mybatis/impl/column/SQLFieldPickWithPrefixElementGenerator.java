package com.lifeonwalden.codeGenerator.mybatis.impl.column;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.constant.SpecialInnerSuffix;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLFieldPickWithPrefixElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.FIELD_PICK_WITH_PREFIX));

        String alias = table.getAlias();
        if (StringUtil.isNotBlank(alias)) {
            element.addElement(SQLFieldPickElementGenerator.setupTrimElement(table, alias.concat(".")));
        } else {
            element.addElement(SQLFieldPickElementGenerator.setupTrimElement(table, "pre_".concat(table.getName().toLowerCase()).concat(".")));
        }
        return element;
    }
}
