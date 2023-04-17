package org.helloframework.redis.core;

import org.helloframework.redis.template.RedisDeleteCallBack;
import org.helloframework.redis.template.RedisHasKeyCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanjian
 */
public abstract class Redis<T> {
    private final Logger logger = LoggerFactory.getLogger(Redis.class);
    private final Map<String, RedisGroup> groups = new HashMap();

    public void setRedisGroups(List<RedisGroup> redisGroups) {
        for (RedisGroup redisGroup : redisGroups) {
            if (StringUtils.isEmpty(redisGroup.getName())) {
                throw new RuntimeException("group name is empty");
            }
            if (groups.containsKey(redisGroup.getName())) {
                throw new RuntimeException("group double");
            }
            groups.put(redisGroup.getName(), redisGroup);
        }
    }

    public <R> R execute(String group, String key, RedisCallback redisCallback) {
        if (groups.isEmpty()) {
            throw new RuntimeException("group is null");
        }
        RedisGroup redisGroup = groups.get(group);
        if (redisGroup == null) {
            throw new RuntimeException(String.format("%s group is null", group));
        }
        String slot = redisGroup.findSlot(key);
        T t = (T) redisGroup.findRedis(slot);
        if (t == null) {
            throw new RuntimeException("Redis is null");
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("redis key: {}，slot: {}", key, slot);
            }
            RedisResult redisResult = redisCallback.execute(key, t);
            if (logger.isDebugEnabled()) {
                logger.debug("redis execute result: {}", redisResult);
            }
            if (RedisResultStatus.SUCCESS.equals(redisResult.getStatus())) {
                return (R) redisResult.getT();
            }
        } catch (Exception ex) {
            throw new RuntimeException("redis  error", ex);
        }
        throw new RuntimeException("redis fail error");
    }

    public Boolean hasKey(String group, String key) {
        return this.execute(group, key, new RedisHasKeyCallBack());
    }

    public void delete(String group, String key) {
        this.execute(group, key, new RedisDeleteCallBack());
    }


}
