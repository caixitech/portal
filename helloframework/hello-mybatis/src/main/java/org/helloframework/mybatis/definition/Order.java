package org.helloframework.mybatis.definition;

/**
 * Author lanjian
 * Email  lanjian
 */
public class Order {
    private String propertyName;
    private OrderBy orderBy;

    public Order(String propertyName, OrderBy orderBy) {
        this.propertyName = propertyName;
        this.orderBy = orderBy;

    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

}
