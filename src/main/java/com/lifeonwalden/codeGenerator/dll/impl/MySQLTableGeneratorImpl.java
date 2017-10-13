package com.lifeonwalden.codeGenerator.dll.impl;

import com.lifeonwalden.codeGenerator.bean.*;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.ColumnGenerator;
import com.lifeonwalden.codeGenerator.dll.TableGenerator;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

import java.util.List;

public class MySQLTableGeneratorImpl implements TableGenerator {

    @Override
    public String generate(Table table, Config config) {
        StringBuilder sb = new StringBuilder();

        ColumnGenerator columnGenerator = new MySQLColumnGeneratorImpl();
        OutputUtilities.newLine(sb.append("CREATE TABLE ").append(table.getName()).append(" ("));
        List<Column> columnList = table.getColumns();
        OutputUtilities.newLine(OutputUtilities.textIndent(sb, 1).append(columnGenerator.generate(columnList.get(0), config)));
        for (int i = 1; i < columnList.size(); i++) {
            OutputUtilities.newLine(OutputUtilities.textIndent(sb, 1).append(",").append(columnGenerator.generate(columnList.get(i), config)));
        }

        if (null != table.getConstraints()) {
            MySQLConstraintGeneratorImpl constraintGenerator = new MySQLConstraintGeneratorImpl();
            for (Constraint constraint : table.getConstraints()) {
                OutputUtilities.newLine(OutputUtilities.textIndent(sb, 1).append(",").append(constraintGenerator.generate(constraint, config)));
            }
        }

        if (null != table.getIndexs()) {
            MySQLIndexGeneratorImpl indexGenerator = new MySQLIndexGeneratorImpl();
            for (Index index : table.getIndexs()) {
                OutputUtilities.newLine(OutputUtilities.textIndent(sb, 1).append(",").append(indexGenerator.generate(index, config)));
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
