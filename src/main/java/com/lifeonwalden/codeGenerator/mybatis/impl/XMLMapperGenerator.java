package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.mybatis.DomGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.*;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLQueryConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLQueryWildConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLWildConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.*;
import com.lifeonwalden.codeGenerator.mybatis.impl.insert.InsertElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.insert.SQLInsertElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.orderby.SQLOrderByElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.resultmap.BaseResultMapElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.resultmap.ResultMapElementXcludeDBFieldGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.select.*;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.*;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.lifeonwalden.codeGenerator.util.TableInfoUtil;
import org.mybatis.generator.dom.constant.XmlConstants;
import org.mybatis.generator.dom.xml.Document;
import org.mybatis.generator.dom.xml.XmlElement;

import java.io.*;

public class XMLMapperGenerator implements DomGenerator {
    private RootElementGenerator rootElementGenerator = new RootElementGenerator();

    // column
    private SQLBaseColumnListElementGenerator sqlBaseColumnListElementGenerator = new SQLBaseColumnListElementGenerator();
    private SQLBaseColumnListWithPrefixElementGenerator sqlBaseColumnListWithPrefixElementGenerator = new SQLBaseColumnListWithPrefixElementGenerator();
    private SQLColumnListElementXcludeDBFieldGenerator sqlColumnListElementXcludeDBFieldGenerator = new SQLColumnListElementXcludeDBFieldGenerator();
    private SQLColumnListWithPrefixElementXcludeDBFieldGenerator sqlColumnListWithPrefixElementXcludeDBFieldGenerator =
            new SQLColumnListWithPrefixElementXcludeDBFieldGenerator();
    private SQLFieldPickElementGenerator sqlFieldPickElementGenerator = new SQLFieldPickElementGenerator();
    private SQLFieldPickWithPrefixElementGenerator sqlFieldPickWithPrefixElementGenerator = new SQLFieldPickWithPrefixElementGenerator();

    // condition
    private SQLConditionElementGenerator sqlConditionElementGenerator = new SQLConditionElementGenerator();
    private SQLQueryConditionElementGenerator sqlQueryConditionElementGenerator = new SQLQueryConditionElementGenerator();
    private SQLQueryWildConditionElementGenerator sqlQueryWildConditionElementGenerator = new SQLQueryWildConditionElementGenerator();
    private SQLWildConditionElementGenerator sqlWildConditionElementGenerator = new SQLWildConditionElementGenerator();

    // delete
    private DeleteElementGenerator deleteElementGenerator = new DeleteElementGenerator();
    private LogicalDeleteElementGenerator logicalDeleteElementGenerator1 = new LogicalDeleteElementGenerator();
    private LogicalRemoveElementGenerator logicalRemoveElementGenerator = new LogicalRemoveElementGenerator();
    private RemoveElementGenerator removeElementGenerator = new RemoveElementGenerator();
    private SQLDeleteElementGenerator sqlDeleteElementGenerator = new SQLDeleteElementGenerator();
    private SQLLogicalDeleteElementGenerator sqlLogicalDeleteElementGenerator = new SQLLogicalDeleteElementGenerator();

    // insert
    private InsertElementGenerator insertElementGenerator = new InsertElementGenerator();
    private SQLInsertElementGenerator sqlInsertElementGenerator = new SQLInsertElementGenerator();

    // resultMap
    private BaseResultMapElementGenerator baseResultMapElementGenerator = new BaseResultMapElementGenerator();
    private ResultMapElementXcludeDBFieldGenerator resultMapElementXcludeDBFieldGenerator = new ResultMapElementXcludeDBFieldGenerator();

    // select
    private DirectGetElementGenerator directGetElementGenerator = new DirectGetElementGenerator();
    private DirectSelectAllElementGenerator directSelectAllElementGenerator = new DirectSelectAllElementGenerator();
    private DirectSelectElementGenerator directSelectElementGenerator = new DirectSelectElementGenerator();
    private DirectSelectWildElementGenerator directSelectWildElementGenerator = new DirectSelectWildElementGenerator();
    private GetElementGenerator getElementGenerator = new GetElementGenerator();
    private SelectAllElementGenerator selectAllElementGenerator = new SelectAllElementGenerator();
    private SelectElementGenerator selectElementGenerator = new SelectElementGenerator();
    private SelectWildElementGenerator selectWildElementGenerator = new SelectWildElementGenerator();
    private SQLDirectSelectElementGenerator sqlDirectSelectElementGenerator = new SQLDirectSelectElementGenerator();
    private SQLSelectElementGenerator sqlSelectElementGenerator = new SQLSelectElementGenerator();

