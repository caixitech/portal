package org.helloframework.gateway.core.netty.client;


import org.helloframework.gateway.common.definition.base.MessageHandler;

/**
 * Created by lanjian
 * message 消息回调执行
 */
public class NettyClientInvoker implements Runnable {
    private byte[] data;
    private MessageHandler messageHandler;


    public NettyClientInvoker(byte[] data, MessageHandler messageHandler) {
        this.data = data;
        this.messageHandler = messageHandler;
    }

    public void run() {
        messageHandler.received(data);
    }
}
