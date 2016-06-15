package com.lifeonwalden.codeGenerator;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.config.Config;

/**
 * Generator based on Const pool
 * 
 * @author luhong
 *
 */
public interface ConstBasedGenerator {
  public String generate(EnumConst enumConst, Config config);
}
