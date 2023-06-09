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
public @interface DocApiService {

    String cnName();

    String serviceName() default "";

    String version();

    String[] methods() default "POST";

    /**
     * 接口描述文本。
     */
    String desc() default "";

    String group() default "";

    /**
     * 引入markdown文档，如xxx.md即可。
     */
    String doc() default "";

    /**
     * 完成开发进度百分比,默认为0,最高100
     *
     * @return
     */
    int finish() default 0;
}
