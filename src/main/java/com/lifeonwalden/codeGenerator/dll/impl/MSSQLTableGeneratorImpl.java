package com.lifeonwalden.codeGenerator.dll.impl;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Constraint;
import com.lifeonwalden.codeGenerator.bean.Index;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.ColumnGenerator;
import com.lifeonwalden.codeGenerator.dll.ConstraintGenerator;
import com.lifeonwalden.codeGenerator.dll.IndexGenerator;
import com.lifeonwalden.codeGenerator.dll.TableGenerator;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

public class MSSQLTableGeneratorImpl implements TableGenerator {

  @Override
  public String generator(Table table, Config config) {
    StringBuilder sb = new StringBuilder();

    String schemaPrefix = "";
    if (null != table.getDatabase().getSchema() && table.getDatabase().getSchema().length() > 0) {
      schemaPrefix = table.getDatabase().getSchema() + ".";
    }
    OutputUtilities.newLine(sb.append("--").append(table.getNote()));
    ColumnGenerator columnGenerator = new MSSQLColumnGeneratorImpl();
    OutputUtilities.newLine(sb.append("CREATE TABLE ").append(schemaPrefix).append(table.getName()).append("("));
    List<Column> columnList = table.getColumns();
    OutputUtilities.textIndent(sb, 1);
    OutputUtilities.newLine(sb.append(columnGenerator.generator(columnList.get(0), config)));
    for (int i = 1; i < columnList.size(); i++) {
      OutputUtilities.textIndent(sb, 1);
      OutputUtilities.newLine(sb.append(",").append(columnGenerator.generator(columnList.get(i), config)));
    }
    if (null != table.getConstraints()) {
      ConstraintGenerator constraintGenerator = new MSSQLConstraintGeneratorImpl();
      for (Constraint constraint : table.getConstraints()) {
        OutputUtilities.textIndent(sb, 1);
        OutputUtilities.newLine(sb.append(",").append(constraintGenerator.generator(constraint, config)));
      }
    }
    OutputUtilities.newLine((OutputUtilities.newLine(sb.append(");"))));

    if (null != table.getIndexs()) {
      IndexGenerator indexGenerator = new MSSQLIndexGeneratorImpl();
      for (Index index : table.getIndexs()) {
        OutputUtilities.newLine(sb.append(indexGenerator.generator(index, config)));
      }
    }
    OutputUtilities.newLine(sb);

    return sb.toString();
  }
}
