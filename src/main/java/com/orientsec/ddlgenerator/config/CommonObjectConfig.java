package com.orientsec.ddlgenerator.config;

import java.io.Serializable;
import java.util.List;

public class CommonObjectConfig implements Serializable {
    private static final long serialVersionUID = -1539149789645231264L;

    private String outputPath;

    private String packageName;

    private List<EnumConfig> enums;

    public List<EnumConfig> getEnums() {
        return enums;
    }

    public void setEnums(List<EnumConfig> enums) {
        this.enums = enums;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