    // order by
    private SQLOrderByElementGenerator sqlOrderByElementGenerator = new SQLOrderByElementGenerator();

    // update
    private DirectUpdateElementGenerator directUpdateElementGenerator = new DirectUpdateElementGenerator();
    private SQLDirectUpdateElementGenerator sqlDirectUpdateElementGenerator = new SQLDirectUpdateElementGenerator();
    private SQLUpdateDynamicElementGenerator sqlUpdateDynamicElementGenerator = new SQLUpdateDynamicElementGenerator();
    private SQLUpdateElementGenerator sqlUpdateElementGenerator = new SQLUpdateElementGenerator();
    private UpdateDynamicElementGenerator updateDynamicElementGenerator = new UpdateDynamicElementGenerator();
    private UpdateElementGenerator updateElementGenerator = new UpdateElementGenerator();

    @Override
    public String generate(Table table, Config config) {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID, config.getEncoding());

        boolean addDBFields = table.getAddDBFields();
        boolean supportWildCondition = TableInfoUtil.checkWildConditionSupport(table);
        boolean hasPrimaryKey = TableInfoUtil.checkPrimaryKey(table);

        XmlElement root = rootElementGenerator.getElement(table, config);

        // resultMap
        root.addElement(baseResultMapElementGenerator.getElement(table, config));
        if (addDBFields) {
            root.addElement(resultMapElementXcludeDBFieldGenerator.getElement(table, config));
        }

        // column
        root.addElement(sqlBaseColumnListElementGenerator.getElement(table, config));
        root.addElement(sqlBaseColumnListWithPrefixElementGenerator.getElement(table, config));
        root.addElement(sqlFieldPickElementGenerator.getElement(table, config));
        root.addElement(sqlFieldPickWithPrefixElementGenerator.getElement(table, config));
        if (addDBFields) {
            root.addElement(sqlColumnListElementXcludeDBFieldGenerator.getElement(table, config));
            root.addElement(sqlColumnListWithPrefixElementXcludeDBFieldGenerator.getElement(table, config));
        }

        // condition
        root.addElement(sqlConditionElementGenerator.getElement(table, config));
        root.addElement(sqlQueryConditionElementGenerator.getElement(table, config));
        if (supportWildCondition) {
            root.addElement(sqlWildConditionElementGenerator.getElement(table, config));
            root.addElement(sqlQueryWildConditionElementGenerator.getElement(table, config));
        }

        // delete
        root.addElement(sqlDeleteElementGenerator.getElement(table, config));
        root.addElement(removeElementGenerator.getElement(table, config));
        if (hasPrimaryKey) {
            root.addElement(deleteElementGenerator.getElement(table, config));
        }
        if (addDBFields) {
            root.addElement(sqlLogicalDeleteElementGenerator.getElement(table, config));
            root.addElement(logicalRemoveElementGenerator.getElement(table, config));
            if (hasPrimaryKey) {
                root.addElement(logicalDeleteElementGenerator1.getElement(table, config));
            }
        }

        // insert
        root.addElement(sqlInsertElementGenerator.getElement(table, config));
        root.addElement(insertElementGenerator.getElement(table, config));

        // order by
        root.addElement(sqlOrderByElementGenerator.getElement(table, config));

        // select
        root.addElement(sqlDirectSelectElementGenerator.getElement(table, config));
        root.addElement(sqlSelectElementGenerator.getElement(table, config));
        root.addElement(directSelectAllElementGenerator.getElement(table, config));
        root.addElement(selectAllElementGenerator.getElement(table, config));
        root.addElement(directSelectElementGenerator.getElement(table, config));
        root.addElement(selectElementGenerator.getElement(table, config));
        if (supportWildCondition) {
            root.addElement(selectWildElementGenerator.getElement(table, config));
            root.addElement(directSelectWildElementGenerator.getElement(table, config));
        }
        if (hasPrimaryKey) {
            root.addElement(directGetElementGenerator.getElement(table, config));
            root.addElement(getElementGenerator.getElement(table, config));
        }

        // update
        if (hasPrimaryKey) {
            root.addElement(sqlDirectUpdateElementGenerator.getElement(table, config));
            root.addElement(sqlUpdateDynamicElementGenerator.getElement(table, config));
            root.addElement(sqlUpdateElementGenerator.getElement(table, config));
            root.addElement(directUpdateElementGenerator.getElement(table, config));
            root.addElement(updateDynamicElementGenerator.getElement(table, config));
            root.addElement(updateElementGenerator.getElement(table, config));
        }

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
