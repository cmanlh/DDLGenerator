package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.BeanTypeEnum;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.constant.SpecialInnerSuffix;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.StringUtil;
import com.lifeonwalden.codeGenerator.util.TableInfoUtil;
import com.lifeonwalden.forestbatis.biz.bean.AbstractMapBean;
import com.lifeonwalden.forestbatis.biz.bean.AbstractParamMapBean;
import com.lifeonwalden.forestbatis.biz.support.OrderBean;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * work with below mapping generator class, it's deprecated.
 *
 * @see com.lifeonwalden.codeGenerator.mybatis.impl.select.SQLDirectSelectElementGeneratorDeprecated
 * @see com.lifeonwalden.codeGenerator.mybatis.impl.update.SQLDirectUpdateElementGeneratorDeprecated
 * <p>
 * Please use below version
 * @see com.lifeonwalden.codeGenerator.javaClass.impl.HashBeanGeneratorImpl
 */
@Deprecated
public class HashBeanGeneratorImplDepracted extends BeanGeneratorImpl {

    @Override
    public String generate(Table table, Config config) {
        generateResultBean(table, config);
        generateParamBean(table, config);
        if (table.getExtProps() != null && table.getExtProps().size() > 0) {
            generateExtParamBean(table, config);
        }
        return null;
    }

