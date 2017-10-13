package com.lifeonwalden.codeGenerator.dll.impl;

import com.lifeonwalden.codeGenerator.bean.Constraint;
import com.lifeonwalden.codeGenerator.bean.IndexColumn;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.ConstraintGenerator;

public class MSSQLConstraintGeneratorImpl implements ConstraintGenerator {

    @Override
    public String generate(Constraint constraint, Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONSTRAINT ").append(constraint.getName()).append(" ").append(constraint.getType().toUpperCase()).append(" (");

        for (IndexColumn column : constraint.getColumns()) {
            sb.append(column.getName()).append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), ")");

        return sb.toString();
    }
}
