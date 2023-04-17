package org.helloframework.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by lanjian on 2016/12/7.
 */
public class ClassUtils {
    public static Class genericType(Integer index, Class clazz) {
        try {
            ParameterizedType genType = (ParameterizedType) clazz.getGenericSuperclass();
            Type[] params = genType.getActualTypeArguments();
            return (Class) params[index];
        } catch (Exception ex) {
            throw new RuntimeException("not found");
        }
    }
}
