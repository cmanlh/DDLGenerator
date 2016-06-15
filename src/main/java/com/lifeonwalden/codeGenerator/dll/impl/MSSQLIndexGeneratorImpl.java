package com.lifeonwalden.codeGenerator.dll.impl;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.Database;
import com.lifeonwalden.codeGenerator.bean.Index;
import com.lifeonwalden.codeGenerator.bean.IndexColumn;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.IndexGenerator;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

public class MSSQLIndexGeneratorImpl implements IndexGenerator {

  @Override
  public String generate(Index index, Config config) {
    StringBuilder sb = new StringBuilder();

    sb.append("CREATE ").append(index.getType()).append(" INDEX ").append(index.getName()).append(" ON ");

    Table table = index.getTable();
    Database database = table.getDatabase();
    if (null != database.getSchema() && database.getSchema().length() > 0) {
      sb.append(database.getSchema()).append(".");
    }

    OutputUtilities.newLine(sb.append(table.getName()).append(" ("));

    List<IndexColumn> indexColumnList = index.getColumns();
    OutputUtilities.textIndent(sb, 1);
    OutputUtilities.newLine(sb.append(indexColumnList.get(0).getName()).append(" ").append(indexColumnList.get(0).getOrder()));
    for (int i = 1; i < indexColumnList.size(); i++) {
      OutputUtilities.textIndent(sb, 1);
      OutputUtilities.newLine(sb.append(",").append(indexColumnList.get(i).getName()).append(" ").append(indexColumnList.get(i).getOrder()));
    }
    sb.append(");");


    return sb.toString();
  }
}
