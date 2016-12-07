package com.lifeonwalden.codeGenerator.javaClass.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import com.squareup.javapoet.CodeBlock;
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
        generateResultBean(table, config);
        generateParamBean(table, config);
        if (table.getExtProps() != null && table.getExtProps().size() > 0) {
            generateExtParamBean(table, config);
        }
        return null;
    }

    private void generateResultBean(Table table, Config config) {
        String className = getResultBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder beanTypeBuilder =
                        TypeSpec.classBuilder(className)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addSuperinterface(ParameterizedTypeName.get(Map.class, String.class, Object.class))
                                        .addField(FieldSpec
                                                        .builder(ParameterizedTypeName.get(Map.class, String.class, Object.class), "dataMap",
                                                                        Modifier.PROTECTED).initializer("new $T<String,Object>()", HashMap.class)
                                                        .build());

        beanTypeBuilder.addMethod(
                        MethodSpec.methodBuilder("size").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(int.class)
                                        .addStatement("return dataMap.size()").build())
                        .addMethod(MethodSpec.methodBuilder("isEmpty").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(boolean.class).addStatement("return dataMap.isEmpty()").build())
                        .addMethod(MethodSpec.methodBuilder("containsKey").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(boolean.class).addParameter(Object.class, "key")
                                        .addStatement("return dataMap.containsKey($L)", "key").build())
                        .addMethod(MethodSpec.methodBuilder("containsValue").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(boolean.class).addParameter(Object.class, "key")
                                        .addStatement("return dataMap.containsValue($L)", "key").build())
                        .addMethod(MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(Object.class)
                                        .addParameter(Object.class, "key").addStatement("return dataMap.get($L)", "key").build())
                        .addMethod(MethodSpec.methodBuilder("put").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(Object.class)
                                        .addParameter(String.class, "key").addParameter(Object.class, "value")
                                        .addStatement("return dataMap.put($L, $L)", "key", "value").build())
                        .addMethod(MethodSpec.methodBuilder("remove").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(Object.class).addParameter(Object.class, "key").addStatement("return dataMap.remove($L)", "key")
                                        .build())
                        .addMethod(MethodSpec
                                        .methodBuilder("putAll")
                                        .addAnnotation(Override.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(void.class)
                                        .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), WildcardTypeName.subtypeOf(String.class),
                                                        WildcardTypeName.subtypeOf(Object.class)), "m").addStatement("dataMap.putAll($L)", "m")
                                        .build())
                        .addMethod(MethodSpec.methodBuilder("clear").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class)
                                        .addStatement("dataMap.clear()").build())
                        .addMethod(MethodSpec.methodBuilder("keySet").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(ParameterizedTypeName.get(Set.class, String.class)).addStatement("return dataMap.keySet()").build())
                        .addMethod(MethodSpec.methodBuilder("values").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(ParameterizedTypeName.get(Collection.class, Object.class)).addStatement("return dataMap.values()")
                                        .build())
                        .addMethod(MethodSpec
                                        .methodBuilder("entrySet")
                                        .addAnnotation(Override.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(ParameterizedTypeName.get(ClassName.get(Set.class),
                                                        ParameterizedTypeName.get(Map.Entry.class, String.class, Object.class)))
                                        .addStatement("return dataMap.entrySet()").build());

        for (Column column : table.getColumns()) {
            methodBuild(beanTypeBuilder, _className, column);
        }

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build())
                            .build()
                            .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()),
                                            config.getEncoding());
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
            if (!javaTypeClassName.equals(ClassName.get(String.class))) {
                staticBlock = true;
                codeBlockBuilder.addStatement("typeMap.put($S, $T.class)", StringUtil.removeUnderline(column.getName()), javaTypeClassName);
            }
        }

        String className = getParamBeanName(table, config);
        ClassName _className = ClassName.get(config.getBeanInfo().getPackageName(), className);
        Builder beanTypeBuilder =
                        TypeSpec.classBuilder(className)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addSuperinterface(ParameterizedTypeName.get(Map.class, String.class, Object.class))
                                        .addField(FieldSpec
                                                        .builder(ParameterizedTypeName.get(Map.class, String.class, Object.class), "dataMap",
                                                                        Modifier.PROTECTED).initializer("new $T<String,Object>()", HashMap.class)
                                                        .build())
                                        .addField(FieldSpec
                                                        .builder(ParameterizedTypeName.get(
                                                                        ClassName.get(Map.class),
                                                                        ClassName.get(String.class),
                                                                        ParameterizedTypeName.get(ClassName.get(Class.class),
                                                                                        WildcardTypeName.subtypeOf(Object.class))), "typeMap",
                                                                        Modifier.PROTECTED, Modifier.STATIC)
                                                        .initializer("new $T<String,Class<?>>()", HashMap.class).build());
        if (staticBlock) {
            beanTypeBuilder.addStaticBlock(codeBlockBuilder.build());
        }

        beanTypeBuilder.addMethod(
                        MethodSpec.methodBuilder("size").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(int.class)
                                        .addStatement("return dataMap.size()").build())
                        .addMethod(MethodSpec.methodBuilder("isEmpty").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(boolean.class).addStatement("return dataMap.isEmpty()").build())
                        .addMethod(MethodSpec.methodBuilder("containsKey").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(boolean.class).addParameter(Object.class, "key")
                                        .addStatement("return dataMap.containsKey($L)", "key").build())
                        .addMethod(MethodSpec.methodBuilder("containsValue").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(boolean.class).addParameter(Object.class, "key")
                                        .addStatement("return dataMap.containsValue($L)", "key").build())
                        .addMethod(MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(Object.class)
                                        .addParameter(Object.class, "key").addStatement("return dataMap.get($L)", "key").build())
                        .addMethod(MethodSpec.methodBuilder("remove").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(Object.class).addParameter(Object.class, "key").addStatement("return dataMap.remove($L)", "key")
                                        .build())
                        .addMethod(MethodSpec
                                        .methodBuilder("putAll")
                                        .addAnnotation(Override.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(void.class)
                                        .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), WildcardTypeName.subtypeOf(String.class),
                                                        WildcardTypeName.subtypeOf(Object.class)), "m").addStatement("dataMap.putAll($L)", "m")
                                        .build())
                        .addMethod(MethodSpec.methodBuilder("clear").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(void.class)
                                        .addStatement("dataMap.clear()").build())
                        .addMethod(MethodSpec.methodBuilder("keySet").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(ParameterizedTypeName.get(Set.class, String.class)).addStatement("return dataMap.keySet()").build())
                        .addMethod(MethodSpec.methodBuilder("values").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                                        .returns(ParameterizedTypeName.get(Collection.class, Object.class)).addStatement("return dataMap.values()")
                                        .build())
                        .addMethod(MethodSpec
                                        .methodBuilder("entrySet")
                                        .addAnnotation(Override.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(ParameterizedTypeName.get(ClassName.get(Set.class),
                                                        ParameterizedTypeName.get(Map.Entry.class, String.class, Object.class)))
                                        .addStatement("return dataMap.entrySet()").build());

        for (Column column : table.getColumns()) {
            methodBuild(beanTypeBuilder, _className, column);
        }

        beanTypeBuilder.addMethod(MethodSpec
                        .methodBuilder("put")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(Object.class)
                        .addParameter(String.class, "key")
                        .addParameter(Object.class, "value")
                        .addCode("if (null == value) { return dataMap.remove(key); } Class<?> clazz = typeMap.get(key);Object _value = null;if (null == clazz || clazz.isInstance(value)) {_value = value;} else if ($T.class.isInstance(value)) { if ($T.class.equals(clazz)) { _value = Integer.valueOf((String) value);} else if ($T.class.equals(clazz)) { _value = new BigDecimal((String) value); } else if ($T.class.equals(clazz)) { _value = new Boolean((String) value); } else if ($T.class.equals(clazz)) { _value = new Date($T.parseLong((String) value)); } else { throw new RuntimeException(\"Invalid data format : \" + key); }} else {throw new RuntimeException(\"Invalid data format : \" + key);} return dataMap.put(key, _value);",
                                        String.class, Integer.class, BigDecimal.class, Boolean.class, Date.class, Long.class).build());

        try {
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build())
                            .build()
                            .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()),
                                            config.getEncoding());
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

        String parentClassName = getParamBeanName(table, config);
        String className = getExtParamBeanName(table, config);
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
            JavaFileTmp.builder(config.getBeanInfo().getPackageName(), beanTypeBuilder.build())
                            .build()
                            .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getBeanInfo().getFolderName()),
                                            config.getEncoding());
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
        com.squareup.javapoet.MethodSpec.Builder getMethodBuilder =
                        MethodSpec.methodBuilder("get" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(column.getName())))
                                        .addModifiers(Modifier.PUBLIC).returns(javaTypeClassName)
                                        .addStatement("return ($T)dataMap.get($S)", javaTypeClassName, StringUtil.removeUnderline(column.getName()));
        if (column.getNote() != null && column.getNote().length() > 0) {
            getMethodBuilder.addJavadoc("$L", column.getNote());
        }
        beanBuilder.addMethod(getMethodBuilder.build());

        com.squareup.javapoet.MethodSpec.Builder setMethodBuilder =
                        MethodSpec.methodBuilder("set" + StringUtil.firstAlphToUpper(StringUtil.removeUnderline(column.getName())))
                                        .returns(className)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(javaTypeClassName, StringUtil.removeUnderline(column.getName()))
                                        .addStatement("dataMap.put($S,$L)", StringUtil.removeUnderline(column.getName()),
                                                        StringUtil.removeUnderline(column.getName())).addStatement("return this");
        if (column.getNote() != null && column.getNote().length() > 0) {
            setMethodBuilder.addJavadoc("$L", column.getNote());
        }
        beanBuilder.addMethod(setMethodBuilder.build());
    }
}
