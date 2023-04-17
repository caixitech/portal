package org.helloframework.gateway.common.definition.apiservice.plugins;

/**
 * Created by lanjian
 */
public class ValidateMessage {
    private String property;
    private String message;
    private Integer code;

    public ValidateMessage(String property, String message, Integer code) {
        this.property = property;
        this.message = message;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidateMessage{" +
                "property='" + property + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
