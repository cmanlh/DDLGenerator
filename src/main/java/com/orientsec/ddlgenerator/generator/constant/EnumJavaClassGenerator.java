package com.orientsec.ddlgenerator.generator.constant;

import com.orientsec.ddlgenerator.config.EnumConfig;

public interface EnumJavaClassGenerator {
    public void generate(EnumConfig enumConfig, String packageName, String outputPath);
}
