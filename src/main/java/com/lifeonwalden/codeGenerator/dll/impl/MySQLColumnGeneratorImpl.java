package com.lifeonwalden.codeGenerator.dll.impl;

import java.util.List;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.ValueEnum;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.dll.ColumnGenerator;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

public class MySQLColumnGeneratorImpl implements ColumnGenerator {

	@Override
	public String generator(Column column, Config config) {
		StringBuilder sb = new StringBuilder();
		sb.append(column.getName()).append(" ").append(column.getType());
		if (null != column.getLength()) {
			sb.append("(").append(column.getLength()).append(")");
		}

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

		List<ValueEnum> options = null;
		if (null != column.getOptionRefObj()) {
			options = column.getOptionRefObj().getOptions();
		} else if (null != column.getOptions()) {
			options = column.getOptions();
		}

		if (null != column.getNote() && column.getNote().length() > 0) {
			OutputUtilities.textIndent(sb, 1).append(" COMMENT '").append(column.getNote());

			if (null != options && options.size() > 0) {
				OutputUtilities.textIndent(sb, 1).append("[");

				for (ValueEnum ve : options) {
					sb.append("(").append(ve.getValue());
					if (null != ve.getDesc()) {
						sb.append(":").append(ve.getDesc());
					}
					sb.append(") ");
				}
				sb.append("]'");
			} else {
				sb.append("'");
			}
		} else if (null != options && options.size() > 0) {
			OutputUtilities.textIndent(sb, 1).append(" COMMENT '[");
			for (ValueEnum ve : options) {
				sb.append("(").append(ve.getValue());
				if (null != ve.getDesc()) {
					sb.append(":").append(ve.getDesc());
				}
				sb.append(") ");
			}
			sb.append("]'");
		}

		return sb.toString();
	}
}
