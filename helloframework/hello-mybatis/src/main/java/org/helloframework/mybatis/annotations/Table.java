package org.helloframework.mybatis.annotations;

import org.helloframework.mybatis.definition.JoinType;

import java.lang.annotation.*;

/**
 * Author lanjian
 * Email  lanjian
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    String table() default "";

    String tableAs() default "";

    JoinType joinType() default JoinType.none;

    String joinTable() default "";

    String joinTableAs() default "";

    String tableOn() default "";

    String joinTableOn() default "";

    String db() default "mysql";

    String sql() default "";

    boolean ase() default false;

//    String sqlCount() default "";
}
