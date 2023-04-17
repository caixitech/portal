package org.helloframework.rocketmq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * Created by lanjian
 */
public class HelloMQProducer extends DefaultMQProducer {
    public SendResult send(String topic, String tag, byte[] body) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return send(new Message(topic, tag, body));
    }
}
