package org.helloframework.redis.spring;

import org.helloframework.redis.core.RedisConfig;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class SpringRedisConfig implements RedisConfig<RedisTemplate> {
    private String group;
    private Integer start;
    private Integer end;
    private RedisTemplate redis;
    private List<String> slots = new ArrayList<>();

    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }


    @Override
    public List<String> getSlots() {
        return slots;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public RedisTemplate getRedis() {
        return redis;
    }

    /**
     * 初始化slot
     *
     * @param slot
     */
    public void init(String slot) {
        for (int i = start; i <= end; i++) {
            slots.add(String.format("%s_%s", slot, i));
        }
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
