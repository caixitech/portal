package org.helloframework.rocketmq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Created by lanjian
 */
public interface QueueMessageHandler {
    void handler(MessageExt ext, ConsumeConcurrentlyContext context);

    String tag();
}
