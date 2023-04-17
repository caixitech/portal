package org.helloframework.gateway.registry.zk.exception;

/**
 * Created by lanjian
 */
public class RpcException extends RuntimeException {
    public RpcException(String message) {
        super(message);
    }

    public RpcException() {
    }


    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

