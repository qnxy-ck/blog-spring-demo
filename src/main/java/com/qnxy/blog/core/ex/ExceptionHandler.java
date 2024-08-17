package com.qnxy.blog.core.ex;

import com.qnxy.blog.core.ResultStatusCode;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 异常处理工具
 *
 * @author Qnxy
 */
public final class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static <S extends Enum<S> & ResultStatusCode> void expectTrue(Supplier<Boolean> supplier, S s, Object... args) {
        Optional.ofNullable(supplier.get())
                .filter(it -> it)
                .orElseThrow(s.createSupplierEx(args));
    }

    public static <S extends Enum<S> & ResultStatusCode> void expectFalse(Supplier<Boolean> supplier, S s, Object... args) {
        Optional.ofNullable(supplier.get())
                .filter(it -> !it)
                .orElseThrow(s.createSupplierEx(args));
    }

    public static <S extends Enum<S> & ResultStatusCode> void expectFalse(boolean flag, S s, Object... args) {
        if (flag) {
            throw s.createEx(args);
        }
    }

    public static <S extends Enum<S> & ResultStatusCode, T> T expectNotNull(Supplier<T> supplier, S s, Object... args) {
        return Optional.ofNullable(supplier.get())
                .orElseThrow(s.createSupplierEx(args));
    }

    public static <S extends Enum<S> & ResultStatusCode, T> void expectNull(Supplier<T> supplier, S s, Object... args) {
        Optional.ofNullable(supplier)
                .filter(it -> it.get() == null)
                .orElseThrow(s.createSupplierEx(args));
    }

}
