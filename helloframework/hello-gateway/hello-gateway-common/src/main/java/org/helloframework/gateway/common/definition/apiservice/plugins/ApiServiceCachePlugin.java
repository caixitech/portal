package org.helloframework.gateway.common.definition.apiservice.plugins;

/**
 * Created by lanjian
 */
public interface ApiServiceCachePlugin extends Plugin {
    /**
     * @param key   用来定义key
     * @param cls   如果获取了数据 需要用cls序列化
     * @param group 分组
     * @return
     */
    Object get(String key, Class cls, String group);

    /**
     * @param out   缓存对象
     * @param time  缓存时间
     * @param group 分组
     * @param cls   如果获取了数据 需要用cls序列化  pb需要 json不需要
     */
    void set(String key, Object out, Class cls, long time, String group);
}
