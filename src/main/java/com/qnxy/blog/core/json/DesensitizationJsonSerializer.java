package com.qnxy.blog.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.qnxy.blog.core.annotations.Desensitization;
import com.qnxy.blog.core.json.desensitization.DesensitizationHandlerFactory;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 被标注的字段 值将被脱敏处理 序列化器
 *
 * @author Qnxy
 */
@Component
public class DesensitizationJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    @Setter(onMethod_ = @Autowired)
    private DesensitizationHandlerFactory desensitizationHandlerFactory;
    private Desensitization desensitization;


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        final String s = this.desensitizationHandlerFactory
                .withValueType(this.desensitization.value(), this.desensitization.customHandlerClass())
                .fillSymbol(this.desensitization.fillSymbol(), value);

        gen.writeString(s);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        this.desensitization = property.getAnnotation(Desensitization.class);
        return this;
    }
    
}
