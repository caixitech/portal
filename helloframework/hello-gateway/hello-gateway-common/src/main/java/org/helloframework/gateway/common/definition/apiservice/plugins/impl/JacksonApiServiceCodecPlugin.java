package org.helloframework.gateway.common.definition.apiservice.plugins.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.helloframework.gateway.common.annotation.ApiPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceCodecPlugin;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date 2018/4/6 - 16:29
 */
@ApiPlugin
public class JacksonApiServiceCodecPlugin implements ApiServiceCodecPlugin {

    private final static Logger log = LoggerFactory.getLogger(JacksonApiServiceCodecPlugin.class);

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //关闭日期序列化为时间戳的功能
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //关闭序列化的时候没有为属性找到getter方法,报错
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //关闭反序列化的时候，没有找到属性的setter报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public byte[] encode(Object o, Class cls) {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (Exception e) {
            log.error("JsonApiServiceCodec encode", e);
            throw new ApiException(GateWayCode.CODEC_CODE);
        }
    }

    public Object decode(byte[] in, Class cls) {
        if (in == null) {
            return null;
        }
        try {
            return objectMapper.readValue(in, cls);
        } catch (Exception e) {
            log.error("JsonApiServiceCodec decode", e);
            throw new ApiException(GateWayCode.CODEC_CODE);
        }
    }
}
