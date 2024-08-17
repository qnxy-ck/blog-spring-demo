package com.qnxy.blog.core.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 字段内容相等性校验
 *
 * @author Qnxy
 */
public class FieldEqualityCheckConstraintValidator implements ConstraintValidator<FieldEqualityCheck, Object> {

    private String baseFieldName;
    private String targetFieldName;

    private static Object fieldValue(Object obj, String fieldName) {
        Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
        if (field == null) {
            throw new IllegalArgumentException("无法找到该字段: " + fieldName);
        }
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, obj);
    }

    @Override
    public void initialize(FieldEqualityCheck constraintAnnotation) {
        this.baseFieldName = constraintAnnotation.value();
        this.targetFieldName = constraintAnnotation.targetFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object baseV = fieldValue(value, baseFieldName);
        Object targetV = fieldValue(value, targetFieldName);

        return Objects.equals(baseV, targetV);
    }


}

