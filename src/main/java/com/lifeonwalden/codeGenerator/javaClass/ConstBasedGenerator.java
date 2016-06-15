package com.lifeonwalden.codeGenerator.javaClass;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.config.Config;

/**
 * Generator based on Const pool
 * 
 * @author luhong
 *
 */
public interface ConstBasedGenerator {
  public void generate(EnumConst enumConst, Config config);
}
