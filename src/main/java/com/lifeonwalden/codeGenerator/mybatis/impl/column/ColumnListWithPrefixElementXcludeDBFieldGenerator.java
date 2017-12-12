package com.lifeonwalden.codeGenerator.mybatis.impl.column;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class ColumnListWithPrefixElementXcludeDBFieldGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.COLUMN_LIST_WITH_PREFIX_XCLUDE_DB_FIELD));

        String alias = table.getAlias();
        TextElement columnListText;
        if (StringUtil.isNotBlank(alias)) {
            columnListText = ColumnListElementXcludeDBFieldGenerator.setupColumnListText(table, alias.concat("."));
        } else {
            columnListText = ColumnListElementXcludeDBFieldGenerator.setupColumnListText(table, "pre_".concat(table.getName().toLowerCase()).concat("."));
        }
        if (null != columnListText) {
            element.addElement(columnListText);
        }

        return element;
    }
}
