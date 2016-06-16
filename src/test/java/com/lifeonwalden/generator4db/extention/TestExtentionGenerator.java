package com.lifeonwalden.generator4db.extention;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Option;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.bean.config.ExtentionGenerator;

public class TestExtentionGenerator implements TableBasedGenerator {

  @Override
  public String generate(Table table, Config config) {
    ExtentionGenerator extention = config.getExtentionMapping().get(TestExtentionGenerator.class.getName());

    System.out.println(extention.getGenerator());

    if (null != extention.getOptions()) {
      for (Option option : extention.getOptions()) {
        System.out.println(option.getOption() + " : " + option.getValue());
      }
    }

    return null;
  }

}
