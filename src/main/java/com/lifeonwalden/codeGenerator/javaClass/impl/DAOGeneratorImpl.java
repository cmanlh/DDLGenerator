package com.lifeonwalden.codeGenerator.javaClass.impl;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFileTmp;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class DAOGeneratorImpl implements TableBasedGenerator {

  public static String getBeanName(Table table, Config config) {
    String namePattern = config.getDaoInfo().getNamePattern(), name;
    if (null == namePattern) {
      name = table.getName() + "Mapper";
    } else {
      name = namePattern.replace("?", StringUtil.firstAlphToUpper(table.getName()));
    }

    return StringUtil.firstAlphToUpper(name);
  }

  @Override
  public String generate(Table table, Config config) {
    String className = getBeanName(table, config);
    Builder daoTypeBuilder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
    ClassName beanClass = ClassName.get(config.getBeanInfo().getPackageName(), BeanGeneratorImpl.getBeanName(table, config));

    daoTypeBuilder.addMethod(MethodSpec.methodBuilder("insert").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
        .addParameter(beanClass, "param").build());

    if (null != table.getPrimaryColumns()) {
      daoTypeBuilder.addMethod(MethodSpec.methodBuilder("update").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
          .addParameter(beanClass, "param").build());

      daoTypeBuilder.addMethod(MethodSpec.methodBuilder("updateDynamic").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
          .addParameter(beanClass, "param").build());

      com.squareup.javapoet.MethodSpec.Builder getBuilder =
          MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addParameter(beanClass, "param").returns(beanClass);

      com.squareup.javapoet.MethodSpec.Builder deleteBuilder =
          MethodSpec.methodBuilder("delete").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addParameter(beanClass, "param").returns(Integer.class);

      daoTypeBuilder.addMethod(getBuilder.build());
      daoTypeBuilder.addMethod(deleteBuilder.build());

      if (null == table.getAddDBFields() || table.getAddDBFields()) {
        com.squareup.javapoet.MethodSpec.Builder logicalDeleteBuilder =
            MethodSpec.methodBuilder("logicalDelete").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addParameter(beanClass, "param")
                .returns(Integer.class);

        daoTypeBuilder.addMethod(logicalDeleteBuilder.build());
      }
    }

    try {
      JavaFileTmp.builder(config.getDaoInfo().getPackageName(), daoTypeBuilder.build()).build()
          .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getDaoInfo().getFolderName()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
