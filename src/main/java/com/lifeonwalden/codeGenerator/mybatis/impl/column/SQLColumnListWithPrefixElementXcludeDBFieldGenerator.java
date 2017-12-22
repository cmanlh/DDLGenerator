package com.lifeonwalden.codeGenerator.mybatis.impl.column;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLColumnListWithPrefixElementXcludeDBFieldGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.COLUMN_LIST_WITH_PREFIX_XCLUDE_DB_FIELD));

        String alias = table.getAlias();
        TextElement columnListText;
        if (StringUtil.isNotBlank(alias)) {
            columnListText = SQLColumnListElementXcludeDBFieldGenerator.setupColumnListText(table, alias.concat("."));
        } else {
            columnListText = SQLColumnListElementXcludeDBFieldGenerator.setupColumnListText(table, NameUtil.getColumnPrefix(table));
        }
        if (null != columnListText) {
            element.addElement(columnListText);
        }

        return element;
    }
}
