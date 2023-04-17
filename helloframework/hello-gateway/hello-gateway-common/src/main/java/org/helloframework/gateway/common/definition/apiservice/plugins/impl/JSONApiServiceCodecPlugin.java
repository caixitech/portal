package org.helloframework.gateway.common.definition.apiservice.plugins.impl;

import org.helloframework.codec.json.JSON;
import org.helloframework.gateway.common.annotation.ApiPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceCodecPlugin;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.helloframework.codec.json.SerializerFeature.*;


/**
 * @date 2018/4/6 - 16:29
 */
@ApiPlugin
public class JSONApiServiceCodecPlugin implements ApiServiceCodecPlugin {

    private final static Logger log = LoggerFactory.getLogger(JSONApiServiceCodecPlugin.class);

    public byte[] encode(Object o, Class cls) {
        try {
            return JSON.toJSONString(o, WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullBooleanAsFalse).getBytes();
        } catch (Exception e) {
            log.error("JsonApiServiceCodec encode", e);
            throw new ApiException(GateWayCode.CODEC_CODE);
        }
    }

    public Object decode(byte[] in, Class cls) {
        if (in == null) {
            return null;
        }
        String data = null;
        try {
            data = new String(in);
            return JSON.parseObject(data, cls);
        } catch (Exception e) {
            log.error("JsonApiServiceCodec decode, data={}", data, e);
            throw new ApiException(GateWayCode.CODEC_CODE);
        }
    }
}
