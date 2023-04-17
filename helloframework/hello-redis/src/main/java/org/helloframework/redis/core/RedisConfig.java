package org.helloframework.redis.core;

import java.util.List;

/**
 * Created by lanjian
 */
public interface RedisConfig<T> {
    List<String> getSlots();

    String getGroup();

    T getRedis();

    void init(String slot);
}
