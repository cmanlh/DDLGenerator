package com.orientsec.ddlgenerator.generator.sqlserverImpl;

import java.util.List;

import com.orientsec.ddlgenerator.Column;
import com.orientsec.ddlgenerator.Constraint;
import com.orientsec.ddlgenerator.Index;
import com.orientsec.ddlgenerator.Table;
import com.orientsec.ddlgenerator.config.IndexConfig;
import com.orientsec.ddlgenerator.config.TableConfig;
import com.orientsec.ddlgenerator.generator.ColumnGenerator;
import com.orientsec.ddlgenerator.generator.TableGenerator;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class TableGeneratorImpl implements TableGenerator {

	@Override
	public String generator(Table table, TableConfig config) {
		StringBuilder sb = new StringBuilder();
		String schemaPrefix = "";

		sb.append("IF EXISTS (SELECT * FROM SYS.TABLES WHERE NAME = '").append(table.getName())
				.append("' AND TYPE = 'U'");
		if (null != config && null != config.getSchema() && config.getSchema().length() > 0) {
			sb.append("AND SCHEMA_NAME(SCHEMA_ID) = '").append(config.getSchema()).append("'");
			schemaPrefix = config.getSchema() + ".";
		}
		sb.append(")").append(OutputUtil.LINE_SEPERATOR);
		sb.append(OutputUtil.INDENT_SIZE).append("DROP TABLE ").append(schemaPrefix).append(table.getName())
				.append(";").append(OutputUtil.LINE_SEPERATOR);
		sb.append("GO").append(OutputUtil.LINE_SEPERATOR);
		sb.append(OutputUtil.LINE_SEPERATOR);

		ColumnGenerator columnGenerator = new ColumnGeneratorImpl();
		sb.append("CREATE TABLE ").append(schemaPrefix).append(table.getName()).append("(")
				.append(OutputUtil.LINE_SEPERATOR);
		List<Column> columnList = table.getColumn();
		sb.append(OutputUtil.INDENT_SIZE).append(columnGenerator.generator(columnList.get(0)))
				.append(OutputUtil.LINE_SEPERATOR);
		for (int i = 1; i < columnList.size(); i++) {
			sb.append(OutputUtil.INDENT_SIZE).append(",").append(columnGenerator.generator(columnList.get(i)))
					.append(OutputUtil.LINE_SEPERATOR);
		}

		ConstraintGeneratorImpl constraintGenerator = new ConstraintGeneratorImpl();
		for (Constraint constraint : table.getConstraint()) {
			sb.append(OutputUtil.INDENT_SIZE).append(",").append(constraintGenerator.generator(constraint))
					.append(OutputUtil.LINE_SEPERATOR);
		}
		sb.append(");").append(OutputUtil.LINE_SEPERATOR);
		sb.append(OutputUtil.LINE_SEPERATOR);

		IndexConfig indexCommon = new IndexConfig();
		indexCommon.setTable(table.getName());
		indexCommon.setTableConfig(config);
		IndexGeneratorImpl indexGenerator = new IndexGeneratorImpl();
		for (Index index : table.getIndex()) {
			sb.append(indexGenerator.generator(index, indexCommon)).append(OutputUtil.LINE_SEPERATOR);
		}

		return sb.toString();
	}
}
