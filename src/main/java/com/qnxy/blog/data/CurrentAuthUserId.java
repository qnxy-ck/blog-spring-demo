package com.qnxy.blog.data;

import lombok.Data;

/**
 * @author Qnxy
 */
@Data
public class CurrentAuthUserId {

    private final Long userId;

    private CurrentAuthUserId(Long userId) {
        this.userId = userId;
    }

    public static CurrentAuthUserId create(Long userId) {
        return new CurrentAuthUserId(userId);
    }
    
}
