package com.lifeonwalden.codeGenerator.mybatis.impl.resultmap;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.XmlElement;

import java.util.ArrayDeque;

public class BaseResultMapElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.RESULT_MAP.getName());

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), DefinedMappingID.BASE_RESULT_MAP));
        element.addAttribute(new Attribute(XMLAttribute.TYPE.getName(), NameUtil.getClassName(table, config)));

        IdElementGenerator idGenerator = new IdElementGenerator();
        ResultElementGenerator resultGenerator = new ResultElementGenerator();

        ArrayDeque<XmlElement> resultQueue = new ArrayDeque<>(table.getColumns().size());
        for (Column column : table.getColumns()) {
            if (ColumnConstraintEnum.PRIMARY_KEY == column.getConstraintType() || ColumnConstraintEnum.UNION_PRIMARY_KEY == column.getConstraintType()) {
                resultQueue.addFirst(idGenerator.getElement(column, config));
            } else {
                resultQueue.addLast(resultGenerator.getElement(column, config));
            }
        }

        for (XmlElement e : resultQueue) {
            element.addElement(e);
        }

        return element;
    }
}
