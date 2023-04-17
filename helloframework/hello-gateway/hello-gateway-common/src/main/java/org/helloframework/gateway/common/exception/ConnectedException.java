package org.helloframework.gateway.common.exception;

/**
 * Created by lanjian
 */
public class ConnectedException extends RuntimeException {
    public ConnectedException() {
    }

    public ConnectedException(String message) {
        super(message);
    }

    public ConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectedException(Throwable cause) {
        super(cause);
    }

}
