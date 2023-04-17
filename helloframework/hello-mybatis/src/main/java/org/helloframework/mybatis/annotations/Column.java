package org.helloframework.mybatis.annotations;

import java.lang.annotation.*;

/**
 * Author lanjian
 * Email  lanjian
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

//    boolean id() default false;

    String column() default "";

    String tableAs() default "";

    String desc() default "";

    String length() default "";

    boolean transfer() default true;

    //    String value() default "11";
//
    boolean nullable() default true;

    boolean ase() default false;


}

