package com.orientsec.ddlgenerator.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.orientsec.ddlgenerator.Table;
import com.orientsec.ddlgenerator.config.TableBuildingConfig;

public class TableBuilderTest {
    @Test
    public void build() throws IOException {
        YamlReader reader = new YamlReader(new FileReader("src//test//resource//cashmgmt_view.yml"));
        TableBuildingConfig tableBuildingConfig = reader.read(TableBuildingConfig.class, Table.class);
        tableBuildingConfig = JSON.parseObject(JSON.toJSONString(tableBuildingConfig), TableBuildingConfig.class);
        TableBuilder tableBuilder = new TableBuilder();
        FileOutputStream fos = new FileOutputStream(new File("target//result.sql"));
        fos.write(tableBuilder.build(tableBuildingConfig).getBytes());
        fos.close();

        reader = new YamlReader(new FileReader("src//test//resource//cashmgmt_user.yml"));
        tableBuildingConfig = reader.read(TableBuildingConfig.class, Table.class);
        tableBuildingConfig = JSON.parseObject(JSON.toJSONString(tableBuildingConfig), TableBuildingConfig.class);
        tableBuilder = new TableBuilder();
        fos = new FileOutputStream(new File("target//result_user.sql"));
        fos.write(tableBuilder.build(tableBuildingConfig).getBytes());
        fos.close();

        reader = new YamlReader(new FileReader("src//test//resource//cashmgmt.yml"));
        tableBuildingConfig = reader.read(TableBuildingConfig.class, Table.class);
        tableBuildingConfig = JSON.parseObject(JSON.toJSONString(tableBuildingConfig), TableBuildingConfig.class);
        tableBuilder = new TableBuilder();
        fos = new FileOutputStream(new File("target//result_business.sql"));
        fos.write(tableBuilder.build(tableBuildingConfig).getBytes());
        fos.close();
    }
}
