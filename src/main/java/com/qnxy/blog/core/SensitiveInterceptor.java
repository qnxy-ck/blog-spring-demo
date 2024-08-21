package com.qnxy.blog.core;

import com.qnxy.blog.core.annotations.Sensitive;
import com.qnxy.blog.core.ciphertext.CiphertextHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.qnxy.blog.core.CommonResultStatusCodeE.SENSITIVE_DATA_PROCESSING_FAILED;

/**
 * Sensitive 注解处理
 * <p>
 * MyBatis 返回值/入参 敏感数据处理
 *
 * @author Qnxy
 */
@Component
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)
})
@RequiredArgsConstructor
@Slf4j
public class SensitiveInterceptor implements Interceptor {

    private static final Map<Class<?>, List<Field>> ENTITY_SENSITIVE_FIELD_MAP_CACHE = new ConcurrentHashMap<>();
    private static final Field parameterObjectField;

    static {
        try {
            parameterObjectField = DefaultParameterHandler.class.getDeclaredField("parameterObject");
            parameterObjectField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final CiphertextHandlerFactory ciphertextHandlerFactory;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final String name = invocation.getMethod().getName();
        if (name.equals("handleResultSets")) {
            return this.handleResultSetsIntercept(invocation);
        } else if (name.equals("setParameters")) {
            return this.setParametersIntercept(invocation);
        }

        return invocation.proceed();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object setParametersIntercept(Invocation invocation) throws Exception {
        final Object target = invocation.getTarget();

        // 单值/map
        final Object parameterObject = parameterObjectField.get(target);

        if (parameterObject == null) {
            // 没有需要处理的参数, 直接返回
            return invocation.proceed();
        }

        if (!(parameterObject instanceof String || parameterObject instanceof Map)) {
            final List<Field> entitySensitiveFieldList = ENTITY_SENSITIVE_FIELD_MAP_CACHE.computeIfAbsent(
                    parameterObject.getClass(),
                    key -> Arrays.stream(key.getDeclaredFields())
                            .filter(it -> it.isAnnotationPresent(Sensitive.class) && it.getType().equals(String.class))
                            .toList()
            );

            for (Field field : entitySensitiveFieldList) {
                field.setAccessible(true);
                final Object v = field.get(parameterObject);
                if (v != null) {
                    final Sensitive sensitive = field.getAnnotation(Sensitive.class);
                    final String encrypt = this.encryptOrDecrypt(sensitive.value(), v.toString(), true);
                    field.set(parameterObject, encrypt);
                }
            }
            return invocation.proceed();
        }

        final Method method = findExecuteMethod(target);

        Map<String, Sensitive> sensitiveParamMap = Arrays.stream(method.getParameters())
                .filter(it -> it.isAnnotationPresent(Sensitive.class))
                .collect(Collectors.toMap(
                        it -> {
                            final Param param = it.getAnnotation(Param.class);
                            return param == null ? it.getName() : param.value();
                        },
                        it -> it.getAnnotation(Sensitive.class)
                ));

        // 单值且字符串 该参数上加上了 Sensitive 注解
        if (parameterObject instanceof String && !sensitiveParamMap.isEmpty()) {
            final Sensitive sensitive = sensitiveParamMap.values().iterator().next();
            final String encrypt = this.encryptOrDecrypt(sensitive.value(), parameterObject.toString(), true);
            parameterObjectField.set(target, encrypt);
            return invocation.proceed();
        }

        final Set<Object> disSet = new HashSet<>();
        if (parameterObject instanceof Map paramMap) {
            for (Object paramValKey : paramMap.keySet()) {
                final Object paramVal = paramMap.get(paramValKey);

                if (disSet.contains(paramVal)) {
                    continue;
                }

                if (paramVal instanceof String paramValStr) {
                    final Sensitive sensitive = sensitiveParamMap.get(paramValKey.toString());
                    if (sensitive != null) {
                        final String encrypt = this.encryptOrDecrypt(sensitive.value(), paramValStr, true);
                        paramMap.put(paramValKey, encrypt);
                    }
                    continue;
                }

                if (paramVal instanceof Collection paramValCollection && !paramValCollection.isEmpty()) {
                    final Object nextObj = paramValCollection.iterator().next();

                    if (nextObj instanceof String) {
                        final Sensitive sensitive = sensitiveParamMap.get(paramValKey.toString());
                        if (sensitive != null) {
                            final List list = paramValCollection.stream()
                                    .map(it -> this.encryptOrDecrypt(sensitive.value(), it.toString(), true))
                                    .toList();
                            paramMap.put(paramValKey, list);
                        }

                        continue;
                    }

                    final List<Field> entitySensitiveFieldList = ENTITY_SENSITIVE_FIELD_MAP_CACHE.computeIfAbsent(
                            nextObj.getClass(),
                            key -> Arrays.stream(key.getDeclaredFields())
                                    .filter(it -> it.getType().equals(String.class) && it.isAnnotationPresent(Sensitive.class))
                                    .toList()
                    );

                    if (!entitySensitiveFieldList.isEmpty()) {
                        for (Object object : paramValCollection) {
                            for (Field field : entitySensitiveFieldList) {
                                field.setAccessible(true);
                                final Object o = field.get(object);
                                Sensitive sensitive = field.getAnnotation(Sensitive.class);
                                final String encrypt = this.encryptOrDecrypt(sensitive.value(), o.toString(), true);
                                field.set(object, encrypt);
                            }
                        }
                        disSet.add(paramValCollection);
                    }

                    continue;
                }

                final List<Field> entitySensitiveFieldList = ENTITY_SENSITIVE_FIELD_MAP_CACHE.computeIfAbsent(
                        paramVal.getClass(),
                        key -> Arrays.stream(key.getDeclaredFields())
                                .filter(it -> it.isAnnotationPresent(Sensitive.class) && it.getType().equals(String.class))
                                .toList()
                );

                for (Field field : entitySensitiveFieldList) {
                    field.setAccessible(true);
                    final Object v = field.get(paramVal);
                    if (v != null) {
                        final Class<? extends CiphertextHandler> value = field.getAnnotation(Sensitive.class).value();
                        final String encrypt = this.encryptOrDecrypt(value, v.toString(), true);
                        field.set(paramVal, encrypt);
                    }
                }

                disSet.add(paramVal);
            }
        }

        return invocation.proceed();
    }


    @SuppressWarnings("unchecked")
    private List<Object> handleResultSetsIntercept(Invocation invocation) throws Exception {
        List<Object> resultList = (List<Object>) invocation.proceed();
        if (resultList.isEmpty()) {
            return resultList;
        }

        final Object resultObj = resultList.get(0);

        if (resultObj instanceof String) {
            final Method method = findExecuteMethod(invocation.getTarget());

            Sensitive sensitive = method.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                // 需要对返回值进行解密操作
                resultList.replaceAll(it -> it == null ? null : this.encryptOrDecrypt(sensitive.value(), it.toString(), false));
            }

            return resultList;
        }


        final List<Field> entitySensitiveFieldList = ENTITY_SENSITIVE_FIELD_MAP_CACHE.computeIfAbsent(
                resultObj.getClass(),
                key -> Arrays.stream(key.getDeclaredFields())
                        .filter(it -> it.getType().equals(String.class) && it.isAnnotationPresent(Sensitive.class))
                        .toList()
        );

        if (entitySensitiveFieldList.isEmpty()) {
            return resultList;
        }


        resultList.forEach(it -> {
            for (Field field : entitySensitiveFieldList) {
                ReflectionUtils.makeAccessible(field);
                Object v = ReflectionUtils.getField(field, it);

                if (v != null) {
                    Sensitive annotation = field.getAnnotation(Sensitive.class);
                    final String decrypt = this.encryptOrDecrypt(annotation.value(), v.toString(), false);
                    ReflectionUtils.setField(field, it, decrypt);
                }
            }
        });

        return resultList;
    }

    private Method findExecuteMethod(Object target) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Field mappedStatementField = target.getClass().getDeclaredField("mappedStatement");
        mappedStatementField.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementField.get(target);

        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);

        return Arrays.stream(Class.forName(className).getMethods())
                .filter(it -> it.getName().equals(methodName))
                .findFirst()
                .orElseThrow();
    }


    /**
     * 编解码处理
     *
     * @param type      处理器类型
     * @param data      待处理数据
     * @param isEncrypt 编码还是解码 true = 编码, false = 解码
     * @return 加解密后的数据
     */
    private String encryptOrDecrypt(Class<? extends CiphertextHandler> type, String data, boolean isEncrypt) {
        final CiphertextHandler ciphertextHandler = this.ciphertextHandlerFactory.withCiphertextHandlerClass(type);

        try {
            return isEncrypt
                    ? ciphertextHandler.encrypt(data)
                    : ciphertextHandler.decrypt(data);
        } catch (Exception e) {
            log.error("数据加解密失败, ciphertextHandler: {}", ciphertextHandler.getClass(), e);
            throw new BizException(SENSITIVE_DATA_PROCESSING_FAILED);
        }

    }


}


