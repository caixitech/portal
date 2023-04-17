package org.helloframework.redis.core;

import org.helloframework.redis.core.hash.RedisHash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanjian
 */
public class RedisGroup {
    private String name;
    private Integer num;
    private final RedisHash redisHash = new RedisHash();
    private Map<String, Object> redis = new HashMap();

    public String findSlot(String key) {
        String slot = redisHash.findSlot(key);
        return slot;
    }

    public Object findRedis(String slot) {
        return redis.get(slot);
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRedisConfigs(List<RedisConfig> redisConfigs) {
        if (num == null) {
            throw new RuntimeException("slot num  must set");
        }
        for (RedisConfig redisConfig : redisConfigs) {
            redisConfig.init(name);
            List<String> slots = redisConfig.getSlots();
            for (String slot : slots) {
                //初始化hash槽
                redisHash.addSlot(slot);
                //关联组和槽的关系
                redis.put(slot, redisConfig.getRedis());
            }
        }
        if (redis.size() != num) {
            throw new RuntimeException(String.format("slot num  not match,slot num:%s,use slot:%s", num, redis.size()));
        }
    }


}
