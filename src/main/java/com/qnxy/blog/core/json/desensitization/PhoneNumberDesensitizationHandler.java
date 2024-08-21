package com.qnxy.blog.core.json.desensitization;

import com.qnxy.blog.core.annotations.Desensitization;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 手机号脱敏处理器实现
 *
 * @author Qnxy
 */
@Component
@Desensitization.Handler(Desensitization.ValueType.PHONE_NUMBER)
public class PhoneNumberDesensitizationHandler implements Desensitization.DesensitizationHandler {

    @Override
    public String fillSymbol(char symbol, String data) {
        if (data == null) {
            return null;
        }

        if (data.length() < 7) {
            return data;
        }

        char[] charArray = data.toCharArray();
        Arrays.fill(charArray, 3, data.length() - 4, symbol);

        return new String(charArray);
    }

}
