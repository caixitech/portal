package org.helloframework.redis.core;


import org.apache.commons.collections.MapUtils;
import org.helloframework.core.utils.BeanUtils;
import org.helloframework.redis.spring.*;
import org.helloframework.redis.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author lanjian
 */
public class RedisOpTemplate implements IRedisOpTemplate {

    public static final Logger logger = LoggerFactory.getLogger(RedisOpTemplate.class);

    private SpringOpRedis redis;

    RedisOpTemplate(SpringOpRedis redis) {
        this.redis = redis;
    }

    public SpringOpRedis getRedis() {
        return redis;
    }

    public void setRedis(SpringOpRedis redis) {
        this.redis = redis;
    }

    /**
     * kv结构获取值方法
     *
     * @param operate
     * @param param
     * @param <R>
     * @return
     */
    @Override
    public <R> R valueGet(RedisKey operate, String... param) {
        return (R) value(operate, param).get();
    }

    /**
     * @param timeout 单位秒
     */
    @Override
    public <R> R valueGet(RedisKey operate, Supplier<R> supplier, long timeout, String... param) {
        R r = valueGet(operate, param);
        if (r == null) {
            r = supplier.get();
            valueSet(r, operate, timeout, param);
            return r;
        }
        return r;
    }

    @Override
    public <R> R valueGet(RedisKey operate, Supplier<R> supplier, Duration duration, String... param) {
        return valueGet(operate, supplier, duration.getSeconds(), param);
    }

    @Override
    public <R> R valueGet(RedisKey operate, Supplier<R> supplier, String... param) {
        R r = valueGet(operate, param);
        if (r == null) {
            r = supplier.get();
            valueSet(r, operate, param);
            return r;
        }
        return r;
    }

    @Override
    public void valueSet(Object object, RedisKey operate, String... param) {
        value(operate, param).set(object);
    }

