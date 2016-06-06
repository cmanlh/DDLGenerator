package com.lifeonwalden.codeGenerator.javaClass.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.ValueEnum;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.javaClass.EnumGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class EnumGeneratorImpl implements EnumGenerator {

	@Override
	public void generate(EnumConst enumConst, Config config) {

		Builder enumTypeBuilder = TypeSpec.enumBuilder(enumConst.getName()).addModifiers(Modifier.PUBLIC);
		ClassName enumClass = ClassName.get(config.getEnumInfo().getPackageName(), enumConst.getName());

		if (null != enumConst.getNote()) {
			enumTypeBuilder.addJavadoc(enumConst.getNote());
		}

		enumTypeBuilder.addEnumConstant("NIL",
				TypeSpec.anonymousClassBuilder("$S, $L, $S, $S", "NIL", 0, "未知", "unknow").addJavadoc("NULL")
						.addJavadoc("\n").build());
		for (ValueEnum valueEnum : enumConst.getOptions()) {
			String enumConstName = valueEnum.getName() == null ? "NULL" : valueEnum.getName();
			String enumConstAlias = valueEnum.getAlias() == null ? "" : valueEnum.getAlias();
			String enumConstDesc = valueEnum.getDesc() == null ? "" : valueEnum.getDesc();
			enumTypeBuilder.addEnumConstant(
					enumConstName,
					TypeSpec.anonymousClassBuilder("$S, $L, $S, $S", enumConstName, valueEnum.getValue(),
							enumConstDesc, enumConstAlias).addJavadoc(enumConstDesc).addJavadoc("\n").build());

		}

		enumTypeBuilder.addField(String.class, "name", Modifier.PRIVATE).addField(int.class, "value", Modifier.PRIVATE)
				.addField(String.class, "desc", Modifier.PRIVATE).addField(String.class, "alias", Modifier.PRIVATE);

		enumTypeBuilder.addField(FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Integer.class), enumClass),
						"valueMapping", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
				.initializer(
						CodeBlock.builder().add("new $T<$L, $L>()", HashMap.class, "Integer", enumConst.getName())
								.build()).build());

		enumTypeBuilder.addField(FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), enumClass),
						"nameMapping", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
				.initializer(
						CodeBlock.builder().add("new $T<$L, $L>()", HashMap.class, "String", enumConst.getName())
								.build()).build());

		enumTypeBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE)
				.addParameter(String.class, "name").addStatement("this.$N = $N", "name", "name")
				.addParameter(int.class, "value").addStatement("this.$N = $N", "value", "value")
				.addParameter(String.class, "desc").addStatement("this.$N = $N", "desc", "desc")
				.addParameter(String.class, "alias").addStatement("this.$N = $N", "alias", "alias").build());

		enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getValue").addModifiers(Modifier.PUBLIC).returns(int.class)
				.addStatement("return this.$N", "value").build());

		enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getName").addModifiers(Modifier.PUBLIC)
				.returns(String.class).addStatement("return this.$N", "name").build());

		enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getDesc").addModifiers(Modifier.PUBLIC)
				.returns(String.class).addStatement("return this.$N", "desc").build());

		enumTypeBuilder.addMethod(MethodSpec.methodBuilder("getAlias").addModifiers(Modifier.PUBLIC)
				.returns(String.class).addStatement("return this.$N", "alias").build());

		enumTypeBuilder.addMethod(MethodSpec.methodBuilder("valueOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(enumClass).addParameter(int.class, "value")
				.addStatement("$T valEnum = $L.get($L)", enumClass, "valueMapping", "value")
				.addStatement("return null == valEnum ? NIL : valEnum").build());

		enumTypeBuilder.addMethod(MethodSpec.methodBuilder("nameOf").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(enumClass).addParameter(String.class, "name")
				.addStatement("$T valEnum = $L.get($L)", enumClass, "nameMapping", "name")
				.addStatement("return null == valEnum ? NIL : valEnum").build());

		enumTypeBuilder.addStaticBlock(CodeBlock.builder()
				.addStatement("$L[] enumArray = $L.values()", enumConst.getName(), enumConst.getName())
				.beginControlFlow("for ($L _enum : enumArray)", enumConst.getName())
				.addStatement("$L.put(_enum.get$L(), _enum)", "valueMapping", "Value")
				.addStatement("$L.put(_enum.get$L(), _enum)", "nameMapping", "Name").endControlFlow().build());

		try {
			JavaFile.builder(config.getEnumInfo().getPackageName(), enumTypeBuilder.build())
					.build()
					.writeTo(
							new File(new File(config.getOutputLocation()).getPath() + "\\"
									+ config.getEnumInfo().getFolderName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
