package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.mybatis.DomGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.SQLBaseColumnListElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.SQLBaseColumnListWithPrefixElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.SQLColumnListElementXcludeDBFieldGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.column.SQLColumnListWithPrefixElementXcludeDBFieldGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.condition.SQLQueryConditionElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.DeleteElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.LogicalDeleteElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.delete.RemoveElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.insert.InsertFullElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.insert.SQLInsertElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.resultmap.BaseResultMapElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.resultmap.ResultMapElementXcludeDBFieldGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.select.SQLSelectElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.select.GetElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.select.SelectElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.SQLUpdateDynamicElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.SQLUpdateElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.UpdateDynamicElementGenerator;
import com.lifeonwalden.codeGenerator.mybatis.impl.update.UpdateElementGenerator;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.constant.XmlConstants;
import org.mybatis.generator.dom.xml.Document;
import org.mybatis.generator.dom.xml.XmlElement;

import java.io.*;

public class XMLMapperGenerator implements DomGenerator {
    private RootElementGenerator rootElementGenerator = new RootElementGenerator();
    private BaseResultMapElementGenerator baseResultMapElementGenerator = new BaseResultMapElementGenerator();
    private SQLBaseColumnListElementGenerator SQLBaseColumnListElementGenerator = new SQLBaseColumnListElementGenerator();
    private SQLBaseColumnListWithPrefixElementGenerator SQLBaseColumnListWithPrefixElementGenerator = new SQLBaseColumnListWithPrefixElementGenerator();
    private ResultMapElementXcludeDBFieldGenerator resultMapElementXcludeDBFieldGenerator = new ResultMapElementXcludeDBFieldGenerator();
    private SQLColumnListElementXcludeDBFieldGenerator SQLColumnListElementXcludeDBFieldGenerator = new SQLColumnListElementXcludeDBFieldGenerator();
    private SQLColumnListWithPrefixElementXcludeDBFieldGenerator SQLColumnListWithPrefixElementXcludeDBFieldGenerator =
            new SQLColumnListWithPrefixElementXcludeDBFieldGenerator();
    private SQLUpdateDynamicElementGenerator sqlUpdateDynamicElementGenerator = new SQLUpdateDynamicElementGenerator();
    private SQLUpdateElementGenerator sqlUpdateElementGenerator = new SQLUpdateElementGenerator();
    private SQLInsertElementGenerator sqlInsertElementGenerator = new SQLInsertElementGenerator();
    private SQLSelectElementGenerator sqlSelectElementGenerator = new SQLSelectElementGenerator();
    private SQLQueryConditionElementGenerator sqlQueryConditionElementGenerator = new SQLQueryConditionElementGenerator();
    private InsertFullElementGenerator insertFullElementGenerator = new InsertFullElementGenerator();
    private DeleteElementGenerator deleteElementGenerator = new DeleteElementGenerator();
    private RemoveElementGenerator removeElementGenerator = new RemoveElementGenerator();
    private LogicalDeleteElementGenerator logicalDeleteElementGenerator = new LogicalDeleteElementGenerator();
    private GetElementGenerator getElementGenerator = new GetElementGenerator();
    private SelectElementGenerator selectElementGenerator = new SelectElementGenerator();
    private UpdateDynamicElementGenerator updateDynamicElementGenerator = new UpdateDynamicElementGenerator();
    private UpdateElementGenerator updateElementGenerator = new UpdateElementGenerator();

    @Override
    public String generate(Table table, Config config) {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID, config.getEncoding());
        Boolean addDBFields = table.getAddDBFields();
        addDBFields = addDBFields != null && addDBFields;//避免返回null的情况
        XmlElement root = rootElementGenerator.getElement(table, config);
        root.addElement(baseResultMapElementGenerator.getElement(table, config));
        root.addElement(SQLBaseColumnListElementGenerator.getElement(table, config));
        root.addElement(SQLBaseColumnListWithPrefixElementGenerator.getElement(table, config));
        if (addDBFields) {
            root.addElement(resultMapElementXcludeDBFieldGenerator.getElement(table, config));
            root.addElement(SQLColumnListElementXcludeDBFieldGenerator.getElement(table, config));
            root.addElement(SQLColumnListWithPrefixElementXcludeDBFieldGenerator.getElement(table, config));
        }
        root.addElement(sqlUpdateDynamicElementGenerator.getElement(table, config));
        root.addElement(sqlUpdateElementGenerator.getElement(table, config));
        root.addElement(sqlInsertElementGenerator.getElement(table, config));
        root.addElement(sqlSelectElementGenerator.getElement(table, config));
        root.addElement(sqlQueryConditionElementGenerator.getElement(table, config));
        root.addElement(insertFullElementGenerator.getElement(table, config));
        root.addElement(selectElementGenerator.getElement(table, config));
        root.addElement(removeElementGenerator.getElement(table, config));

        if (null != table.getPrimaryColumns() && table.getPrimaryColumns().size() > 0) {
            root.addElement(deleteElementGenerator.getElement(table, config));
            root.addElement(getElementGenerator.getElement(table, config));
            root.addElement(updateDynamicElementGenerator.getElement(table, config));
            root.addElement(updateElementGenerator.getElement(table, config));

            if (addDBFields) {//null == table.getAddDBFields() || table.getAddDBFields()
                root.addElement(logicalDeleteElementGenerator.getElement(table, config));
            }
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
