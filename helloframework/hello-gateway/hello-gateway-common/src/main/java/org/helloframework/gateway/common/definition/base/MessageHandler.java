package org.helloframework.gateway.common.definition.base;


public interface MessageHandler<T> {

    /**
     * on message received.
     *
     * @param message message.
     */
    void received(byte[] message);

    MessageCountDownLatch registerMessageCountDownLatch(String msgid);

    void removeMessageCountDownLatch(String msgid);

    void destroy();
}