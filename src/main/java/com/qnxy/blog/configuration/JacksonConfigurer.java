package com.qnxy.blog.configuration;

import com.qnxy.blog.core.json.LocalDateTimeJsonSerializer;
import com.qnxy.blog.core.json.LongValueJsonSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Jackson 增加自定义配置
 *
 * @author Qnxy
 */
@Component
public class JacksonConfigurer {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return it -> it.serializers(
                new LongValueJsonSerializer(),
                new LocalDateTimeJsonSerializer()
        );
    }


}
