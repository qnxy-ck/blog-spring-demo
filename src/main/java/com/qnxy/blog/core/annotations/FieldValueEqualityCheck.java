package com.qnxy.blog.core.annotations;

import com.qnxy.blog.core.validation.FieldEqualityCheckConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 判断两个类成员属性值是否相等
 *
 * @author Qnxy
 */
@Documented
@Constraint(validatedBy = FieldEqualityCheckConstraintValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface FieldValueEqualityCheck {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     * 需要对比的字段是哪个
     */
    String value();

    /**
     * 需要和那个进行相等比较
     */
    String targetFieldName();


}
