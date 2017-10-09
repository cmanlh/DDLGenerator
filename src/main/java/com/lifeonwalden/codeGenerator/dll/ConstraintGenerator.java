package com.lifeonwalden.codeGenerator.dll;

import com.lifeonwalden.codeGenerator.bean.Constraint;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface ConstraintGenerator {
    String generate(Constraint constraint, Config config);
}
