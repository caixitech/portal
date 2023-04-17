package org.helloframework.gateway.common.netty;

/**
 * Created by lanjian
 */
public class IdleException extends RuntimeException {
    public IdleException() {
    }

    public IdleException(String message) {
        super(message);
    }

    public IdleException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdleException(Throwable cause) {
        super(cause);
    }

}
