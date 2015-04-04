package com.orientsec.ddlgenerator.builder;

import com.orientsec.ddlgenerator.Table;
import com.orientsec.ddlgenerator.config.TableBuildingConfig;
import com.orientsec.ddlgenerator.generator.TableGenerator;
import com.orientsec.ddlgenerator.generator.sqlserverImpl.TableGeneratorImpl;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class TableBuilder {
	public String build(TableBuildingConfig config) {
		StringBuilder sb = new StringBuilder();

		if (null != config.getConfig()) {
			sb.append("USE ").append(config.getConfig().getDatabase()).append(";").append(OutputUtil.LINE_SEPERATOR);
			sb.append(OutputUtil.LINE_SEPERATOR);
		}

		TableGenerator tableGenerator = new TableGeneratorImpl();
		for (Table table : config.getTable()) {
			sb.append(tableGenerator.generator(table, config.getConfig()));
		}
		return sb.toString();
	}
}
