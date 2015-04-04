package com.orientsec.ddlgenerator.generator;

import com.orientsec.ddlgenerator.Table;
import com.orientsec.ddlgenerator.config.TableConfig;

public interface TableGenerator {
	public String generator(Table table, TableConfig config);
}
