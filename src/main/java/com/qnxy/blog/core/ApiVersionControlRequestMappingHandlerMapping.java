package com.qnxy.blog.core;

import com.qnxy.blog.core.annotations.ApiVersion;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.regex.Pattern;


/**
 * @author Qnxy
 */
@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
public class ApiVersionControlRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /**
     * 当请求接口时如果没有指定版本参数
     * 并且该接口存在多个版本 使用高版本还是默认版本
     * <p>
     * 为 true 则使用最高版本
     * 为 false 则使用低版本
     */
    private final boolean highVersion;

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return conditionBy(handlerType);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return conditionBy(method);
    }

    private ApiRequestCondition conditionBy(AnnotatedElement element) {
        final ApiVersion apiVersion = element.getAnnotation(ApiVersion.class);
        if (apiVersion == null) {
            return new ApiRequestCondition(ApiVersion.DEFAULT_VERSION);
        }
        return new ApiRequestCondition(apiVersion.value());
    }

    @Data
    private class ApiRequestCondition implements RequestCondition<ApiRequestCondition> {

        private static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+(\\.\\d+){2}$");

        private final String apiVersion;

        public ApiRequestCondition(String apiVersion) {
            if (isInvalidVersion(apiVersion)) {
                throw new IllegalArgumentException("无效的 API 版本: " + apiVersion);
            }
            this.apiVersion = apiVersion;
        }

        /**
         * 判断接口版本是否为不可用的
         *
         * @return 不可用为true
         */
        public static boolean isInvalidVersion(String input) {
            if (input == null) {
                return true;
            }
            return !VERSION_PATTERN.matcher(input).matches();
        }

        /**
         * 比较两个版本的大小
         *
         * @return v1 > v2 返回 1, 等于为 0, 小于为 -1
         */
        public static int compare(String v1, String v2) {
            if (v1.equals(v2)) {
                return 0;
            }

            String[] version1Codes = v1.split("\\.");
            String[] version2Codes = v2.split("\\.");
            for (int i = 0; i < version1Codes.length; i++) {
                int code1 = Integer.parseInt(version1Codes[i]);
                int code2 = Integer.parseInt(version2Codes[i]);
                if (code1 > code2) {
                    return 1;
                } else if (code1 < code2) {
                    return -1;
                }
            }
            return 0;
        }

        /**
         * 判断条件使用那个
         * 这里优先使用方法上的
         *
         * @param other 方法上构建的条件, this 为类上的条件
         * @return .
         */
        @Override
        public ApiRequestCondition combine(ApiRequestCondition other) {
            if (!other.apiVersion.equals(ApiVersion.DEFAULT_VERSION)) {
                // 如果方法上的版本和默认一致则使用类上的
                return other;
            }
            return this;
        }

        /**
         * 根据当前请求获取一个条件
         *
         * @param request 请求信息
         * @return .
         */
        @Override
        public ApiRequestCondition getMatchingCondition(HttpServletRequest request) {
            final String requestApiVersion = request.getHeader(ApiVersion.API_VERSION_CONTROL_REQUEST_HEADER_NAME);

            if (requestApiVersion == null) {
                // 如果没有指定版本, 则返回所有版本在 compareTo 方法中进行比较查找使用哪一个
                return this;
            }

            if (isInvalidVersion(requestApiVersion)) {
                throw new BizException(CommonResultStatusCodeE.ILLEGAL_VERSION_FORMAT, requestApiVersion);
            }

            return compare(apiVersion, requestApiVersion) == 0 ? this : null;
        }

        @Override
        public int compareTo(ApiRequestCondition other, HttpServletRequest request) {
            if (ApiVersionControlRequestMappingHandlerMapping.this.highVersion) {
                // 判断是否使用最大的版本
                return compare(apiVersion, other.apiVersion) > 0 ? -1 : 1;
            }

            return compare(apiVersion, other.apiVersion);
        }

    }

}
