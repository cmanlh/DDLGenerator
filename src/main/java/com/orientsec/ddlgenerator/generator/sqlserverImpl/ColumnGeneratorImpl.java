package com.orientsec.ddlgenerator.generator.sqlserverImpl;

import com.orientsec.ddlgenerator.Column;
import com.orientsec.ddlgenerator.ValueEnum;
import com.orientsec.ddlgenerator.generator.ColumnGenerator;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class ColumnGeneratorImpl implements ColumnGenerator {

	@Override
	public String generator(Column column) {
		StringBuilder sb = new StringBuilder();
		sb.append(column.getName()).append(" ").append(column.getType());
		if (column.isRequired()) {
			sb.append(" ").append("NOT NULL");
		}

		if (null != column.getDefaultVal() && column.getDefaultVal().length() > 0) {
			String dataType = column.getType().toUpperCase();
			if (dataType.contains("CHAR")) {
				sb.append(" DEFAULT ").append("'").append(column.getDefaultVal()).append("'");
			} else {
				sb.append(" DEFAULT ").append(column.getDefaultVal());
			}
		}

		if (null != column.getNote() && column.getNote().length() > 0) {
			sb.append(OutputUtil.INDENT_SIZE).append(" -- ").append(column.getNote());

			if (null != column.getValueEnum() && column.getValueEnum().size() > 0) {
				sb.append(OutputUtil.INDENT_SIZE).append("[");
				for (ValueEnum ve : column.getValueEnum()) {
					sb.append("(").append(ve.getValue()).append(":").append(ve.getDesc()).append(") ");
				}
				sb.append("]");
			}
		} else if (null != column.getValueEnum() && column.getValueEnum().size() > 0) {
			sb.append(OutputUtil.INDENT_SIZE).append(" -- [");
			for (ValueEnum ve : column.getValueEnum()) {
				sb.append("(").append(ve.getValue()).append(":").append(ve.getDesc()).append(") ");
			}
			sb.append("]");
		}

		return sb.toString();
	}
}
