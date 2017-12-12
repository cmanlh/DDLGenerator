package com.lifeonwalden.codeGenerator.mybatis.impl.condition;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.constant.SpecialInnerSuffix;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.BatisMappingUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

public class SQLWildConditionElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.WILD_CONDITION));

        for (Column column : table.getColumns()) {
            String propertyName = StringUtil.removeUnderline(column.getName());
            if (column.isEnableIn()) {
                element.addElement(BatisMappingUtil.ifSetConditionFragment(column, propertyName.concat(SpecialInnerSuffix.IN), "AND ", "", " IN "));
            }
            if (column.isEnableNotIn()) {
                element.addElement(BatisMappingUtil.ifSetConditionFragment(column, propertyName.concat(SpecialInnerSuffix.NOT_IN), "AND ", "", " NOT IN "));
            }
            if (column.isEnableLike() && allowedLike(column)) {
                element.addElement(BatisMappingUtil.ifConditionFragment(column, propertyName.concat(SpecialInnerSuffix.LIKE), "AND ", "", " LIKE "));
            }
            if (column.isEnableNotLike() && allowedLike(column)) {
                element.addElement(BatisMappingUtil.ifConditionFragment(column, propertyName.concat(SpecialInnerSuffix.NOT_LIKE), "AND ", "", " NOT LIKE "));
            }
        }

        return element;
    }

    public boolean allowedLike(Column column) {
        String javaType = column.getJavaType();
        if (!StringUtil.isNotBlank(javaType)) {
            javaType = JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getJavaType();
        }

        return "String".equalsIgnoreCase(javaType) || "java.lang.String".equalsIgnoreCase(javaType);
    }
}
