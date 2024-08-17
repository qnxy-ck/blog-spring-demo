package com.qnxy.blog.core.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 资源地址自动添加前缀
 * 只能作用在 String 类型字段上, 其他无效
 *
 * @author Qnxy
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = ResourceAccessJsonSerializer.class)
@JacksonAnnotationsInside
public @interface ResourceAccess {

    /**
     * 是否为多个资源
     */
    boolean multiple() default false;

    /**
     * 如果为多个资源, 它们的分隔符是什么
     */
    String separator() default ",";

    /**
     * 是否将结果转为集合
     */
    boolean outputArray() default false;


}
