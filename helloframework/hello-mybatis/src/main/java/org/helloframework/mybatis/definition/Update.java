package org.helloframework.mybatis.definition;

/**
 * Author lanjian
 * Email  lanjian
 */
public class Update {
    private Object value;
    private String property;
    private String valueAt;

    public Update(Object value, String property, String valueAt) {
        this.value = value;
        this.property = property;
        this.valueAt = valueAt;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValueAt() {
        return "v" + valueAt;
    }

    public void setValueAt(String valueAt) {
        this.valueAt = valueAt;
    }
}
