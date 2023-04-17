package org.helloframework.redis.spring;

import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisStruct;
import org.helloframework.redis.utils.RedisUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lanjian
 */
public class BoundSetOperationsDecorator implements BoundSetOperations {

    private final static RedisStruct struct = RedisStruct.SET;
    BoundSetOperations boundSetOperations;
    RedisKey operate;

    public BoundSetOperationsDecorator(BoundSetOperations boundSetOperations, RedisKey operate) {
        this.boundSetOperations = boundSetOperations;
        this.operate = operate;
    }

    @Override
    public Long add(Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        RedisUtils.check(values[0], operate, struct);
        return boundSetOperations.add(values);
    }

    @Override
    public Long remove(Object... values) {
        return boundSetOperations.remove(values);
    }

    @Override
    public Object pop() {
        return boundSetOperations.pop();
    }

    @Override
    public Boolean move(Object destKey, Object value) {
        //todo
        return boundSetOperations.move(destKey, value);
    }

    @Override
    public Long size() {
        return boundSetOperations.size();
    }

    @Override
    public Boolean isMember(Object o) {
        return boundSetOperations.isMember(o);
    }

    @Override
    public Set intersect(Object key) {
        return boundSetOperations.intersect(key);
    }

    @Override
    public Set intersect(Collection keys) {
        return boundSetOperations.intersect(keys);
    }

    @Override
    public void intersectAndStore(Object key, Object destKey) {
        boundSetOperations.intersectAndStore(key, destKey);
    }

    @Override
    public void intersectAndStore(Collection keys, Object destKey) {
        boundSetOperations.intersectAndStore(keys, destKey);
    }

    @Override
    public Set union(Object key) {
        return boundSetOperations.union(key);
    }

    @Override
    public Set union(Collection keys) {
        return boundSetOperations.union(keys);
    }

    @Override
    public void unionAndStore(Object key, Object destKey) {
        boundSetOperations.unionAndStore(key, destKey);
    }

    @Override
    public void unionAndStore(Collection keys, Object destKey) {
        boundSetOperations.unionAndStore(keys, destKey);
    }

    @Override
    public Set diff(Object key) {
        return boundSetOperations.diff(key);
    }

    @Override
    public Set diff(Collection keys) {
        return boundSetOperations.diff(keys);
    }

    @Override
    public void diffAndStore(Object keys, Object destKey) {
        boundSetOperations.diffAndStore(keys, destKey);
    }

    @Override
    public void diffAndStore(Collection keys, Object destKey) {
        boundSetOperations.diffAndStore(keys, destKey);
    }

    @Override
    public Set members() {
        return boundSetOperations.members();
    }

    @Override
    public Object randomMember() {
        return boundSetOperations.randomMember();
    }

    @Override
    public Set distinctRandomMembers(long count) {
        return boundSetOperations.distinctRandomMembers(count);
    }

    @Override
    public List randomMembers(long count) {
        return boundSetOperations.randomMembers(count);
    }

    @Override
    public Cursor scan(ScanOptions options) {
        return boundSetOperations.scan(options);
    }

    @Override
    public RedisOperations getOperations() {
        return boundSetOperations.getOperations();
    }

    @Override
    public Object getKey() {
        return boundSetOperations.getKey();
    }

    @Override
    public DataType getType() {
        return boundSetOperations.getType();
    }

    @Override
    public Long getExpire() {
        return boundSetOperations.getExpire();
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return boundSetOperations.expire(timeout, unit);
    }

    @Override
    public Boolean expireAt(Date date) {
        return boundSetOperations.expireAt(date);
    }

    @Override
    public Boolean persist() {
        return boundSetOperations.persist();
    }

    @Override
    public void rename(Object newKey) {
        boundSetOperations.rename(newKey);
    }
}
