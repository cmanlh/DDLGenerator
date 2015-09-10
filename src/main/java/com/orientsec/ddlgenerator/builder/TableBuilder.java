package com.orientsec.ddlgenerator.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.orientsec.ddlgenerator.Table;
import com.orientsec.ddlgenerator.config.CommonObjectConfig;
import com.orientsec.ddlgenerator.config.EnumConfig;
import com.orientsec.ddlgenerator.config.TableBuildingConfig;
import com.orientsec.ddlgenerator.generator.TableGenerator;
import com.orientsec.ddlgenerator.generator.constant.impl.EnumJavaClassGeneratorImpl;
import com.orientsec.ddlgenerator.generator.constant.impl.EnumSQLGeneratorImpl;
import com.orientsec.ddlgenerator.generator.constant.impl.EnumXMLGeneratorImpl;
import com.orientsec.ddlgenerator.generator.sqlserverImpl.TableGeneratorImpl;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class TableBuilder {
    public String build(TableBuildingConfig config) {
        StringBuilder sb = new StringBuilder();

        if (null != config.getConfig()) {
            sb.append("USE ").append(config.getConfig().getDatabase()).append(";").append(OutputUtil.LINE_SEPERATOR);
            sb.append(OutputUtil.LINE_SEPERATOR);
        }

        TableGenerator tableGenerator = new TableGeneratorImpl();
        for (Table table : config.getTable()) {
            sb.append(tableGenerator.generator(table, config.getConfig()));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        if (null == args || args.length != 2 || args.length != 1) {
            System.out.println("No arguments");
        }

        if (2 == args.length) {
            System.out.println("\"" + args[0] + "\"");
            System.out.println("\"" + args[1] + "\"");
            YamlReader reader = new YamlReader(new FileReader(args[0]));
            TableBuildingConfig tableBuildingConfig = reader.read(TableBuildingConfig.class, Table.class);
            tableBuildingConfig = JSON.parseObject(JSON.toJSONString(tableBuildingConfig), TableBuildingConfig.class);
            TableBuilder tableBuilder = new TableBuilder();
            FileOutputStream fos = new FileOutputStream(new File(args[1]));
            fos.write(tableBuilder.build(tableBuildingConfig).getBytes());
            fos.close();
            reader.close();
        } else {
            System.out.println("\"" + args[0] + "\"");
            YamlReader reader = new YamlReader(new FileReader(args[0]));
            CommonObjectConfig commonObjectConfigBuilder = reader.read(CommonObjectConfig.class);
            commonObjectConfigBuilder = JSON.parseObject(JSON.toJSONString(commonObjectConfigBuilder), CommonObjectConfig.class);
            EnumJavaClassGeneratorImpl javaEnum = new EnumJavaClassGeneratorImpl();
            for (EnumConfig enumConfig : commonObjectConfigBuilder.getEnums())
                javaEnum.generate(enumConfig, commonObjectConfigBuilder.getPackageName(), commonObjectConfigBuilder.getOutputPath());

            EnumXMLGeneratorImpl xmlEnum = new EnumXMLGeneratorImpl();
            xmlEnum.generate(commonObjectConfigBuilder);
            EnumSQLGeneratorImpl sqlEnum = new EnumSQLGeneratorImpl();
            sqlEnum.generate(commonObjectConfigBuilder);
        }
    }
}
