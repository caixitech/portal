package org.helloframework.mybatis.cache;

/**
 * Created by lanjian
 */
public interface CacheCall<T> {
    T get(String key);

    void set(String key, T t);

    String key();
}
