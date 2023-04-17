package org.helloframework.gateway.common.definition.base;


import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lanjian
 */
public interface LoadBalance<T> {

    T random();

    int size();

    void init();

    Map<T, Integer> data();

    int timeout();

    int max();

    void updateMethod(MethodInfo methodInfo);

    AtomicInteger working();

}