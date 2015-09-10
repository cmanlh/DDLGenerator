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
import com.orientsec.ddlgenerator.generator.constant.EnumSQLGenerator;
import com.orientsec.ddlgenerator.generator.util.OutputUtil;

public class EnumSQLGeneratorImpl implements EnumSQLGenerator {

    @Override
    public void generate(CommonObjectConfig config) {
        StringBuilder builder = new StringBuilder();

        for (EnumConfig enumConfig : config.getEnums()) {
            String tableName = enumConfig.getName();
            if (enumConfig.getName().endsWith("Enum")) {
                tableName = tableName.substring(0, tableName.length() - 4);
            }
            builder.append("CREATE TABLE ").append(tableName).append("(").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append("id int NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",name varchar(32) NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",description nvarchar(32) NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",note nvarchar(128)").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",createUser varchar(16) NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",createTime datetime NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",updateUser varchar(16) NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(OutputUtil.INDENT_SIZE).append(",updateTime datetime NOT NULL").append(OutputUtil.LINE_SEPERATOR);
            builder.append(",CONSTRAINT ").append(tableName).append("_PK PRIMARY KEY (id)");
            builder.append(");").append(OutputUtil.LINE_SEPERATOR).append(OutputUtil.LINE_SEPERATOR);

            for (ValueEnum valueEnum : enumConfig.getOptions()) {
                builder.append("insert into ").append(tableName).append(" values(").append(valueEnum.getValue()).append(", '")
                        .append(valueEnum.getName()).append("', '").append(valueEnum.getDesc()).append("', '")
                        .append(null == valueEnum.getNote() ? "" : valueEnum.getNote())
                        .append("', 'luhong', current_timestamp, 'luhong', current_timestamp);").append(OutputUtil.LINE_SEPERATOR);
            }
            builder.append(OutputUtil.LINE_SEPERATOR);
        }
        builder.append(OutputUtil.LINE_SEPERATOR);

        OutputStreamWriter osw = null;
        try {
            File path = new File(config.getOutputPath());
            System.out.println(path.getPath());
            osw = new OutputStreamWriter(new FileOutputStream(path.getPath() + "/enumSQLScript.sql"), "UTF-8");
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
