package org.helloframework.gateway.common.utils;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtoBufUtil {
    /**
     * 对象序列化
     *
     * @param o   需要序列化对象
     * @param <T> 序列化对象类型
     * @return
     */
    public static <T> byte[] serialize(T o, Class cls) {
        try {
            Schema schema = RuntimeSchema.getSchema(cls); //通过对象的类构建对应的schema
            return GraphIOUtil.toByteArray(o, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));//保存数据
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 对象反序列化
     *
     * @param bytes 对象字节数组
     * @param clazz Class对象
     * @param <T>   反序列化对象
     * @return
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            Schema schema = RuntimeSchema.getSchema(clazz); //通过对象的类构建对应的schema；
            T obj = (T) schema.newMessage(); //通过schema新建一个对象，这里需要转换一下
            GraphIOUtil.mergeFrom(bytes, obj, schema);//数据反序列化
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
