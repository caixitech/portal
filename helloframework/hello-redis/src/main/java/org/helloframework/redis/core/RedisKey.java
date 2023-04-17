package org.helloframework.redis.core;

public class RedisKey {
    private String key;

    private String group;

    /**
     * redis的数据结构
     */
    private RedisStruct struct;

    private Class type;

    public RedisKey(String key, String group, RedisStruct struct, Class type) {
        this.key = key;
        this.group = group;
        this.struct = struct;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public RedisStruct getStruct() {
        return struct;
    }

    public void setStruct(RedisStruct struct) {
        this.struct = struct;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String gen(String... key) {
        if (key.length > 0) {
            return String.format(this.key, key);
        } else {
            return this.key;
        }
    }
}
