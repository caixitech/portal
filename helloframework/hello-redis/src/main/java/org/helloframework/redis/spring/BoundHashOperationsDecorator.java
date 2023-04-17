package org.helloframework.redis.spring;

import org.helloframework.redis.core.RedisKey;
import org.helloframework.redis.core.RedisStruct;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lanjian
 */
public class BoundHashOperationsDecorator implements BoundHashOperations {
    private final static RedisStruct struct = RedisStruct.HASH;
    private BoundHashOperations boundHashOperations;
    private RedisKey operate;

    public BoundHashOperationsDecorator(BoundHashOperations boundHashOperations, RedisKey operate) {
        this.boundHashOperations = boundHashOperations;
        this.operate = operate;
    }

    @Override
    public Long delete(Object... keys) {
        return boundHashOperations.delete(keys);
    }

    @Override
    public Boolean hasKey(Object key) {
        return boundHashOperations.hasKey(key);
    }

    @Override
    public Object get(Object key) {
        return boundHashOperations.get(key);
    }

    @Override
    public List multiGet(Collection keys) {
        return boundHashOperations.multiGet(keys);
    }

    @Override
    public Long increment(Object key, long delta) {
        return boundHashOperations.increment(key, delta);
    }

    @Override
    public Double increment(Object key, double delta) {
        return boundHashOperations.increment(key, delta);
    }

    @Override
    public Set keys() {
        return boundHashOperations.keys();
    }

    @Override
    public Long lengthOfValue(Object hashKey) {
        return boundHashOperations.lengthOfValue(hashKey);
    }

    @Override
    public Long size() {
        return boundHashOperations.size();
    }

    @Override
    public void putAll(Map m) {
        if (operate.getStruct() != null && operate.getStruct() != struct) {
            throw new UnsupportedOperationException("redis struct not match,must HASH");
        }
        boundHashOperations.putAll(m);
    }

    @Override
    public void put(Object key, Object value) {
        boundHashOperations.put(key, value);
    }

    @Override
    public Boolean putIfAbsent(Object key, Object value) {
        return boundHashOperations.putIfAbsent(key, value);
    }

    @Override
    public List values() {
        return boundHashOperations.values();
    }

    @Override
    public Map entries() {
        return boundHashOperations.entries();
    }

    @Override
    public Cursor<Map.Entry> scan(ScanOptions options) {
        return boundHashOperations.scan(options);
    }

    @Override
    public RedisOperations getOperations() {
        return boundHashOperations.getOperations();
    }

    @Override
    public Object getKey() {
        return boundHashOperations.getKey();
    }

    @Override
    public DataType getType() {
        return boundHashOperations.getType();
    }

    @Override
    public Long getExpire() {
        return boundHashOperations.getExpire();
    }

    @Override
    public Boolean expire(long timeout, TimeUnit unit) {
        return boundHashOperations.expire(timeout, unit);
    }

    @Override
    public Boolean expireAt(Date date) {
        return boundHashOperations.expireAt(date);
    }

    @Override
    public Boolean persist() {
        return boundHashOperations.persist();
    }

    @Override
    public void rename(Object newKey) {
        boundHashOperations.rename(newKey);
    }
}
