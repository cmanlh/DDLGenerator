package com.lifeonwalden.codeGenerator.dll;

import com.lifeonwalden.codeGenerator.bean.Index;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface IndexGenerator {
  public String generate(Index index, Config config);
}
