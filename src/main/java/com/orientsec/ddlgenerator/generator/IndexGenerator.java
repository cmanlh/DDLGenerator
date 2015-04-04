package com.orientsec.ddlgenerator.generator;

import com.orientsec.ddlgenerator.Index;
import com.orientsec.ddlgenerator.config.IndexConfig;

public interface IndexGenerator {
	public String generator(Index index, IndexConfig config);
}
