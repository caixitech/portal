package org.helloframework.http.annotation;

import org.helloframework.http.common.HttpFormat;
import org.helloframework.http.common.HttpParamType;

/**
 * 追加到url上的数据
 */
public @interface HttpParam {
    HttpParamType param();

    String name() default "";

    HttpFormat format() default HttpFormat.NONE;
}
