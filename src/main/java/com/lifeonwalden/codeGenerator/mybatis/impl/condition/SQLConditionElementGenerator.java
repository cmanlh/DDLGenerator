package com.lifeonwalden.codeGenerator.mybatis.impl.condition;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.constant.SpecialInnerSuffix;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.BatisMappingUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.lifeonwalden.codeGenerator.util.TableInfoUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLConditionElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.CONDITION));

        for (Column column : table.getColumns()) {
            String propertyName = StringUtil.removeUnderline(column.getName());
            element.addElement(BatisMappingUtil.ifConditionFragment(column, propertyName, "AND ", "", " = "));

            if (TableInfoUtil.allowedDateRange(column)) {
                dateFieldExtension(column, element);
            }
        }

        return element;
    }

    private void dateFieldExtension(Column column, XmlElement element) {
        String propertyName = StringUtil.removeUnderline(column.getName());
        element.addElement(BatisMappingUtil.ifConditionFragment(column, propertyName.concat(SpecialInnerSuffix.START), "<![CDATA[ AND ", " ]]>", " >= "));
        element.addElement(BatisMappingUtil.ifConditionFragment(column, propertyName.concat(SpecialInnerSuffix.END), "<![CDATA[ AND ", " ]]>", " <= "));
    }
}
