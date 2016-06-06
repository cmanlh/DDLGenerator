/**
 * 
 */
package com.lifeonwalden.codeGenerator.javaClass;

import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;

/**
 * @author luhong
 *
 */
public interface DAOGenerator {

  public void generate(Table table, Config config);
}
