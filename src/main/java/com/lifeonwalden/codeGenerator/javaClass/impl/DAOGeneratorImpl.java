package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.constant.DefinedMappingID;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.lifeonwalden.codeGenerator.util.TableInfoUtil;
import com.lifeonwalden.forestbatis.biz.dao.CommonDAO;
import com.lifeonwalden.forestbatis.biz.dao.KeyBasedDAO;
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
        boolean hasPrimaryKey = TableInfoUtil.checkPrimaryKey(table), hasDBFields = table.getAddDBFields(), supportWildCondition = TableInfoUtil.checkWildConditionSupport(table);
        ClassName resultBeanClass = ClassName.get(config.getBeanInfo().getPackageName(), NameUtil.getResultBeanName(table, config));

        Builder daoTypeBuilder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
        if (hasPrimaryKey) {
            daoTypeBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(KeyBasedDAO.class), resultBeanClass));
        } else {
            daoTypeBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(CommonDAO.class), resultBeanClass));
        }

        if (hasDBFields) {
            if (hasPrimaryKey) {
                daoTypeBuilder.addMethod(MethodSpec.methodBuilder(DefinedMappingID.LOGICAL_DELETE).addJavadoc("logically delete a record based on primary key")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addParameter(resultBeanClass, "param").returns(Integer.class).build());
            }
            daoTypeBuilder.addMethod(MethodSpec.methodBuilder(DefinedMappingID.LOGICAL_REMOVE).addJavadoc("logically remove based on query condition")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addParameter(resultBeanClass, "param").returns(Integer.class).build());
        }

        if (supportWildCondition) {
            daoTypeBuilder.addMethod(MethodSpec.methodBuilder(DefinedMappingID.SELECT_WILD).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addJavadoc("select special fields based on advanced query condition")
                    .addParameter(resultBeanClass, "param").returns(ParameterizedTypeName.get(ClassName.get(List.class), resultBeanClass)).build());
            daoTypeBuilder.addMethod(MethodSpec.methodBuilder(DefinedMappingID.DIRECT_SELECT_WILD).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addJavadoc("select based on advanced query condition")
                    .addParameter(resultBeanClass, "param").returns(ParameterizedTypeName.get(ClassName.get(List.class), resultBeanClass)).build());
        }

        // TODO
        daoTypeBuilder.addMethod(MethodSpec.methodBuilder(DefinedMappingID.REMOVE_ALL).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addJavadoc("remove all data from table").returns(Integer.class)
                .addAnnotation(Deprecated.class)
                .addJavadoc("Not safe operation, please use remove() instead.")
                .build());

        try {
            JavaFileTmp.builder(config.getDaoInfo().getPackageName(), daoTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getDaoInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
