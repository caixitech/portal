package org.helloframework.rocketmq;

import com.alibaba.rocketmq.common.message.Message;

/**
 * Created by lanjian
 */
public abstract class HelloMessage<T> {
    private T t;


    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public HelloMessage(T t) {
        this.t = t;
    }

    public Message message() {
        Message msg = new Message(topic(), tag(), body());
        return msg;
    }

    protected abstract byte[] body();

    protected abstract String topic();

    protected abstract String tag();

}
