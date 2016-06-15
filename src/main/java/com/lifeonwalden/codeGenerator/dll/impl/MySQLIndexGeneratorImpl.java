package com.lifeonwalden.codeGenerator.dll.impl;

import com.lifeonwalden.codeGenerator.bean.Index;
import com.lifeonwalden.codeGenerator.bean.IndexColumn;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.IndexGenerator;

public class MySQLIndexGeneratorImpl implements IndexGenerator {

  @Override
  public String generate(Index index, Config config) {
    StringBuilder sb = new StringBuilder();

    sb.append(index.getType());

    if (null != index.getName()) {
      sb.append(" ").append(index.getName());
    }

    sb.append(" (");
    for (IndexColumn column : index.getColumns()) {
      sb.append(column.getName());
      if (null != column.getOrder()) {
        sb.append(" ").append(column.getOrder());
      }
      sb.append(", ");
    }

    sb.replace(sb.length() - 2, sb.length(), ")");

    return sb.toString();
  }
}
