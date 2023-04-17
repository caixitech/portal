package org.helloframework.redis.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author lanjian
 * @date 18/4/27 下午4:46
 */
public class KryoSerializer implements RedisSerializer<Object> {
    private final Kryo kryo;
    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    public KryoSerializer() {
        this.kryo = new Kryo();
    }

    public static void main(String[] args) {
        Kryo kryo = new Kryo();
        String atom = "这是一个测试数据";
        byte[] data = atom.getBytes();
        Output output = new Output(2048);
        kryo.writeClassAndObject(output, atom);
        byte[] data2 = output.toBytes();
        System.out.println(String.format("data1 length: %d,data2 length %d", data, data2));
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        } else {
            if (obj instanceof Integer || obj instanceof Long
                    || obj instanceof String || obj instanceof Double || obj instanceof Float
                    || obj.getClass().isPrimitive()) {
                return stringRedisSerializer.serialize(obj.toString());
            }
            byte[] buffer = new byte[2048];
            Output output = new Output(buffer);
            try {
                kryo.writeClassAndObject(output, obj);
                return output.toBytes();
            } catch (Exception e) {
                throw new SerializationException("Cannot serialize", e);
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            Input input = new Input(bytes);
            try {
                int classID = input.readVarInt(true);
                Registration registration = kryo.getClassResolver().getRegistration(classID);
                if (registration == null) {
                    return stringRedisSerializer.deserialize(bytes);
                }
                input.setPosition(0);
                return kryo.readClassAndObject(input);
            } catch (Exception e) {
                throw new SerializationException("Cannot deserialize", e);
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
    }
}
