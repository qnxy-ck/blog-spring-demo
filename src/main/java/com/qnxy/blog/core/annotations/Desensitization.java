package com.qnxy.blog.core.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qnxy.blog.core.json.DesensitizationJsonSerializer;

import java.lang.annotation.*;


/**
 * 被标注的字段 值将被脱敏处理
 *
 * @author Qnxy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationJsonSerializer.class)
public @interface Desensitization {

    /**
     * 被标注的值类型是什么
     */
    ValueType value();

    /**
     * 填充符号
     */
    char fillSymbol() default '*';

    /**
     * 当 {@link Desensitization#value()} 值为 {@link ValueType#CUSTOMER} 时该值必填 (必须使用已实现类 class 覆盖, 默认不起作用)
     */
    Class<? extends DesensitizationHandler> customHandlerClass() default DesensitizationHandler.class;


    /**
     * 需要处理值的类型是什么
     */
    enum ValueType {
        PHONE_NUMBER,
        EMAIL,
        ADDRESS,
        ID_CARD,
        USERNAME,

        /**
         * 自定义
         * <p>
         * 需要实现 {@link DesensitizationHandler} 接口
         * 并且需要指定该实现类的class在 {@link Desensitization#customHandlerClass()} 中 (默认的不起任何作用, 需要覆盖)
         */
        CUSTOMER
    }

    /**
     * 用于标注已实现的 {@link DesensitizationHandler} 类
     * <p>
     * {@link ValueType#CUSTOMER} 类型无需注解!!! 注解了也不生效
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Handler {

        /**
         * 该处理器的类型是什么
         */
        ValueType value();
    }

    /**
     * 脱敏处理器接口, 各个类型或自定义类型脱敏 需要实现该接口
     * 并在对应实现类上标注当前实现类对应的是那种类型
     * <p>
     * {@link ValueType#CUSTOMER} 类型无需注解!!! 注解了也不生效
     */
    @FunctionalInterface
    interface DesensitizationHandler {

        String fillSymbol(char symbol, String data);

    }

}

