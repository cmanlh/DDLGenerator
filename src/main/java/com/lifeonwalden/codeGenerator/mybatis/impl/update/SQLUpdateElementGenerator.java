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

public class SQLUpdateElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.UPDATE_SQL));

        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(NameUtil.getTableName(table,config)).append(" set ");

        int tmpSize = sb.length();
        for (Column column : table.getColumns()) {
            if (column.getConstraintType() == ColumnConstraintEnum.PRIMARY_KEY
                    || column.getConstraintType() == ColumnConstraintEnum.UNION_PRIMARY_KEY) {
                continue;
            }
            sb.append(column.getName()).append(" = ");
            BatisMappingUtil.valueFragment(sb, column, StringUtil.removeUnderline(column.getName()));
            sb.append(",");
        }

        if (sb.length() > tmpSize) {
            element.addElement((new TextElement(sb.substring(0, sb.length() - 1))));
        }

        return element;
    }
}
