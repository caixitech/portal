package org.helloframework.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;

/**
 * Created by lanjian
 */
public abstract class HelloMQConsumer extends DefaultMQPushConsumer {

    private HelloMessageListenerConcurrently messageListenerConcurrently;


    public void setMessageListenerConcurrently(HelloMessageListenerConcurrently messageListenerConcurrently) {
        this.messageListenerConcurrently = messageListenerConcurrently;
    }

    public abstract String topic();

    public abstract void init();

    @Override
    public void start() throws MQClientException {
        if (messageListenerConcurrently == null) {
            throw new RuntimeException("KuYinConsumer start error");
        }
        String topic = topic();
        init();
        subscribe(topic, "*");
        setInstanceName(topic);
        registerMessageListener(messageListenerConcurrently);
        super.start();
    }
}
