package com.lifeonwalden.codeGenerator.mybatis;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface DomGenerator {

	public void generate(Table table, Config config);
}
