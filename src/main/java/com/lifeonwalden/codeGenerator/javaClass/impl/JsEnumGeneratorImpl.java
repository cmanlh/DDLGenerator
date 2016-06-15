package com.lifeonwalden.codeGenerator.javaClass.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.lifeonwalden.codeGenerator.ConstBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.ValueEnum;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

public class JsEnumGeneratorImpl implements ConstBasedGenerator {

  @Override
  public String generate(List<EnumConst> enumConstList, Config config) {
    StringBuilder sb = new StringBuilder();

    for (EnumConst enumConst : enumConstList) {
      if (null != enumConst.getNote() && enumConst.getNote().length() > 0) {
        OutputUtilities.newLine(sb.append("// ").append(enumConst.getNote()));
      }

      sb.append("var ").append(enumConst.getName()).append(" = {");
      for (ValueEnum value : enumConst.getOptions()) {
        sb.append(value.getName()).append(" : { name : '").append(value.getName()).append("', value :").append(value.getValue()).append(", alias :'")
            .append(value.getAlias()).append("',note:'").append(value.getDesc()).append("'},");
      }
      sb.deleteCharAt(sb.length() - 1).append("};");
      OutputUtilities.newLine(sb);
    }

    try {
      File outputFile =
          new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getConstInfo().getFolderName() + "\\"
              + config.getConstInfo().getPackageName() + ".js");
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), config.getEncoding()));
      bw.write(sb.toString());
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
