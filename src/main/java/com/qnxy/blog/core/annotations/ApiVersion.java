package com.qnxy.blog.core.annotations;


import java.lang.annotation.*;


/**
 * 接口版本指定
 * <p>
 * 版本格式为 三段 0-9 的数字组成, 每段用 . 分隔
 *
 * @author Qnxy
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    /**
     * 默认版本
     */
    String DEFAULT_VERSION = "1.0.0";

    /**
     * 接口版本控制请求头名称
     */
    String API_VERSION_CONTROL_REQUEST_HEADER_NAME = "X-Api-Version-Control";


    /**
     * 指定该接口的版本号
     */
    String value() default DEFAULT_VERSION;

}
