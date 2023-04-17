package org.helloframework.gateway.common.exception;

/**
 * Created by macintosh
 */
public class ApiCountDownException extends ApiException {
    public ApiCountDownException(GateWayCode exceptionCode) {
        super(exceptionCode);
    }

    public ApiCountDownException(String code, String msg) {
        super(code, msg);
    }

    public ApiCountDownException(String message, String code, String msg) {
        super(message, code, msg);
    }

    public ApiCountDownException(String message, Throwable cause, String code, String msg) {
        super(message, cause, code, msg);
    }

    public ApiCountDownException(Throwable cause, String code, String msg) {
        super(cause, code, msg);
    }
}
