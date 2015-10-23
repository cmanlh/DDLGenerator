package com.orientsec.ddlgenerator.generator.constant.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.orientsec.ddlgenerator.ValueEnum;
import com.orientsec.ddlgenerator.config.EnumConfig;
import com.orientsec.ddlgenerator.generator.constant.EnumJavaClassGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class EnumJavaClassGeneratorImpl implements EnumJavaClassGenerator {

    @Override
    public void generate(EnumConfig enumConfig, String packageName, String outputPath) {
        String prefix = null == enumConfig.getPrefix() ? "" : enumConfig.getPrefix();

        Builder enumTypeBuilder = TypeSpec.enumBuilder(enumConfig.getName()).addModifiers(Modifier.PUBLIC);
        ClassName enumClass = ClassName.get(packageName, enumConfig.getName());

        if (null != enumConfig.getDesc()) {
            enumTypeBuilder.addJavadoc(enumConfig.getDesc());
        }

        for (ValueEnum valueEnum : enumConfig.getOptions()) {
            String enumConstantInstanseName =
                    valueEnum.getName() == null ? "NULL" : (valueEnum.getName().equals("NIL") ? valueEnum.getName() : prefix + valueEnum.getName());
            String enumConstantName = valueEnum.getName() == null ? "NULL" : valueEnum.getName();
            String enumConstantNote = valueEnum.getNote() == null ? "" : valueEnum.getNote();
            String enumConstantDesc = valueEnum.getDesc() == null ? "" : valueEnum.getDesc();
            enumTypeBuilder.addEnumConstant(
                    enumConstantInstanseName,
                    TypeSpec.anonymousClassBuilder("$S, $L, $S, $S", enumConstantName, valueEnum.getValue(), enumConstantDesc, enumConstantNote)
                            .addJavadoc("$${@$L@$L.getValue()}\n\n", enumClass.toString(), enumConstantName).addJavadoc(enumConstantDesc)
                            .addJavadoc("\n").build());

        }

        enumTypeBuilder.addField(String.class, "name", Modifier.PRIVATE).addField(int.class, "value", Modifier.PRIVATE)
                .addField(String.class, "desc", Modifier.PRIVATE).addField(String.class, "note", Modifier.PRIVATE);

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Integer.class), enumClass), "valueMapping",
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $T<$L, $L>()", HashMap.class, "Integer", enumConfig.getName()).build()).build());

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), enumClass), "nameMapping",
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $T<$L, $L>()", HashMap.class, "String", enumConfig.getName()).build()).build());

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), enumClass), "descMapping",
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $T<$L, $L>()", HashMap.class, "String", enumConfig.getName()).build()).build());


        enumTypeBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).addParameter(String.class, "name")
                .addStatement("this.$N = $N", "name", "name").addParameter(int.class, "value").addStatement("this.$N = $N", "value", "value")
                .addParameter(String.class, "desc").addStatement("this.$N = $N", "desc", "desc").addParameter(String.class, "note")
                .addStatement("this.$N = $N", "note", "note").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getValue").addModifiers(Modifier.PUBLIC).returns(int.class)
                .addStatement("return this.$N", "value").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getName").addModifiers(Modifier.PUBLIC).returns(String.class)
                .addStatement("return this.$N", "name").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getDesc").addModifiers(Modifier.PUBLIC).returns(String.class)
                .addStatement("return this.$N", "desc").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getNote").addModifiers(Modifier.PUBLIC).returns(String.class)
                .addStatement("return this.$N", "note").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("valueOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(int.class, "value").addStatement("$T valEnum = $L.get($L)", enumClass, "valueMapping", "value")
                .addStatement("return null == valEnum ? NIL : valEnum").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("nameOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(String.class, "name").addStatement("$T valEnum = $L.get($L)", enumClass, "nameMapping", "name")
                .addStatement("return null == valEnum ? NIL : valEnum").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("descOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(String.class, "desc").addStatement("$T valEnum = $L.get($L)", enumClass, "descMapping", "desc")
                .addStatement("return null == valEnum ? NIL : valEnum").build());

        enumTypeBuilder.addStaticBlock(CodeBlock.builder().addStatement("$L[] enumArray = $L.values()", enumConfig.getName(), enumConfig.getName())
                .beginControlFlow("for ($L _enum : enumArray)", enumConfig.getName())
                .addStatement("$L.put(_enum.get$L(), _enum)", "valueMapping", "Value")
                .addStatement("$L.put(_enum.get$L(), _enum)", "nameMapping", "Name")
                .addStatement("$L.put(_enum.get$L(), _enum)", "descMapping", "Desc").endControlFlow().build());


        try {
            JavaFile.builder(packageName, enumTypeBuilder.build()).build().writeTo(new File(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
