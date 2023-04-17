package org.helloframework.rocketmq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Created by lanjian
 */
public abstract class HelloQueueMessageHandler implements QueueMessageHandler {
    @Override
    public void handler(MessageExt ext, ConsumeConcurrentlyContext context) {
        try {
            service(ext, context);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public abstract void service(MessageExt ext, ConsumeConcurrentlyContext context) throws Exception;

    @Override
    public abstract String tag();
}
