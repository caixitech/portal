package org.helloframework.mybatis.exception;

/**
 * Author lanjian
 * Email  lanjian
 */
public class QueryException extends RuntimeException {
    public QueryException() {
        super();
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }
}
