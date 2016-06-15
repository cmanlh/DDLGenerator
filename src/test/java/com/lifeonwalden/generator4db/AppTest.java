package com.lifeonwalden.generator4db;

import org.junit.Test;

import com.lifeonwalden.codeGenerator.GenerateCodeMain;

public class AppTest {
  @Test
  public void generateCodeMainTest() {
    String[] param = {"D:\\workspace\\DDLGenerator\\resources"};

    GenerateCodeMain.main(param);
  }
}
