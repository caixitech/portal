package org.helloframework.http.common;

public class HttpInfo {
    private HttpParamType httpParamType;
    private String name;
    private Object value;
    private HttpFormat format;

    public HttpInfo(HttpParamType httpParamType, String name, Object value, HttpFormat format) {
        this.httpParamType = httpParamType;
        this.name = name;
        this.value = value;
        this.format = format;
    }

    public HttpFormat getFormat() {
        return format;
    }

    public void setFormat(HttpFormat format) {
        this.format = format;
    }

    public HttpParamType getHttpParamType() {
        return httpParamType;
    }

    public void setHttpParamType(HttpParamType httpParamType) {
        this.httpParamType = httpParamType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
