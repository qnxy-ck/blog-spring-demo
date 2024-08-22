package com.qnxy.blog.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * long 类型数据写出时改写为字符串输出
 * <p>
 * 避免数据精度丢失问题
 *
 * @author Qnxy
 */
public class LongValueJsonSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(value.toString());
        }
    }

    @Override
    public Class<Long> handledType() {
        return Long.class;
    }
    
}
