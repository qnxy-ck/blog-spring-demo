package com.qnxy.blog.core;

import static com.qnxy.blog.core.enums.CommonResultStatusCodeE.DATA_ADDITION_FAILED;
import static com.qnxy.blog.core.enums.CommonResultStatusCodeE.DATA_UPDATE_FAILED;

/**
 * 返回结果工具
 *
 * @author Qnxy
 */
public final class ResultUtil {

    private ResultUtil() {
    }

    public static void updateSuc(long updateRowNum) {
        if (updateRowNum <= 0) {
            throw DATA_UPDATE_FAILED.createEx();
        }
    }

    public static void insertSuc(long updateRowNum) {
        if (updateRowNum <= 0) {
            throw DATA_ADDITION_FAILED.createEx();
        }
    }


}
