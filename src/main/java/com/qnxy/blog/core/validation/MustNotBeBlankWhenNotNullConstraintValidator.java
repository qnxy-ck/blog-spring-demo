package com.qnxy.blog.core.validation;

import com.qnxy.blog.core.annotations.MustNotBeBlankWhenNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Qnxy
 */
public class MustNotBeBlankWhenNotNullConstraintValidator implements ConstraintValidator<MustNotBeBlankWhenNotNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return !value.isBlank();

    }
}
