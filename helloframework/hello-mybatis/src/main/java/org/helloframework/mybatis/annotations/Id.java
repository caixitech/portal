package org.helloframework.mybatis.annotations;

import org.helloframework.mybatis.definition.GenerationType;

import java.lang.annotation.*;

/**
 * Author lanjian
 * Email  lanjian
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    GenerationType generated() default GenerationType.UUID;
}

