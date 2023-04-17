package org.helloframework.redis.spring;

import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisStruct;
import org.helloframework.redis.utils.RedisUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author lanjian
 */
public class BoundValueOperationsDecorator implements BoundValueOperations {
    private final static RedisStruct struct = RedisStruct.VALUE;
    BoundValueOperations boundValueOperations;
    RedisKey operate;

    public BoundValueOperationsDecorator(BoundValueOperations boundValueOperations, RedisKey operate) {
        this.boundValueOperations = boundValueOperations;
        this.operate = operate;
    }

    @Override
    public void set(Object value) {
        RedisUtils.check(value, operate, struct);
        boundValueOperations.set(value);
    }

    @Override
    public void set(Object value, long timeout, TimeUnit unit) {
        RedisUtils.check(value, operate, struct);
        boundValueOperations.set(value, timeout, unit);
    }

    @Override
    public void set(Object value, Duration timeout) {
        RedisUtils.check(value, operate, struct);
        boundValueOperations.set(value, timeout);
    }

    @Override
    public Boolean setIfAbsent(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundValueOperations.setIfAbsent(value);
    }

    @Override
    public Boolean setIfAbsent(Object value, long timeout, TimeUnit unit) {
        RedisUtils.check(value, operate, struct);
        return boundValueOperations.setIfAbsent(value, timeout, unit);
    }

    @Override
    public Boolean setIfPresent(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundValueOperations.setIfPresent(value);
    }

    @Override
    public Boolean setIfPresent(Object value, long timeout, TimeUnit unit) {
        RedisUtils.check(value, operate, struct);
        return boundValueOperations.setIfPresent(value, timeout, unit);
    }

    @Override
    public Object get() {
        return boundValueOperations.get();
    }

    @Override
    public Object getAndSet(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundValueOperations.getAndSet(value);
    }

    @Override
    public Long increment() {
        return null;
    }

    @Override
    public Long increment(long delta) {
        return boundValueOperations.increment(delta);
    }

    @Override
    public Double increment(double delta) {
        return boundValueOperations.increment(delta);
    }

    @Override
    public Long decrement() {
        return null;
    }

    @Override
    public Long decrement(long delta) {
        return null;
    }

    @Override
    public Integer append(String value) {
        return boundValueOperations.append(value);
    }

    @Override
    public String get(long start, long end) {
        return boundValueOperations.get(start, end);
    }

    @Override
    public void set(Object value, long offset) {
        RedisUtils.check(value, operate, struct);
        boundValueOperations.set(value, offset);
    }

    @Override
    public Long size() {
        return boundValueOperations.size();
    }

    @Override
    public RedisOperations getOperations() {
        return boundValueOperations.getOperations();
    }

    @Override
    public Object getKey() {
        return boundValueOperations.getKey();
    }

    @Override
    public DataType getType() {
        return boundValueOperations.getType();
    }

    @Override
    public Long getExpire() {
        return boundValueOperations.getExpire();
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return boundValueOperations.expire(timeout, unit);
    }

    @Override
    public Boolean expireAt(Date date) {
        return boundValueOperations.expireAt(date);
    }

    @Override
    public Boolean persist() {
        return boundValueOperations.persist();
    }

    @Override
    public void rename(Object newKey) {
        boundValueOperations.rename(newKey);
    }
}
