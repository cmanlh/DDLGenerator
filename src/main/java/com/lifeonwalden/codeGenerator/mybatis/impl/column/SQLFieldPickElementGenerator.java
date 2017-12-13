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

public class SQLFieldPickElementGenerator implements TableElementGenerator {

    public static XmlElement setupTrimElement(Table table, String columnPrefix) {
        XmlElement trimElement = new XmlElement(XMLTag.TRIM.getName());
        trimElement.addAttribute(new Attribute(XMLAttribute.SUFFIX_OVERRIDES.getName(), ","));

        if (StringUtil.isNotBlank(columnPrefix)) {
            for (Column column : table.getColumns()) {
                String columnName = StringUtil.removeUnderline(column.getName());
                XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
                String pickedName = columnName.concat(SpecialInnerSuffix.PICKED);
                ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), pickedName.concat(" != null AND ").concat(pickedName).concat(" == true")));

                TextElement fieldText = new TextElement(columnPrefix.concat(column.getName().concat(",")));
                ifElement.addElement(fieldText);

                trimElement.addElement(ifElement);
            }
        } else {
            for (Column column : table.getColumns()) {
                String columnName = StringUtil.removeUnderline(column.getName());
                String pickedName = columnName.concat(SpecialInnerSuffix.PICKED);
                XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
                ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), pickedName.concat(" != null AND ").concat(pickedName).concat(" == true")));

                TextElement fieldText = new TextElement(column.getName().concat(","));
                ifElement.addElement(fieldText);

                trimElement.addElement(ifElement);
            }
        }

        return trimElement;
    }

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.FIELD_PICK));
        element.addElement(setupTrimElement(table, null));

        return element;
    }
}
