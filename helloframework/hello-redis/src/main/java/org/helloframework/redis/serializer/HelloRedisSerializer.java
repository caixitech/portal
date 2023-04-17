package org.helloframework.redis.serializer;


import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author lanjian
 */
public class HelloRedisSerializer implements RedisSerializer<Object> {

    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    private final String PROTOSTUFF_STAMP = "hello";

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        } else {
            if (obj instanceof Integer || obj instanceof Long || obj instanceof Boolean
                    || obj instanceof String || obj instanceof Double || obj instanceof Float
                    || obj.getClass().isPrimitive()) {
                return stringRedisSerializer.serialize(obj.toString());
            }
            //新增数据均使用protostuff序列化
            RedisBean redisBean = new RedisBean(obj, PROTOSTUFF_STAMP);
            return ProtoBufUtil.serialize(redisBean, RedisBean.class);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            //使用protostuff反序列化
            try {
                RedisBean redisBean = ProtoBufUtil.deserialize(bytes, RedisBean.class);
                if (PROTOSTUFF_STAMP.equalsIgnoreCase(redisBean.getStamp())) {
                    return redisBean.getObj();
                }
            } catch (Exception ex) {
                //容错
            }
            //使用stringSerializer反序列化
            try {
                Object obj = stringRedisSerializer.deserialize(bytes);
                return obj;
            } catch (Exception ex) {
                //容错
            }

            return null;
        }
    }
}
