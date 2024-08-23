package com.qnxy.blog.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 验证数据是否符合期待值
 * 不符合则抛出异常 {@link BizException}
 *
 * @author Qnxy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VerificationExpectations {

    /**
     * 期待为 false 不为 false 则抛出指定状态码异常
     */
    public static <S extends Enum<S> & ResultStatusCode> void expectFalse(Boolean value, S s, Object... args) {
        if (value != Boolean.FALSE) {
            throw s.createEx(args);
        }
    }

    /**
     * 期待为 true 不为 true 则抛出指定状态码异常
     */
    public static <S extends Enum<S> & ResultStatusCode> void expectTrue(Boolean value, S s, Object... args) {
        if (value != Boolean.TRUE) {
            throw s.createEx(args);
        }
    }

    /**
     * 期待为 null 不为 null 则抛出指定状态码异常
     */
    public static <S extends Enum<S> & ResultStatusCode, T> void expectNull(T value, S s, Object... args) {
        if (value != null) {
            throw s.createEx(args);
        }
    }

    /**
     * 期待为非 null 为 null 则抛出指定状态码异常
     */
    public static <S extends Enum<S> & ResultStatusCode, T> T expectNonNull(T value, S s, Object... args) {
        if (value == null) {
            throw s.createEx(args);
        }
        return value;
    }

    /**
     * 要求必须大于 0 否则抛出指定状态码异常
     */
    public static <S extends Enum<S> & ResultStatusCode> void expectGtZero(long value, S s, Object... args) {
        if (value <= 0) {
            throw s.createEx(args);
        }
    }

    
    
    /*
        ==============================数据库 DML 操作返回值成功校验=================================
     */

    public static void expectUpdateOk(long value) {
        expectGtZero(value, CommonResultStatusCodeE.DATA_UPDATE_FAILED);
    }

    public static void expectUpdateOk(boolean value) {
        expectTrue(value, CommonResultStatusCodeE.DATA_UPDATE_FAILED);
    }

    public static void expectInsertOk(long value) {
        expectGtZero(value, CommonResultStatusCodeE.DATA_ADDITION_FAILED);
    }

    public static void expectInsertOk(boolean value) {
        expectTrue(value, CommonResultStatusCodeE.DATA_ADDITION_FAILED);
    }

    public static void expectDeleteOk(long value) {
        expectGtZero(value, CommonResultStatusCodeE.DATA_DELETION_FAILED);
    }

    public static void expectDeleteOk(boolean value) {
        expectTrue(value, CommonResultStatusCodeE.DATA_DELETION_FAILED);
    }

     /*
        =============================================================================
     */


    /**
     * 期待没有异常, 如果执行中发生了异常则抛出指定状态码的异常
     * <p>
     * 如果执行中抛出了 {@link BizException} 将被原样抛出
     */
    public static <S extends Enum<S> & ResultStatusCode, T> T expectNotException(Callable<T> execute, S s, Object... args) {
        return expectNotException(execute, Exception.class, s, args);
    }

    /**
     * 处理没有执行结果的
     */
    public static <S extends Enum<S> & ResultStatusCode> void expectNotException(VoidCallable func, S s, Object... args) {
        expectNotException(func, Exception.class, s, args);
    }

    public static <S extends Enum<S> & ResultStatusCode> void expectNotException(VoidCallable func, Class<? extends Exception> exClass, S s, Object... args) {
        Objects.requireNonNull(func);
        expectNotException(() -> {
            func.call();
            return null;
        }, s, args);
    }

    public static <S extends Enum<S> & ResultStatusCode> void expectNotIOException(VoidCallable func, S s, Object... args) {
        expectNotException(func, IOException.class, s, args);
    }

    public static <S extends Enum<S> & ResultStatusCode, T> T expectNotIOException(Callable<T> execute, S s, Object... args) {
        return expectNotException(execute, IOException.class, s, args);
    }


    /**
     * 期待没有指定类型的异常, 如果执行中发生了指定的异常则抛出指定状态码的异常
     * <p>
     * 如果执行中抛出了 {@link BizException} 将被原样抛出
     */
    public static <S extends Enum<S> & ResultStatusCode, T> T expectNotException(Callable<T> execute, Class<? extends Exception> exClass, S s, Object... args) {
        Objects.requireNonNull(execute);

        try {
            return execute.call();
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            if (exClass.isAssignableFrom(e.getClass())) {
                throw new BizException(e, s, args);
            }

            throw new NestedException(e);
        }
    }

    @FunctionalInterface
    public interface VoidCallable {

        void call() throws Exception;

    }


    /**
     * 结果期待验证时产生的嵌套异常
     */
    public static class NestedException extends RuntimeException {
        public NestedException(Throwable cause) {
            super(cause);
        }
    }

}