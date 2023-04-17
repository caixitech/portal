package org.helloframework.redis.spring;

import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisStruct;
import org.helloframework.redis.utils.RedisUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lanjian
 */
public class BoundZSetOperationsDecorator implements BoundZSetOperations {

    private final static RedisStruct struct = RedisStruct.ZSET;

    BoundZSetOperations defaultBoundHashOperations;
    RedisKey operate;

    public BoundZSetOperationsDecorator(BoundZSetOperations defaultBoundHashOperations, RedisKey operate) {
        this.defaultBoundHashOperations = defaultBoundHashOperations;
        this.operate = operate;
    }

    @Override
    public Boolean add(Object value, double score) {
        RedisUtils.check(value, operate, struct);
        return defaultBoundHashOperations.add(value, score);
    }

    @Override
    public Long remove(Object... values) {
        return defaultBoundHashOperations.remove(values);
    }

    @Override
    public Double incrementScore(Object value, double delta) {
        RedisUtils.check(value, operate, struct);
        return defaultBoundHashOperations.incrementScore(value, delta);
    }

    @Override
    public Long rank(Object o) {
        return defaultBoundHashOperations.rank(o);
    }

    @Override
    public Long reverseRank(Object o) {
        return defaultBoundHashOperations.reverseRank(o);
    }

    @Override
    public Set range(long start, long end) {
        return defaultBoundHashOperations.range(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple> rangeWithScores(long start, long end) {
        return defaultBoundHashOperations.rangeWithScores(start, end);
    }

    @Override
    public Set rangeByScore(double min, double max) {
        return defaultBoundHashOperations.rangeByScore(min, max);
    }

    @Override
    public Set<ZSetOperations.TypedTuple> rangeByScoreWithScores(double min, double max) {
        return defaultBoundHashOperations.rangeByScoreWithScores(min, max);
    }

    @Override
    public Set reverseRange(long start, long end) {
        return defaultBoundHashOperations.reverseRange(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple> reverseRangeWithScores(long start, long end) {
        return defaultBoundHashOperations.reverseRangeWithScores(start, end);
    }

    @Override
    public Set reverseRangeByScore(double min, double max) {
        return defaultBoundHashOperations.reverseRangeByScore(min, max);
    }

    @Override
    public Set<ZSetOperations.TypedTuple> reverseRangeByScoreWithScores(double min, double max) {
        return defaultBoundHashOperations.reverseRangeByScoreWithScores(min, max);
    }

    @Override
    public Long count(double min, double max) {
        return defaultBoundHashOperations.count(min, max);
    }

    @Override
    public Long size() {
        return defaultBoundHashOperations.size();
    }

    @Override
    public Long zCard() {
        return defaultBoundHashOperations.zCard();
    }

    @Override
    public Double score(Object o) {
        return defaultBoundHashOperations.score(o);
    }

    @Override
    public Long removeRange(long start, long end) {
        return defaultBoundHashOperations.removeRange(start, end);
    }

    @Override
    public Long removeRangeByScore(double min, double max) {
        return defaultBoundHashOperations.removeRangeByScore(min, max);
    }

    @Override
    public Long unionAndStore(Object otherKey, Object destKey) {
        return defaultBoundHashOperations.unionAndStore(otherKey, destKey);
    }

    @Override
    public Long unionAndStore(Collection otherKeys, Object destKey) {
        return defaultBoundHashOperations.unionAndStore(otherKeys, destKey);
    }

    @Override
    public Long unionAndStore(Collection otherKeys, Object destKey, RedisZSetCommands.Aggregate aggregate) {
        return null;
    }

    @Override
    public Long unionAndStore(Collection otherKeys, Object destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return null;
    }

    @Override
    public Long intersectAndStore(Object otherKey, Object destKey) {
        return defaultBoundHashOperations.intersectAndStore(otherKey, destKey);
    }

    @Override
    public Long intersectAndStore(Collection otherKeys, Object destKey) {
        return defaultBoundHashOperations.intersectAndStore(otherKeys, destKey);
    }

    @Override
    public Long intersectAndStore(Collection otherKeys, Object destKey, RedisZSetCommands.Aggregate aggregate) {
        return defaultBoundHashOperations.intersectAndStore(otherKeys, destKey,aggregate);
    }

    @Override
    public Long intersectAndStore(Collection otherKeys, Object destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return defaultBoundHashOperations.intersectAndStore(otherKeys, destKey,aggregate,weights);
    }

    @Override
    public Cursor<ZSetOperations.TypedTuple> scan(ScanOptions options) {
        return defaultBoundHashOperations.scan(options);
    }

    @Override
    public Set rangeByLex(RedisZSetCommands.Range range) {
        return defaultBoundHashOperations.rangeByLex(range);
    }

    @Override
    public Set rangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return defaultBoundHashOperations.rangeByLex(range, limit);
    }

    @Override
    public RedisOperations getOperations() {
        return defaultBoundHashOperations.getOperations();
    }

    @Override
    public Long add(Set set) {
        if (set == null || set.size() == 0) {
            return 0L;
        }
        for (Object obj : set) {
            RedisUtils.check(((ZSetOperations.TypedTuple) obj).getValue(), operate, struct);
        }
        return defaultBoundHashOperations.add(set);
    }

    @Override
    public Object getKey() {
        return defaultBoundHashOperations.getKey();
    }

    @Override
    public DataType getType() {
        return defaultBoundHashOperations.getType();
    }

    @Override
    public Long getExpire() {
        return defaultBoundHashOperations.getExpire();
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return defaultBoundHashOperations.expire(timeout, unit);
    }

    @Override
    public Boolean expireAt(Date date) {
        return defaultBoundHashOperations.expireAt(date);
    }

    @Override
    public Boolean persist() {
        return defaultBoundHashOperations.persist();
    }

    @Override
    public void rename(Object newKey) {
        defaultBoundHashOperations.rename(newKey);
    }
}
