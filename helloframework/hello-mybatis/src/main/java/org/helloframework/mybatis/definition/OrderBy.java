package org.helloframework.mybatis.definition;


import org.helloframework.mybatis.exception.QueryException;

/**
 * Author lanjian
 * Email  lanjian
 * 用 DESC 表示按倒序排序(即：从大到小排序)
 * 用 ACS   表示按正序排序(即：从小到大排序)
 */
public enum OrderBy {
    ASC, DESC;

    /**
     * OBC专用
     *
     * @param propertyName
     * @return
     */
    public Order order(String propertyName) {
        switch (this) {
            case ASC:
                return new Order(propertyName, ASC);
            case DESC:
                return new Order(propertyName, DESC);
            default:
                throw new QueryException("enum OrderBy is null，pls choose one！");
        }
    }
}
