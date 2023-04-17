package org.helloframework.mybatis.exception;

/**
 * Created by lanjian on 31/08/2017.
 */
public class SQLProviderException extends RuntimeException {


    public SQLProviderException() {
    }

    public SQLProviderException(String message) {
        super(message);
    }

    public SQLProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLProviderException(Throwable cause) {
        super(cause);
    }

}
