package com.lifeonwalden.codeGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Constraint;
import com.lifeonwalden.codeGenerator.bean.Database;
import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.IndexColumn;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Generator;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.dll.TableGenerator;
import com.lifeonwalden.codeGenerator.dll.impl.MySQLTableGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.BeanGenerator;
import com.lifeonwalden.codeGenerator.javaClass.DAOGenerator;
import com.lifeonwalden.codeGenerator.javaClass.DTOGenerator;
import com.lifeonwalden.codeGenerator.javaClass.EnumGenerator;
import com.lifeonwalden.codeGenerator.javaClass.impl.BeanGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.DAOGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.DTOGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.EnumGeneratorImpl;
import com.lifeonwalden.codeGenerator.js.JsEnumGenerator;
import com.lifeonwalden.codeGenerator.js.impl.JsEnumGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.impl.XMLMapperGenerator;
import com.thoughtworks.xstream.XStream;

public class GenerateCodeMain {
  public static void main(String[] args) {
    File configFileLocation = new File(args[0]);
    if (!configFileLocation.exists() && !configFileLocation.isDirectory()) {
      System.out.println("The directory is not exist.");
    }

    for (File templateFile : configFileLocation.listFiles()) {
      XStream xStream = new XStream();
      xStream.processAnnotations(Generator.class);
      Generator generator = (Generator) xStream.fromXML(templateFile);
      init(generator);
      Database database = generator.getDatabase();
      if (null != database.getConstPool()) {
        EnumGenerator enumJavaClassGenerator = new EnumGeneratorImpl();
        for (EnumConst enumConst : database.getConstPool()) {
          enumJavaClassGenerator.generate(enumConst, generator.getConfig());
        }

        JsEnumGenerator jsEnumGenerator = new JsEnumGeneratorImpl();
        jsEnumGenerator.generate(database.getConstPool(), generator.getConfig());
      }
      TableGenerator tableGenerator = new MySQLTableGeneratorImpl();
      DAOGenerator daoGenerator = new DAOGeneratorImpl();
      DTOGenerator dtoGenerator = new DTOGeneratorImpl();
      BeanGenerator beanGenerator = new BeanGeneratorImpl();

      XMLMapperGenerator xmlMapperGenerator = new XMLMapperGenerator();
      StringBuilder sqlBuilder = new StringBuilder();
      for (Table table : database.getTables()) {
        sqlBuilder.append(tableGenerator.generator(table, generator.getConfig()));
        daoGenerator.generate(table, generator.getConfig());
        dtoGenerator.generate(table, generator.getConfig());
        beanGenerator.generate(table, generator.getConfig());

        xmlMapperGenerator.generate(table, generator.getConfig());
      }
      try {
        String file =
            new File(generator.getConfig().getOutputLocation()).getPath() + "\\" + database.getName() + ".sql";
        BufferedWriter bw =
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), generator.getConfig().getEncoding()));
        bw.write(sqlBuilder.toString());
        bw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void init(Generator generator) {
    Database database = generator.getDatabase();
    Map<String, EnumConst> constMapping = new HashMap<String, EnumConst>();
    if (null != database.getConstMapping()) {
      for (EnumConst enumCost : database.getConstPool()) {
        constMapping.put(enumCost.getName(), enumCost);
      }
      database.setConstMapping(constMapping);
    }

    for (Table table : database.getTables()) {
      if (table.getAddDBFields() == null || table.getAddDBFields()) {
        List<Column> columnList = table.getColumns();
        for (Column column : database.getDbFields()) {
          columnList.add(column.copy());
        }
      }

      Map<String, Column> columnMapping = new HashMap<String, Column>();
      for (Column column : table.getColumns()) {
        columnMapping.put(column.getName(), column);

        if (null != column.getOptionRef()) {
          column.setOptionRefObj(constMapping.get(column.getOptionRef()));
        }

      }
      table.setColumnMapping(columnMapping);

      if (null != table.getConstraints()) {
        List<Constraint> constraintList = table.getConstraints();
        for (Constraint constraint : constraintList) {
          ColumnConstraintEnum constraintEnum = ColumnConstraintEnum.forAlias(constraint.getType().toUpperCase());

          if (ColumnConstraintEnum.PRIMARY_KEY == constraintEnum) {
            List<Column> primaryCols = new ArrayList<Column>();

            for (IndexColumn indexColumn : constraint.getColumns()) {
              Column column = columnMapping.get(indexColumn.getName());
              primaryCols.add(column);
              if (constraint.getColumns().size() > 0) {
                column.setConstraintType(ColumnConstraintEnum.UNION_PRIMARY_KEY);
              } else {
                column.setConstraintType(ColumnConstraintEnum.PRIMARY_KEY);
              }
            }

            table.setPrimaryColumns(primaryCols);
          }
        }
      }
    }
  }
}
