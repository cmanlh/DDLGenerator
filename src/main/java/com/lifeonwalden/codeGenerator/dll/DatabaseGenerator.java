package com.lifeonwalden.codeGenerator.dll;

import com.lifeonwalden.codeGenerator.bean.Database;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface DatabaseGenerator {
    String generate(Database database, Config config);
}
