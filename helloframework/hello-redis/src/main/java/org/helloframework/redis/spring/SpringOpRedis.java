package org.helloframework.redis.spring;

import org.helloframework.redis.core.Redis;
import org.helloframework.redis.core.RedisResult;
import org.helloframework.redis.core.RedisResultStatus;
import org.springframework.data.redis.core.*;

/**
 * Created by lanjian
 */
public class SpringOpRedis extends Redis<RedisTemplate> {

    public BoundHashOperations hash(String group, String key) {
        return execute(group, key, (SpringDataRedisCallback) (key1, template) -> new RedisResult(RedisResultStatus.SUCCESS, template.boundHashOps(key1)));
    }

    public BoundGeoOperations geo(String group, String key) {
        return execute(group, key, (SpringDataRedisCallback) (key1, template) -> new RedisResult(RedisResultStatus.SUCCESS, template.boundGeoOps(key1)));
    }

    public BoundListOperations list(String group, String key) {
        return execute(group, key, (SpringDataRedisCallback) (key1, template) -> new RedisResult(RedisResultStatus.SUCCESS, template.boundListOps(key1)));
    }

    public BoundSetOperations set(String group, String key) {
        return execute(group, key, (SpringDataRedisCallback) (key1, template) -> new RedisResult(RedisResultStatus.SUCCESS, template.boundSetOps(key1)));
    }


    public BoundValueOperations value(String group, String key) {
        return execute(group, key, (SpringDataRedisCallback) (key1, template) -> new RedisResult(RedisResultStatus.SUCCESS, template.boundValueOps(key1)));
    }


    public BoundZSetOperations zset(String group, String key) {
        return execute(group, key, (SpringDataRedisCallback) (key1, template) -> new RedisResult(RedisResultStatus.SUCCESS, template.boundZSetOps(key1)));
    }

}
