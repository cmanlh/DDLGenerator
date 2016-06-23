package com.lifeonwalden.codeGenerator;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.util.StringUtil;

/**
 * Generator based on Const pool
 * 
 * @author luhong
 *
 */
public interface ConstBasedGenerator {

  public static String getConstName(String constName, Config config) {
    String namePattern = config.getConstInfo().getNamePattern(), name;
    if (null == namePattern) {
      name = constName + "Enum";
    } else {
      name = namePattern.replace("?", StringUtil.firstAlphToUpper(constName));
    }

    return StringUtil.firstAlphToUpper(name);
  }

  public String generate(List<EnumConst> enumConstList, Config config);
}
