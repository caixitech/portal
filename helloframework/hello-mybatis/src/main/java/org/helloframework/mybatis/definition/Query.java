package org.helloframework.mybatis.definition;

/**
 * Author lanjian
 * Email  lanjian
 */
public class Query {
    private Object value;
    private String property;
    private String valueAt;
    private Where where;

    public Query(Where where) {
        this.where = where;
    }

    public Query(Object value, String property, String valueAt, Where where) {
        this.value = value;
        this.property = property;
        this.valueAt = valueAt;
        this.where = where;
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
        return valueAt;
    }

    public void setValueAt(String valueAt) {
        this.valueAt = valueAt;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }
}
