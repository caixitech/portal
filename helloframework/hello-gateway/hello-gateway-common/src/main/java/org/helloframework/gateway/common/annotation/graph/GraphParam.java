package org.helloframework.gateway.common.annotation.graph;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GraphParam {
    GraphParamOP[] ops() default {GraphParamOP.set, GraphParamOP.eq, GraphParamOP.gt, GraphParamOP.gte, GraphParamOP.lt, GraphParamOP.lte};

    String criteria() default "";

    boolean array() default false;
}
