package org.helloframework.redis.utils;

import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisStruct;

/**
 * lanjian
 */
public class RedisUtils {

    public static void check(Object value, RedisKey operate, RedisStruct struct) {
        if (operate.getStruct() != null && operate.getStruct() != struct) {
            throw new UnsupportedOperationException("redis struct not match,must HASH strcut");
        }

        if (operate.getType() != null && !operate.getType().isAssignableFrom(value.getClass())) {//数据类型不匹配
            throw new IllegalArgumentException("value class type not match");
        }
    }
}
