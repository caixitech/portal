package org.helloframework.redis.spring;

import org.helloframework.core.utils.PageUtils;
import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisStruct;
import org.helloframework.redis.utils.RedisUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lanjian
 */
public class BoundListOperationsDecorator implements BoundListOperations {

    private final static RedisStruct struct = RedisStruct.LIST;
    BoundListOperations boundListOperations;
    RedisKey operate;

    public BoundListOperationsDecorator(BoundListOperations boundListOperations, RedisKey operate) {
        this.boundListOperations = boundListOperations;
        this.operate = operate;

    }

    @Override
    public List range(long start, long end) {
        return boundListOperations.range(start, end);
    }

    public List page(long page, long size) {
        long start = PageUtils.redisBegin(Long.valueOf(page).intValue(), Long.valueOf(size).intValue()).longValue();
        long end = PageUtils.redisEnd(Long.valueOf(page).intValue(), Long.valueOf(size).intValue()).longValue();
        return boundListOperations.range(start, end);
    }

    @Override
    public void trim(long start, long end) {
        boundListOperations.trim(start, end);
    }

    @Override
    public Long size() {
        return boundListOperations.size();
    }

    @Override
    public Long leftPush(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundListOperations.leftPush(value);
    }

    @Override
    public Long leftPushAll(Object[] values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        RedisUtils.check(values[0], operate, struct);
        return boundListOperations.leftPushAll(values);
    }

    @Override
    public Long leftPushIfPresent(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundListOperations.leftPushIfPresent(value);
    }

    @Override
    public Long leftPush(Object pivot, Object value) {
        RedisUtils.check(value, operate, struct);
        return boundListOperations.leftPush(pivot, value);
    }

    @Override
    public Long rightPush(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundListOperations.rightPush(value);
    }

    @Override
    public Long rightPushAll(Object[] values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        RedisUtils.check(values[0], operate, struct);
        return boundListOperations.rightPushAll(values);
    }

    @Override
    public Long rightPushIfPresent(Object value) {
        RedisUtils.check(value, operate, struct);
        return boundListOperations.rightPushIfPresent(value);
    }

    @Override
    public Long rightPush(Object pivot, Object value) {
        RedisUtils.check(value, operate, struct);
        return boundListOperations.rightPush(pivot, value);
    }

    @Override
    public void set(long index, Object value) {
        RedisUtils.check(value, operate, struct);
        boundListOperations.set(index, value);
    }

    @Override
    public Long remove(long count, Object value) {
        return boundListOperations.remove(count, value);
    }

    @Override
    public Object index(long index) {
        return boundListOperations.index(index);
    }

    @Override
    public Object leftPop() {
        return boundListOperations.leftPop();
    }

    @Override
    public Object leftPop(long timeout, TimeUnit unit) {
        return boundListOperations.leftPop(timeout, unit);
    }

    @Override
    public Object rightPop() {
        return boundListOperations.rightPop();
    }

    @Override
    public Object rightPop(long timeout, TimeUnit unit) {
        return boundListOperations.rightPop(timeout, unit);
    }

    @Override
    public RedisOperations getOperations() {
        return boundListOperations.getOperations();
    }

    @Override
    public Object getKey() {
        return boundListOperations.getKey();
    }

    @Override
    public DataType getType() {
        return boundListOperations.getType();
    }

    @Override
    public Long getExpire() {
        return boundListOperations.getExpire();
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return boundListOperations.expire(timeout, unit);
    }

    @Override
    public Boolean expireAt(Date date) {
        return boundListOperations.expireAt(date);
    }

    @Override
    public Boolean persist() {
        return boundListOperations.persist();
    }

    @Override
    public void rename(Object newKey) {
        boundListOperations.rename(newKey);
    }
}
