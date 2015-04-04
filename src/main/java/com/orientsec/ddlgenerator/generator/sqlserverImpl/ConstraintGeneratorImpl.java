package com.orientsec.ddlgenerator.generator.sqlserverImpl;

import com.orientsec.ddlgenerator.Constraint;
import com.orientsec.ddlgenerator.generator.ConstraintGenerator;

public class ConstraintGeneratorImpl implements ConstraintGenerator {

	@Override
	public String generator(Constraint constraint) {
		StringBuilder sb = new StringBuilder();
		sb.append("CONSTRAINT ").append(constraint.getName()).append(" ").append(constraint.getType()).append(" (");

		for (String column : constraint.getColumn()) {
			sb.append(column).append(", ");
		}

		sb.replace(sb.length() - 2, sb.length(), ")");

		return sb.toString();
	}
}
