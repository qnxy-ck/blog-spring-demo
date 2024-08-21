package com.qnxy.blog.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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


}
