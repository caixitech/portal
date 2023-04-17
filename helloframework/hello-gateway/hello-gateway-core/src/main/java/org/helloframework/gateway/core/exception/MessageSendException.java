package org.helloframework.gateway.core.exception;

/**
 * Created by lanjian
 */
public class MessageSendException extends RuntimeException {
    public MessageSendException() {
    }

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageSendException(Throwable cause) {
        super(cause);
    }

}
