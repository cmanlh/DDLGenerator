package com.lifeonwalden.codeGenerator.mybatis.impl;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.mybatis.DomGenerator;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import org.mybatis.generator.dom.constant.XmlConstants;
import org.mybatis.generator.dom.xml.Document;
import org.mybatis.generator.dom.xml.XmlElement;

import java.io.*;

public class XMLMapperGenerator implements DomGenerator {
    private RootElementGenerator rootElementGenerator = new RootElementGenerator();
    private BaseResultMapElementGenerator baseResultMapElementGenerator = new BaseResultMapElementGenerator();
    private BaseColumnListElementGenerator baseColumnListElementGenerator = new BaseColumnListElementGenerator();
    private BaseColumnListWithPrefixElementGenerator baseColumnListWithPrefixElementGenerator = new BaseColumnListWithPrefixElementGenerator();
    private ResultMapElementXcludeDBFieldGenerator resultMapElementXcludeDBFieldGenerator = new ResultMapElementXcludeDBFieldGenerator();
    private ColumnListElementXcludeDBFieldGenerator columnListElementXcludeDBFieldGenerator = new ColumnListElementXcludeDBFieldGenerator();
    private ColumnListWithPrefixElementXcludeDBFieldGenerator columnListWithPrefixElementXcludeDBFieldGenerator =
            new ColumnListWithPrefixElementXcludeDBFieldGenerator();
    private SQLDynamicUpdateElementGenerator sqlDynamicUpdateElementGenerator = new SQLDynamicUpdateElementGenerator();
    private SQLUpdateElementGenerator sqlUpdateElementGenerator = new SQLUpdateElementGenerator();
    private SQLInsertElementGenerator sqlInsertElementGenerator = new SQLInsertElementGenerator();
    private SQLSelectElementGenerator sqlSelectElementGenerator = new SQLSelectElementGenerator();
    private SQLQueryConditionElementGenerator sqlQueryConditionElementGenerator = new SQLQueryConditionElementGenerator();
    private InsertFullElementGenerator insertFullElementGenerator = new InsertFullElementGenerator();
    private DeleteElementGenerator deleteElementGenerator = new DeleteElementGenerator();
    private RemoveElementGenerator removeElementGenerator = new RemoveElementGenerator();
    private LogicalDeleteElementGenerator logicalDeleteElementGenerator = new LogicalDeleteElementGenerator();
    private SelectGetElementGenerator selectGetElementGenerator = new SelectGetElementGenerator();
    private SelectQueryElementGenerator selectQueryElementGenerator = new SelectQueryElementGenerator();
    private UpdateDynamicElementGenerator updateDynamicElementGenerator = new UpdateDynamicElementGenerator();
    private UpdateFullElementGenerator updateFullElementGenerator = new UpdateFullElementGenerator();

    @Override
    public String generate(Table table, Config config) {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID, config.getEncoding());

        XmlElement root = rootElementGenerator.getElement(table, config);
        root.addElement(baseResultMapElementGenerator.getElement(table, config));
        root.addElement(baseColumnListElementGenerator.getElement(table, config));
        root.addElement(baseColumnListWithPrefixElementGenerator.getElement(table, config));
        root.addElement(resultMapElementXcludeDBFieldGenerator.getElement(table, config));
        root.addElement(columnListElementXcludeDBFieldGenerator.getElement(table, config));
        root.addElement(columnListWithPrefixElementXcludeDBFieldGenerator.getElement(table, config));
        root.addElement(sqlDynamicUpdateElementGenerator.getElement(table, config));
        root.addElement(sqlUpdateElementGenerator.getElement(table, config));
        root.addElement(sqlInsertElementGenerator.getElement(table, config));
        root.addElement(sqlSelectElementGenerator.getElement(table, config));
        root.addElement(sqlQueryConditionElementGenerator.getElement(table, config));
        root.addElement(insertFullElementGenerator.getElement(table, config));
        root.addElement(selectQueryElementGenerator.getElement(table, config));
        root.addElement(removeElementGenerator.getElement(table, config));

        if (null != table.getPrimaryColumns() && table.getPrimaryColumns().size() > 0) {
            root.addElement(deleteElementGenerator.getElement(table, config));
            root.addElement(selectGetElementGenerator.getElement(table, config));
            root.addElement(updateDynamicElementGenerator.getElement(table, config));
            root.addElement(updateFullElementGenerator.getElement(table, config));

            if (null == table.getAddDBFields() || table.getAddDBFields()) {
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
