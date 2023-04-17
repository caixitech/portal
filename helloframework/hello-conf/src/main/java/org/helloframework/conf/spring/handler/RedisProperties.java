package org.helloframework.conf.spring.handler;

import org.apache.commons.lang3.StringUtils;
import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisOpTemplate;
import org.helloframework.redis.core.RedisStruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//PropertySourcesPlaceholderConfigurer
//PropertyPlaceholderConfigurer
public class RedisProperties extends PropertySourcesPlaceholderConfigurer implements ApplicationContextAware {
    private String keys;
    private String group;
    private String redis;
    private ApplicationContext applicationContext;

    public String getRedis() {
        return redis;
    }

    public void setRedis(String redis) {
        this.redis = redis;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    protected void loadProperties(Properties properties) throws IOException {
        if (group == null) {
            throw new RuntimeException("group is null");
        }
        RedisOpTemplate redisOpTemplate = null;
        if (StringUtils.isNotBlank(getRedis())) {
            redisOpTemplate = applicationContext.getBean(getRedis(), RedisOpTemplate.class);
        } else {
            redisOpTemplate = applicationContext.getBean(RedisOpTemplate.class);
        }
        if (redisOpTemplate == null) {
            throw new RuntimeException("redis not found");
        }
        String[] keys = StringUtils.split(getKeys(), ",");
        if (keys != null) {
            for (String key : keys) {
                //redis里面取出值进行赋值
                Map<String, String> props = redisOpTemplate.hashGet(new RedisKey(key, group, RedisStruct.HASH, HashMap.class));
                Set<String> keySet = props.keySet();
                for (String k : keySet) {
                    properties.setProperty(k, props.get(k));
                }
            }
        } else {
            throw new RuntimeException("keys is null");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
