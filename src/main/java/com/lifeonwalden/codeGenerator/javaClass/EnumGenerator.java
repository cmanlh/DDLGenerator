package com.lifeonwalden.codeGenerator.javaClass;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface EnumGenerator {
  public void generate(EnumConst enumConst, Config config);
}
