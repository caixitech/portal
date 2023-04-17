package org.helloframework.redis.template;

import org.helloframework.redis.core.RedisResult;
import org.helloframework.redis.core.RedisResultStatus;
import org.helloframework.redis.spring.SpringDataRedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yoqu
 * @date 2018/4/26 - 22:30
 */
public class RedisHasKeyCallBack implements SpringDataRedisCallback {


    @Override
    public RedisResult execute(String key, RedisTemplate template) {
        return new RedisResult(RedisResultStatus.SUCCESS, template.hasKey(key));
    }
}
