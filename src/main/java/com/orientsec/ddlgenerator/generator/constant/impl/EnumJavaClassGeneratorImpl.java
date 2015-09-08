package com.orientsec.ddlgenerator.generator.constant.impl;

import java.io.File;
import java.io.IOException;
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

        Builder enumTypeBuilder = TypeSpec.enumBuilder(enumConfig.getName()).addModifiers(Modifier.PUBLIC);
        ClassName enumClass = ClassName.get(packageName, enumConfig.getName());


        for (ValueEnum valueEnum : enumConfig.getOptions()) {
            String enumConstantName = valueEnum.getName() == null ? "NULL" : valueEnum.getName();
            String enumConstantNote = valueEnum.getNote() == null ? "" : valueEnum.getNote();
            String enumConstantDesc = valueEnum.getDesc() == null ? "" : valueEnum.getDesc();
            enumTypeBuilder.addEnumConstant(enumConstantName,
                    TypeSpec.anonymousClassBuilder("$S, $L, $S, $S", enumConstantName, valueEnum.getValue(), enumConstantDesc, enumConstantNote)
                            .addJavadoc("$${@$L@$L.getValue()}", enumClass.toString(), enumConstantName).build());

        }
        enumTypeBuilder.addField(String.class, "name", Modifier.PRIVATE).addField(int.class, "value", Modifier.PRIVATE)
                .addField(String.class, "desc", Modifier.PRIVATE).addField(String.class, "note", Modifier.PRIVATE);

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Integer.class), enumClass), "valueMapping",
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $L<$L, $L>()", "HashMap", "Integer", enumConfig.getName()).build()).build());

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), enumClass), "nameMapping",
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $L<$L, $L>()", "HashMap", "String", enumConfig.getName()).build()).build());

        enumTypeBuilder.addField(FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), enumClass), "descMapping",
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(CodeBlock.builder().add("new $L<$L, $L>()", "HashMap", "String", enumConfig.getName()).build()).build());


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
                .addParameter(int.class, "value").addStatement("return $L.get($L)", "valueMapping", "value").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("nameOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(String.class, "name").addStatement("return $L.get($L)", "nameMapping", "name").build());

        enumTypeBuilder.addMethod(MethodSpec.methodBuilder("descOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(enumClass)
                .addParameter(String.class, "desc").addStatement("return $L.get($L)", "descMapping", "desc").build());

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