    @Override
    public void valueSet(Object object, RedisKey operate, long timeout, String... param) {
        value(operate, param).set(object, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void valueSet(Object object, RedisKey operate, Duration timeout, String... param) {
        value(operate, param).set(object, timeout);
    }

    /**
     * hashGet获取值
     *
     * @param operate
     * @param param
     * @return
     */
    @Override
    public Map hashGet(RedisKey operate, String... param) {
        try {
            return hash(operate, param).entries();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <R> R hashGet(Class cls, RedisKey operate, String... param) {
        try {
            Map map = hashGet(operate, param);
            if (MapUtils.isEmpty(map)) {
                return null;
            }
            return BeanUtils.populate(cls, map);
        } catch (Exception e) {
            logger.error("hash get value error.class:{} operate :{},param:{}", cls, operate, Arrays.toString(param));
            throw e;
        }

    }

    @Override
    public <R> R hashGetByKey(String key, RedisKey operate, String... param) {
        BoundHashOperations operations = hash(operate, param);
        return (R) operations.get(key);
    }


    /**
     * delete键值
     *
     * @param operate
     * @param param
     * @return
     */
    @Override
    public void delete(RedisKey operate, String... param) {
        redis.delete(operate.getGroup(), operate.gen(param));
    }

    @Override
    public BoundGeoOperations geo(RedisKey operate, String... param) {
        return redis.geo(operate.getGroup(), operate.gen(param));
    }

    @Override
    public BoundHashOperationsDecorator hash(RedisKey operate, String... param) {
        return new BoundHashOperationsDecorator(redis.hash(operate.getGroup(), operate.gen(param)), operate);
    }

    @Override
    public void hashSet(Object o, RedisKey operate, String... param) {
        RedisUtils.check(o, operate, RedisStruct.HASH);
        Map map = BeanUtils.describe(o);
        redis.hash(operate.getGroup(), operate.gen(param)).putAll(map);
    }

    @Override
    public void hashSetByHashKey(String key, Object o, RedisKey operate, String... param) {
        redis.hash(operate.getGroup(), operate.gen(param)).put(key, o);
    }

    @Override
    public BoundValueOperationsDecorator value(RedisKey operate, String... param) {
        return new BoundValueOperationsDecorator(redis.value(operate.getGroup(), operate.gen(param)), operate);
    }

    @Override
    public BoundSetOperationsDecorator set(RedisKey operate, String... param) {
        return new BoundSetOperationsDecorator(redis.set(operate.getGroup(), operate.gen(param)), operate);
    }

    @Override
    public BoundListOperationsDecorator list(RedisKey operate, String... param) {
        return new BoundListOperationsDecorator(redis.list(operate.getGroup(), operate.gen(param)), operate);
    }

    @Override
    public BoundZSetOperationsDecorator zset(RedisKey operate, String... param) {
        return new BoundZSetOperationsDecorator(redis.zset(operate.getGroup(), operate.gen(param)), operate);
    }

    @Override
    public Set reverseRangeByScore(RedisKey operate, double min, double max, long offset, long count, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
                RedisResult<Set> redisResult = new RedisResult(RedisResultStatus.SUCCESS, set);
                return redisResult;
            }
        });
    }

    @Override
    public Set rangeByScore(RedisKey operate, double min, double max, long offset, long count, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                Set set = redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
                RedisResult<Set> redisResult = new RedisResult(RedisResultStatus.SUCCESS, set);
                return redisResult;
            }
        });
    }

    @Override
    public Set rangeByScore(RedisKey operate, double min, double max, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                Set set = redisTemplate.opsForZSet().rangeByScore(key, min, max);
                RedisResult<Set> redisResult = new RedisResult(RedisResultStatus.SUCCESS, set);
                return redisResult;
            }
        });
    }

    @Override
    public Set<ZSetOperations.TypedTuple> reverseRangeByScoreWithScores(RedisKey operate, double min, double max, long offset, long count, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                Set set = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
                RedisResult<Set> redisResult = new RedisResult(RedisResultStatus.SUCCESS, set);
                return redisResult;
            }
        });
    }

    @Override
    public Set<ZSetOperations.TypedTuple> rangeByScoreWithScores(RedisKey operate, double min, double max, long offset, long count, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                Set set = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
                RedisResult<Set> redisResult = new RedisResult(RedisResultStatus.SUCCESS, set);
                return redisResult;
            }
        });
    }


    /**
     * 判断是否有key
     *
     * @param operate
     * @param param
     * @return
     */
    @Override
    public Boolean hasKey(RedisKey operate, String... param) {
        return redis.hasKey(operate.getGroup(), operate.gen(param));
    }

    /**
     * 返回当前Redis服务器时间。
     */
    @Override
    public Long time(RedisKey operate) {
        return redis.execute(operate.getGroup(), "", new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                RedisConnection connection = null;
                try {
                    connection = redisTemplate.getConnectionFactory().getConnection();
                    Long ret = connection.time();
                    RedisResult<Long> redisResult = new RedisResult(RedisResultStatus.SUCCESS, ret);
                    return redisResult;
                } finally {
                    // 释放redis连接。
                    RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory(), false);
                }
            }
        });
    }

    @Override
    public Long ttl(RedisKey operate, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                RedisConnection connection = null;
                try {
                    connection = redisTemplate.getConnectionFactory().getConnection();
                    Long ret = connection.ttl(key.getBytes());
                    RedisResult<Long> redisResult = new RedisResult(RedisResultStatus.SUCCESS, ret);
                    return redisResult;
                } finally {
                    // 释放redis连接。
                    RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory(), false);
                }
            }
        });
    }

    @Override
    public boolean exists(RedisKey operate, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), new SpringDataRedisCallback() {
            @Override
            public RedisResult execute(String key, RedisTemplate redisTemplate) {
                RedisConnection connection = null;
                try {
                    connection = redisTemplate.getConnectionFactory().getConnection();
                    Boolean ret = connection.exists(key.getBytes());
                    RedisResult<Boolean> redisResult = new RedisResult(RedisResultStatus.SUCCESS, ret);
                    return redisResult;
                } finally {
                    // 释放redis连接。
                    RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory(), false);
                }
            }
        });
    }

    /**
     * 执行节点方法.
     *
     * @param operate
     * @param callBack
     * @param param
     * @param <R>
     * @return
     */
    @Override
    public <R> R execute(RedisKey operate, SpringDataRedisCallback callBack, String... param) {
        return redis.execute(operate.getGroup(), operate.gen(param), callBack);
    }

}
