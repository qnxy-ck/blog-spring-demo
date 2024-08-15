package com.qnxy.blog.core;

import java.lang.annotation.*;

/**
 * 对某个接口进行忽略认证
 *
 * @author Qnxy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface IgnoreAuth {

}
