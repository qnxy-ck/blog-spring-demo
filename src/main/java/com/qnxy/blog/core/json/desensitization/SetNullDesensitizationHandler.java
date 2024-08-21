package com.qnxy.blog.core.json.desensitization;

import com.qnxy.blog.core.annotations.Desensitization;
import org.springframework.stereotype.Component;

/**
 * 将结果设置为空
 *
 * @author Qnxy
 */
@Component
@Desensitization.Handler(Desensitization.ValueType.SET_NULL)
public class SetNullDesensitizationHandler implements Desensitization.DesensitizationHandler {

    @Override
    public String fillSymbol(char symbol, String data) {
        return null;
    }

}
