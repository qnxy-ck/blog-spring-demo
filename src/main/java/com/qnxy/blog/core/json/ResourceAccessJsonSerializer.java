package com.qnxy.blog.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.qnxy.blog.configuration.ProjectConfigurationProperties;
import com.qnxy.blog.controller.FileOperateController;
import com.qnxy.blog.core.annotations.ResourceAccess;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 资源地址自动添加前缀 Json 序列化器
 *
 * @author Qnxy
 */
@Component
public class ResourceAccessJsonSerializer extends JsonSerializer<String> implements ContextualSerializer, InitializingBean {

    @Setter(onMethod_ = @Autowired)
    private ProjectConfigurationProperties projectConfigurationProperties;
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
        return String.format(
                "%s?%s=%s",
                this.projectConfigurationProperties.getFileAccessAddress(),
                FileOperateController.PARAM_NAME,
                uri.trim()
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final String fileAccessAddress = this.projectConfigurationProperties.getFileAccessAddress();

        if (!StringUtils.hasText(fileAccessAddress)) {
            throw new IllegalArgumentException("文件访问路径前缀未设置: ProjectConfigurationProperties.fileAccessAddress");
        }

        try {
            new URL(fileAccessAddress);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("文件访问路径前缀无效", e);
        }
    }

}
