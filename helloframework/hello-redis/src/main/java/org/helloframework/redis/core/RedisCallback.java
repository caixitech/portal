package org.helloframework.redis.core;

/**
 * Created by lanjian
 */
public interface RedisCallback<T> {
    RedisResult execute(String key, T t);
}
