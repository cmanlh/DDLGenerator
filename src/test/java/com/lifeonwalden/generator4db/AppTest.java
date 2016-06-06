package com.lifeonwalden.generator4db;

import org.junit.Test;

import com.lifeonwalden.codeGenerator.GenerateCodeMain;

public class AppTest {
  @Test
  public void generateCodeMainTest() {
    String[] param = {"C:\\Users\\HongLu\\git\\DDLGenerator\\resources"};

    GenerateCodeMain.main(param);
  }
}
