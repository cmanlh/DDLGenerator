package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.BeanTypeEnum;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.lifeonwalden.codeGenerator.util.TableInfoUtil;
import com.lifeonwalden.forestbatis.biz.bean.BaseBean;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class BeanGeneratorImpl implements TableBasedGenerator {

    @Override
    public String generate(Table table, Config config) {
        generateParamBean(table, config);
        if (!NameUtil.getParamBeanName(table, config).equals(NameUtil.getResultBeanName(table, config))) {
            generateResultBean(table, config);
        }

        return null;
    }

    private void generateParamBean(Table table, Config config) {
        String className = NameUtil.getParamBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder dtoTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addSuperinterface(ClassName.get(BaseBean.class));

        dtoTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", TableInfoUtil.getSerialVersionUID(table, BeanTypeEnum.POJO_PARAM), "L").build());

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
            String propertyName = StringUtil.removeUnderline(column.getName());
            String methodNameFragment = StringUtil.firstAlphToUpper(propertyName);
            dtoTypeBuilder.addField(FieldSpec.builder(javaTypeClassName, propertyName, Modifier.PRIVATE)
                    .addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("get" + methodNameFragment)
                    .addModifiers(Modifier.PUBLIC).returns(javaTypeClassName)
                    .addStatement("return this.$L", propertyName).addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("set" + methodNameFragment)
                    .returns(_className).addModifiers(Modifier.PUBLIC)
                    .addParameter(javaTypeClassName, propertyName)
                    .addStatement("this.$L = $L", propertyName, propertyName)
                    .addStatement("return this").addJavadoc("$L", column.getNote()).build());
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), dtoTypeBuilder.build())
                    .build()
                    .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()),
                            config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateResultBean(Table table, Config config) {
        String className = NameUtil.getResultBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder dtoTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addSuperinterface(ClassName.get(BaseBean.class));

        dtoTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", TableInfoUtil.getSerialVersionUID(table, BeanTypeEnum.POJO), "L").build());

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
            String propertyName = StringUtil.removeUnderline(column.getName());
            String methodNameFragment = StringUtil.firstAlphToUpper(propertyName);
            dtoTypeBuilder.addField(FieldSpec.builder(javaTypeClassName, propertyName, Modifier.PRIVATE)
                    .addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("get" + methodNameFragment)
                    .addModifiers(Modifier.PUBLIC).returns(javaTypeClassName)
                    .addStatement("return this.$L", propertyName).addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("set" + methodNameFragment)
                    .returns(_className).addModifiers(Modifier.PUBLIC)
                    .addParameter(javaTypeClassName, propertyName)
                    .addStatement("this.$L = $L", propertyName, propertyName)
                    .addStatement("return this").addJavadoc("$L", column.getNote()).build());
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), dtoTypeBuilder.build())
                    .build()
                    .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()),
                            config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
