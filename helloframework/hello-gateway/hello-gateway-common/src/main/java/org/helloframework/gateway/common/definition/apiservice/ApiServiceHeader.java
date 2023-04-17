package org.helloframework.gateway.common.definition.apiservice;

import java.util.Map;

/**
 * Created by lanjian
 */
public class ApiServiceHeader {
    private Long timestamp;
    private String sign;
    private Map<String, String> headers;
    private String path;
    private String method;
    private String appSecret;
    private boolean enableReplyCheck;
    private boolean enableSignCheck;


    public ApiServiceHeader(Long timestamp, String sign) {
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public ApiServiceHeader(boolean enableReplyCheck, boolean enableSignCheck,
                            Long timestamp, String sign, String appSecret, String path, String method, Map<String, String> headers) {
        this.enableReplyCheck = enableReplyCheck;
        this.enableSignCheck = enableSignCheck;
        this.timestamp = timestamp;
        this.sign = sign;
        this.appSecret = appSecret;
        this.path = path;
        this.method = method;
        this.headers = headers;
    }

    public boolean isEnableReplyCheck() {
        return enableReplyCheck;
    }

    public void setEnableReplyCheck(boolean enableReplyCheck) {
        this.enableReplyCheck = enableReplyCheck;
    }

    public boolean isEnableSignCheck() {
        return enableSignCheck;
    }

    public void setEnableSignCheck(boolean enableSignCheck) {
        this.enableSignCheck = enableSignCheck;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "ApiServiceHeader{" +
                "timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                '}';
    }
}
