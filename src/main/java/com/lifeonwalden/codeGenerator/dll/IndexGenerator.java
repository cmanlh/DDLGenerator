package com.lifeonwalden.codeGenerator.dll;

import com.lifeonwalden.codeGenerator.bean.Index;
import com.lifeonwalden.codeGenerator.bean.config.Config;

public interface IndexGenerator {
  public String generator(Index index, Config config);
}
