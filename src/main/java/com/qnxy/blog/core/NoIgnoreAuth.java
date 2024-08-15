package com.qnxy.blog.core;

import java.lang.annotation.*;

/**
 * @author Qnxy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface NoIgnoreAuth {

}
