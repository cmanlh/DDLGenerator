package com.lifeonwalden.codeGenerator.dll;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface ColumnGenerator {
    String generate(Column column, Config config);
}
