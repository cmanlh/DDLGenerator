package com.lifeonwalden.codeGenerator.javaClass.impl;

import com.lifeonwalden.codeGenerator.ConstBasedGenerator;
import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.ValueEnum;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.util.OutputUtilities;

import java.io.*;
import java.util.List;

public class JsEnumGeneratorImpl implements ConstBasedGenerator {

    @Override
    public String generate(List<EnumConst> enumConstList, Config config) {
        StringBuilder sb = new StringBuilder();

        OutputUtilities.newLine(sb
                .append("if (typeof Object.assign != 'function') {  Object.assign = function(target) {    'use strict';    if (target == null) {      throw new TypeError('Cannot convert undefined or null to object');    }    target = Object(target);    for (var index = 1; index < arguments.length; index++) {      var source = arguments[index];      if (source != null) {        for (var key in source) {          if (Object.prototype.hasOwnProperty.call(source, key)) {            target[key] = source[key];          }        }      }    }    return target;  };}"));
        OutputUtilities.newLine(sb);

        OutputUtilities.newLine(sb.append("function Enum(){this._index = []; this._values=[]}"));
        OutputUtilities.newLine(sb
                .append("Enum.prototype.values=function(){if(0==this._values.length){for (var i in this){var tmp = this[i]; if((typeof tmp == 'object') && tmp.hasOwnProperty('value') && tmp.hasOwnProperty('desc')){this._values.push(tmp);this._index[tmp.value]=tmp;}}} return this._values;};"));
        OutputUtilities.newLine(sb.append("Enum.prototype.valueOf=function(val){if(0==this._index.length) this.values();return this._index[val];};"));
        OutputUtilities.newLine(sb);

        for (EnumConst enumConst : enumConstList) {
            generateEnum(enumConst, sb);
        }

        try {
            File outputFile =
                    new File(new File(config.getOutputLocation()).getPath() + File.separator + config.getConstInfo().getFolderName()
                            + File.separator + config.getConstInfo().getPackageName() + ".js");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), config.getEncoding()));
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void generateEnum(EnumConst enumConst, StringBuilder sb) {
        for (ValueEnum value : enumConst.getOptions()) {
            if (null != value.getSubEnum() && value.getSubEnum().size() == 1) {
                EnumConst subEnumConst = value.getSubEnum().get(0);
                generateEnum(subEnumConst,sb);
            }
        }

        if (null != enumConst.getNote() && enumConst.getNote().length() > 0) {
            OutputUtilities.newLine(sb.append("// ").append(enumConst.getNote()));
        }

        sb.append("var ").append(enumConst.getName()).append(" = Object.assign(new Enum(), {");
        for (ValueEnum value : enumConst.getOptions()) {
            sb.append(value.getName()).append(" : { name : '").append(value.getName()).append("', value :").append(value.getValue())
                    .append(", alias :'").append(value.getAlias()).append("',desc:'").append(value.getDesc());

            if (null != value.getSubEnum() && value.getSubEnum().size() == 1) {
                EnumConst subEnumConst = value.getSubEnum().get(0);
                sb.append("', subEnum :").append(subEnumConst.getName()).append("},");
            }else {
                sb.append("'},");
            }
        }
        sb.deleteCharAt(sb.length() - 1).append("});");
        OutputUtilities.newLine(sb);
    }
}
