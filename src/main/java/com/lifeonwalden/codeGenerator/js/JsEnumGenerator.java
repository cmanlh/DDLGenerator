package com.lifeonwalden.codeGenerator.js;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface JsEnumGenerator {
  public void generate(List<EnumConst> enumConstList, Config config);
}
