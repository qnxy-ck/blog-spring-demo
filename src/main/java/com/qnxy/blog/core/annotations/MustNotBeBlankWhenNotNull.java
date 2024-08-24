package com.qnxy.blog.core.annotations;

import com.qnxy.blog.core.validation.MustNotBeBlankWhenNotNullConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 字段可以为 null
 * 如果不为 null 的时候, 该值不能为空字符
 *
 * @author Qnxy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MustNotBeBlankWhenNotNullConstraintValidator.class)
public @interface MustNotBeBlankWhenNotNull {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
