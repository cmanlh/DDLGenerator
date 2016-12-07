package com.lifeonwalden.codeGenerator.mybatis.impl;

import java.util.List;

import org.mybatis.generator.dom.xml.Attribute;
import org.mybatis.generator.dom.xml.TextElement;
import org.mybatis.generator.dom.xml.XmlElement;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.javaClass.impl.BeanGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.TableElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLAttribute;
import com.lifeonwalden.codeGenerator.mybatis.constant.XMLTag;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;
import com.lifeonwalden.codeGenerator.util.StringUtil;

public class LogicalDeleteElementGenerator implements TableElementGenerator {

    public XmlElement getElement(Table table, Config config) {
        XmlElement element = new XmlElement(XMLTag.UPDATE.getName());
        String className = config.getBeanInfo().getPackageName() + "." + BeanGeneratorImpl.getResultBeanName(table, config);

        element.addAttribute(new Attribute(XMLAttribute.ID.getName(), "logicalDelete"));
        element.addAttribute(new Attribute(XMLAttribute.PARAMETER_TYPE.getName(), className));

        StringBuilder sb = new StringBuilder();

        List<Column> primaryKey = table.getPrimaryColumns();
        if (null != primaryKey && primaryKey.size() > 0) {
            sb.append("update ")
                            .append(table.getName())
                            .append(" set logicalDel = 1, updateTime = #{updateTime, jdbcType=TIMESTAMP}, updateUser = #{updateUser, jdbcType=VARCHAR}")
                            .append(" where ");
            for (Column column : primaryKey) {
                sb.append(column.getName()).append(" = ");
                sb.append("#{").append(StringUtil.removeUnderline(column.getName())).append(", jdbcType=")
                                .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
                if (null != column.getTypeHandler()) {
                    sb.append(", typeHandler=").append(column.getTypeHandler());
                }
                sb.append("} AND ");
            }
            sb = sb.replace(sb.length() - 5, sb.length(), "");

            for (Column column : table.getColumns()) {
                if ("owner".equalsIgnoreCase(column.getName())) {
                    XmlElement ifElement = new XmlElement(XMLTag.IF.getName());
                    ifElement.addAttribute(new Attribute(XMLAttribute.TEST.getName(), column.getName() + " != null"));

                    StringBuilder setValueText = new StringBuilder("AND ");
                    setValueText.append(column.getName()).append(" = ");
                    setValueText.append("#{").append(StringUtil.removeUnderline(column.getName())).append(", jdbcType=")
                                    .append(JdbcTypeEnum.nameOf(column.getType().toUpperCase()).getName());
                    if (null != column.getTypeHandler()) {
                        setValueText.append(", typeHandler=").append(column.getTypeHandler());
                    }
                    setValueText.append("}");
                    TextElement setValue = new TextElement(setValueText.toString());
                    ifElement.addElement(setValue);

                    OutputUtilities.newLine(sb).append(ifElement.getFormattedContent(2));

                    break;
                }
            }
        } else {
            throw new RuntimeException("Should not delete one record without a primary key.");
        }

        TextElement fromElement = new TextElement(sb.toString());
        element.addElement(fromElement);

        return element;
    }
}
