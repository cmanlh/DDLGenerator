package com.orientsec.ddlgenerator.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.orientsec.ddlgenerator.Table;
import com.orientsec.ddlgenerator.config.TableBuildingConfig;
import com.orientsec.ddlgenerator.generator.TableGenerator;
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
        if (null == args || args.length != 2) {
            System.out.println("No arguments");
        }

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
    }
}
