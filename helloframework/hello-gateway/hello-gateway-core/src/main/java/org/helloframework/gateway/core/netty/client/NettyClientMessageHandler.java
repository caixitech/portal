package org.helloframework.gateway.core.netty.client;

import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceResponse;
import org.helloframework.gateway.common.definition.base.MessageCountDownLatch;
import org.helloframework.gateway.common.definition.base.MessageHandler;
import org.helloframework.gateway.common.exception.ApiCountDownException;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.ProtoBufUtil;
import org.helloframework.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lanjian
 * client 消息处理
 */
public class NettyClientMessageHandler implements MessageHandler {

    private final static Logger log = LoggerFactory.getLogger(NettyClientMessageHandler.class);
    private final Map<String, MessageCountDownLatch> messageCountDownLatchMap = new ConcurrentHashMap<String, MessageCountDownLatch>();


    public MessageCountDownLatch registerMessageCountDownLatch(String msgid) {
        log.debug("register msgid  {}", msgid);
        if (StringUtils.isEmpty(msgid)) {
            throw new RuntimeException("msgid is null");
        }
        MessageCountDownLatch messageCountDownLatch = new MessageCountDownLatch(Constants.GW_SEND_COUNT_DOWN_NUM);
        messageCountDownLatchMap.put(msgid, messageCountDownLatch);
        return messageCountDownLatch;
    }

    public void removeMessageCountDownLatch(String msgid) {
        log.debug("remove msgid {}", msgid);
        if (StringUtils.isEmpty(msgid)) {
            throw new RuntimeException("msgid is null");
        }
        messageCountDownLatchMap.remove(msgid);
    }

    //销毁所有的countdown
    public void destroy() {
        if (messageCountDownLatchMap.isEmpty()) {
            return;
        }
        for (MessageCountDownLatch messageCountDownLatch : messageCountDownLatchMap.values()) {
            messageCountDownLatch.setException(new ApiCountDownException(GateWayCode.GW_OFFLINE_CODE));
            messageCountDownLatch.countDown();
        }
    }

    public void received(byte[] message) {
        try {
            ApiServiceResponse apiServiceResponse = ProtoBufUtil.deserialize(message, ApiServiceResponse.class);
            MessageCountDownLatch messageCountDownLatch = messageCountDownLatchMap.get(apiServiceResponse.getId());
            if (messageCountDownLatch != null) {
                //如果远端有异常  直接包装一个ApiException
                byte[] data = apiServiceResponse.getMessage();
                if (Arrays.equals(data, Constants.ERROR_INFO)) {
                    messageCountDownLatch.setException(new ApiException(apiServiceResponse.getException(), apiServiceResponse.getCode(), apiServiceResponse.getMsg(), apiServiceResponse.getLogLevel(), apiServiceResponse.getDesc()));
                }
                messageCountDownLatch.setMessage(data);
                messageCountDownLatch.countDown();
                log.debug("received time 消息id：{}", apiServiceResponse.getId());
            }
        } catch (Exception ex) {
            log.warn("received message:" + new String(message), ex);
        }
    }
}
