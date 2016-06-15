/**
 * 
 */
package com.lifeonwalden.codeGenerator.javaClass;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

/**
 * Generator based on Table
 * 
 * @author luhong
 *
 */
public interface TableBasedGenerator {

  public void generate(Table table, Config config);
}
