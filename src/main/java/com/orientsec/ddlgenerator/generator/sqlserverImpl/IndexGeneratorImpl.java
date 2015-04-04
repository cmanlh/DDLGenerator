package com.orientsec.ddlgenerator.generator.sqlserverImpl;

import java.util.List;

import com.orientsec.ddlgenerator.Index;
import com.orientsec.ddlgenerator.IndexColumn;
import com.orientsec.ddlgenerator.config.IndexConfig;
import com.orientsec.ddlgenerator.generator.IndexGenerator;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class IndexGeneratorImpl implements IndexGenerator {

	@Override
	public String generator(Index index, IndexConfig config) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE ").append(index.getType()).append(" INDEX ").append(index.getName()).append(" ON ");

		if (null != config.getTableConfig() && null != config.getTableConfig().getSchema()
				&& config.getTableConfig().getSchema().length() > 0) {
			sb.append(config.getTableConfig().getSchema()).append(".");
		}

		sb.append(config.getTable()).append(" (").append(OutputUtil.LINE_SEPERATOR);

		List<IndexColumn> indexColumnList = index.getColumn();
		sb.append(OutputUtil.INDENT_SIZE).append(indexColumnList.get(0).getName()).append(" ")
				.append(indexColumnList.get(0).getOrder()).append(OutputUtil.LINE_SEPERATOR);
		for (int i = 1; i < indexColumnList.size(); i++) {
			sb.append(OutputUtil.INDENT_SIZE).append(",").append(indexColumnList.get(i).getName()).append(" ")
					.append(indexColumnList.get(i).getOrder()).append(OutputUtil.INDENT_SIZE);
		}
		sb.append(");");

		return sb.toString();
	}
}
