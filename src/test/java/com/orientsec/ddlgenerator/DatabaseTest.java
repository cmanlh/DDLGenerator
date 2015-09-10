package com.orientsec.ddlgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.orientsec.ddlgenerator.config.CommonObjectConfig;
import com.orientsec.ddlgenerator.config.EnumConfig;
import com.orientsec.ddlgenerator.config.TableBuildingConfig;
import com.orientsec.ddlgenerator.generator.constant.impl.EnumJavaClassGeneratorImpl;
import com.orientsec.ddlgenerator.generator.constant.impl.EnumSQLGeneratorImpl;
import com.orientsec.ddlgenerator.generator.constant.impl.EnumXMLGeneratorImpl;

public class DatabaseTest {
    @Test
    public void desieralize() throws IOException {
        YamlReader reader = new YamlReader(new FileReader("src//test//resource//test.yaml"));
        TableBuildingConfig tableBuilder = reader.read(TableBuildingConfig.class);
        FileOutputStream fos = new FileOutputStream(new File("target//result.json"));
        fos.write(JSON.toJSONString(tableBuilder).getBytes());
        fos.close();
    }

    @Test
    public void desieralize_common() throws IOException {
        YamlReader reader = new YamlReader(new FileReader("src//test//resource//common.yml"));
        CommonObjectConfig commonObjectConfigBuilder = reader.read(CommonObjectConfig.class);
        FileOutputStream fos = new FileOutputStream(new File("target//result.json"));
        fos.write(JSON.toJSONString(commonObjectConfigBuilder).getBytes());
        fos.close();

        commonObjectConfigBuilder = JSON.parseObject(JSON.toJSONString(commonObjectConfigBuilder), CommonObjectConfig.class);
        EnumJavaClassGeneratorImpl hel = new EnumJavaClassGeneratorImpl();
        EnumXMLGeneratorImpl hel2 = new EnumXMLGeneratorImpl();
        EnumSQLGeneratorImpl hel3 = new EnumSQLGeneratorImpl();
        for (EnumConfig enumConfig : commonObjectConfigBuilder.getEnums())
            hel.generate(enumConfig, commonObjectConfigBuilder.getPackageName(), commonObjectConfigBuilder.getOutputPath());
        hel2.generate(commonObjectConfigBuilder);
        hel3.generate(commonObjectConfigBuilder);
    }
}
