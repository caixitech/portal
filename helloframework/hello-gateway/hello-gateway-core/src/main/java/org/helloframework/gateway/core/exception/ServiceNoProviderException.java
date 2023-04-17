package org.helloframework.gateway.core.exception;

/**
 * Created by lanjian on 2017/11/28.
 */
public class ServiceNoProviderException extends RuntimeException {
    public ServiceNoProviderException() {
        super();
    }

    public ServiceNoProviderException(String message) {
        super(message);
    }

    public ServiceNoProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNoProviderException(Throwable cause) {
        super(cause);
    }
}
