package org.helloframework.testcase.config;

import io.restassured.http.ContentType;
import org.apache.http.client.config.RequestConfig;
import org.helloframework.testcase.common.SignHandler;
import org.helloframework.testcase.common.Then;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @ClassName TestCaseConfig
 * @Author lanjian
 * @date 2020.01.14 17:37
 */


public class TestCaseConfig {
    private String serverUrl;
    private SignHandler signHandler;
    private ContentType contentType;
    private Map<String, Object> params = new HashMap<String, Object>();
    private Map<String, Object> cookies = new HashMap<String, Object>();
    private Map<String, Object> headers = new HashMap<String, Object>();
    private RequestConfig requestConfig;
    private String path;
    private Then then;

    public Then getThen() {
        return then;
    }

    public void setThen(Then then) {
        this.then = then;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public SignHandler getSignHandler() {
        return signHandler;
    }

    public void setSignHandler(SignHandler signHandler) {
        this.signHandler = signHandler;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, Object> cookies) {
        this.cookies = cookies;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TestCaseConfig header(String key, Object v) {
        this.headers.put(key, v);
        return this;
    }

    public TestCaseConfig cookie(String key, Object v) {
        this.cookies.put(key, v);
        return this;
    }

    public TestCaseConfig param(String key, Object v) {
        this.params.put(key, v);
        return this;
    }

    public TestCaseConfig then(Then then) {
        this.then = then;
        return this;
    }
}
