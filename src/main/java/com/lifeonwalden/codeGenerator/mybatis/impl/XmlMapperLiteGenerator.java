package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.mybatis.DomGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.SQLBaseColumnListElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.SQLBaseColumnListWithPrefixElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLQueryConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.DeleteByKeyElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.DeleteDynamicElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.SQLDeleteElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.insert.InsertElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.insert.SQLInsertElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.resultmap.BaseResultMapElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.select.*;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.SQLUpdateDynamicElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.SQLUpdateElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.UpdateDynamicElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.UpdateElementGenerator;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.constant.XmlConstants;
import org.mybatis.generator.dom.xml.Document;
import org.mybatis.generator.dom.xml.XmlElement;

import java.io.*;

public class XmlMapperLiteGenerator implements DomGenerator {
    private RootElementGenerator rootElementGenerator = new RootElementGenerator();
    // column
    private SQLBaseColumnListElementGenerator sqlBaseColumnListElementGenerator = new SQLBaseColumnListElementGenerator();
    private SQLBaseColumnListWithPrefixElementGenerator sqlBaseColumnListWithPrefixElementGenerator = new SQLBaseColumnListWithPrefixElementGenerator();
    private SQLQueryConditionElementGenerator sqlQueryConditionElementGenerator = new SQLQueryConditionElementGenerator();

    // delete
    private DeleteByKeyElementGenerator deleteElementGenerator = new DeleteByKeyElementGenerator();
    // insert
    private InsertElementGenerator insertElementGenerator = new InsertElementGenerator();
    private SQLInsertElementGenerator sqlInsertElementGenerator = new SQLInsertElementGenerator();

    // resultMap
    private BaseResultMapElementGenerator baseResultMapElementGenerator = new BaseResultMapElementGenerator();
    private SQLConditionElementGenerator sqlConditionElementGenerator = new SQLConditionElementGenerator();
    private GetByKeyElementGenerator getElementGenerator = new GetByKeyElementGenerator();
    private SelectBaseElementGenerator selectElementGenerator = new SelectBaseElementGenerator();
    private SQLSelectElementGenerator sqlSelectElementGenerator = new SQLSelectElementGenerator();
    private SQLUpdateDynamicElementGenerator sqlUpdateDynamicElementGenerator = new SQLUpdateDynamicElementGenerator();
    private SQLUpdateElementGenerator sqlUpdateElementGenerator = new SQLUpdateElementGenerator();
    private UpdateDynamicElementGenerator updateDynamicElementGenerator = new UpdateDynamicElementGenerator();
    private UpdateElementGenerator updateElementGenerator = new UpdateElementGenerator();
    private DeleteDynamicElementGenerator removeElementGenerator = new DeleteDynamicElementGenerator();
    private SQLDeleteElementGenerator sqlDeleteElementGenerator = new SQLDeleteElementGenerator();
    private QueryElementGenerator queryElementGenerator = new QueryElementGenerator();

    @Override
    public String generate(Table table, Config config) {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID, config.getEncoding());
        //boolean hasPrimaryKey = TableInfoUtil.checkPrimaryKey(table);
        XmlElement root = rootElementGenerator.getElement(table, config);
        // resultMap
        root.addElement(baseResultMapElementGenerator.getElement(table, config));

        root.addElement(sqlBaseColumnListElementGenerator.getElement(table, config));//BaseColumnList
        root.addElement(sqlBaseColumnListWithPrefixElementGenerator.getElement(table, config));//baseColumnListWithPrefix
        root.addElement(sqlInsertElementGenerator.getElement(table, config));//insertSQL
        root.addElement(sqlDeleteElementGenerator.getElement(table, config));
        root.addElement(sqlUpdateElementGenerator.getElement(table, config));//updateSQL
        root.addElement(sqlSelectElementGenerator.getElement(table, config));//querySQL
        root.addElement(sqlConditionElementGenerator.getElement(table, config));//condition
        root.addElement(sqlQueryConditionElementGenerator.getElement(table, config));//queryCondition
        root.addElement(sqlUpdateDynamicElementGenerator.getElement(table, config));//dynamicUpdateSQL
        root.addElement(insertElementGenerator.getElement(table, config));//insert
        root.addElement(deleteElementGenerator.getElement(table, config));
        root.addElement(removeElementGenerator.getElement(table, config));
        root.addElement(updateElementGenerator.getElement(table, config));
        root.addElement(updateDynamicElementGenerator.getElement(table, config));
        root.addElement(selectElementGenerator.getElement(table, config));
        root.addElement(getElementGenerator.getElement(table, config));
        root.addElement(queryElementGenerator.getElement(table, config));
        document.setRootElement(root);

        try {
            File folder =
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getMybatisInfo().getFolderName()
                            + File.separator + config.getDaoInfo().getPackageName().replace(".", File.separator));

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String file = folder.getPath() + File.separator + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(table.getName())) + "Mapper.xml";

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), config.getEncoding()));
            bw.write(document.getFormattedContent());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
