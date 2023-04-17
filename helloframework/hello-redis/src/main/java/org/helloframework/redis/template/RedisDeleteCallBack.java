package org.helloframework.redis.template;

import org.helloframework.redis.core.RedisResult;
import org.helloframework.redis.core.RedisResultStatus;
import org.helloframework.redis.spring.SpringDataRedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yoqu
 * @date 2018/4/26 - 22:30
 */
public class RedisDeleteCallBack implements SpringDataRedisCallback {


    @Override
    public RedisResult execute(String key, RedisTemplate template) {
        template.delete(key);
        return new RedisResult(RedisResultStatus.SUCCESS);
    }
}
