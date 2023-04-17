package org.helloframework.docs.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DocApiCodes {
    /**
     * 返回码定义
     *
     * @return
     */
    DocApiCode[] value();
}
