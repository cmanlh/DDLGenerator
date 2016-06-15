package com.lifeonwalden.codeGenerator;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.config.Config;

/**
 * Generator based on Const pool
 * 
 * @author luhong
 *
 */
public interface ConstBasedGenerator {
  public String generate(List<EnumConst> enumConstList, Config config);
}
