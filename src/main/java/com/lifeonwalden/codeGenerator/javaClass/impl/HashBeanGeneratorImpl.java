package com.lifeonwalden.codeGenerator.javaClass.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFileTmp;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import com.squareup.javapoet.WildcardTypeName;

public class HashBeanGeneratorImpl extends BeanGeneratorImpl {

  @Override
  public String generate(Table table, Config config) {
    String className = getBeanName(table, config);
    ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
    Builder beanTypeBuilder =
        TypeSpec
            .classBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ParameterizedTypeName.get(Map.class, String.class, Object.class))
            .addField(
                FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, Object.class), "dataMap", Modifier.PRIVATE)
                    .initializer("new $T<String,Object>()", HashMap.class).build());

    beanTypeBuilder
        .addMethod(
            MethodSpec.methodBuilder("size").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(int.class)
                .addStatement("return dataMap.size()").build())
        .addMethod(
            MethodSpec.methodBuilder("isEmpty").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(boolean.class)
                .addStatement("return dataMap.isEmpty()").build())
        .addMethod(
            MethodSpec.methodBuilder("containsKey").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(boolean.class)
                .addParameter(Object.class, "key").addStatement("return dataMap.containsKey($L)", "key").build())
        .addMethod(
            MethodSpec.methodBuilder("containsValue").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(boolean.class)
                .addParameter(Object.class, "key").addStatement("return dataMap.containsValue($L)", "key").build())
        .addMethod(
            MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(Object.class)
                .addParameter(Object.class, "key").addStatement("return dataMap.get($L)", "key").build())
        .addMethod(
            MethodSpec.methodBuilder("put").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(Object.class)
                .addParameter(String.class, "key").addParameter(Object.class, "value").addStatement("return dataMap.put($L, $L)", "key", "value")
                .build())
        .addMethod(
            MethodSpec.methodBuilder("remove").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(Object.class)
                .addParameter(Object.class, "key").addStatement("return dataMap.remove($L)", "key").build())
        .addMethod(
            MethodSpec
                .methodBuilder("putAll")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(
                    ParameterizedTypeName.get(ClassName.get(Map.class), WildcardTypeName.subtypeOf(String.class),
                        WildcardTypeName.subtypeOf(Object.class)), "m").addStatement("dataMap.putAll($L)", "m").build())
        .addMethod(
            MethodSpec.methodBuilder("clear").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class)
                .addStatement("dataMap.clear()").build())
        .addMethod(
            MethodSpec.methodBuilder("keySet").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Set.class, String.class)).addStatement("return dataMap.keySet()").build())
        .addMethod(
            MethodSpec.methodBuilder("values").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Collection.class, Object.class)).addStatement("return dataMap.values()").build())
        .addMethod(
            MethodSpec.methodBuilder("entrySet").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Set.class), ParameterizedTypeName.get(Map.Entry.class, String.class, Object.class)))
                .addStatement("return dataMap.entrySet()").build());

    for (Column column : table.getColumns()) {
      String javaType = column.getJavaType();
      if (null == javaType) {
        JdbcTypeEnum jdbcType = JdbcTypeEnum.nameOf(column.getType().toUpperCase());
        if (null == jdbcType) {
          throw new RuntimeException("unknow jdbc type : " + column.getType().toUpperCase());
        }
        javaType = jdbcType.getJavaType();
      }

      ClassName javaTypeClassName = ClassName.bestGuess(javaType);
      beanTypeBuilder.addField(FieldSpec.builder(javaTypeClassName, column.getName(), Modifier.PRIVATE).addJavadoc("$L", column.getNote()).build());

      beanTypeBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(column.getName())).addModifiers(Modifier.PUBLIC)
          .returns(javaTypeClassName).addStatement("return ($T)dataMap.get($S)", javaTypeClassName, column.getName())
          .addJavadoc("$L", column.getNote()).build());

      beanTypeBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtil.firstAlphToUpper(column.getName())).returns(_className)
          .addModifiers(Modifier.PUBLIC).addParameter(javaTypeClassName, column.getName())
          .addStatement("dataMap.put($S,$L)", column.getName(), column.getName()).addStatement("return this").addJavadoc("$L", column.getNote())
          .build());
    }

    try {
      JavaFileTmp
          .builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build())
          .build()
          .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()),
              config.getEncoding());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
