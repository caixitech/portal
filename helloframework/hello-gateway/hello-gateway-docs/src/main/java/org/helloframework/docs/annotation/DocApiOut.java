package org.helloframework.docs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lanjian
 */
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DocApiOut {
    DocApiFiled[] value() default {};
}
