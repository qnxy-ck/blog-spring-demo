package com.qnxy.blog.data.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户注册事件类型
 *
 * @author Qnxy
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public final class UserRegisterEvent {

    private final Long userId;

    public static UserRegisterEvent of(final Long userId) {
        return new UserRegisterEvent(userId);
    }
}
