package com.qnxy.blog.core.json.desensitization;

import com.qnxy.blog.core.annotations.Desensitization;
import com.qnxy.blog.core.annotations.Desensitization.ValueType;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 脱敏处理器工厂 {@link Desensitization.DesensitizationHandler}
 *
 * @author Qnxy
 */
@Component
public class DesensitizationHandlerFactory implements InitializingBean {

    private static final Map<ValueType, Desensitization.DesensitizationHandler> DESENSITIZATION_HANDLER_MAP = new HashMap<>();
    private static final Map<Class<? extends Desensitization.DesensitizationHandler>, Desensitization.DesensitizationHandler> CUSTOMER_DESENSITIZATION_HANDLER_MAP = new HashMap<>();
    @Setter(onMethod_ = @Autowired)
    private List<Desensitization.DesensitizationHandler> desensitizationHandlerList;

    public Desensitization.DesensitizationHandler withValueType(ValueType type, Class<? extends Desensitization.DesensitizationHandler> customDesensitizationHandlerClass) {
        Desensitization.DesensitizationHandler desensitizationHandler;

        if (type == ValueType.CUSTOMER) {
            if (customDesensitizationHandlerClass.equals(Desensitization.DesensitizationHandler.class)) {
                throw new IllegalArgumentException("当使用自定义时, 你必须使用你的实现类覆盖 Desensitization.customHandlerClass 属性");
            }

            desensitizationHandler = CUSTOMER_DESENSITIZATION_HANDLER_MAP.get(customDesensitizationHandlerClass);
        } else {
            desensitizationHandler = DESENSITIZATION_HANDLER_MAP.get(type);
        }

        if (desensitizationHandler == null) {
            throw new IllegalArgumentException(String.format("当前类型: [%s] 找不到对应的脱敏处理器", type));
        }

        return desensitizationHandler;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for (Desensitization.DesensitizationHandler desensitizationHandler : this.desensitizationHandlerList) {
            final Desensitization.Handler handler = desensitizationHandler.getClass().getAnnotation(Desensitization.Handler.class);
            if (handler == null) {
                CUSTOMER_DESENSITIZATION_HANDLER_MAP.put(desensitizationHandler.getClass(), desensitizationHandler);
                continue;
            }

            if (handler.value() == ValueType.CUSTOMER) {
                continue;
            }

            final Desensitization.DesensitizationHandler r = DESENSITIZATION_HANDLER_MAP.put(handler.value(), desensitizationHandler);
            if (r != null) {
                throw new IllegalStateException(String.format("类型: [%s] 脱敏处理器重复: [%s, %s]", handler.value(), r, desensitizationHandler));
            }
        }
    }


}
