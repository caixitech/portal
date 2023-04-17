package org.helloframework.gateway.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lanjian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Protobuf {

    /**
     * 是否跳过生成pb文件
     *
     * @return
     */
    boolean skipGen() default false;
}
