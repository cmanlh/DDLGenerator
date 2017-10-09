package com.lifeonwalden.generator4db;

import org.junit.Test;

import com.lifeonwalden.codeGenerator.GenerateCodeMain;

public class AppTest {
    @Test
    public void generateCodeMainTest() {
        String root_path = "D:\\workspace\\DDLGenerator\\";
        String[] param = {root_path + "resources\\xml2db", root_path + "target"};

        GenerateCodeMain.main(param);
    }
}
