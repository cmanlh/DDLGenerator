package com.lifeonwalden.codeGenerator.mybatis.impl.update;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.BatisMappingUtil;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLDirectUpdateElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.DIRECT_UPDATE_SQL));

        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(NameUtil.getTableName(table,config));
        element.addElement(new TextElement(sb.toString()));

        final String PICKED_COLUMN_LIST = "pickedColumnList";
        XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
        ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), PICKED_COLUMN_LIST.concat(" != null and ").concat(PICKED_COLUMN_LIST).concat(".size() > 0")));
        element.addElement(ifElement);

        XmlElement foreachElement = new XmlElement(XMLTag.FOR_EACH.getName());
        foreachElement.addAttribute(new Attribute(XMLAttribute.ITEM.getName(), "item"));
        foreachElement.addAttribute(new Attribute(XMLAttribute.COLLECTION.getName(), PICKED_COLUMN_LIST));
        foreachElement.addAttribute(new Attribute(XMLAttribute.OPEN.getName(), " SET "));
        foreachElement.addAttribute(new Attribute(XMLAttribute.SEPARATOR.getName(), ","));
        ifElement.addElement(foreachElement);

        XmlElement chooseElement = new XmlElement(XMLTag.CHOOSE.getName());
        foreachElement.addElement(chooseElement);

        for (Column column : table.getColumns()) {
            if (column.getConstraintType() == ColumnConstraintEnum.PRIMARY_KEY
                    || column.getConstraintType() == ColumnConstraintEnum.UNION_PRIMARY_KEY) {
                continue;
            }

            String propertyName = StringUtil.removeUnderline(column.getName());
            XmlElement whenElement = new XmlElement(XMLTag.WHEN.getName());
            whenElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), "item".concat(" == '").concat(propertyName).concat("'")));
            StringBuilder valueText = new StringBuilder();
            valueText.append(column.getName()).append(" = ");
            BatisMappingUtil.valueFragment(valueText, column, propertyName);
            whenElement.addElement(new TextElement(valueText.toString()));
            chooseElement.addElement(whenElement);
        }

        return element;
    }
}
