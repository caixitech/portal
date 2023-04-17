package org.helloframework.gateway.common.exception;

/**
 * Created by lanjian
 */
public class DigestException extends RuntimeException {
    public DigestException() {
    }

    public DigestException(String message) {
        super(message);
    }

    public DigestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigestException(Throwable cause) {
        super(cause);
    }

}
