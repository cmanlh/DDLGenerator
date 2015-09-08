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
import com.orientsec.ddlgenerator.generator.constant.EnumXMLGenerator;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class EnumXMLGeneratorImpl implements EnumXMLGenerator {

    @Override
    public void generate(CommonObjectConfig config) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append(OutputUtil.LINE_SEPERATOR);

        builder.append("<Enums>").append(OutputUtil.LINE_SEPERATOR);

        builder.append("<!--用于配置下拉框的项，以及对PB文件当中的枚举类进行描述说明 -->").append(OutputUtil.LINE_SEPERATOR);
        builder.append("<!--id为标识序号，type对应枚举类或数据源名称，对应OrientSec.Util.CacheType的枚举字段名,此CacheType作为缓存数据当中Key-->").append(OutputUtil.LINE_SEPERATOR);
        builder.append("<!--type以Enum作为后缀，说明是在cashmgmt文件的PB枚举类，否则为数据源名称-->").append(OutputUtil.LINE_SEPERATOR);
        builder.append("<!--Item元素表示缓存数据项，当中的Id为枚举字段值时可省略不写，如果为自定义序号时，默认以0开头，依次+1时，也可省略不写，否则要写，如BuySellType -->").append(OutputUtil.LINE_SEPERATOR);
        builder.append("<!--Item元素为缓存项的名称，description为枚举字段名的描述说明 -->").append(OutputUtil.LINE_SEPERATOR);

        for (EnumConfig enumConfig : config.getEnums()) {
            builder.append(OutputUtil.INDENT_SIZE).append("<EnumSource type=\"").append(enumConfig.getName());
            if (null != enumConfig.getDesc()) {
                builder.append("\" description=\"").append(enumConfig.getDesc());
            }
            builder.append("\">").append(OutputUtil.LINE_SEPERATOR);


            builder.append(OutputUtil.INDENT_SIZE).append(OutputUtil.INDENT_SIZE).append("<Items>").append(OutputUtil.LINE_SEPERATOR);
            for (ValueEnum valueEnum : enumConfig.getOptions()) {
                if (Integer.parseInt(valueEnum.getValue()) == 0) continue;
                builder.append(OutputUtil.INDENT_SIZE).append(OutputUtil.INDENT_SIZE).append(OutputUtil.INDENT_SIZE).append("<Item id=\"")
                        .append(valueEnum.getValue()).append("\" description=\"").append(valueEnum.getDesc()).append("\"/>")
                        .append(OutputUtil.LINE_SEPERATOR);
            }
            builder.append(OutputUtil.INDENT_SIZE).append(OutputUtil.INDENT_SIZE).append("<Items>").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append("</EnumSource>").append(OutputUtil.LINE_SEPERATOR).append(OutputUtil.LINE_SEPERATOR);
        }
        builder.append("</Enums>");

        OutputStreamWriter osw = null;
        try {
            File path = new File(config.getOutputPath());
            System.out.println(path.getPath());
            osw = new OutputStreamWriter(new FileOutputStream(path.getPath() + "/Enums.xml"), "UTF-8");
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