    private void generateResultBean(Table table, Config config) {
        String className = NameUtil.getResultBeanName(table, config);
        ClassName beanClass = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder beanTypeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
                .superclass(AbstractMapBean.class);
        beanTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", TableInfoUtil.getSerialVersionUID(table, BeanTypeEnum.HASH), "L").build());

        String orderByProperty = "orderBy", orderByParameter = "orderList";
        ParameterizedTypeName orderByParamType = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(OrderBean.class));
        beanTypeBuilder.addMethod(MethodSpec.methodBuilder("set".concat(StringUtil.firstAlphToUpper(orderByProperty))).returns(beanClass)
                .addModifiers(Modifier.PUBLIC).addParameter(orderByParamType, orderByParameter)
                .addStatement("dataMap.put($S,$L)", orderByProperty, orderByParameter)
                .addStatement("return this").build());
        beanTypeBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(orderByProperty)).addModifiers(Modifier.PUBLIC)
                .returns(orderByParamType)
                .addCode(CodeBlock.builder().addStatement("Object val = dataMap.get($S)", orderByProperty)
                        .beginControlFlow("if (null == val)").addStatement("return null").endControlFlow().addStatement("return ($T)val", orderByParamType)
                        .build()).build());

        for (Column column : table.getColumns()) {
            String propertyName = StringUtil.removeUnderline(column.getName());
            beanTypeBuilder.addField(FieldSpec.builder(String.class, StringUtil.firstAlphToUpper(propertyName), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("\"$L\"", propertyName).build());
            methodBuild(beanTypeBuilder, beanClass, column, true, true);
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateParamBean(Table table, Config config) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        boolean staticBlock = false;
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
            if (!javaTypeClassName.equals(ClassName.get(String.class))) {
                staticBlock = true;
                codeBlockBuilder.addStatement("typeMap.put($S, $T.class)", propertyName, javaTypeClassName);
                if (javaTypeClassName.equals(ClassName.get(Date.class))) {
                    codeBlockBuilder.addStatement("typeMap.put($S, $T.class)", propertyName.concat(SpecialInnerSuffix.START), javaTypeClassName);
                    codeBlockBuilder.addStatement("typeMap.put($S, $T.class)", propertyName.concat(SpecialInnerSuffix.END), javaTypeClassName);
                }
            }
        }

        String className = NameUtil.getParamBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder beanTypeBuilder =
                TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
                        .superclass(AbstractParamMapBean.class);

        beanTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", TableInfoUtil.getSerialVersionUID(table, BeanTypeEnum.HASH_PARAM), "L").build());

        if (staticBlock) {
            beanTypeBuilder.addStaticBlock(codeBlockBuilder.build());
        }

        for (Column column : table.getColumns()) {
            methodBuild(beanTypeBuilder, _className, column, true, false);
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

        beanTypeBuilder.addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$L$L", TableInfoUtil.getSerialVersionUID(table, BeanTypeEnum.HASH_EXT_PARAM), "L").build());

        if (staticBlock) {
            beanTypeBuilder.addStaticBlock(codeBlockBuilder.build());
        }

        for (Column column : table.getExtProps()) {
            methodBuild(beanTypeBuilder, _className, column, false, false);
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void methodBuild(Builder beanBuilder, ClassName beanClass, Column column, boolean advanced, boolean complicated) {
        String javaType = column.getJavaType();
        if (null == javaType) {
            JdbcTypeEnum jdbcType = JdbcTypeEnum.nameOf(column.getType().toUpperCase());
            if (null == jdbcType) {
                throw new RuntimeException("unknow jdbc type : " + column.getType().toUpperCase());
            }
            javaType = jdbcType.getJavaType();
        }
        ClassName javaTypeClassName = ClassName.bestGuess(javaType);
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(List.class), javaTypeClassName);
        String propertyName = StringUtil.removeUnderline(column.getName());

        buildSetMethod(beanBuilder, beanClass, javaTypeClassName, propertyName, column);
        buildGetMethod(beanBuilder, javaTypeClassName, propertyName, column);

        if (advanced) {
            if (TableInfoUtil.allowedDateRange(column)) {
                String dateStartName = propertyName.concat(SpecialInnerSuffix.START);
                buildSetMethod(beanBuilder, beanClass, javaTypeClassName, dateStartName, column);
                buildGetMethod(beanBuilder, javaTypeClassName, dateStartName, column);

                String dateEndName = propertyName.concat(SpecialInnerSuffix.END);
                buildSetMethod(beanBuilder, beanClass, javaTypeClassName, dateEndName, column);
                buildGetMethod(beanBuilder, javaTypeClassName, dateEndName, column);
            }
        }

        if (complicated) {
            String pickedName = propertyName.concat(SpecialInnerSuffix.PICKED);
            buildSetMethod(beanBuilder, beanClass, ClassName.get(Boolean.class), pickedName, column);
            buildGetMethod(beanBuilder, ClassName.get(Boolean.class), pickedName, column);

            if (column.isEnableIn()) {
                String inName = propertyName.concat(SpecialInnerSuffix.IN);
                buildSetMethod(beanBuilder, beanClass, parameterizedTypeName, inName, column);
                buildGetMethod(beanBuilder, parameterizedTypeName, inName, column);
            }

            if (column.isEnableNotIn()) {
                String notInName = propertyName.concat(SpecialInnerSuffix.NOT_IN);
                buildSetMethod(beanBuilder, beanClass, parameterizedTypeName, notInName, column);
                buildGetMethod(beanBuilder, parameterizedTypeName, notInName, column);
            }

            if (column.isEnableLike() && TableInfoUtil.allowedLike(column)) {
                String likeName = propertyName.concat(SpecialInnerSuffix.LIKE);
                buildSetMethod(beanBuilder, beanClass, javaTypeClassName, likeName, column);
                buildGetMethod(beanBuilder, javaTypeClassName, likeName, column);
            }

            if (column.isEnableNotLike() && TableInfoUtil.allowedLike(column)) {
                String notLikeName = propertyName.concat(SpecialInnerSuffix.NOT_LIKE);
                buildSetMethod(beanBuilder, beanClass, javaTypeClassName, notLikeName, column);
                buildGetMethod(beanBuilder, javaTypeClassName, notLikeName, column);
            }
        }
    }

    private void buildSetMethod(Builder beanBuilder, ClassName className, TypeName javaTypeClassName, String propertyName, Column column) {
        MethodSpec.Builder setMethodBuilder =
                MethodSpec.methodBuilder("set" + StringUtil.firstAlphToUpper(propertyName)).returns(className)
                        .addModifiers(Modifier.PUBLIC).addParameter(javaTypeClassName, propertyName)
                        .addStatement("dataMap.put($S,$L)", propertyName, propertyName)
                        .addStatement("return this");
        if (column.getNote() != null && column.getNote().length() > 0) {
            setMethodBuilder.addJavadoc("$L", column.getNote());
        }
        beanBuilder.addMethod(setMethodBuilder.build());
    }

    private void buildGetMethod(Builder beanBuilder, TypeName javaTypeClassName, String propertyName, Column column) {
        MethodSpec.Builder getMethodBuilder =
                MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(propertyName)).addModifiers(Modifier.PUBLIC)
                        .returns(javaTypeClassName)
                        .addCode(CodeBlock.builder().addStatement("Object val = dataMap.get($S)", propertyName)
                                .beginControlFlow("if (null == val)").addStatement("return null").endControlFlow().addStatement("return ($T)val", javaTypeClassName)
                                .build());
        if (column.getNote() != null && column.getNote().length() > 0) {
            getMethodBuilder.addJavadoc("$L", column.getNote());
        }
        beanBuilder.addMethod(getMethodBuilder.build());
    }
}
