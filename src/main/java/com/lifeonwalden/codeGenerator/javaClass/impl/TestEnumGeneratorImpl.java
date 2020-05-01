package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.ConstBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.ValueEnum;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestEnumGeneratorImpl implements ConstBasedGenerator {

    @Override
    public String generate(List<EnumConst> enumConstList, Config config) {
        for (EnumConst enumConst : enumConstList) {
            generateEnum(enumConst, config);
        }
        return null;
    }

    private void generateEnum(EnumConst enumConst, Config config) {
        String constClassName = ConstBasedGenerator.getConstName(enumConst.getName(), config);
        Builder enumTypeBuilder = TypeSpec.enumBuilder(constClassName).addModifiers(Modifier.PUBLIC);
        ClassName enumClass = ClassName.get(config.getConstInfo().getPackageName().concat(".test"), constClassName);

        if (null != enumConst.getNote()) {
            enumTypeBuilder.addJavadoc(enumConst.getNote());
        }

        enumTypeBuilder.addEnumConstant("NIL", TypeSpec.anonymousClassBuilder("$S, $L, $S, $S", "NIL", 0, "未知", "unknow").addJavadoc("NULL")
                .addJavadoc("\n").build());
        for (ValueEnum valueEnum : enumConst.getOptions()) {
            String enumConstName = valueEnum.getName() == null ? "NULL" : valueEnum.getName();
            String enumConstAlias = valueEnum.getAlias() == null ? "" : valueEnum.getAlias();
            String enumConstDesc = valueEnum.getDesc() == null ? "" : valueEnum.getDesc();

            enumTypeBuilder.addEnumConstant(
                    enumConstName,
                    TypeSpec.anonymousClassBuilder("$S, $L, $S, $S", enumConstName, valueEnum.getValue(), enumConstDesc, enumConstAlias)
                            .addJavadoc("$${@$L@$L.getValue()}", enumClass, enumConstName).addJavadoc("\n\n").addJavadoc(enumConstDesc).addJavadoc("\n").build());

            if (null != valueEnum.getSubEnum() && valueEnum.getSubEnum().size() == 1) {
                EnumConst subEnumConst = valueEnum.getSubEnum().get(0);
                generateEnum(subEnumConst, config);
            }
        }

        enumTypeBuilder.addField(String.class, "name", Modifier.PRIVATE).addField(int.class, "value", Modifier.PRIVATE)
                .addField(String.class, "desc", Modifier.PRIVATE).addField(String.class, "alias", Modifier.PRIVATE);

        enumTypeBuilder.addCodeBlockNode(new CodeBlockNode().addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Integer.class), enumClass), "valueMapping", Modifier.PRIVATE,
                        Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $T<$T, $T>()", HashMap.class, ClassName.get(Integer.class), enumClass).build()).build())
                .addStaticBlock(CodeBlock.builder().addStatement("$T[] enumArray = $T.values()", enumClass, enumClass)
                        .beginControlFlow("for ($T _enum : enumArray)", enumClass).addStatement("$L.put(_enum.get$L(), _enum)", "valueMapping", "Value")
                        .addStatement("$L.put(_enum.get$L(), _enum)", "nameMapping", "Name").endControlFlow().build()));

        enumTypeBuilder.addCodeBlockNode(new CodeBlockNode().addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Integer.class), enumClass), "testMapping", Modifier.PRIVATE,
                        Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $T<$T, $T>()", HashMap.class, ClassName.get(Integer.class), enumClass).build()).build()));

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), enumClass), "nameMapping", Modifier.PRIVATE,
                        Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $T<$T, $T>()", HashMap.class, ClassName.get(String.class), enumClass).build()).build());

        enumTypeBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).addParameter(String.class, "name")
                .addStatement("this.$N = $N", "name", "name").addParameter(int.class, "value").addStatement("this.$N = $N", "value", "value")
                .addParameter(String.class, "desc").addStatement("this.$N = $N", "desc", "desc").addParameter(String.class, "alias")
                .addStatement("this.$N = $N", "alias", "alias").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getValue").addModifiers(Modifier.PUBLIC).returns(int.class)
                .addStatement("return this.$N", "value").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getName").addModifiers(Modifier.PUBLIC).returns(String.class)
                .addStatement("return this.$N", "name").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getDesc").addModifiers(Modifier.PUBLIC).returns(String.class)
                .addStatement("return this.$N", "desc").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getAlias").addModifiers(Modifier.PUBLIC).returns(String.class)
                .addStatement("return this.$N", "alias").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("valueOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(int.class, "value").addStatement("$T valEnum = $L.get($L)", enumClass, "valueMapping", "value")
                .addStatement("return null == valEnum ? NIL : valEnum").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("nameOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(String.class, "name").addStatement("$T valEnum = $L.get($L)", enumClass, "nameMapping", "name")
                .addStatement("return null == valEnum ? NIL : valEnum").build());


        try {
            JavaFileTmp
                    .builder(config.getConstInfo().getPackageName().concat(".test"), enumTypeBuilder.build())
                    .build()
                    .writeTo(new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getConstInfo().getFolderName()),
                            config.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
