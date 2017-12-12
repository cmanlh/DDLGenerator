package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DAOGeneratorImpl implements TableBasedGenerator {

    @Override
    public String generate(Table table, Config config) {
        String className = NameUtil.getDaoName(table, config);
        Builder daoTypeBuilder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
        ClassName resultBeanClass = ClassName.get(config.getBeanInfo().getPackageName(), NameUtil.getResultBeanName(table, config));

        daoTypeBuilder.addMethod(MethodSpec.methodBuilder("insert").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
                .addParameter(resultBeanClass, "param").build());

        daoTypeBuilder.addMethod(MethodSpec.methodBuilder("select").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(resultBeanClass, "param").returns(ParameterizedTypeName.get(ClassName.get(List.class), resultBeanClass)).build());

        daoTypeBuilder.addMethod(MethodSpec.methodBuilder("remove").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
                .addParameter(resultBeanClass, "param").build());

        if (null != table.getPrimaryColumns()) {
            daoTypeBuilder.addMethod(MethodSpec.methodBuilder("update").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
                    .addParameter(resultBeanClass, "param").build());

            daoTypeBuilder.addMethod(MethodSpec.methodBuilder("updateDynamic").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(Integer.class)
                    .addParameter(resultBeanClass, "param").build());

            com.squareup.javapoet.MethodSpec.Builder getBuilder = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(resultBeanClass, "param").returns(resultBeanClass);

            com.squareup.javapoet.MethodSpec.Builder deleteBuilder = MethodSpec.methodBuilder("delete").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(resultBeanClass, "param").returns(Integer.class);

            daoTypeBuilder.addMethod(getBuilder.build());
            daoTypeBuilder.addMethod(deleteBuilder.build());

            if (table.getAddDBFields()) {
                com.squareup.javapoet.MethodSpec.Builder logicalDeleteBuilder = MethodSpec.methodBuilder("logicalDelete")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addParameter(resultBeanClass, "param").returns(Integer.class);

                daoTypeBuilder.addMethod(logicalDeleteBuilder.build());
            }
        }

        try {
            JavaFileTmp.builder(config.getDaoInfo().getPackageName(), daoTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getDaoInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
