package org.helloframework.docs.annotation;

import java.lang.annotation.*;

/**
 * 时间轴信息。
 * Created by zhpeng2 on 2017/9/29.
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocTimeline {
    /**
     * 时间。时间轴会按时间字段排序，格式：yyyy-MM-dd。
     */
    String time();

    /**
     * 时间轴内容。
     */
    String content();
}
