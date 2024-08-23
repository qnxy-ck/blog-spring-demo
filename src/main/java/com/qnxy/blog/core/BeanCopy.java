package com.qnxy.blog.core;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * bean 对象内容复制
 *
 * @author Qnxy
 */
public final class BeanCopy {

    public static <T> T copyValue(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <E> List<E> copyValue(List<?> source, Supplier<E> createObj) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        Objects.requireNonNull(createObj);

        return source.stream()
                .map(it -> copyValue(it, createObj.get()))
                .collect(Collectors.toList());
    }

    public static <E> Set<E> copyValue(Set<?> source, Supplier<E> createObj) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        Objects.requireNonNull(createObj);

        return source.stream()
                .map(it -> copyValue(it, createObj.get()))
                .collect(Collectors.toSet());
    }

}
