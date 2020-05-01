package com.lifeonwalden.generator4db;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class Test {
    public static void main(String[] args) {
        Schema<Long> schema = RuntimeSchema.getSchema(Long.class);
        byte[] bytes = ProtobufIOUtil.toByteArray(1024l, schema, LinkedBuffer.use(new byte[1024]));
        ProtobufIOUtil.mergeFrom(bytes, Long.valueOf(0), schema);

        System.out.println(Long.valueOf(0));
    }
}
