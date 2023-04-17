package org.helloframework.gateway.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lanjian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ApiFieldDoc {
    String desc() default "";

    boolean required() default true;

    String remark() default "";

    Class cls() default Object.class;

}
