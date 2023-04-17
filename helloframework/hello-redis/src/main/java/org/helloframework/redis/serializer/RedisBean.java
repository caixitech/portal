package org.helloframework.redis.serializer;

import io.protostuff.Tag;

import java.io.Serializable;

/**
 * @author lanjian
 * ProtostuffWrapper
 */
public class RedisBean implements Serializable {

    @Tag(1)
    private Object obj;
    @Tag(2)
    private String stamp;


    public RedisBean(Object obj, String stamp) {
        this.obj = obj;
        this.stamp = stamp;
    }

    public RedisBean() {

    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
