package org.helloframework.redis.spring;

import org.helloframework.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by lanjian
 */
public interface SpringDataRedisCallback extends RedisCallback<RedisTemplate> {

}
