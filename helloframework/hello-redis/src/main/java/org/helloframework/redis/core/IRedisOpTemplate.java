package org.helloframework.redis.core;

import org.helloframework.redis.spring.SpringDataRedisCallback;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author lanjian
 */
public interface IRedisOpTemplate {
    <R> R valueGet(RedisKey operate, String... param);

    /**
     * @param timeout 单位秒
     */
    <R> R valueGet(RedisKey operate, Supplier<R> supplier, long timeout, String... param);

    <R> R valueGet(RedisKey operate, Supplier<R> supplier, Duration duration, String... param);

    <R> R valueGet(RedisKey operate, Supplier<R> supplier, String... param);

    void valueSet(Object object, RedisKey operate, String... param);

    void valueSet(Object object, RedisKey operate, long timeout, String... param);

    void valueSet(Object object, RedisKey operate, Duration timeout, String... param);

    /**
     * hashGet获取值
     *
     * @param operate
     * @param param
     * @return
     */
    Map hashGet(RedisKey operate, String... param);


    <R> R hashGet(Class cls, RedisKey operate, String... param);

    <R> R hashGetByKey(String key, RedisKey operate, String... param);

    /**
     * delete键值
     *
     * @param operate
     * @param param
     * @return
     */
    void delete(RedisKey operate, String... param);

    /**
     * 执行节点方法.
     *
     * @param operate
     * @param callBack
     * @param param
     * @param <R>
     * @return
     */
    <R> R execute(RedisKey operate, SpringDataRedisCallback callBack, String... param);


    BoundGeoOperations geo(RedisKey operate, String... param);


    BoundHashOperations hash(RedisKey operate, String... param);

    void hashSet(Object o, RedisKey operate, String... param);

    void hashSetByHashKey(String key, Object o, RedisKey operate, String... param);

    BoundValueOperations value(RedisKey operate, String... param);


    BoundSetOperations set(RedisKey operate, String... param);


    BoundListOperations list(RedisKey operate, String... param);


    BoundZSetOperations zset(RedisKey operate, String... param);

    Set reverseRangeByScore(RedisKey operate, double min, double max, long offset, long count, String... param);

    Set rangeByScore(RedisKey operate, double min, double max, long offset, long count, String... param);

    Set rangeByScore(RedisKey operate, double min, double max, String... param);

    Set<ZSetOperations.TypedTuple> reverseRangeByScoreWithScores(RedisKey operate, double min, double max, long offset, long count, String... param);

    Set<ZSetOperations.TypedTuple> rangeByScoreWithScores(RedisKey operate, double min, double max, long offset, long count, String... param);

    Boolean hasKey(RedisKey operate, String... param);

    /**
     * 返回当前Redis服务器时间。
     */
    Long time(RedisKey operate);

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * @return 当 key 不存在时，返回 -2。当 key 存在但没有设置剩余生存时间时，返回 -1。否则，以秒为单位，返回 key 的剩余生存时间。
     */
    Long ttl(RedisKey operate, String... param);

    /**
     * 检查给定 key 是否存在。
     *
     * @return 若 key 存在，返回 true，否则返回 false。
     */
    boolean exists(RedisKey operate, String... param);
}
