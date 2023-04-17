package org.helloframework.gateway.common.definition.base;

import org.helloframework.gateway.common.exception.ApiException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lanjian
 */
public class MessageCountDownLatch extends CountDownLatch {
    private Object message;

    private ApiException exception;

    public ApiException getException() {
        return exception;
    }

    public void setException(ApiException exception) {
        this.exception = exception;
    }

    /**
     * Constructs a {@code CountDownLatch} initialized with the given count.
     *
     * @param count the number of times {@link #countDown} must be invoked
     *              before threads can pass through {@link #await}
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public MessageCountDownLatch(int count) {
        super(count);
    }


    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }


}
