package org.helloframework.gateway.core.exception;

/**
 * Created by lanjian
 */
public class ServiceInvokeException extends RuntimeException {

    public ServiceInvokeException() {
    }

    public ServiceInvokeException(String message) {
        super(message);
    }

    public ServiceInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceInvokeException(Throwable cause) {
        super(cause);
    }

}
