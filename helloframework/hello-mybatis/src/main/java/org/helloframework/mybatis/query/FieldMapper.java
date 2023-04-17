package org.helloframework.mybatis.query;

/**
 * Created by lanjian on 2018/1/23.
 */
public interface FieldMapper<I, O> {
    I mapper(O o);
}
