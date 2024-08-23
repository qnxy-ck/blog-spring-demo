package com.qnxy.blog.core;

import com.qnxy.blog.core.annotations.FieldSensitive;
import com.qnxy.blog.core.ciphertext.CiphertextHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.qnxy.blog.core.CommonResultStatusCodeE.SENSITIVE_DATA_PROCESSING_FAILED;

/**
 * {@link FieldSensitive} 注解处理
 * <p>
 * <p>
 * MyBatis 返回值/入参 敏感数据处理
 * 不支持简单值处理, 所以需要处理加解密一定需要放在类中并且加上 {@link FieldSensitive} 注解, 被注解的字段只能是字符串类型
 * <p>
 * 在对参数进行处理过程中, 可能出现同一个参数对象中的字段被加密两次的情况
 * 为了处理这个问题在加密后的字符串前面增加了一个标志 {@link FieldSensitiveInterceptor#ENTITY_SENSITIVE_FIELD_MAP_CACHE}
 * 如果检测到了已经被加密, 则会忽略加密
 * 当然 实际存储密文到数据库之前是不会存在这个标志的, 在存入之前去除
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
public class FieldSensitiveInterceptor extends BaseTypeHandler<String> implements Interceptor {

    private static final Map<Class<?>, Field[]> ENTITY_SENSITIVE_FIELD_MAP_CACHE = new ConcurrentHashMap<>();
    private static final String CIPHERTEXT_START_FLAG = "__FIELD_SENSITIVE_INTERCEPTOR_CIPHERTEXT_FLAG__";

    private final CiphertextHandlerFactory ciphertextHandlerFactory;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        return switch (invocation.getMethod().getName()) {
            case "handleResultSets" -> this.handleResultSetsIntercept(invocation);
            case "setParameters" -> this.setParametersIntercept(invocation);
            default -> invocation.proceed();
        };
    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {

        if (parameter.startsWith(CIPHERTEXT_START_FLAG)) {
            final String s = parameter.substring(FieldSensitiveInterceptor.CIPHERTEXT_START_FLAG.length());
            ps.setString(i, s);
            return;
        }

        ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }


    /**
     * 返回值解密处理
     */
    private List<?> handleResultSetsIntercept(Invocation invocation) throws Exception {

        final List<?> resultList = (List<?>) invocation.proceed();
        if (resultList.isEmpty()) {
            return resultList;
        }

        Optional.ofNullable(this.findEntitySensitiveStringFields(resultList.get(0).getClass()))
                .ifPresent(fields -> resultList.forEach(it -> this.modifyEntitySensitiveFieldsValue(it, fields, false)));

        return resultList;
    }

    private Object setParametersIntercept(Invocation invocation) throws Exception {
        final ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        final Object parameterObject = parameterHandler.getParameterObject();

        if (parameterObject == null) {
            return invocation.proceed();
        }

        /*
            处理参数为 MapperMethod.ParamMap<?> 的情况
            其中的元素存在重复, 对已处理过的值加入 set 集合中进行去除处理
            
            Map 中 Value 的类型分别为集合和实体类的类型, 需要分别进行处理
         */
        final Set<Object> set = new HashSet<>();
        if (parameterObject instanceof Map<?, ?> paramMap) {
            for (Object mapKey : paramMap.keySet()) {
                final Object mapValue = paramMap.get(mapKey);

                if (mapValue == null || set.contains(mapValue)) {
                    continue;
                }

                if (mapValue instanceof Collection<?> collection && !collection.isEmpty()) {
                    this.setParamWithCollection(collection);
                    set.add(mapValue);
                    continue;
                }

                Optional.ofNullable(this.findEntitySensitiveStringFields(mapValue.getClass()))
                        .ifPresent(it -> this.modifyEntitySensitiveFieldsValue(mapValue, it, true));

                set.add(mapValue);
            }
        }
        
        /*
            当执行参数为集合的情况, 并且集合不为空
         */
        if (parameterObject instanceof Collection<?> collection && !collection.isEmpty()) {
            this.setParamWithCollection(collection);
            return invocation.proceed();
        }

        // 执行参数为单个实体类的情况
        Optional.ofNullable(this.findEntitySensitiveStringFields(parameterObject.getClass()))
                .ifPresent(it -> this.modifyEntitySensitiveFieldsValue(parameterHandler, it, true));

        return invocation.proceed();
    }

    /**
     * 处理数据类型为集合的参数
     * <p>
     * 为集合的每个元素进行加密
     */
    private void setParamWithCollection(Collection<?> collection) {
        final Class<?> cla = collection.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(Object::getClass)
                .orElse(null);

        if (cla == null) {
            return;
        }

        Optional.ofNullable(this.findEntitySensitiveStringFields(cla))
                .ifPresent(fields -> collection.stream()
                        .filter(Objects::nonNull)
                        .forEach(it -> this.modifyEntitySensitiveFieldsValue(it, fields, true))
                );
    }

    /**
     * 查询指定实体是否存在需要加解密字段
     *
     * @param entityClass 需要查找的实体类型
     * @return 如果找到则返回该字段的 Field 对象, 未找到返回 null
     */
    private Field[] findEntitySensitiveStringFields(Class<?> entityClass) {
        final Field[] fields = ENTITY_SENSITIVE_FIELD_MAP_CACHE.computeIfAbsent(
                entityClass,
                key -> Arrays.stream(entityClass.getDeclaredFields())
                        .filter(it -> it.getType().equals(String.class) && it.isAnnotationPresent(FieldSensitive.class))
                        .peek(it -> it.setAccessible(true))
                        .toArray(Field[]::new)
        );

        return fields.length == 0 ? null : fields;
    }

    /**
     * 对需要加解密的对象字段进行操作
     *
     * @param target    需要加解密的对象
     * @param fields    需要加解密对象的字段对象数组
     * @param isEncrypt 是否为加密 true = 加密, 否则为解密
     */
    private void modifyEntitySensitiveFieldsValue(Object target, Field[] fields, boolean isEncrypt) {
        for (Field field : fields) {
            final Object v = ReflectionUtils.getField(field, target);

            if (v != null) {
                /*
                    如果该值已经被加密 则直接返回不进行操作
                 */
                if (v.toString().startsWith(CIPHERTEXT_START_FLAG)) {
                    continue;
                }

                final FieldSensitive annotation = field.getAnnotation(FieldSensitive.class);
                final String decrypt = this.encryptOrDecrypt(annotation.value(), v.toString(), isEncrypt);
                ReflectionUtils.setField(field, target, decrypt);
            }
        }
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
            if (isEncrypt) {
                return CIPHERTEXT_START_FLAG + ciphertextHandler.encrypt(data);
            }

            return ciphertextHandler.decrypt(data);

        } catch (Exception e) {
            log.error("数据{}失败, ciphertextHandler: {}", isEncrypt ? "加密" : "解密", ciphertextHandler.getClass(), e);
            throw new BizException(SENSITIVE_DATA_PROCESSING_FAILED);
        }
    }

}


