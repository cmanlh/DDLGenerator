package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.TableBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.util.NameUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFileTmp;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

/**
 * Created by chenjinsen on 2018/3/14.
 */
public class MapperGeneratorImpl implements TableBasedGenerator {

    @Override
    public String generate(Table table, Config config) {
        String className = NameUtil.getDaoName(table, config), packageName = config.getDaoInfo().getPackageName();
        ClassName resultBeanClass = ClassName.get(config.getBeanInfo().getPackageName(), NameUtil.getResultBeanName(table, config));

        TypeSpec.Builder daoTypeBuilder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
        ClassName supperClassName = ClassName.get(packageName, "BaseMapper");// extends BaseMapper<T>
        daoTypeBuilder.addSuperinterface(ParameterizedTypeName.get(supperClassName, resultBeanClass));

        try {
            JavaFileTmp.builder(packageName, daoTypeBuilder.build()).build().writeTo(
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getDaoInfo().getFolderName()), config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

