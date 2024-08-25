package com.qnxy.blog.core.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qnxy.blog.core.json.StaticI18nCodeJsonSerializer;

import java.lang.annotation.*;

/**
 * 将此字段 code 值转换为对应的国际化值
 *
 * @author Qnxy
 */
@JsonSerialize(using = StaticI18nCodeJsonSerializer.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
public @interface StaticI18nCode {
}
