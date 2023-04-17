package org.helloframework.rocketmq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanjian
 */
public abstract class HelloMessageListenerConcurrently implements MessageListenerConcurrently {
    private static final Logger logger = LoggerFactory.getLogger(HelloMessageListenerConcurrently.class);

    private final Map<String, QueueMessageHandler> queueMessageHandlers = new HashMap();

    public abstract void contextInit(ConsumeConcurrentlyContext context);

    public void setMessageHandlers(List<QueueMessageHandler> messageHandlers) {
        for (QueueMessageHandler queueMessageHandler : messageHandlers) {
            synchronized (queueMessageHandlers) {
                queueMessageHandlers.put(queueMessageHandler.tag(), queueMessageHandler);
            }
        }
    }


    public void errorHandler(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        if (queueMessageHandlers.isEmpty()) {
            throw new RuntimeException("messageHandler is empty");
        }
        contextInit(context);
        for (MessageExt messageExt : list) {
            try {
                String tags = messageExt.getTags();
                QueueMessageHandler messageHandler = queueMessageHandlers.get(messageExt.getTags());
                if (messageHandler == null) {
                    throw new RuntimeException(String.format("%s handler not found", tags));
                }
                messageHandler.handler(messageExt, context);
            } catch (Exception ex) {
                errorHandler(ex);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
