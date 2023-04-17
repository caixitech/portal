package org.helloframework.core.exception;

/**
 * Created by lanjian on 2014/7/25.
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

    public DigestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//        super(message, cause, enableSuppression, writableStackTrace);
//        super();
    }
}
