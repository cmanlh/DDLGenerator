package com.orientsec.ddlgenerator.generator.constant.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.orientsec.ddlgenerator.ValueEnum;
import com.orientsec.ddlgenerator.config.CommonObjectConfig;
import com.orientsec.ddlgenerator.config.EnumConfig;
import com.orientsec.ddlgenerator.generator.constant.EnumCSClassGenerator;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class EnumCSClassGeneratorImpl implements EnumCSClassGenerator {

    @Override
    public void generate(CommonObjectConfig config) {
        StringBuilder builder = new StringBuilder();

        builder.append("namespace com.orientsec.cashmgmt.pb").append(OutputUtil.LINE_SEPERATOR).append("{").append(OutputUtil.LINE_SEPERATOR);

        for (EnumConfig enumConfig : config.getEnums()) {
            if (null != enumConfig.getDesc()) {
                builder.append(OutputUtil.INDENT_SIZE).append("/// <summary>").append(OutputUtil.LINE_SEPERATOR).append(OutputUtil.INDENT_SIZE)
                        .append("/// ").append(enumConfig.getDesc()).append(OutputUtil.LINE_SEPERATOR).append(OutputUtil.INDENT_SIZE)
                        .append("/// </summary>").append(OutputUtil.LINE_SEPERATOR);
            }
            builder.append(OutputUtil.INDENT_SIZE).append("public enum ").append(enumConfig.getName());

            builder.append(OutputUtil.INDENT_SIZE).append("{").append(OutputUtil.LINE_SEPERATOR);
            for (ValueEnum valueEnum : enumConfig.getOptions()) {
                builder.append(OutputUtil.INDENT_SIZE).append(OutputUtil.INDENT_SIZE).append("[Description(\"").append(valueEnum.getDesc())
                        .append("\")]").append(OutputUtil.LINE_SEPERATOR);
                builder.append(OutputUtil.INDENT_SIZE).append(OutputUtil.INDENT_SIZE).append(valueEnum.getName()).append(" = ")
                        .append(valueEnum.getValue()).append(",").append(OutputUtil.LINE_SEPERATOR);
            }
            builder.append(OutputUtil.INDENT_SIZE).append("}").append(OutputUtil.LINE_SEPERATOR).append(OutputUtil.LINE_SEPERATOR);
        }
        builder.append("}");

        OutputStreamWriter osw = null;
        try {
            File path = new File(config.getOutputPath());
            System.out.println(path.getPath());
            osw = new OutputStreamWriter(new FileOutputStream(path.getPath() + "/Enums.cs"), "UTF-8");
            osw.write(builder.toString().toCharArray(), 0, builder.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
