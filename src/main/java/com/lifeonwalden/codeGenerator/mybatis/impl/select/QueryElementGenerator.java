package com.lifeonwalden.codeGenerator.mybatis.impl.select;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

public class QueryElementGenerator implements TableElementGenerator {
    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.SELECT.getName());
        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "query"));
        element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), "com.orientsec.sql.Condition"));
        element.addAttribute(new Attribute(XMLAttribute.RESULT_MAP.getName(), DefinedMappingID.BASE_RESULT_MAP));
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT <if test=\"top>=1\">TOP (#{top})</if><if test=\"distinctable\"> DISTINCT </if><include refid=\"com.orientsec.common.mapper.sqlColumns\" />");
        TextElement txtElement = new TextElement(sb.toString());
        element.addElement(txtElement);

        sb.setLength(0);
        sb.append("FROM ").append(table.getName()).append(" <include refid=\"com.orientsec.common.mapper.join\" />");
        txtElement = new TextElement(sb.toString());
        element.addElement(txtElement);

        sb.setLength(0);
        sb.append("<where> ").append("<include refid=\"com.orientsec.common.mapper.condition\" />").append("</where>");
        txtElement = new TextElement(sb.toString());
        element.addElement(txtElement);

        XmlElement includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), "com.orientsec.common.mapper.groupBy"));
        element.addElement(includeElement);

        includeElement = new XmlElement(XMLTag.INCLUDE.getName());
        includeElement.addAttribute(new Attribute(XMLAttribute.REF_ID.getName(), "com.orientsec.common.mapper.orderBy"));
        element.addElement(includeElement);

        return element;
    }
}
