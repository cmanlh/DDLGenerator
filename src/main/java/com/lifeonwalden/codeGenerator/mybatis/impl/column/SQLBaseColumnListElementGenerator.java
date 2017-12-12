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

import javax.xml.soap.Text;

public class SQLBaseColumnListElementGenerator implements TableElementGenerator {

    public static TextElement setupColumnListText(Table table, String columnPrefix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotBlank(columnPrefix)) {
            for (Column column : table.getColumns()) {
                sb.append(columnPrefix).append(column.getName()).append(",");
            }
        } else {
            for (Column column : table.getColumns()) {
                sb.append(column.getName()).append(",");
            }
        }

        if (sb.length() > 0) {
            return new TextElement(sb.substring(0, sb.length() - 1));
        } else {
            return null;
        }
    }

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SQL.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.BASE_COLUMN_LIST));

        TextElement columnListText = setupColumnListText(table, null);
        if (null != columnListText) {
            element.addElement(columnListText);
        }

        return element;
    }
}
