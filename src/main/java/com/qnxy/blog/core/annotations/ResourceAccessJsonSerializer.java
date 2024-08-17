package com.qnxy.blog.core.annotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.qnxy.blog.configuration.ProjectConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 资源地址自动添加前缀 Json 序列化器
 *
 * @author Qnxy
 */
@Component
@RequiredArgsConstructor
public class ResourceAccessJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final ProjectConfigurationProperties projectConfigurationProperties;

    private ResourceAccess resourceAccess;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!StringUtils.hasText(value)) {
            return;
        }

        if (!this.resourceAccess.multiple()) {
            gen.writeString(this.splicing(value));
            return;
        }

        final String[] split = value.split(this.resourceAccess.separator());

        for (int i = 0; i < split.length; i++) {
            split[i] = this.splicing(split[i]);
        }

        if (this.resourceAccess.outputArray()) {
            gen.writeArray(split, 0, split.length);
        } else {
            gen.writeString(String.join(this.resourceAccess.separator(), split));
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        final ResourceAccess resourceAccess = property.getAnnotation(ResourceAccess.class);

        if (property.getType().isTypeOrSubTypeOf(String.class)) {
            this.resourceAccess = resourceAccess;
            return this;
        }

        return prov.findValueSerializer(property.getType(), property);
    }


    private String splicing(String uri) {
        return this.projectConfigurationProperties.getFileUploadServerBaseUrl() + uri.trim();
    }

}
