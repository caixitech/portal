package org.helloframework.docs.annotation;

/**
 * Created by lanjian
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DocApiOutDTO {
    Class clazz();

    String desc() default "";

    /**
     * 返回值是否
     *
     * @return
     */
    boolean generics() default false;

    /**
     * 泛型类型.
     *
     * @return
     */
    Class genericType() default Object.class;
}
