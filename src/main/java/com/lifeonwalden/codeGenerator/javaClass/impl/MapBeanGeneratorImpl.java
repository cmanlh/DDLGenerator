package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MapBeanGeneratorImpl extends BeanGeneratorImpl {

    @Override
    public String generate(Table table, Config config) {
        generateResultBean(table, config);
        //generateParamBean(table, config);
        if (table.getExtProps() != null && table.getExtProps().size() > 0) {
            generateExtParamBean(table, config);
        }
        return null;
    }

    protected void generateResultBean(Table table, Config config) {
        String className = NameUtil.getResultBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        ClassName supperClassName = ClassName.get(_className.packageName(), "MapBeanBase");// extends MapBeanBase
        Builder beanTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).superclass(supperClassName);

        for (Column column : table.getColumns()) {
            methodBuild(beanTypeBuilder, _className, column);
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateExtParamBean(Table table, Config config) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        boolean staticBlock = false;
        for (Column column : table.getExtProps()) {
            String javaType = column.getJavaType();
            if (null == javaType) {
                JdbcTypeEnum jdbcType = JdbcTypeEnum.nameOf(column.getType().toUpperCase());
                if (null == jdbcType) {
                    throw new RuntimeException("unknow jdbc type : " + column.getType().toUpperCase());
                }
                javaType = jdbcType.getJavaType();
            }
            ClassName javaTypeClassName = ClassName.bestGuess(javaType);
            if (!javaTypeClassName.equals(ClassName.get(String.class))) {
                staticBlock = true;
                codeBlockBuilder.addStatement("typeMap.put($S, $T.class)", StringUtil.removeUnderline(column.getName()), javaTypeClassName);
            }
        }

        String parentClassName = NameUtil.getParamBeanName(table, config);
        String className = NameUtil.getExtParamBeanName(table, config);
        ClassName _parentClassName = ClassName.get(config.getBeanInfo().getPackageName(), parentClassName);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder beanTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).superclass(_parentClassName);
        if (staticBlock) {
            beanTypeBuilder.addStaticBlock(codeBlockBuilder.build());
        }

        for (Column column : table.getExtProps()) {
            methodBuild(beanTypeBuilder, _className, column);
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void methodBuild(Builder beanBuilder, ClassName className, Column column) {
        String javaType = column.getJavaType();
        if (null == javaType) {
            JdbcTypeEnum jdbcType = JdbcTypeEnum.nameOf(column.getType().toUpperCase());
            if (null == jdbcType) {
                throw new RuntimeException("unknow jdbc type : " + column.getType().toUpperCase());
            }
            javaType = jdbcType.getJavaType();
        }

        ClassName javaTypeClassName = ClassName.bestGuess(javaType);
        buildSetMethod(beanBuilder, className, javaTypeClassName, column.getName(), column);
        buildGetMethod(beanBuilder, javaTypeClassName, column.getName(), column);

        if (javaTypeClassName.equals(ClassName.get(Date.class))) {
            String dateStartName = column.getName().concat("Start");
            buildSetMethod(beanBuilder, className, javaTypeClassName, dateStartName, column);
            buildGetMethod(beanBuilder, javaTypeClassName, dateStartName, column);

            String dateEndName = column.getName().concat("End");
            buildSetMethod(beanBuilder, className, javaTypeClassName, dateEndName, column);
            buildGetMethod(beanBuilder, javaTypeClassName, dateEndName, column);
        }
    }

    private void buildSetMethod(Builder beanBuilder, ClassName className, ClassName javaTypeClassName, String columnName, Column column) {
        MethodSpec.Builder setMethodBuilder =
                MethodSpec.methodBuilder("set" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(columnName))).returns(className)
                        .addModifiers(Modifier.PUBLIC).addParameter(javaTypeClassName, StringUtil.removeUnderline(columnName))
                        .addStatement("dataMap.put($S,$L)", StringUtil.removeUnderline(columnName), StringUtil.removeUnderline(columnName))
                        .addStatement("return this");
        if (column.getNote() != null && column.getNote().length() > 0) {
            setMethodBuilder.addJavadoc("$L", column.getNote());
        }
        beanBuilder.addMethod(setMethodBuilder.build());
    }

    private void buildGetMethod(Builder beanBuilder, ClassName javaTypeClassName, String columnName, Column column) {
        MethodSpec.Builder getMethodBuilder =
                MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(columnName))).addModifiers(Modifier.PUBLIC)
                        .returns(javaTypeClassName)
                        .addCode(CodeBlock.builder().addStatement("Object val = dataMap.get($S)", StringUtil.removeUnderline(columnName))
                                .addStatement("return val != null? ($T)val : null", javaTypeClassName)
                                .build());
        if (column.getNote() != null && column.getNote().length() > 0) {
            getMethodBuilder.addJavadoc("$L", column.getNote());
        }
        beanBuilder.addMethod(getMethodBuilder.build());
    }
}
