package org.helloframework.gateway.common.exception;

/**
 * Created by lanjian
 */
public class FuseException extends RuntimeException {
    public FuseException() {
    }

    public FuseException(String message) {
        super(message);
    }

    public FuseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FuseException(Throwable cause) {
        super(cause);
    }
}
