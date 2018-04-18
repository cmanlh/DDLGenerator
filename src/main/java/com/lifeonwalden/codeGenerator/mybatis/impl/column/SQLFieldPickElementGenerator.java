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

public class SQLFieldPickElementGenerator implements TableElementGenerator {

    public static XmlElement setupTrimElement(Table table, String columnPrefix) {
        final String PICKED_COLUMN_LIST = "pickedColumnList";
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), PICKED_COLUMN_LIST.concat(" != null and ").concat(PICKED_COLUMN_LIST).concat(".size() > 0")));

        XmlElement foreachElement = new XmlElement(XMLTag.FOR_EACH.getName());
        foreachElement.addAttribute(new Attribute(XMLAttribute.ITEM.getName(), "item"));
        foreachElement.addAttribute(new Attribute(XMLAttribute.COLLECTION.getName(), PICKED_COLUMN_LIST));
        foreachElement.addAttribute(new Attribute(XMLAttribute.SEPARATOR.getName(), ","));
        ifElement.addElement(foreachElement);

        XmlElement chooseElement = new XmlElement(XMLTag.CHOOSE.getName());
        foreachElement.addElement(chooseElement);

        for (Column column : table.getColumns()) {
            String columnName = StringUtil.removeUnderline(column.getName());
            XmlElement whenElement = new XmlElement(XMLTag.WHEN.getName());
            whenElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), "item".concat(" == '").concat(columnName).concat("'")));

            String _columnPrefix = StringUtil.isNotBlank(columnPrefix) ? columnPrefix : "";
            TextElement fieldText = new TextElement(_columnPrefix.concat(column.getName()));
            whenElement.addElement(fieldText);

            chooseElement.addElement(whenElement);
        }

        return ifElement;
    }

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.FIELD_PICK));
        element.addElement(setupTrimElement(table, null));

        return element;
    }
}
