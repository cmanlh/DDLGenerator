package com.lifeonwalden.generator4db;

import com.lifeonwalden.codeGenerator.GenerateCodeMain;
import org.junit.Test;

public class AppTest {
    @Test
    public void generateCodeMainTest() {
        String root_path = "C:\\Users\\HongLu\\IdeaProjects\\DDLGenerator\\";
        String[] param = {root_path + "resources\\xml2db", root_path + "target"};

        GenerateCodeMain.main(param);
    }
}
