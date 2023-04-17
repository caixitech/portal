package org.helloframework.gateway.common.definition.apiservice.plugins.impl;

import org.helloframework.gateway.common.annotation.ApiPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceCodecPlugin;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.ProtoBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lanjian
 */
@ApiPlugin
public class ProtobufApiServiceCodecPlugin implements ApiServiceCodecPlugin {
    private final static Logger log = LoggerFactory.getLogger(ProtobufApiServiceCodecPlugin.class);

    public byte[] encode(Object object, Class cls) {
        try {
            return ProtoBufUtil.serialize(object, cls);
        } catch (Exception e) {
            log.error("pb encode cls:{},data:{}", cls.getName(), object.toString(), e);
            throw new ApiException(GateWayCode.CODEC_CODE);
        }
    }


    public Object decode(byte[] in, Class cls) {
        if (in == null) {
            return null;
        }
        try {
            return ProtoBufUtil.deserialize(in, cls);//数据反序列化
        } catch (Exception ex) {
            log.error("pb decode cls:{},data:{}", cls.getName(), new String(in), ex);
            throw new ApiException(GateWayCode.CODEC_CODE);
        }

    }
}
