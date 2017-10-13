package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class BeanGeneratorImpl implements TableBasedGenerator {

    public static String getParamBeanName(Table table, Config config) {
        String namePattern = config.getBeanInfo().getParamNamePattern(), name;
        if (null == namePattern) {
            name = StringUtil.removeUnderline(table.getName());
        } else {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(StringUtil.removeUnderline(table.getName())));
        }

        return StringUtil.firstAlphToUpper(name);
    }

    public static String getExtParamBeanName(Table table, Config config) {
        String namePattern = config.getBeanInfo().getParamNamePattern(), name;
        if (null == namePattern) {
            name = StringUtil.removeUnderline(table.getName()) + "Ext";
        } else {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(StringUtil.removeUnderline(table.getName())) + "Ext");
        }

        return StringUtil.firstAlphToUpper(name);
    }

    public static String getResultBeanName(Table table, Config config) {
        String namePattern = config.getBeanInfo().getResultNamePattern(), name;
        if (null == namePattern) {
            name = StringUtil.removeUnderline(table.getName());
        } else {
            name = namePattern.replace("?", StringUtil.firstAlphToUpper(StringUtil.removeUnderline(table.getName())));
        }

        return StringUtil.firstAlphToUpper(name);
    }

    @Override
    public String generate(Table table, Config config) {
        generateParamBean(table, config);
        if (!getParamBeanName(table, config).equals(getResultBeanName(table, config))) {
            generateResultBean(table, config);
        }

        return null;
    }

    private void generateParamBean(Table table, Config config) {
        String className = getParamBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder dtoTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addSuperinterface(ClassName.get(Serializable.class));

        dtoTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", System.currentTimeMillis(), "L").build());

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
            dtoTypeBuilder.addField(FieldSpec.builder(javaTypeClassName, StringUtil.removeUnderline(column.getName()), Modifier.PRIVATE)
                    .addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(column.getName())))
                    .addModifiers(Modifier.PUBLIC).returns(javaTypeClassName)
                    .addStatement("return this.$L", StringUtil.removeUnderline(column.getName())).addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(column.getName())))
                    .returns(_className).addModifiers(Modifier.PUBLIC)
                    .addParameter(javaTypeClassName, StringUtil.removeUnderline(column.getName()))
                    .addStatement("this.$L = $L", StringUtil.removeUnderline(column.getName()), StringUtil.removeUnderline(column.getName()))
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
        String className = getResultBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder dtoTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addSuperinterface(ClassName.get(Serializable.class));

        dtoTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", System.currentTimeMillis(), "L").build());

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
            dtoTypeBuilder.addField(FieldSpec.builder(javaTypeClassName, StringUtil.removeUnderline(column.getName()), Modifier.PRIVATE)
                    .addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(column.getName())))
                    .addModifiers(Modifier.PUBLIC).returns(javaTypeClassName)
                    .addStatement("return this.$L", StringUtil.removeUnderline(column.getName())).addJavadoc("$L", column.getNote()).build());

            dtoTypeBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(column.getName())))
                    .returns(_className).addModifiers(Modifier.PUBLIC)
                    .addParameter(javaTypeClassName, StringUtil.removeUnderline(column.getName()))
                    .addStatement("this.$L = $L", StringUtil.removeUnderline(column.getName()), StringUtil.removeUnderline(column.getName()))
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
