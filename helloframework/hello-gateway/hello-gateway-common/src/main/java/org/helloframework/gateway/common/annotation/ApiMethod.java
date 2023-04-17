package org.helloframework.gateway.common.annotation;

import org.helloframework.gateway.common.definition.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lanjian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiMethod {
//    String name() default "";

    String json() default "";

    String version() default "1.0";

    int timeout() default Constants.DEFAULT_SEND_CONNECT_TIMEOUT;

    boolean mesh() default true;

    boolean sync() default false;
}
