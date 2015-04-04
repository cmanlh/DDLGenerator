package com.orientsec.ddlgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.orientsec.ddlgenerator.config.TableBuildingConfig;

public class DatabaseTest {
	@Test
	public void desieralize() throws IOException {
		YamlReader reader = new YamlReader(new FileReader("src//test//resource//test.yaml"));
		TableBuildingConfig tableBuilder = reader.read(TableBuildingConfig.class);
		FileOutputStream fos = new FileOutputStream(new File("target//result.json"));
		fos.write(JSON.toJSONString(tableBuilder).getBytes());
		fos.close();
	}
}
