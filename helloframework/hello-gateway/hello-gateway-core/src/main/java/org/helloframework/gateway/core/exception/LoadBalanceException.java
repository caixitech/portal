package org.helloframework.gateway.core.exception;

/**
 * Created by lanjian on 2017/11/28.
 */
public class LoadBalanceException extends RuntimeException {

    public LoadBalanceException() {
    }

    public LoadBalanceException(String message) {
        super(message);
    }

    public LoadBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadBalanceException(Throwable cause) {
        super(cause);
    }
}
