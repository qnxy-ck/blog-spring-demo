package com.qnxy.blog.core;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式常量
 *
 * @author Qnxy
 */
public final class DateTimeFormatterConst {

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
