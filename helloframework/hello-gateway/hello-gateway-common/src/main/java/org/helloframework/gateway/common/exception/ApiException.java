package org.helloframework.gateway.common.exception;

import org.slf4j.event.Level;

/**
 * Created by lanjian on 23/08/2017.
 */
public class ApiException extends RuntimeException {
    private String code;
    private String msg;
    private String desc;
    private Level logLevel = Level.ERROR;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public ApiException(GateWayCode exceptionCode) {
        super(String.format("code: %s, msg: %s", exceptionCode.getCode(), exceptionCode.getMsg()));
        this.code = exceptionCode.getCode();
        this.msg = exceptionCode.getMsg();
    }

    public ApiException(GateWayCode exceptionCode, Level logLevel) {
        super(String.format("code: %s, msg: %s", exceptionCode.getCode(), exceptionCode.getMsg()));
        this.code = exceptionCode.getCode();
        this.msg = exceptionCode.getMsg();
        this.logLevel = logLevel;
    }

    public ApiException(GateWayCode exceptionCode, String message) {
        super(String.format("%s, code: %s, msg: %s", message, exceptionCode.getCode(), exceptionCode.getMsg()));
        this.code = exceptionCode.getCode();
        this.msg = exceptionCode.getMsg();
    }


    public ApiException(String code, String msg) {
        super(String.format("code: %s, msg: %s", code, msg));
        this.code = code;
        this.msg = msg;
    }

    public ApiException(String message, String code, String msg) {
        super(String.format("%s, code: %s, msg: %s", message, code, msg));
        this.code = code;
        this.msg = msg;
    }

    public ApiException(String message, String code, String msg, Level logLevel, String desc) {
        super(String.format("%s, code: %s, msg: %s", message, code, msg));
        this.code = code;
        this.msg = msg;
        this.logLevel = logLevel;
        this.desc = desc;
    }

    public ApiException(String message, Throwable cause, String code, String msg) {
        super(String.format("%s, code: %s, msg: %s", message, code, msg), cause);
        this.code = code;
        this.msg = msg;
    }

    public ApiException(Throwable cause, String code, String msg) {
        super(String.format("code: %s, msg: %s", code, msg), cause);
        this.code = code;
        this.msg = msg;
    }

    public ApiException(Throwable cause, GateWayCode exceptionCode) {
        super(String.format("code: %s, msg: %s", exceptionCode.getCode(), exceptionCode.getMsg()), cause);
        this.code = exceptionCode.getCode();
        this.msg = exceptionCode.getMsg();
    }

    @Override
    public String toString() {
        return super.toString() + " {" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                "}";
    }
}
