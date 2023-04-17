package org.helloframework.gateway.common.definition.apiservice;

import io.protostuff.Tag;
import org.slf4j.event.Level;

/**
 * Created by lanjian
 */
public class ApiServiceResponse {

    @Tag(1)
    private String id;
    @Tag(2)
    private String nonce;
    @Tag(3)
    private byte[] message;
    @Tag(4)
    private String exception;
    @Tag(5)
    private String code;
    @Tag(6)
    private String msg;
    @Tag(7)
    private Level logLevel;
    @Tag(8)
    private String desc;

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

    public ApiServiceResponse(String id, String nonce, byte[] message) {
        this.id = id;
        this.nonce = nonce;
        this.message = message;
    }

    public ApiServiceResponse(String id, String nonce, byte[] message, String exception, String code, String msg, Level logLevel, String desc) {
        this.id = id;
        this.nonce = nonce;
        this.message = message;
        this.exception = exception;
        this.code = code;
        this.msg = msg;
        this.logLevel = logLevel;
        this.desc = desc;
    }

    public ApiServiceResponse() {

    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

