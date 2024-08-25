package com.qnxy.blog.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

/**
 * 返回国际化信息
 *
 * @author Qnxy
 */
@Component
@RequiredArgsConstructor
public class StaticI18nCodeJsonSerializer extends JsonSerializer<String> {

    private final MessageSource messageSource;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            return;
        }

        final Locale locale = LocaleContextHolder.getLocale();
        final String message = this.messageSource.getMessage(value, null, locale);
        gen.writeString(message);

    }

}
