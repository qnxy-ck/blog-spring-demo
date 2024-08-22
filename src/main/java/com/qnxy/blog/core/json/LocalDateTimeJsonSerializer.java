package com.qnxy.blog.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qnxy.blog.core.DateTimeFormatterConst;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Qnxy
 */
public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {


    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            return;
        }

        final String r = value.format(DateTimeFormatterConst.DEFAULT_DATE_TIME_FORMATTER);
        gen.writeString(r);
    }

    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }
}
