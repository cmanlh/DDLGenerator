package com.lifeonwalden.codeGenerator.util;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface NameUtil {
    static String getClassName(Table table, Config config) {
        return config.getBeanInfo().getPackageName() + "." + getResultBeanName(table, config);
    }

    static String getNamespace(Table table, Config config) {
        return config.getDaoInfo().getPackageName().concat(".").concat(getDaoName(table, config));
    }

    static String getParamBeanName(Table table, Config config) {
        String namePattern = config.getBeanInfo().getParamNamePattern(), name = StringUtil.removeUnderline(table.getName());
        if (StringUtil.isNotBlank(namePattern)) {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(name));
        }

        return StringUtil.firstAlphToUpper(name);
    }

    static String getExtParamBeanName(Table table, Config config) {
        String namePattern = config.getBeanInfo().getParamNamePattern(), name, _name = StringUtil.removeUnderline(table.getName());
        if (null == namePattern) {
            name = _name.concat("Ext");
        } else {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(_name).concat("Ext"));
        }

        return StringUtil.firstAlphToUpper(name);
    }

    static String getResultBeanName(Table table, Config config) {
        String namePattern = config.getBeanInfo().getResultNamePattern(), name = StringUtil.removeUnderline(table.getName());
        if (StringUtil.isNotBlank(namePattern)) {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(name));
        }

        return StringUtil.firstAlphToUpper(name);
    }

    static String getDaoName(Table table, Config config) {
        String namePattern = config.getDaoInfo().getNamePattern(), name, _name = StringUtil.removeUnderline(table.getName());
        if (null == namePattern) {
            name = _name.concat("Mapper");
        } else {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(_name));
        }

        return StringUtil.firstAlphToUpper(name);
    }

    static String getColumnPrefix(Table table) {
        return "pre_".concat(table.getName().toLowerCase()).concat(".");
    }
}
