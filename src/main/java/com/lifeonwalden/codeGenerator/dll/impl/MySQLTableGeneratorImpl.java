package com.lifeonwalden.codeGenerator.dll.impl;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Constraint;
import com.lifeonwalden.codeGenerator.bean.Index;
import com.lifeonwalden.codeGenerator.bean.Option;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.ColumnGenerator;
import com.lifeonwalden.codeGenerator.dll.TableGenerator;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

public class MySQLTableGeneratorImpl implements TableGenerator {

  @Override
  public String generator(Table table, Config config) {
    StringBuilder sb = new StringBuilder();

    ColumnGenerator columnGenerator = new MySQLColumnGeneratorImpl();
    OutputUtilities.newLine(sb.append("CREATE TABLE ").append(table.getName()).append(" ("));
    List<Column> columnList = table.getColumns();
    OutputUtilities
        .newLine(OutputUtilities.textIndent(sb, 1).append(columnGenerator.generator(columnList.get(0), config)));
    for (int i = 1; i < columnList.size(); i++) {
      OutputUtilities.newLine(
          OutputUtilities.textIndent(sb, 1).append(",").append(columnGenerator.generator(columnList.get(i), config)));
    }

    if (null != table.getConstraints()) {
      MySQLConstraintGeneratorImpl constraintGenerator = new MySQLConstraintGeneratorImpl();
      for (Constraint constraint : table.getConstraints()) {
        OutputUtilities.newLine(
            OutputUtilities.textIndent(sb, 1).append(",").append(constraintGenerator.generator(constraint, config)));
      }
    }

    if (null != table.getIndexs()) {
      MySQLIndexGeneratorImpl indexGenerator = new MySQLIndexGeneratorImpl();
      for (Index index : table.getIndexs()) {
        OutputUtilities
            .newLine(OutputUtilities.textIndent(sb, 1).append(",").append(indexGenerator.generator(index, config)));
      }
    }
    sb.append(")");

    if (null != table.getOptions()) {
      OutputUtilities.newLine(sb);
      for (Option option : table.getOptions()) {
        OutputUtilities.newLine(sb.append(option.getOption()).append("=").append(option.getValue()));
      }
    }

    OutputUtilities.newLine(sb.append(";"));

    return sb.toString();
  }
}
