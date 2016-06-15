/**
 * 
 */
package com.lifeonwalden.codeGenerator;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

/**
 * Generator based on Table
 * 
 * @author luhong
 *
 */
public interface TableBasedGenerator {

  public String generate(Table table, Config config);
}
